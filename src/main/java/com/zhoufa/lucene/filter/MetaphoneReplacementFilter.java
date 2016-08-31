package com.zhoufa.lucene.filter;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-31-14
 */
public class MetaphoneReplacementFilter extends TokenFilter {

    private static final String METAPHONE = "METAPHONE";

    private Metaphone metaphone = new Metaphone();

    private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);

    private final TypeAttribute typeAttr = addAttribute(TypeAttribute.class);

    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    public MetaphoneReplacementFilter(TokenStream input) {
        super(input);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            String encoded;
            encoded = metaphone.encode(termAttr.toString());   //#4
            termAttr.copyBuffer(encoded.toCharArray(), 0, encoded.length());
            typeAttr.setType(METAPHONE);
            return true;
        } else
            return false;
    }

}
