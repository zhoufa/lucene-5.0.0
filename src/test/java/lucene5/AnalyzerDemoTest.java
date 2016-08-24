package lucene5;

import com.zhoufa.lucene.analyzer.AnalyzerDemo;
import junit.framework.TestCase;

import java.io.IOException;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-24-11
 */
public class AnalyzerDemoTest extends TestCase {

//    private String msg = "The quick brown fox jumped over the lazy dogs";
    private String msg = "XY&Z Corporation - xyz@example.com";

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
}
