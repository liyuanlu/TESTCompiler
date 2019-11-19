package app.analysis;

import java.io.*;

public class ReadTxtUtil {

    public static String readStringFromTxt(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sbf = new StringBuilder();
        String tempStr;
        while ((tempStr = reader.readLine()) != null){
            sbf.append(tempStr);
            sbf.append(' ');    //换行转化为空格
        }
        reader.close();
        return sbf.toString();
    }


    public static void saveStringToTxt(String path,String content) throws IOException {
        File file = new File(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content,0,content.length()-1);
        writer.flush();
        writer.close();
    }

}
