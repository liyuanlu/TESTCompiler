package app.analysis;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by lylQAQ
 * On 2019/11/19.
 */
public class AbstractMachine {

    private static final String NOT_VALUE = "NULL";

    private Stack<Integer> stack = new Stack<>();
    private int[] memory = new int[100];
    private Map<String,Integer> labelTable = new HashMap<>();
    private String[][] introduces = new String[1000][2];
    private int pc = 0;
    private int length;

    public void start() throws IOException {
        File file = new File("./src/app/middle.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String temp;
        while ((temp = reader.readLine()) != null){
            String[] strings = temp.split(" ");
            if (2 == strings.length){
                introduces[pc][0] = strings[0];
                introduces[pc][1] = strings[1];
            }else if (1 == strings.length){
                introduces[pc][0] = strings[0];
                introduces[pc][1] = NOT_VALUE;
                labelTable.put(strings[0],pc);
            }else {

            }
            pc++;
        }
        length = pc;
        execute();
    }

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
    }

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
                break;
            case "BR":
                pc = labelTable.get(strings[1]);
                break;

        }
    }

    private void operateNoValue(String string){
        Scanner scanner = new Scanner(System.in);
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
                stack.push(scanner.nextInt());
                scanner.close();
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
