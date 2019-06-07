package SyntaxAnalizer;

import LexAnalizer.Lexeme;

import java.util.List;

public class Service {

    public static void throwError(String errType) {
        System.out.println("Syntax error: " + errType);
    }

    public static String lineColumnError(LexemeList lexemeList) {
        return " Line = "
                + lexemeList.getFirst().lexemeRow;
        // + ". Column = " + lexemeList.getFirst().lexemeColumn;
    }

    public static void printTree(Node node, String tabs) {
        if (node.getData() != null)
            System.out.println(tabs + node.getRule() + " " + node.getData());
        else System.out.println(tabs + node.getRule());
        tabs += "   ";
        for (Node node1 : node.getChildren()) {
            if (node1.getData() != null) {
                System.out.println(printData(node1, tabs));
            } else {
                printTree(node1, tabs);
            }
        }
    }

    public static String printData(Node node, String tabs) {
        return tabs +  node.getData() + " " + node.getRule() + " " ;
    }
}
