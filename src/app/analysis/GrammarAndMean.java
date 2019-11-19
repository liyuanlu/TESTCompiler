package app.analysis;

import java.util.*;

/**
 * 语法分析及中间代码生成程序
 * Created by LazyLu
 * On 2019/11/15.
 */
public class GrammarAndMean {

    private static final int MAX_VAR_TABLE_P = 1000;

    private StringBuilder middle = new StringBuilder();

    private List<String> ID;
    private List<String> NUM;
    private List<String> firstA = new ArrayList<>();
    private List<String> firstB = new ArrayList<>();
    private List<String> firstC = new ArrayList<>();
    private List<String> firstD = new ArrayList<>();
    private List<String> firstE = new ArrayList<>();
    private List<String> firstF = new ArrayList<>();
    private List<String> firstG = new ArrayList<>();
    private List<String> firstH = new ArrayList<>();
    private List<String> firstI = new ArrayList<>();
    private List<String> firstJ = new ArrayList<>();
    private List<String> firstK = new ArrayList<>();
    private List<String> firstL = new ArrayList<>();
    private List<String> firstM = new ArrayList<>();
    private List<String> firstN = new ArrayList<>();
    private List<String> firstO = new ArrayList<>();
    private List<String> firstP = new ArrayList<>();
    private List<String> firstQ = new ArrayList<>();
    private List<String> firstR = new ArrayList<>();
    private List<String> firstS = new ArrayList<>();
    private List<String> firstT = new ArrayList<>();
    private List<String> firstU = new ArrayList<>();
    private List<String> firstV = new ArrayList<>();
    private List<String> firstW = new ArrayList<>();
    private List<String> firstX = new ArrayList<>();
    private List<String> firstY = new ArrayList<>();
    private List<String> firstZ = new ArrayList<>();
    private List<String> firstAA = new ArrayList<>();
    private List<String> firstAB = new ArrayList<>();

    private List<String> followProgram = new ArrayList<>();
    private List<String> followDeclarationList = new ArrayList<>();
    private List<String> followDeclarationListNew = new ArrayList<>();
    private List<String> followStatementList = new ArrayList<>();
    private List<String> followStatementListNew = new ArrayList<>();
    private List<String> followArithmeticExpression = new ArrayList<>();
    private List<String> followTermNew = new ArrayList<>();
    private List<String> followArithmeticExpressionNew = new ArrayList<>();

    private List<String> texts;

    private int index = 0;

    private String ch = "";

    private Symbol[] varTable = new Symbol[MAX_VAR_TABLE_P];
    private int varTableP = 0;
    private int labelP = 0;
    private int dataP = 0;
    private Set<String> notDefaultValue = new HashSet<>();

    public GrammarAndMean(List<String> texts, List<String> ID, List<String> NUM) {
        this.texts = texts;
        this.ID = ID;
        this.NUM = NUM;
        initSet();
    }

    /**
     * init sets.
     */
    private void initSet() {
        firstA.add("{");
        firstB.add("int");
        firstB.add("ε");
        firstC.addAll(firstB);
        firstD.add("int");
        firstE.add("if");
        firstE.add("while");
        firstE.add("for");
        firstE.add("read");
        firstE.add("write");
        firstE.add("{");
        firstE.addAll(ID);
        firstE.add(";");
        firstE.add("ε");
        firstF.addAll(firstE);
        firstG.add("if");
        firstH.add("while");
        firstI.add("for");
        firstJ.add("read");
        firstK.add("write");
        firstL.add("{");
        firstM.addAll(ID);
        firstN.add(";");
        firstO.add("while");
        firstP.add("for");
        firstQ.add("read");
        firstR.addAll(ID);
        firstS.add("(");
        firstS.addAll(ID);
        firstS.addAll(NUM);
        firstT.addAll(firstS);
        firstU.add("(");
        firstV.add("*");
        firstV.add("/");
        firstV.add("ε");
        firstW.add("+");
        firstW.add("-");
        firstW.add("ε");
        firstX.add("write");
        firstY.add("{");
        firstZ.addAll(ID);
        firstAA.add("if");
        firstAB.add("(");
        firstAB.addAll(ID);
        firstAB.addAll(NUM);
        followProgram.add("#");
        followDeclarationList.add("if");
        followDeclarationList.add("while");
        followDeclarationList.add("for");
        followDeclarationList.add("read");
        followDeclarationList.add("write");
        followDeclarationList.add("{");
        followDeclarationList.add(";");
        followDeclarationList.addAll(ID);
        followDeclarationListNew.add("if");
        followDeclarationListNew.add("while");
        followDeclarationListNew.add("for");
        followDeclarationListNew.add("read");
        followDeclarationListNew.add("write");
        followDeclarationListNew.add("{");
        followDeclarationListNew.addAll(ID);
        followDeclarationListNew.add(";");
        followStatementList.add("{");
        followStatementListNew.add("}");
        followArithmeticExpression.add(";");
        followArithmeticExpression.add(">=");
        followArithmeticExpression.add("<=");
        followArithmeticExpression.add("!=");
        followArithmeticExpression.add("==");
        followArithmeticExpression.add(")");
        followArithmeticExpression.add(">");
        followArithmeticExpression.add("<");
        followArithmeticExpressionNew.addAll(followArithmeticExpression);
        followTermNew.add(";");
        followTermNew.add(")");
        followTermNew.add("+");
        followTermNew.add("-");
        followTermNew.add("!=");
        followTermNew.add("==");
        followTermNew.add(">=");
        followTermNew.add("<=");
        followTermNew.add("<");
        followTermNew.add(">");
        followTermNew.add("(");
        followTermNew.addAll(ID);
        followTermNew.addAll(NUM);
    }

