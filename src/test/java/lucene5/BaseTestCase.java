package lucene5;

import junit.framework.TestCase;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-22-17
 */
public class BaseTestCase extends TestCase {
    private static String INDEX_DIR = "E:\\solrhome\\Lucene-index";

    public static IndexSearcher searcher;
    public static IndexReader reader;

    static {
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIR)));
            searcher = new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
