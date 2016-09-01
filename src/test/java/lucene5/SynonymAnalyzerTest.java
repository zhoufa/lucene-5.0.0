package lucene5;

import com.zhoufa.lucene.analyzer.MetaphoneReplacementAnalyzer;
import com.zhoufa.lucene.analyzer.synonym.BaseSynonymEngine;
import com.zhoufa.lucene.analyzer.synonym.SynonymAnalyzer;
import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-09-01-16
 */
public class SynonymAnalyzerTest extends TestCase {

    private IndexSearcher searcher;

    private final SynonymAnalyzer analyzer = new SynonymAnalyzer(new BaseSynonymEngine());

    protected void setUp() throws IOException {
        Directory directory = new RAMDirectory();
        org.apache.lucene.index.IndexWriter writer = new org.apache.lucene.index.IndexWriter(directory, new IndexWriterConfig(analyzer));
        Document document = new Document();
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        document.add(new Field("content", "The quick brown fox jumps over the lazy dogs", type));
        writer.addDocument(document);
        writer.close();

        searcher = new IndexSearcher(DirectoryReader.open(directory));
    }



    public void testSearchByApi() throws ParseException, IOException {
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse("fox jumps");
        System.out.println(query.toString("content"));
        assertEquals(1, searcher.search(query, 1).totalHits);

    }
}
