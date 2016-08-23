package com.zhoufa.lucene.customer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.NumericUtils;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-23-11
 */
public class CustomQueryParser extends QueryParser {

    private static String INTFIELD_NAME = "pubmonth";

    public CustomQueryParser(String f, Analyzer a) {
        super(f, a);
    }

    protected Query newRangeQuery(String field, String part1, String part2, boolean startInclusive,
                                  boolean endInclusive) {

        if (INTFIELD_NAME.equals(field)) {
            return NumericRangeQuery.newIntRange(field, Integer.parseInt(part1), Integer.parseInt(part2),
                    startInclusive, endInclusive);
        }
        return super.newRangeQuery(field, part1, part2, startInclusive, endInclusive);
    }

    protected Query newTermQuery(Term term) {
        if (INTFIELD_NAME.equals(term.field())) {

            BytesRefBuilder byteRefBuilder = new BytesRefBuilder();
            NumericUtils.intToPrefixCoded(Integer.parseInt(term.text()), 0, byteRefBuilder);

            return new TermQuery(new Term(term.field(), byteRefBuilder.get()));
        }
        return super.newTermQuery(term);

    }
}
