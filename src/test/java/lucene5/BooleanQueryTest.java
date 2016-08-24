package lucene5;

import com.zhoufa.lucene.util.TestUtil;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-23-13
 */
public class BooleanQueryTest extends BaseTestCase {

    public void testAdd() throws IOException {
        TermQuery termQuery = new TermQuery(new Term("subject", "apache"));

        NumericRangeQuery nQuery = NumericRangeQuery.newIntRange("pubmonth", 200705, 200708, false, false);
        BooleanQuery bQuery = new BooleanQuery();
        bQuery.add(termQuery, BooleanClause.Occur.MUST);
        bQuery.add(nQuery, BooleanClause.Occur.MUST);
        TopDocs docs = searcher.search(bQuery, 10);
        System.out.println("totalHits:" + docs.totalHits);
        assertTrue(TestUtil.hitsIncludeTitle(searcher, docs,
                "Ant in Action"));
        reader.close();
    }

    public void testQueryParserOfBooleanQuery() throws ParseException, IOException {
        QueryParser parser = new QueryParser("title", new SimpleAnalyzer());
        Query query = parser.parse("-ant +action");
        TopDocs docs = searcher.search(query, 10);
//        assertEquals(1, docs.totalHits);
        assertTrue(TestUtil.hitsIncludeTitle(searcher, docs, "Tapestry in Action"));
        reader.close();
    }
}
