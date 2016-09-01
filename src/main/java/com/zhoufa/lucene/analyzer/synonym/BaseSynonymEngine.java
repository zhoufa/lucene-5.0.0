package com.zhoufa.lucene.analyzer.synonym;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-09-01-09
 */
public class BaseSynonymEngine implements SynonymEngine {

    private static Map<String, String[]> map = new HashMap<>();

    static{
        map.put("quick", new String[]{"fast", "speedy"});
        map.put("jumps", new String[]{"leaps", "hops"});
        map.put("over", new String[]{"above"});
        map.put("lazy", new String[]{"apathetic", "sluggish"});
        map.put("dogs", new String[]{"canines", "pooches"});
    }

    @Override
    public String[] getSynonyms(String s) throws IOException {
        return map.get(s);
    }
}
