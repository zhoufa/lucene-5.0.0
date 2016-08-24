package lucene5;

import junit.framework.TestCase;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-24-15
 */
public class WildcardAndFuzzyQueryTest extends TestCase {

    private Directory directory;

    private void indexSingleFileDoc(Field[] fields) throws IOException {
        directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        org.apache.lucene.index.IndexWriter writer = new IndexWriter(directory, config);
        Document document;
        for (Field field : fields) {
            document = new Document();
            document.add(field);
            writer.addDocument(document);
        }
        writer.close();
    }

    public void testWildcard() throws IOException {
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexOptions(IndexOptions.DOCS);
        indexSingleFileDoc(new Field[]{new Field("contents", "wild", type)
                                         ,new Field("contents", "child", type)
                                         ,new Field("contents", "mild", type)});

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new WildcardQuery(new Term("contents", "?il*"));
        TopDocs docs = searcher.search(query, 10);
        assertEquals(2, docs.totalHits);
        System.out.println(docs.scoreDocs[0].score);
        System.out.println(docs.scoreDocs[1].score);
        reader.close();
    }

    public void testFuzzyQuery() throws IOException {
        FieldType type = new FieldType();
        type.setStored(true);
        type.setIndexOptions(IndexOptions.DOCS);
        indexSingleFileDoc(new Field[]{new Field("contents", "fuzzy", type)
                ,new Field("contents", "wuzzy", type)});

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new FuzzyQuery(new Term("contents", "fuzza"));
        TopDocs docs = searcher.search(query, 10);
        assertEquals(2, docs.totalHits);
        assertTrue(docs.scoreDocs[0].score != docs.scoreDocs[1].score);
//        System.out.println("第一个分数："+docs.scoreDocs[0].score);
//        System.out.println("第二个分数："+docs.scoreDocs[1].score);

        Document document = searcher.doc(docs.scoreDocs[0].doc);
        assertEquals("fuzzy", document.get("contents"));
        reader.close();
    }
}
