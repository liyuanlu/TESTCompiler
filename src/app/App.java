package app;

import app.analysis.LanguageAnalysis;
import app.analysis.WordAnalysis;

public class App {

    private static final String FILE_PATH = "/home/liyuanlu/newDisk/data/IdeaProjects/CompilerPrinple/src/app/input3.txt";

    public static void main(String[] args) throws Exception {
        WordAnalysis wordAnalysis = new WordAnalysis(FILE_PATH);
        wordAnalysis.readText();
        wordAnalysis.analysisText();
        wordAnalysis.saveText();
        LanguageAnalysis languageAnalysis = new LanguageAnalysis(wordAnalysis.getWords(),wordAnalysis.getID(),wordAnalysis.getNUM());
        languageAnalysis.start();
    }

}