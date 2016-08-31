package lucene5;

import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-31-11
 */
public class KeyWordAnalyzeTest extends TestCase {

    private IndexSearcher searcher;

    public void setUp() throws IOException {
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());
        org.apache.lucene.index.IndexWriter writer = new IndexWriter(directory, config);
        Document document = new Document();
        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        type.setTokenized(false);
        document.add(new Field("partnum", "Q36", type));

        type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS);
        type.setStored(true);
        document.add(new Field("description", "Illidium Space Modulator",  type));
        writer.addDocument(document);
        writer.close();

        searcher = new IndexSearcher(DirectoryReader.open(directory));
    }

    public void testBasicQueryParse() throws ParseException, IOException {
        QueryParser parser = new QueryParser("description", new SimpleAnalyzer());
        Query query = parser.parse("partnum:Q36 AND SPACE");
        System.out.println("query： "+query.toString("description"));
        assertEquals("note Q36 -> q", "+partnum:q +space", query.toString("description"));
        System.out.println(searcher.search(query, 1).totalHits);
    }

    /**
     * 分成两个来查询，这样就会把partnum当成一个Keyword,Q36
     * @throws ParseException
     * @throws IOException
     */
    public void testPerFieldAnalyer() throws ParseException, IOException {
        Map<String, Analyzer> fieldAnalyzers = new HashMap<>();
        fieldAnalyzers.put("partnum", new KeywordAnalyzer());
        PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper( new SimpleAnalyzer(), fieldAnalyzers);
        QueryParser parser = new QueryParser("description", analyzer);
        Query query = parser.parse("partnum:Q36 AND SPACE");
        System.out.println("query： "+query.toString("description"));
        assertEquals("note Q36 -> q", "+partnum:Q36 +space", query.toString("description"));
        System.out.println(searcher.search(query, 1).totalHits);
    }

}
