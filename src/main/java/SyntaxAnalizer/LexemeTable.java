package SyntaxAnalizer;

import java.util.HashMap;
import java.util.Map;

public class LexemeTable {
    private static Map<String, Integer> lexemesMap = new HashMap<>();
    private static Map<Integer, String> lexemesCodes = new HashMap<>();

    public static Map<String, Integer> getLexemesMap() {
        return  LexemeTable.lexemesMap;
    }

    public static void setLexemesMap(Map<String, Integer> lexemesMap) {
        LexemeTable.lexemesMap = lexemesMap;
    }

    public static void invertLexemesMap(Map<String, Integer> map) {
        map.forEach((string, integer)-> lexemesCodes.put(integer, string));
    }

    public static Map<Integer, String> getLexemesCodes() {
        return lexemesCodes;
    }

    public static void print() {
        lexemesMap.forEach((val, str)-> System.out.println(" " + val + " : " + str));
    }
}
