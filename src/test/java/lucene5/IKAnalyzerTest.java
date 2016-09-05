package lucene5;

import junit.framework.TestCase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @projectName lucene-5.0.0
 * @createDate 2016-09-05
 */
public class IKAnalyzerTest extends TestCase {

    private IndexSearcher searcher;

    public void setUp() throws IOException {
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
        org.apache.lucene.index.IndexWriter writer = new IndexWriter(directory, config);
        Document document = new Document();
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        document.add(new Field("content", "中国G20峰会在杭州举行", type));
        document.add(new Field("city", "杭州", type));
        writer.addDocument(document);
        writer.close();

        searcher = new IndexSearcher(DirectoryReader.open(directory));
    }

    public void testIKAnalyzer() throws ParseException, IOException {
        QueryParser parser = new QueryParser("content", new IKAnalyzer());
        Query query = parser.parse("中国");
        assertEquals(1, searcher.search(query, 1).totalHits);
    }
}
