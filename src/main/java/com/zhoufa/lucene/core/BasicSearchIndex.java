package com.zhoufa.lucene.core;

import junit.framework.TestCase;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-22-16
 */
public class BasicSearchIndex extends TestCase{

    private static String INDEX_DIR = "E:\\solrhome\\Lucene-index";

    private static IndexSearcher searcher;
    private static IndexReader reader;

    static {
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIR)));
            searcher = new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testTerm() throws IOException {
        Query query = new TermQuery(new Term("subject", "ant"));
        TopDocs docs = searcher.search(query, 10);
        System.out.println(docs.totalHits);
        assertEquals("JDwA", 1, docs.totalHits);
        reader.close();
    }

    public void testQueryParse() throws IOException, ParseException {
        QueryParser parser = new QueryParser("contents", new SimpleAnalyzer());
        Query query = parser.parse("+JUNIT -ANT");
        TopDocs docs = searcher.search(query, 10);
        assertEquals(1, docs.totalHits);
        Document doc = searcher.doc(docs.scoreDocs[0].doc);
        Explanation explanation = searcher.explain(query, docs.scoreDocs[0].doc);
        System.out.println("explanation:"+explanation.toString());
        System.out.println("title:" + doc.get("title"));
        System.out.println("subject:" + doc.get("subject"));

        query = new QueryParser("contents",
                new SimpleAnalyzer()).parse("mock OR junit");
        docs = searcher.search(query, 10);
        docs.getMaxScore();
        System.out.println("totalHits:"+docs.totalHits);
        reader.close();
    }

    public void testBasicSearch() throws IOException {
        Query query = new TermQuery(new Term("subject", "lucene"));
        TopDocs docs = searcher.search(query, 10);
        assertEquals(1, docs.totalHits);
        Document doc = searcher.doc(docs.scoreDocs[0].doc);
        System.out.println("pubmonth:" + doc.get("pubmonth"));
        reader.close();
    }

    public void testKeyword() throws Exception {

        Term t = new Term("isbn", "1930110995");
        Query query = new TermQuery(t);
        TopDocs docs = searcher.search(query, 10);
        assertEquals("JUnit in Action", 1, docs.totalHits);  //wrong
        reader.close();
    }
}
