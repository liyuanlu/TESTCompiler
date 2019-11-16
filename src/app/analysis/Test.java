package app.analysis;

import java.util.List;

/**
 * Created by LazyLu
 * On 2019/11/15.
 */
public class Test{


//    public Test(List<String> texts, List<String> ID, List<String> NUM) {
//        super(texts, ID, NUM);
//    }

    private static final int MAX_VAR_TABLE_P = 1000;

    private List<String> ID;
    private List<String> NUM;

    private String ch;
    private String value;

    private Symbol[] varTable = new Symbol[MAX_VAR_TABLE_P];
    private int index = 0;
    private int varTableP = 0;
    private int labelP = 0;
    private int dataP = 0;

    private void getNextSymbol(){
        //TODO 读取并输出下一个变量的变量名以及值
    }

    private String getIndexSymbol(int index){
        return null;
    }

    private String getIndexValue(int index){
        return null;
    }

    private int program(){
        int es = 0;
        int i;
        getNextSymbol();
        if (!"{".equals(ch)){
            es = 1;
            return es;
        }
        getNextSymbol();
        es = declarationList();
        if (es > 0){
            return es;
        }
        if ("}".equals(ch)){
            es = 2;
            return es;
        }
        print("STOP");
        return es;
    }

    private int declarationList() {
        int es = 0;
        while ("int".equals(ch)){
            es = declarationStat();
            if (es > 0){
                return es;
            }
        }
        return es;
    }

    private int declarationStat() {
        int es = 0;
        getNextSymbol();
        if (ID.contains(ch)){
            return 3;
        }
        es = nameDef(value);
        if (es > 0){
            return es;
        }
        getNextSymbol();
        if (!";".equals(ch)){
            return 4;
        }
        getNextSymbol();
        return es;
    }

    private int statementList(){
        int es = 0;
        while (!"}".equals(ch)){
            es = statement();
            if (es > 0){
                return es;
            }
        }
        return es;
    }

    private int statement() {
        int es = 0;
        if (es == 0 && "if".equals(ch)){
            es = ifStat();
        }
        if (es == 0 && "while".equals(ch)){
            es = whileStat();
        }
        if (es == 0 && "for".equals(ch)){
            es = forStat();
        }
        if (es == 0 && "read".equals(ch)){
            es = readStat();
        }
        if (es == 0 && "write".equals(ch)){
            es = writeStat();
        }
        if (es == 0 && "{".equals(ch)){
            es = compoundStat();
        }
        if (es == 0 && (ID.contains(ch) || NUM.contains(ch) || "(".equals(ch))){
            es = expressionStat();
        }
        return es;
    }

    private int expressionStat() {
        int es = 0;
        if (";".equals(ch)){
            getNextSymbol();
            return es;
        }
        es = expression();
        if (es > 0){
            return es;
        }
        print("POP");
        if (";".equals(ch)){
            getNextSymbol();
            return es;
        }else {
            return 4;
        }
    }

    private int compoundStat() {
        int es = 0;
        getNextSymbol();
        es = statementList();
        return es;
    }

    private int writeStat() {
        int es = 0;
        getNextSymbol();
        es = expression();
        if (es > 0){
            return es;
        }
        if (!";".equals(ch)){
            return 4;
        }
        print("OUT");
        getNextSymbol();
        return es;
    }

    private int readStat() {
        int es = 0;
        int address = 0;
        getNextSymbol();
        if (!ID.contains(ch)){
            return 3;
        }
        es = 23;
        for (Symbol s : varTable){
            if (ch.equals(s.name)){
                address = s.address;
                es = 0;
            }
        }
        if (es > 0){
            return es;
        }
        print("IN");
        print("STO " + address);
        print("POP");
        getNextSymbol();
        if (!";".equals(ch)){
            return 4;
        }
        getNextSymbol();
        return es;
    }

    private int forStat() {
        int es = 0;
        int label1;
        int label2;
        int label3;
        int label4;
        getNextSymbol();
        if (!"(".equals(ch)){
            return 5;
        }
        getNextSymbol();
        es = expression();
        if (es > 0){
            return es;
        }
        print("POP");
        if (!";".equals(ch)){
            return 4;
        }
        label1 = labelP++;
        print("LABEL:" + label1);
        getNextSymbol();
        es = expression();
        if (es > 0){
            return es;
        }
        label2 = labelP++;
        print("BRF LABEL" + label2);
        label3 = labelP++;
        print("BR LABEL" + label3);
        if (!";".equals(ch)){
            return 4;
        }
        label4 = labelP++;
        print("LABEL:" + label4);
        getNextSymbol();
        es = expression();
        if (es > 0){
            return es;
        }
        print("POP");
        print("BR LABEL" + label1);
        if (!")".equals(ch)){
            return 6;
        }
        print("LABEL:" + label3);
        getNextSymbol();
        es = statement();
        if (es > 0){
            return es;
        }
        print("BR LABEL" + label4);
        print("LABEL" + label2 + ":");
        return es;
    }

