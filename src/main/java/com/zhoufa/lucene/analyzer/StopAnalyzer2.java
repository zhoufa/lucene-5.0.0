package com.zhoufa.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.util.CharArraySet;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-30-11
 */
public class StopAnalyzer2 extends Analyzer{

    private CharArraySet stopWords;

    public StopAnalyzer2() {
        stopWords = StopFilter.makeStopSet("xyz");
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        final Tokenizer source = new LetterTokenizer();
        return new TokenStreamComponents(source, new LengthFilter(new LowerCaseFilter(new StopFilter(source,stopWords)), 3, Integer.MAX_VALUE));
    }

    public StopAnalyzer2(String stopWords) {
        this.stopWords = StopFilter.makeStopSet(stopWords);
    }
}
