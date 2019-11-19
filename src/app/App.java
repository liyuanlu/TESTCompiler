package app;

import app.analysis.AbstractMachine;
import app.analysis.GrammarAndMean;
import app.analysis.WordAnalysis;

public class App {

    public static void main(String[] args) throws Exception {
        WordAnalysis words = new WordAnalysis();
        words.start();
        System.out.println("词法分析成功！");
        GrammarAndMean language = new GrammarAndMean(words.getWords(),words.getID(),words.getNUM());
        language.start();
        System.out.println("语法分析成功");
        AbstractMachine machine = new AbstractMachine();
        System.out.println("输入10个数字");
        machine.start();
    }

}