    //获取下一个字符函数
    private void getNextSymbol() throws Exception {
        if (texts.size() == 0){     //提示没有单词
            System.out.println("no word！");
        }else if (index < texts.size()){
            ch = texts.get(index);
            index++;
        }else {
            //读完单词后为匹配抛出异常
            throw new Exception("error");
        }
    }

    /**
     * 查找符号表
     * @param name 符号名字
     * @return 在内存中的地址
     */
    private int lookUp(String name) throws Exception {
        for (int i = 0; i < varTableP; i++){
            if (name.equals(varTable[i].name)){
                if (isFirst(name)){
                    //第一次出现后标记为出现过
                    varTable[i].isFirst = false;
                }else {
                    //不是第一次出现且没有赋初值则提示
                    if (!varTable[i].isInit){
                        notDefaultValue.add(name);
                    }
                }
                return varTable[i].address;
            }
        }
        throw new Exception(name + "未定义");
    }

    /**
     * 把变量添加到符号表
     * @param name 变量名
     */
    private void nameDef(String name) throws Exception {
        if (varTableP > MAX_VAR_TABLE_P){
            throw new Exception("符号表已满！");
        }
        varTable[varTableP] = new Symbol();
        varTable[varTableP].name = name;
        varTable[varTableP].address = dataP;
        dataP++;
        varTableP++;
    }

    /**
     * 修改第一次出现标记
     * @param name 变量名
     * @param isInit 是否出现
     */
    private void setFirst(String name,boolean isInit){
        for (int i = 0; i < varTableP; i++){
            if (name.equals(varTable[i].name)){
                varTable[i].isFirst = isInit;
                return;
            }
        }
    }

    /**
     * 修改是否赋初值标记
     * @param name 变量名
     * @param isInit 是否赋初值
     */
    private void setInit(String name,boolean isInit){
        for (int i = 0; i < varTableP; i++){
            if (name.equals(varTable[i].name)){
                varTable[i].isInit = isInit;
                return;
            }
        }
    }

    private boolean isFirst(String name) throws Exception {
        for(Symbol symbol : varTable){
            if (name.equals(symbol.name)){
                return symbol.isFirst;
            }
        }
        throw new Exception("没有找到该变量是否第一次出现！");
    }

    //语法分析程序开始
    public void start() throws Exception {
        getNextSymbol();    //获取下一个字符
        try {
            program();
            System.out.println("No errors！");
            if (notDefaultValue.size() > 0){
                //输出没有赋初值的警告
                System.out.println("Warning:");
                for (String s : notDefaultValue){
                    System.out.println("变量"+ s + "没有赋初值！");
                }
            }
            ReadTxtUtil.saveStringToTxt("/home/liyuanlu/newDisk/data/IdeaProjects/CompilerPrinple/src/app/middle.txt",middle.toString());
        } catch (Exception e) {
            //输出语法分析错误
            System.out.println("index:" + index + " 符号" + ch + " " + e.getMessage());
        }
    }

    //A
    private void program() throws Exception {
        //FIRST集合判断
        if (firstA.contains(String.valueOf(ch))){
           getNextSymbol();     //非终结符函数递归调用
           declarationList();
           statementList();
        }else if (firstA.contains("ε") && followProgram.contains(ch)){
            //FOLLOW集合判断，满足条件推出ε，直接返回。
            getNextSymbol();
            return;
        }else {
            //抛出语法错误
            error(ch + "错误");
        }
        print("STOP");
    }

    //E
    private void statementList() throws Exception {
        if (firstE.contains(ch)){
            statementListNew();
        }else if (firstE.contains("ε") && followStatementList.contains(ch)){
            return;
        }else {
            error(ch + "错误");
        }
    }

    //F
    private void statementListNew() throws Exception {
        if (firstF.contains(ch)){
            statement();
            statementListNew();
        }else if (firstF.contains("ε") && followStatementListNew.contains(ch)){
            return;
        }else {
            error(ch + "错误");
        }
    }

