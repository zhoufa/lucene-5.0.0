package com.zhoufa.lucene.core;

import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-22-17
 */
public class RangeQueryTest extends BaseTestCase {

    public void testInclusive() throws IOException {
        TermRangeQuery rangeQuery = new TermRangeQuery("pubmonth", new BytesRef("188805"), new BytesRef("199912"), true, true);

        TopDocs docs = searcher.search(rangeQuery, 10);
//        Explanation explanation = searcher.explain(rangeQuery, docs.scoreDocs[0].doc);
//        System.out.println(explanation.toString());
        System.out.println("totalHits:"+docs.totalHits);
        reader.close();
    }
}
