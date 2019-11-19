package app.analysis;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Test语言抽象机
 * Created by lylQAQ
 * On 2019/11/19.
 */
public class AbstractMachine {

    private static final String NOT_VALUE = "NULL";
    private static final String MIDDLE_CODE_PATH = "./src/app/middle.txt";

    private Scanner scanner = new Scanner(System.in);
    private Stack<Integer> stack = new Stack<>();
    private int[] memory = new int[100];                        //模拟内存
    private Map<String,Integer> labelTable = new HashMap<>();   //label标签表
    private String[][] introduces = new String[1000][2];        //指令
    private int pc = 0;                                         //当前指令位置
    private int length;                                         //指令总长度

    /**
     * 抽象机开始函数
     */
    public void start() throws IOException {
        //读取中间代码
        File file = new File(MIDDLE_CODE_PATH);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String temp;
        while ((temp = reader.readLine()) != null){
            String[] strings = temp.split(" ");
            if (2 == strings.length){
                introduces[pc][0] = strings[0];
                introduces[pc][1] = strings[1];
            }else if (1 == strings.length){
                introduces[pc][0] = strings[0];
                introduces[pc][1] = NOT_VALUE;  //不带操作数的赋特定值作为标记
                //label标签添加到labelTable中
                if (strings[0].contains(":")){
                    labelTable.put(strings[0].substring(0,strings[0].length()-1),pc);
                }
            }
            pc++;           //指向下一条命令
        }
        length = pc;
        execute();
    }

    /**
     * 执行中间代码
     */
    private void execute(){
        pc = 0;
        while (pc < length){
            if (!NOT_VALUE.equals(introduces[pc][1])){
                operateWithValue(introduces[pc]);
            }else {
                operateNoValue(introduces[pc][0]);
            }
            pc++;
        }
        scanner.close();
    }

    /**
     * 执行带操作数指令
     * @param strings 操作码与操作数
     */
    private void operateWithValue(String[] strings){
        switch (strings[0]){
            case "LOADI":
                stack.push(Integer.parseInt(strings[1]));
                break;
            case "STO":
                memory[Integer.parseInt(strings[1])] = stack.peek();
                break;
            case "LOAD":
                stack.push(memory[Integer.parseInt(strings[1])]);
                break;
            case "BRF":
                if (stack.peek() == 0){
                    pc = labelTable.get(strings[1]);
                }
                stack.pop();
                break;
            case "BR":
                pc = labelTable.get(strings[1]);
                break;

        }
    }

    /**
     * 执行不带操作数指令
     * @param string 操作码
     */
    private void operateNoValue(String string){
        int top;
        int next;
        switch (string){
            case "POP":
                stack.pop();
                break;
            case "LES":
                top = stack.peek();
                stack.pop();
                next = stack.peek();
                stack.pop();
                if (next < top){
                    stack.push(1);
                }else {
                    stack.push(0);
                }
                break;
            case "IN":
                int x = scanner.nextInt();
                stack.push(x);
                break;
            case "ADD":
                top = stack.peek();
                stack.pop();
                next = stack.peek();
                stack.pop();
                stack.push(top + next);
                break;
            case "MULT":
                top = stack.peek();
                stack.pop();
                next = stack.peek();
                stack.pop();
                stack.push(top*next);
                break;
            case "OUT":
                System.out.println(stack.peek());
                stack.pop();
                break;
            case "STOP":
                System.out.println("程序结束！");
                break;
        }
    }

    public static void main(String[] args) {
        try {
            new AbstractMachine().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
