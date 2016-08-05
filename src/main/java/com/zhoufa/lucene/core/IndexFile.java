package com.zhoufa.lucene.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-04-14
 */
public class IndexFile {

    public static void main(String[] args) throws IOException {
        createIndex("E:\\study\\Lucene-writer\\writer.txt", "E:\\study\\Lucene-index");

        readIndex("E:\\study\\Lucene-index");
    }

    private static void readIndex(String indexPath) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        PhraseQuery query = new PhraseQuery();
        query.add(new Term("contents", "内容"));
//        TopScoreDocCollector collector = TopScoreDocCollector.create(1);
        TopDocs topDocs = searcher.search(query, 1);
        ScoreDoc[] docs = topDocs.scoreDocs;
        System.out.println("文件个数："+docs.length);
        for (ScoreDoc doc : docs) {
            Document document = searcher.doc(doc.doc);
            System.out.println("文档内容" + document.get("contents"));
            System.out.println("文件路径" + document.get("path"));
        }
    }

    /**
     *
     * @param dirPath 读取文件的目录
     * @param indexPath 索引文件
     * @throws IOException
     */
    private static void createIndex(String dirPath, String indexPath) throws IOException {

        createIndex(dirPath, indexPath, false);
    }

    /**
     *
     * @param dirPath     读取文件的目录
     * @param indexPath     索引文件
     * @param createOrAppend    true： 始终创建索引  false ： 没有就创建，否则只是读取
     * @throws IOException
     */
    private static void createIndex(String dirPath, String indexPath, boolean createOrAppend) throws IOException {
        long start = System.currentTimeMillis();
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        Path docDirPath = Paths.get(dirPath);
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

    private static void indexDocs(final IndexWriter writer, Path docDirPath) throws IOException {
        if (Files.isDirectory(docDirPath)) {
            System.out.println("directory");
            Files.walkFileTree(docDirPath, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return null;
                }

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    System.out.println(path.getFileName());
                    indexDoc(writer, path, attrs.lastModifiedTime().toMillis());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return null;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return null;
                }
            });
        } else {
            indexDoc(writer, docDirPath, Files.getLastModifiedTime(docDirPath).toMillis());
        }
    }

    /**
     * 读取文件创建索引
     *
     * @param writer
     *            索引写入器
     * @param file
     *            文件路径
     * @param lastModified
     *            文件最后一次修改时间
     * @throws IOException
     */
    private static void indexDoc(IndexWriter writer, Path file, long lastModified)
            throws IOException {
        InputStream stream = Files.newInputStream(file);
        Document doc = new Document();

        Field pathField = new StringField("path", "我是地址", Field.Store.YES);
        doc.add(pathField);

//        doc.add(new LongField("modified", lastModified, Field.Store.YES));
        doc.add(new TextField("contents", "我是内容",Field.Store.YES));
//        doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

        if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
            System.out.println("adding " + file);
            writer.addDocument(doc);
            writer.addDocuments(null);
        } else {
            System.out.println("updating " + file);
            writer.updateDocument(new Term("path", file.toString()), doc);
        }
        writer.commit();
    }

    /**
     * InputStream转换成String
     * @param is    输入流对象
     * @return
     */
    private static String intputStream2String(InputStream is) {
        BufferedReader bufferReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            bufferReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((line = bufferReader.readLine()) != null) {
                stringBuilder.append(line + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }
}