    //G
    private void statement() throws Exception {
        //逐层非终结符递归调用
        if (firstG.contains(ch)){
            ifStat();
        }else if (firstH.contains(ch)){
            whileStat();
        }else if (firstI.contains(ch)){
            forStat();
        }else if (firstJ.contains(ch)){
            readStat();
        }else if (firstK.contains(ch)){
            writeStat();
        }else if (firstL.contains(ch)){
            compoundStat();
        }else if (firstM.contains(ch)){
            assignmentStat();
        }else if (firstN.contains(ch)){
            getNextSymbol();
        }else{
            error(ch + "错误");
        }
    }

    //Z
    private void assignmentStat() throws Exception {
        if (firstZ.contains(ch)){
            assignmentExpression();
            print("POP");
            if (";".equals(ch)){
                //匹配到终结符后读取下一个字符并返回
                getNextSymbol();
            }else{
                error("index:" + index + "  " + ch + "前缺少;");
            }
        }else{
            error(ch + "错误");
        }
    }

    //Y
    private void compoundStat() throws Exception {
        if (firstY.contains(ch)){
            getNextSymbol();
            statementList();
            if ("}".equals(ch)){
                getNextSymbol();
            }else{
                error("index:" + index + "  " + ch + "前缺少)");
            }
        }else{
            error(ch + "错误");
        }
    }

    //X
    private void writeStat() throws Exception {
        if (firstX.contains(ch)){

            getNextSymbol();
            arithmeticExpression();
            if (";".equals(ch)){
                print("OUT");
                getNextSymbol();
            }else{
                error("index:" + index + "  " + ch + "前缺少;");
            }
        }else{
            error(ch + "错误");
        }
    }

    //Q
    private void readStat() throws Exception {
        int address;
        if (firstQ.contains(ch)){
            getNextSymbol();
            if (ID.contains(ch)){
                setInit(ch,true);
                address = lookUp(ch);
                print("IN");
                print("STO " + address);
                print("POP");
                getNextSymbol();
                if (";".equals(ch)){
                    getNextSymbol();
                }else{
                    error("index:" + index + "  " + ch + "前缺少;");
                }
            }else{
                error(ch + "不是变量名");
            }
        }else{
            error(ch + "错误");
        }
    }

    //P
    private void forStat() throws Exception {
        int label1,label2,label3,label4;
        if(firstP.contains(ch)){
            getNextSymbol();
            if ("(".equals(ch)){
                getNextSymbol();
                assignmentExpression();
                print("POP");
                if (";".equals(ch)){
                    label1 = labelP++;
                    print("LABEL" + label1 + ":");
                    getNextSymbol();
                    boolExpression();
                    label2 = labelP++;
                    print("BRF LABEL" + label2);
                    label3 = labelP++;
                    print("BR LABEL" + label3);
                    if (";".equals(ch)){
                        label4 = labelP++;
                        print("LABEL" + label4 + ":");
                        getNextSymbol();
                        assignmentExpression();
                        print("POP");
                        print("BR LABEL" + label1);
                        if (")".equals(ch)){
                            print("LABEL" + label3 + ":");
                            getNextSymbol();
                            statement();
                            print("BR LABEL" + label4);
                            print("LABEL" + label2 + ":");
                        }else{
                            error("index:" + index + "  " + ch + "前缺少)");
                        }
                    }else{
                        error("index:" + index + "  " + ch + "前缺少;");
                    }
                }else{
                    error("index:" + index + "  " + ch + "前缺少;");
                }
            }else{
                error("index:" + index + "  " + ch + "前缺少(");
            }
        }else{
            error(ch + "错误");
        }
    }

    //ID
    private void assignmentExpression() throws Exception {
        if (ID.contains(ch)){
            int address;
            setFirst(ch,true);      //标记为第一次出现
            address = lookUp(ch);
            String var = ch;                   //保存当前符号
            getNextSymbol();
            if ("=".equals(ch)){
                getNextSymbol();
                arithmeticExpression();
                print("STO " + address);
                setInit(var,true);      //标记已经赋初值
            }else{
                error("index:" + index + "  " + ch + "前缺少=");
            }
        }else{
            error(ch + "错误");
        }
    }

    //S
    private void arithmeticExpression() throws Exception {
        if (firstS.contains(ch)){
            term();
            arithmeticExpressionNew();
        }else if (firstS.contains("ε") && followArithmeticExpression.contains(ch)){
            getNextSymbol();
        }else {
            error(ch + "错误");
        }
    }

    //
    private void arithmeticExpressionNew() throws Exception {
        if ("+".equals(ch) || "-".equals(ch)){
            String token = ch;
            getNextSymbol();
            term();
            if ("+".equals(token)){
                print("ADD");
            }else {
                print("SUB");
            }
            arithmeticExpressionNew();
        }else if (firstW.contains("ε") && followArithmeticExpressionNew.contains(ch)){
            return;
        }else{
            error(ch + "错误");
        }
    }

