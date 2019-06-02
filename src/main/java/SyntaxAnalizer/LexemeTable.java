package SyntaxAnalizer;

import java.util.HashMap;
import java.util.Map;

public class LexemeTable {
    private static Map<String, Integer> lexemesMap = new HashMap<>();

    public static Map<String, Integer> getLexemesMap() {
        return  LexemeTable.lexemesMap;
    }

    public static void setLexemesMap(Map<String, Integer> lexemesMap) {
        LexemeTable.lexemesMap = lexemesMap;
    }
//    public static void setLexemesMap(Map<String, Integer> map) {
//        map.forEach((string, integer)-> lexemesMap.put(integer, string));
//    }

    public static void print() {
        lexemesMap.forEach((val, str)-> System.out.println(" " + val + " : " + str));
    }
}
