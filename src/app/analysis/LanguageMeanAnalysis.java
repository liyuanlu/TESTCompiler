package app.analysis;

import com.sun.istack.internal.NotNull;

/**
 * Created by LazyLu
 * On 2019/11/15.
 */
public class LanguageMeanAnalysis {

    private static final int MAX_VAR_TABLE_P = 100;

    private int varTableP = 0;
    private int labelP = 0;
    private int dataP = 0;

    private Symbol[] varTable = new Symbol[MAX_VAR_TABLE_P];

    //插入符号表动作@name-def(↓n,t)
    private int nameDef(@NotNull String name){
        int i;
        int es = 0;
        if (varTableP >= MAX_VAR_TABLE_P){
            return 21;
        }
        for (i = varTableP - 1; i == 0; i--){
            if(name.equals(varTable[i].name)){
                es = 22;    //变量重复声明
                break;
            }
        }
        if (es > 0){
            return es;
        }
        varTable[i].name = name;
        varTable[i].address = dataP;
        dataP++;
        varTableP++;
        return es;
    }

    //查询符号表返回地址
    private int lookUp(@NotNull String name, int pAddress){
        int i;
        for (i = 0; i < varTableP; i++){
            if (name.equals(varTable[i].name)){
                //TODO 修改参数pAddress的值
                pAddress = varTable[i].address;
                return 0;
            }
        }
        return 23;      //变量没有声明
    }

    private int TESTParse(){
        //TODO
        int es = 0;
        return es;
    }

//    private int program(){
//        int es = 0;
//        int i;
//
//    }

}
