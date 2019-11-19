package app.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析程序
 * Created by LazyLu
 * On 2019/11/15.
 */
public class WordAnalysis {

    private static final String SOURCE_CODE_PATH = "./src/app/SourceCode.txt";
//    private static final String SOURCE_CODE_PATH = "./src/app/GrammarTestSourceCode.txt";
//    private static final String SOURCE_CODE_PATH = "./src/app/WordTestSourceCode.txt";
    private static final String SOURCE_CODE_WORDS_PATH = "./src/app/words.txt";

    private List<String> words = new ArrayList<>();     //保留字
    private List<Character> divideChars = new ArrayList<>();    //分界符
    private List<Character> operator = new ArrayList<>();       //+ - *
    private List<Character> operator1 = new ArrayList<>();      //< > =
    private String text;
    private List<String> errors = new ArrayList<>();
    private String right = "";
    private int type = 0;
    private List<String> ID = new ArrayList<>();
    private List<String> NUM = new ArrayList<>();
    private List<String> reserveWords = new ArrayList<>();

    public WordAnalysis() {
        initWords();
    }

    public void start() throws IOException {
        readSourceCode();
        analysisCode();
        saveWords();
    }

    private void initWords(){
        divideChars.add('(');
        divideChars.add(')');
        divideChars.add(';');
        divideChars.add('{');
        divideChars.add('}');
        operator.add('+');
        operator.add('-');
        operator.add('*');
        operator1.add('<');
        operator1.add('>');
        operator1.add('=');
        reserveWords.add("if");
        reserveWords.add("else");
        reserveWords.add("for");
        reserveWords.add("while");
        reserveWords.add("int");
        reserveWords.add("write");
        reserveWords.add("read");
    }

    private void analysisCode(){
        char[] chars = text.toCharArray();
        int status = 0;
        char ch;
        char prech;
        int index = 0;
        for (int i = 0; i < chars.length; i++){
            ch = chars[i];
            if (i == 0){
                prech = ' ';
            }else {
                prech = chars[i-1];
            }
            if (status == 0){
                index = i;
            }
            status = statusTransform(status,ch,prech);
            switch (status){
                case -1:
                    error(chars,i);
                    status = 0;
                    break;
                case -3:
                    error(chars,i-1);
                    status = 0;
                    break;
                case -2:
                    status = 0;
                    i--;
                    saveRightWord(index,i);
                    index = i + 1;
                    break;
                case -4:
                    status = 0;
            }
        }
        if (status == 8 || status == 9){
            finalError(index);
        }else if (status == 6){
            System.out.println("error:！字符不合法" + "  index:" + index);
        }
        for (String s : errors){
            System.out.println(s);
        }
    }

    /**
     * 状态转换方法
     * @param statusNo 当前状态编号
     * @param ch    当前识别的符号
     * @param preCh 上一个识别的符号
     * @return  下一个状态编号
     */
    private int statusTransform(int statusNo,char ch,char preCh){
        type = statusNo;
        if (' ' == ch){     //对输入字符为空格进行额外处理
            if (8 == statusNo || 0 ==statusNo){
                return statusNo;
            }else {
                return -2;
            }
        }else if ('\n' == ch){
            return -2;
        }
        switch (statusNo){
            case 0:             //当前状态为其实状态
                if ('0' == ch){
                    return 11;
                }
                if (Character.isLetter(ch)){
                    return 1;
                }else if (Character.isDigit(ch)){
                    return 2;
                }else if (divideChars.contains(ch)){
                    return 3;
                }else if (operator.contains(ch)){
                    return 4;
                }else if (operator1.contains(ch)){
                    return 5;
                }else if ('!' == ch){
                    return 6;
                }else if ('/' == ch){
                    return 7;
                }else {
                    return -1;
                }
            case 1:
                if (Character.isDigit(ch) || Character.isLetter(ch)){
                    return 1;
                }else {
                    return -2;      //该状态表明需要已识别一个正确的单词并退回一个字符
                }
            case 2:
                if ('0' == preCh){
                    if (ch >= '1' && ch <= '9'){
                        return 2;
                    }else {
                        return -2;
                    }
                }else {
                    if (Character.isDigit(ch)){
                        return 2;
                    }else {
                        return -2;
                    }
                }
            case 3:
            case 4:
            case 10:
            case 11:
                return -2;
            case 5:
                if ('=' == ch){
                    return 4;
                }else {
                    return -2;
                }
            case 6:
                if ('=' == ch){
                    return 4;
                }else {
                    return -3;
                }
            case 7:
                if ('*' == ch){
                    return 8;
                }else {
                    return -2;
                }
            case 8:
                if ('*' == ch){
                    return 9;
                }else {
                    return 8;
                }
            case 9:
                if ('*' == ch){
                    return 9;
                }else if ('/' == ch){
                    return 10;
                }else {
                    return 8;
                }
            default:
                    return -1;      //未处理的情况都返回为错误状态
        }
    }

    /**
     * 保存单词
     */
    private void saveWords() throws IOException {
        ReadTxtUtil.saveStringToTxt(SOURCE_CODE_WORDS_PATH,right);
    }

    /**
     * 读取源代码
     */
    private void readSourceCode() throws IOException {
        text = ReadTxtUtil.readStringFromTxt(SOURCE_CODE_PATH);
    }


    public List<String> getWords(){
        return words;
    }

    public List<String> getID(){
        return ID;
    }

    public List<String> getNUM(){
        return NUM;
    }

    private void saveRightWord(int start, int end){
        right += text.substring(start,end+1) + "\n";
        words.add(text.substring(start,end+1));
        if (type == 1 && !reserveWords.contains(text.substring(start,end+1))){
            ID.add(text.substring(start,end+1));
        }else if (type == 2 || type == 11){
            NUM.add(text.substring(start,end+1));
        }
    }

    private void error(char[] chars,int index) {
        errors.add("error,未知符号: " + chars[index] + "  index:" + index);
    }

    private void finalError(int index){
        errors.add("error,注释不完整:" + text.substring(index,text.length()-1) + "  index:" + index);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
