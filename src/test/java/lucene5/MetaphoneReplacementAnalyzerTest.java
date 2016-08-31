package lucene5;

import com.zhoufa.lucene.analyzer.MetaphoneReplacementAnalyzer;
import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 *     Metaphone 算法
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-31-14
 */
public class MetaphoneReplacementAnalyzerTest extends TestCase {
    public void testKoolKat() throws IOException, ParseException {
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new MetaphoneReplacementAnalyzer();
        org.apache.lucene.index.IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer));
        Document document = new Document();
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        document.add(new Field("content", "cool cat", type));
        writer.addDocument(document);
        writer.close();

        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
        Query query = new QueryParser("content", new MetaphoneReplacementAnalyzer()).parse("kool kat");

        TopScoreDocCollector collector = TopScoreDocCollector.create(1);
        searcher.search(query, collector);

        assertEquals(1, collector.getTotalHits());

        int docId = collector.topDocs().scoreDocs[0].doc;
        document = searcher.doc(docId);
        System.out.println(document.get("content"));;
    }
}
