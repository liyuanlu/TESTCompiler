package app.analysis;

import java.io.*;

public class Utils {

    public static String readStringFromTxt(String path){
        if (path == null || path.isEmpty()){
            System.out.println("file path is null !");
            return "";
        }
        File file = new File(path);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null){
                sbf.append(tempStr);
                sbf.append(' ');
            }
            reader.close();
            return sbf.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    public static void saveStringToTxt(String path,String content){
        File file = new File(path);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content,0,content.length()-1);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
