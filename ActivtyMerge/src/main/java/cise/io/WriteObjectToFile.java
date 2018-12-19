package cise.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

    /**
     * Created by wangchunhui on 2017/12/18.
     */
    public class WriteObjectToFile {
        public static void writeObjectToFile(Object obj)
        {
            File file =new File("../dataTest/test.txt");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                ObjectOutputStream objOut=new ObjectOutputStream(out);
                objOut.writeObject(obj);
                objOut.flush();
                objOut.close();
                System.out.println("write object success!");
            } catch (IOException e) {
                System.out.println("write object failed");
                e.printStackTrace();
            }
        }

    }

