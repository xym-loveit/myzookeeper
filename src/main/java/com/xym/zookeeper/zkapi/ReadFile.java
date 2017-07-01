package com.xym.zookeeper.zkapi;

import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * desc
 *
 * @author xym
 */
public class ReadFile {
    public static void main(String[] args) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = Files.newReader(new File("src/main/resource/a.txt"), Charset.forName("UTF-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                for (String str : URLDecoder.decode(line, "utf-8").split(",")) {
                    System.out.println(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}