package com.zhoufa.lucene.analyzer.synonym;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.Stack;

/**
 * @author zhou.fa@diligentfirst.com
 * @creator 2016-08-31-17
 */
public class SynonymFilter extends TokenFilter {
    private static final String TOKEN_TYPE_SYNONYM = "SYNONYM";

    private SynonymEngine synonymEngine;

    private Stack<String > synonymStack;

    private AttributeSource.State current;

    private final CharTermAttribute charTermAttr = addAttribute(CharTermAttribute.class);

    private final TypeAttribute typeAttr = addAttribute(TypeAttribute.class);

    private final PositionIncrementAttribute positionAttr = addAttribute(PositionIncrementAttribute.class);

    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     */
    SynonymFilter(TokenStream input, SynonymEngine synonymEngine) {
        super(input);
        this.synonymEngine = synonymEngine;
        synonymStack = new Stack<>();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (synonymStack.size() > 0) {
            String synonym = synonymStack.pop();
            restoreState(current);
            charTermAttr.copyBuffer(synonym.toCharArray(), 0, synonym.length());
            typeAttr.setType(TOKEN_TYPE_SYNONYM);
            positionAttr.setPositionIncrement(0);
            return true;
        }

        if (!input.incrementToken()) {
            return false;
        }

        addAliasesToStack();
        return true;
    }

    private void addAliasesToStack() throws IOException {
        String[] synonyms = synonymEngine.getSynonyms(charTermAttr.toString());
        if (synonyms == null) {
            return;
        }

        for (String synonym : synonyms) {
            synonymStack.push(synonym);
        }
        current = captureState();
    }
}