    private int whileStat() {
        int es = 0;
        int label1;
        int label2;
        label1 = labelP++;
        print("LABEL" + label1);
        getNextSymbol();
        if (!"(".equals(ch)){
            return 5;
        }
        getNextSymbol();
        es = expression();
        if (es > 0){
            return es;
        }
        label2 = labelP++;
        print("BRF LABEL" + label2);
        getNextSymbol();
        es = statement();
        print("BR LABEL" + label1);
        print("LABEL:" + label2);
        return es;
    }

    private int ifStat() {
        int es = 0;
        int label1;
        int label2;
        getNextSymbol();
        if (!"(".equals(ch)){
            return 5;
        }
        getNextSymbol();
        es = expression();
        if (es > 0){
            return es;
        }
        if (!")".equals(ch)){
            return 6;
        }
        label1 = labelP++;
        print("BRF LABEL" + label1);
        getNextSymbol();
        es = statement();
        if (es > 0){
            return es;
        }
        label2 = labelP++;
        print("BR LABEL" + label2);
        print("LABEL:" + label1);
        if ("else".equals(ch)){
            getNextSymbol();
            es = statement();
            if (es > 0){
                return es;
            }
        }
        print("LABEL:" + label2);
        return es;
    }

    private int expression() {
        int es = 0;
        int fileAdd;
        String token,token1;
        if (ID.contains(ch)){
            fileAdd = index;
            getNextSymbol();
            token = ch;
            token1 = value;
            index--;
            getNextSymbol();
            if ("=".equals(token)){
                int address = 0;
                es = 23;
                for (Symbol s : varTable){
                    if (ch.equals(s.name)){
                        address = s.address;
                        es = 0;
                    }
                }
                if (es > 0){
                    return es;
                }
                getNextSymbol();
                es = boolExpr();
                if (es > 0){
                    return es;
                }
                print("STO " + address);
            }else {
                index = fileAdd;
                print(ch + " " + value);
                es = boolExpr();
                if (es > 0){
                    return es;
                }
            }
        }else {
            es = boolExpr();
        }
        return es;
    }

    private int boolExpr(){
        int es = 0;
        es = additiveExpr();
        if (es > 0){
            return es;
        }
        if (">".equals(ch) || ">=".equals(ch) || "<".equals(ch) ||
        "<=".equals(ch) || "==".equals(ch) || "!=".equals(ch)){
            String token = ch;
            getNextSymbol();
            es = additiveExpr();
            if(es > 0){
                return es;
            }
            if (">".equals(token)){
                print("GT");
            }
            if (">=".equals(token)){
                print("GE");
            }
            if ("<".equals(token)){
                print("LES");
            }
            if ("<=".equals(token)){
                print("LE");
            }
            if ("==".equals(token)){
                print("EQ");
            }
            if ("!=".equals(token)){
                print("NOTEQ");
            }
        }
        return es;
    }

    private int additiveExpr() {
        int es = 0;
        es = term();
        if (es > 0){
            return es;
        }
        while ("+".equals(ch) || "-".equals(ch)){
            String token;
            token = ch;
            getNextSymbol();
            es = term();
            if (es > 0){
                return es;
            }
            if ("+".equals(token)){
                print("ADD");
            }
            if ("-".equals(token)){
                print("SUB");
            }
        }
        return es;
    }

    private int term() {
        int es = 0;
        es = factor();
        if (es > 0){
            return es;
        }
        while ("*".equals(ch) || "/".equals(ch)){
            String token;
            token = ch;
            getNextSymbol();
            es = factor();
            if (es > 0){
                return es;
            }
            if ("*".equals(token)){
                print("MUL");
            }
            if ("/".equals(token)){
                print("DIV");
            }
        }
        return es;
    }

    private int factor() {
        int es = 0;
        if ("(".equals(ch)){
            getNextSymbol();
            es = expression();
            if (es > 0){
                return es;
            }
            if (!")".equals(ch)){
                return 6;
            }
            getNextSymbol();
        }else {
            if (ID.contains(ch)){
                int address = 0;
                es = 23;
                for (Symbol s : varTable){
                    if (ch.equals(s.name)){
                        address = s.address;
                        es = 0;
                    }
                }
                if (es > 0){
                    return es;
                }
                print("LOAD" + address);
                getNextSymbol();
                return es;
            }
            if (NUM.contains(ch)){
                print("LOADI" + value);
                getNextSymbol();
                return es;
            }else {
                es = 7;
                return es;
            }
        }
        return es;
    }

    private int nameDef(String name) {
        int i;
        int es = 0;
        if (varTableP >= MAX_VAR_TABLE_P){
            return 21;
        }
        for (i = varTableP - 1;i == 0; i--){
            if (varTable[i].name.equals(name)){
                es = 22;
                break;
            }
        }
        if (es > 0){
            return es;
        }
        varTable[varTableP].name = name;
        varTable[varTableP].address = dataP;
        dataP++;
        varTableP++;
        return es;
    }

    private void print(String string){
        System.out.println(string);
    }

}
