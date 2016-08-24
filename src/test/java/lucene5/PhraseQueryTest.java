package lucene5;

import junit.framework.TestCase;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class PhraseQueryTest extends TestCase{
    private IndexSearcher searcher;

    public void setUp() throws IOException {
        RAMDirectory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new WhitespaceAnalyzer());
        config.setMaxBufferedDocs(Integer.MAX_VALUE);
        IndexWriter writer = new IndexWriter(directory, config);
        Document document = new Document();
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);
        //对于PharseQuery来说，索引设置用下面这个，它前面三个“DOCS_AND_FREQS”， “DOCS” "NONE" 你
        //查有关于position的query都会报错
        //.java.lang.IllegalStateException: field "field" was indexed without position data; cannot run PhraseQuery (term=quick)
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);//通常还是用这个比较好，
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
        System.out.println(match(phrase, 1));
        assertFalse("NO", match(phrase, 0));
    }

    //反向查询
    public void testReverse() throws IOException {
        String[] phrase = {"fox", "quick"};
        assertTrue(match(phrase, 3));
        assertFalse(match(phrase, 2));
    }

    public void testMultiple() throws IOException {
//        the quick brown fox jump over the lazy dog
        String[] phrase = {"quick", "jump" ,"lazy"};
        assertFalse(match(phrase, 3));
        System.out.println(match(phrase, 4));

        assertTrue(match(new String[]{"lazy", "jump", "quick"}, 8));
    }

    //我看的理解是当用QueryParser短语查询时，不会按照顺序去match,书上说的是用SpanNearQuery可以实现
    public void testQueryParserByPhraseQuery() throws ParseException, IOException {
        QueryParser parser = new QueryParser("field", new StandardAnalyzer());
        Query query = parser.parse("lazy quick");
        TopDocs docs = searcher.search(query, 10);
        System.out.println(query.toString("field")+"; num:"+docs.totalHits);
//        System.out.println(query instanceof PhraseQuery);
    }
}