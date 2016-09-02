package com.zhoufa.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.util.CharArraySet;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-09-02-11
 */
@Deprecated
public class PositionPorterStopAnalyzer extends Analyzer {

    private CharArraySet stopWords;

    public PositionPorterStopAnalyzer(CharArraySet stopWords) {
        this.stopWords = stopWords;
    }

    public PositionPorterStopAnalyzer() {
        this.stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new LowerCaseTokenizer();
        StopFilter sFilter = new StopFilter(source, stopWords);

        return new TokenStreamComponents(source, new PorterStemFilter(sFilter));
    }
}
