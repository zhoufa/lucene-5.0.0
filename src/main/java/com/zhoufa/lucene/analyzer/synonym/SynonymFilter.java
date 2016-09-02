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

    private int skippedPositions;

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
            restoreState(current);
            String synonymPop = synonymStack.pop();
            charTermAttr.copyBuffer(synonymPop.toCharArray(), 0, synonymPop.length());
            positionAttr.setPositionIncrement(0);
            //为什么在这也返回return true,因为当第一次走的时候，stack是空的，然后会去向stack里面添加同义词，当第二次走的时候，
            //其实这个term已经结束了，会走#2步，但是检查出stack里面有值，它会走这里面，然后会把你设置这个term的同义词放到相关属性
            //上面，这样SynonymAnalyzer就可以起到作用了。
//            if (skippedPositions != 0) {
//                positionAttr.setPositionIncrement(positionAttr.getPositionIncrement() + skippedPositions);
//            }
            System.out.println("position: "+positionAttr.getPositionIncrement());
            System.out.println("term2: "+charTermAttr.toString());
            System.out.println("================================");

            return true;
        }

        if (!input.incrementToken()) { // #2
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
        System.out.println("term1: "+charTermAttr.toString());
        for (String synonym : synonyms) {
            typeAttr.setType(TOKEN_TYPE_SYNONYM);
            synonymStack.push(synonym);
        }
        current = captureState();
    }
}