    //T
    private void term() throws Exception {
        if (firstT.contains(ch)){
            factor();
            termNew();
        }else {
            error(ch + "错误");
        }
    }

    private void termNew() throws Exception {
        if ("*".equals(ch) || "/".equals(ch)){
            String token = ch;
            getNextSymbol();
            termNew();
            factor();
            if ("*".equals(token)){
                print("MULT");
            }else {
                print("DIV");
            }
        }else if (firstV.contains("ε") && followTermNew.contains(ch)){
            return;
        }else{
            error(ch + "错误");
        }
    }

    //U
    private void factor() throws Exception {
        if (firstU.contains(ch)){
            getNextSymbol();
            arithmeticExpression();
            if (")".equals(ch)){
                getNextSymbol();
            }else{
                error("index:" + index + "  " + ch + "前缺少)");
            }
        }else if (ID.contains(ch)){
            int address;
            address = lookUp(ch);
            print("LOAD " + address);
            getNextSymbol();
        }else if (NUM.contains(ch)){
            print("LOADI " + ch);
            getNextSymbol();
        }else{
            error(ch + "错误");
        }
    }

    //O
    private void whileStat() throws Exception {
        int label1,label2;
        if (firstO.contains(ch)){
            label1 = labelP++;
            print("LABEL" + label1 + ":");
            getNextSymbol();
            if ("(".equals(ch)){
                getNextSymbol();
                boolExpression();
                if (")".equals(ch)){
                    label2 = labelP++;
                    print("BRF LABEL" + label2);
                    getNextSymbol();
                    statement();
                    print("BR LABEL" + label1);
                    print("LABEL" + label2 + ":");
                }else{
                    error("index:" + index + "  " + ch + "前缺少)");
                }
            }else{
                error("index:" + index + "  " + ch + "前缺少(");
            }
        }else{
            error(ch + "错误");
        }
    }

    //AB
    private void boolExpression() throws Exception {
        if (firstAB.contains(ch)){
            arithmeticExpression();
            switch (ch){
                case ">":
                case "<":
                case ">=":
                case "<=":
                case "==":
                case "!=":
                    String token = ch;
                    getNextSymbol();
                    arithmeticExpression();
                    if (">".equals(token)){
                        print("GT");
                    }else if (">=".equals(token)){
                        print("GE");
                    }else if ("<".equals(token)){
                        print("LES");
                    }else if ("<=".equals(token)){
                        print("LE");
                    }else if ("==".equals(token)){
                        print("EQ");
                    }else if ("!=".equals(token)){
                        print("NOTEQ");
                    }
                    return;
                default:
                error(ch + "错误");
            }
        }else{
            error(ch + "错误");
        }
    }

    //AA
    private void ifStat() throws Exception {
        int label1,label2;
        if (firstAA.contains(ch)){
            getNextSymbol();
            if ("(".equals(ch)){
                getNextSymbol();
                boolExpression();
                if (")".equals(ch)){
                    label1 = labelP++;
                    print("BRF LABEL" + label1);
                    getNextSymbol();
                    statement();
                    label2 = labelP++;
                    if ("else".equals(ch)){
                        getNextSymbol();
                        statement();
                    }
                    print("LABEL" + label2 + ":");
                }else{
                    error("index:" + index + "  " + ch + "前缺少)");
                }
            }else{
                error("index:" + index + "  " + ch + "前缺少(");
            }
        }else{
            error(ch + "错误");
        }
    }

    //B
    private void declarationList() throws Exception {
        if (firstB.contains(ch)){
            declarationListNew();
        }else if (firstB.contains("ε") && followDeclarationList.contains(ch)){
            getNextSymbol();
        }else {
            error(ch + "错误");
        }
    }

    //C
    private void declarationListNew() throws Exception {
        if (firstC.contains(ch)){
            declarationStat();
            declarationListNew();
        }else if (firstC.contains("ε") && followDeclarationListNew.contains(ch)){
            return;
        }else{
            error(ch + "错误");
        }
    }

    //D
    private void declarationStat() throws Exception {
        if (firstD.contains(ch)){
            getNextSymbol();
            if (ID.contains(ch)){
                nameDef(ch);
                getNextSymbol();
                if (";".equals(ch)){
                    getNextSymbol();
                }else{
                    error("index:" + index + "  " + ch + "前缺少;");
                }
            }else{
                error(ch + "不是变量名");
            }
        }else{
            error(ch + "错误");
        }
    }

    private void error(String message) throws Exception {
        throw new Exception(message);
    }

    private void print(String message){
        System.out.println(message);
        middle.append(message).append("\n");
    }

}
