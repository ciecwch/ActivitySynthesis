package cise.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by wangchunhui on 2017/12/19.
 */
public class WriteStringToFile {
    public static void writeStringToFile(String s) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("../dataTest/wrong.txt"));
            bufferedWriter.write(s);
            System.out.println("write string success!");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("write string failed");
            e.printStackTrace();
        }
    }
}
