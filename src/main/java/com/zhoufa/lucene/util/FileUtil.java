package com.zhoufa.lucene.util;

import java.io.File;

/**
 * @author zhou.fa@diligentfirst.com
 * @create 2016-08-11-16
 */
public class FileUtil {

    private static boolean flag = false;

    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (files != null) {
            for (File file : files) {
                //删除子文件
                if (file.isFile()) {
                    flag = deleteFile(file.getAbsolutePath());
                    if (!flag) break;
                } //删除子目录
                else {
                    flag = deleteDirectory(file.getAbsolutePath());
                    if (!flag) break;
                }
            }
        }
        if (!flag) return false;
        //删除当前目录
        return dirFile.delete();
    }

    private static boolean deleteFile(String sPath) {
        flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
