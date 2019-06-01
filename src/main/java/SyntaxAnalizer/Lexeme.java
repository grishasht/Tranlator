package SyntaxAnalizer;

import java.util.HashMap;
import java.util.Map;

public class Lexeme {
    private static Map<Integer, String> lexemesMap = new HashMap<>();

    public static Map<Integer, String> getLexemesMap() {
        return  Lexeme.lexemesMap;
    }

    public static void setLexemesMap(Map<String, Integer> map) {
        map.forEach((string, integer)-> lexemesMap.put(integer, string));
    }

    public static void print() {
        lexemesMap.forEach((val, str)-> System.out.println(" " + val + " : " + str));
    }
}
