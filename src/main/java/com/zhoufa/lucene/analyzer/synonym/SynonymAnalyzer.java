package com.zhoufa.lucene.analyzer.synonym;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-31-17
 */
public class SynonymAnalyzer extends Analyzer {
    private SynonymEngine synonymEngine;

    public SynonymAnalyzer(SynonymEngine synonymEngine) {
        this.synonymEngine = synonymEngine;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new StandardTokenizer();
        return new TokenStreamComponents(source, new SynonymFilter(new LowerCaseFilter(new StopFilter(source, StopAnalyzer.ENGLISH_STOP_WORDS_SET)), synonymEngine));
    }
}
