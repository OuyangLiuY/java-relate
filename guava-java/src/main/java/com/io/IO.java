package com.io;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.File;
import java.net.URL;
import java.util.List;

public class IO {
    public static void main(String[] args) {


        File file = new File("test.txt");
        List<String> list = null;
        try {
            list = Files.readLines(file, Charsets.UTF_8);
        } catch (Exception e) {
        }
      /*
        Files.copy(from,to);  //复制文件
        Files.deleteDirectoryContents(File directory); //删除文件夹下的内容(包括文件与子文件夹)
        Files.deleteRecursively(File file); //删除文件或者文件夹
        Files.move(File from, File to); //移动文件
        */
        URL url = Resources.getResource("abc.xml"); //获取classpath根下的abc.xml文件url

    }
}
