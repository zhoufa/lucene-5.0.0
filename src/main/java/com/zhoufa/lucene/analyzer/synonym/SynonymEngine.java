package com.zhoufa.lucene.analyzer.synonym;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-31-17
 */
public interface SynonymEngine {

    String[] getSynonyms(String s) throws IOException;
}
