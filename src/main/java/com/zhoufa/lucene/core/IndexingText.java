package com.zhoufa.lucene.core;

import junit.framework.TestCase;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;

import java.io.IOException;
import java.util.Date;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-18-11
 */
public class IndexingText extends TestCase{
    private String[] ids = {"1", "2"};
    private String[] unindexed = {"Netherlands", "Italy"};
    private String[] unstored = {"Amsterdam has lots of bridges", "venice has lots of canals"};
    private String[] text = {"Amsterdam", "venice"};

    private Directory directory;

    @Before
    protected void setUp() throws Exception{
        directory = new RAMDirectory();
        IndexWriter writer = getWriter();
        for (int i=0; i<ids.length; i++) {
            Document doc = new Document();
            FieldType fieldType = new FieldType();//默认的是不存储，不建索引
            fieldType.setStored(true);
            fieldType.setIndexOptions(IndexOptions.DOCS);
            doc.add(new Field("id",ids[i], fieldType));
            doc.add(new Field("country", unindexed[i], Field.Store.YES, Field.Index.NO));
            doc.add(new Field("contents", unstored[i], Field.Store.NO, Field.Index.ANALYZED));
            doc.add(new Field("city", text[i], Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("date", DateTools.dateToString(new Date(), DateTools.Resolution.DAY), Field.Store.YES, Field.Index.NO));
            writer.addDocument(doc);
        }
        writer.close();
    }

    private IndexWriter getWriter() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(new WhitespaceAnalyzer());
        return new IndexWriter(directory, config);
    }

    private int getHitCount(String fieldName, String searchThing) throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new TermQuery(new Term(fieldName, searchThing));
        TopDocs docs = searcher.search(query, 10);
        return docs.totalHits;
    }

    public void testIndexWriter() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(ids.length, writer.numDocs());
        writer.close();
    }

//    maxDoc()  returns the total number of deleted or un-deleted documents in the index,
//    whereas numDocs() returns only the number of un-deleted documents.

    //删除索引--未优化合并之前
    public void testDeleteBeforIndexMerge() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(2, writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.commit();
        assertTrue(writer.hasDeletions()); //是否存在删除的内容
        assertEquals(2, writer.maxDoc());//包含所有状态的document
        assertEquals(1, writer.numDocs());//未删除的document
        writer.close();
    }

    //删除索引--未优化合并之后
    public void testDeleteAfterIndexMerge() throws IOException {
        IndexWriter writer = getWriter();
        assertEquals(2, writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.forceMergeDeletes();//合并之后，删除的document就没了
        writer.commit();
        assertFalse(writer.hasDeletions());
        assertEquals(1, writer.maxDoc());
        assertEquals(1, writer.numDocs());
        writer.close();
    }

    public void testIndexReader() throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
//        assertEquals(ids.length, reader.maxDoc());
//        assertEquals(ids.length, reader.numDocs());
        IndexSearcher searcher = new IndexSearcher(reader);
//        Query query = new TermQuery(new Term("id", "1"));
        Query query = new TermQuery(new Term("contents", "bridges"));
        TopDocs docs = searcher.search(query, 3);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (ScoreDoc doc: scoreDocs) {
            Document document = searcher.doc(doc.doc);
            System.out.println("ID：" + document.get("id"));
            System.out.println("country：" + document.get("country"));
            System.out.println("contents：" + document.get("contents"));
            System.out.println("city：" + document.get("city"));
            System.out.println("date：" + document.get("date"));
        }
        reader.close();
    }

    //更新docment
    public void testupdate() throws IOException {
        assertEquals(1, getHitCount("city", "Amsterdam"));
        IndexWriter writer = getWriter();

        Document doc = new Document();
        doc.add(new Field("id", "1", Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("country", "Netherlands", Field.Store.YES, Field.Index.NO));
        doc.add(new Field("contents", "Amsterdam has lots of bridges", Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field("city", "Haag", Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("date", DateTools.dateToString(new Date(), DateTools.Resolution.DAY), Field.Store.YES, Field.Index.NO));
        writer.updateDocument(new Term("id", "1"), doc);
        writer.close();
        assertEquals(0, getHitCount("city", "Amsterdam"));

        assertEquals(1, getHitCount("city", "Haag"));
    }

    public static void main(String[] args) throws Exception {
        IndexingText it = new IndexingText();
        it.setUp();
        System.out.println("hitcount: "+it.getHitCount("id", "1"));
        it.testIndexReader();
    }
}
