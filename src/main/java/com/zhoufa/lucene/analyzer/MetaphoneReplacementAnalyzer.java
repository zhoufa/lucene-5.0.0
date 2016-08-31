package com.zhoufa.lucene.analyzer;

import com.zhoufa.lucene.filter.MetaphoneReplacementFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

/**
 * 近音词
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-31-14
 */
public class MetaphoneReplacementAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new LetterTokenizer();
        //一个Analyzer不能有多个Tokenizer，是经过一个Tokenizer产生一个TokenStream,然后用TokenFilter去过滤之前产生的TokenStream
        //Tokenize -->  TokenStream --> TokenFilter
//        return new TokenStreamComponents(source, new MetaphoneReplacementFilter(new LetterTokenizer()));,
        return new TokenStreamComponents(source, new MetaphoneReplacementFilter(source));
    }
}
