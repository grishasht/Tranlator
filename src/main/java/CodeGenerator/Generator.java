package CodeGenerator;

import LexAnalizer.Lexeme;
import SyntaxAnalizer.LexemeList;
import SyntaxAnalizer.LexemeTable;
import SyntaxAnalizer.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Generator {
    private FileWriter fileOut;
    private Set<String> identSet = new HashSet<>();
    private List<String> procedures = new LinkedList<>();
    private Map<Integer, String> lexemeTable;

    {
        LexemeTable.invertLexemesMap(LexemeTable.getLexemesMap());
        lexemeTable = LexemeTable.getLexemesCodes();
    }
//    private Node node;
//
//    public void setNode(Node node) {
//        this.node = node;
//    }

    public void setFileOut(String path) {
        try {
            this.fileOut = new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFile() {
        try {
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String program(Node node) {
        for (Node child : node.getChildren()) {
            try {
                if (child.getRule().equals("<procedure-identifier>")) {
                    procedureIdentifier(child);
                } else if (node.getRule().equals("<identifier>")) {
                    return identifier(node);
                } else if (child.getRule().equals("<variable-declaration>")) {
                    variableDeclaration(child);
                } else if (child.getRule().equals("<declaration>")) {
                    declaration(child);
                } else if (child.getRule().equals("<block>")) {
                    block(child);
                } else if (child.getRule().equals("<statement-list>")) {
                    fileOut.write("CODE SEGMENT" + '\n');
                    statementList(child);
                    fileOut.write("CODE ENDS" + '\n');
                } else {
                    program(child);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return "";
    }

    private String statementList(Node child) {
        return null;
    }

    private void block(Node child) {
        program(child);
        try {
            fileOut.write(procedures.get(procedures.size() - 1) + " END" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void declaration(Node child) {
        Integer code = Integer.valueOf(child.getChildren().get(0)
                .getChildren().get(0)
                .getChildren().get(0)
                .getData());
    }

    private void variableDeclaration(Node child) {
        try {
            fileOut.write("DATA SEGMENT" + "\n");
            program(child);
            fileOut.write("DATA ENDS" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String identifier(Node child) {
        return lexemeTable.get(Integer.parseInt(child.getChildren().get(0).getData()));
    }

    private void procedureIdentifier(Node child) {
        String procName = program(child.getChildren().get(0));
        try {
            fileOut.write(procName + " PROC" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        procedures.add(procName);
    }

}
