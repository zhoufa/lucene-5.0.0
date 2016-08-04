package com.zhoufa.lucene.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-04-14
 */
public class IndexFile {

    public static void main(String[] args) throws IOException {
        createIndex("", "");
    }

    /**
     *
     * @param dirPath 文件
     * @param indexPath 索引文件
     * @throws IOException
     */
    public static void createIndex(String dirPath, String indexPath) throws IOException {

        createIndex(dirPath, indexPath, false);
    }

    private static void createIndex(String dirPath, String indexPath, boolean createOrAppend) throws IOException {
        long start = System.currentTimeMillis();
        Directory dir = FSDirectory.open(Paths.get(indexPath, new String[0]));
        Path docDirPath = Paths.get(dirPath, new String[0]);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        if (createOrAppend) {
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }
        IndexWriter writer = new IndexWriter(dir, indexWriterConfig);
        indexDocs(writer, docDirPath);
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println("Time consumed:" + (end - start) + " ms");
    }

    private static void indexDocs(IndexWriter writer, Path docDirPath) throws IOException {
        if (Files.isDirectory(docDirPath)) {

        }
    }
}
