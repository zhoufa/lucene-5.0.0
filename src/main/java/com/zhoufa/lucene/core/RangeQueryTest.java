package com.zhoufa.lucene.core;

import com.zhoufa.lucene.customer.CustomQueryParser;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-22-17
 */
public class RangeQueryTest extends BaseTestCase {

    public void testInclusive() throws IOException {
        //区间查询的时候，存的是数据就用NumericRangeQuery， 如果存的是字符串的话，就用TermRangeQuery
//        Query query = NumericRangeQuery.newIntRange("pubmonth", 199900, 199910, true, true);
        //maxInclusive true包含， false不包含
        Query query = NumericRangeQuery.newIntRange("pubmonth", 199900, 199910, false, false);
        TopDocs docs = searcher.search(query, 10);
//        Explanation explanation = searcher.explain(rangeQuery, docs.scoreDocs[0].doc);
//        System.out.println(explanation.toString());
        System.out.println("totalHits:"+docs.totalHits);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (ScoreDoc doc : scoreDocs) {
            Document document = searcher.doc(doc.doc);
            System.out.println("title:>>>   " + document.get("title"));
            System.out.println("author:>>>   " + document.get("author"));
            System.out.println("subject:>>>   " + document.get("subject"));
        }
        reader.close();
    }

    //如果用用QueryParse转成 NumericRangeQuery的话 需要自己重写一个QueryParse
    public void testQueryParserByRangeQueryTest() throws ParseException, IOException {

        CustomQueryParser parser = new CustomQueryParser("pubmonth", new SimpleAnalyzer());
        Query query = parser.parse("pubmonth:{199900 TO  199910}");
        assertTrue(query instanceof NumericRangeQuery);//怎么转换成NumericRangeQuery
        System.out.println(query.toString("pubmonth"));
        TopDocs docs = searcher.search(query, 10);

        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (ScoreDoc doc : scoreDocs) {
            Document document = searcher.doc(doc.doc);
            System.out.println("title:>>>   " + document.get("title"));
            System.out.println("author:>>>   " + document.get("author"));
        }
        reader.close();
        System.out.println(docs.totalHits);
    }
}
