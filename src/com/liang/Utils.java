package com.liang;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2021/1/9 16:32
 * @content
 */
public class Utils {

    public static String exec(String cmd,String dir) {
        StringBuilder line = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("cmd /c "+cmd,null,new File(dir));
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String l;
            while ((l = buf.readLine()) != null) {
                line.append(l).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line.toString();
    }
}
