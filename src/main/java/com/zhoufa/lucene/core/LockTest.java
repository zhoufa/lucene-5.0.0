package com.zhoufa.lucene.core;

import junit.framework.TestCase;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-22-10
 */
public class LockTest extends TestCase {

    private Directory dir;

    protected void setUp() throws IOException {
        String indexDir = System.getProperty("java.io.tmpdir", "tmp") +
                System.getProperty("file.separator") + "index";
        dir = FSDirectory.open(Paths.get(indexDir));
    }

    public void testwritelock() throws IOException {
        IndexWriter writer1 = null;
        IndexWriter writer2 = null;
        try {
            writer1 = new IndexWriter(dir, new IndexWriterConfig(new WhitespaceAnalyzer()));
//            writer1.
            writer2 = new IndexWriter(dir, new IndexWriterConfig(new WhitespaceAnalyzer()));
//            MergePolicy
//            HTMLDocumentImpl
            fail("We should never reach this point");
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        }finally {
            writer1.close();
            assertNull(writer2);
        }
    }

}
