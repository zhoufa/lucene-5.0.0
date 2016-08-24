package lucene5;

import junit.framework.TestCase;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class PhraseQueryTest extends TestCase{
    private IndexSearcher searcher;

    public void setUp() throws IOException {
        RAMDirectory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new WhitespaceAnalyzer());
        IndexWriter writer = new IndexWriter(directory, config);
        Document document = new Document();
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);
        fieldType.setIndexOptions(IndexOptions.DOCS);
        document.add(new Field("field", "the quick brown fox jump over the lazy dog", fieldType));
        writer.addDocument(document);
        writer.close();
        searcher = new IndexSearcher(DirectoryReader.open(directory));
    }

   private boolean match(String[] phrase, Integer slop) throws IOException {
       PhraseQuery phraseQuery = new PhraseQuery();
       phraseQuery.setSlop(slop);
       for (String aPhrase : phrase) {
           phraseQuery.add(new Term("field", aPhrase));
       }
       TopDocs docs = searcher.search(phraseQuery, 10);
       return docs.totalHits > 0;
   }

    public void testSlopComparsion() throws IOException {
        String[] phrase = {"quick", "fox"};
        assertTrue("YES", match(phrase, 1));
        assertFalse("NO", match(phrase, 0));
    }
}