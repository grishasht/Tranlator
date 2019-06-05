package SyntaxAnalizer;

import LexAnalizer.Lexeme;

public class Service {

    public static void throwError(String errType) {
        System.out.println("Syntax error: " + errType);
    }

    public static String lineColumnError(LexemeList lexemeList) {
        return " Line = "
                + lexemeList.getFirst().lexemeRow;
        // + ". Column = " + lexemeList.getFirst().lexemeColumn;
    }

    public static void printTree(Node node, int i) {
        for (Node node1 : node.getChildren()) {
            System.out.println(node.getChildren() + setTabs(i));
            if (node1 != null)
                printTree(node1, ++i);
        }
    }

    public static String setTabs(int n) {
        String tabs = "";
        for(int i = 0; i < n; i++){
            tabs += "  ";
        }
        return tabs;
    }
}
