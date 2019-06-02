package SyntaxAnalizer;

public class Service {

    public static void throwError(String errType) {
        System.out.println("Syntax error: " + errType);
    }

    public static Integer getLexemeCode(String lexeme) {
        //if (LexemeTable.getLexemesMap().get(lexeme) != null)
            return LexemeTable.getLexemesMap().get(lexeme);
        //else return 0;
    }
}
