package lucene5;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-23-10
 */
public class PrefixQueryTest extends BaseTestCase {

    public void testPrefixQueryTest() throws IOException {
        Term term = new Term("category", "/computers/programming");
        Query query = new PrefixQuery(term);
        System.out.println(query.toString());
        TopDocs docs = searcher.search(query, 10);
        System.out.println("programmingAndBelow:" + docs.totalHits);
        int programmingAndBelow = docs.totalHits;

        docs = searcher.search(new TermQuery(term), 10);
        int justProgramming = docs.totalHits;
        System.out.println("justProgramming:" + docs.totalHits);
        assertTrue(programmingAndBelow > justProgramming);
        reader.close();
    }

    public void testQueryParserByPrefixQueryTest() throws ParseException, IOException {
        QueryParser parser = new QueryParser("category", new KeywordAnalyzer());
        Query query = parser.parse("\\/computers\\/programming*");
        System.out.println(query.toString("category"));
        TopDocs docs = searcher.search(query, 10);
       int programmingAndBelow = docs.totalHits;

        System.out.println("programmingAndBelow:" + docs.totalHits);
        QueryParser parserT = new QueryParser("category", new KeywordAnalyzer());
        query = parserT.parse("\\/computers\\/programming");
        docs = searcher.search(query, 10);
        System.out.println(query.toString("category"));
        int justProgramming = docs.totalHits;
        System.out.println("justProgramming:" + docs.totalHits);
        assertTrue(programmingAndBelow > justProgramming);
        reader.close();
    }
}
