package app;

import app.analysis.LanguageAnalysis;
import app.analysis.WordAnalysis;

public class App {

    private static final String FILE_PATH = "D:\\QQFiles\\963187848\\FileRecv\\MobileFile\\wordanalysis(1)\\WordAnalysis\\src\\app\\input2.txt";

    public static void main(String[] args) throws Exception {
        WordAnalysis wordAnalysis = new WordAnalysis(FILE_PATH);
        wordAnalysis.readText();
        wordAnalysis.analysisText();
        wordAnalysis.saveText();
        LanguageAnalysis languageAnalysis = new LanguageAnalysis(wordAnalysis.getWords(),wordAnalysis.getID(),wordAnalysis.getNUM());
        languageAnalysis.start();
    }

}