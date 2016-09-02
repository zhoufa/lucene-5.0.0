package lucene5;

import com.zhoufa.lucene.analyzer.AnalyzerDemo;
import com.zhoufa.lucene.analyzer.synonym.BaseSynonymEngine;
import com.zhoufa.lucene.analyzer.synonym.SynonymAnalyzer;
import junit.framework.TestCase;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-24-11
 */
public class AnalyzerDemoTest extends TestCase {

    private String msg = "The quick brown fox jumps over the lazy dogs";
//    private String msg = "XY&Z Corporation to - xyz@example.com";

    private AnalyzerDemo demo;

    protected void setUp() {
        demo = new AnalyzerDemo();
    }

    public void testWhitespaceAnalyzer() throws IOException {
        demo.whitespaceAnalyzer(msg);
    }

    //会去掉字符和数字
    public void testSimpleAnalyzer() throws IOException {
        demo.simpleAnalyzer(msg);
    }

    public void testStandardAnalyzer() throws IOException {
        demo.standardAnalyzer(msg);
    }

    public void testStoppaceAnalyzer() throws IOException {
        demo.stopAnalyzer(msg);
    }

    public void testStopAnalyzer() throws IOException {
        demo.stopAnalyzer2(msg);
    }

    public void testMetaphoneAnalyzer() throws IOException {
        demo.metaphone("The quick brown fox jumped over the lazy dogs");
        System.out.println();
        demo.metaphone("Tha quik brown phox jumpd ovvar tha lazi dogz");
    }

    public void testSynonymAnalyzer() throws IOException {
        demo.synonym("The quick brown fox jumps over the lazy dogs");
        AnalyzerDemo.assertTokensEqual(new SynonymAnalyzer(new BaseSynonymEngine()), "jumps", new String[] {"jumps", "hops", "leaps"});

    }
}
