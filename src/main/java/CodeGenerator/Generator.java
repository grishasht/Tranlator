package CodeGenerator;

import LexAnalizer.ParseSig;
import SyntaxAnalizer.LexemeList;
import SyntaxAnalizer.LexemeTable;
import SyntaxAnalizer.Node;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Generator {
    private FileWriter fileOut;
    private Set<String> identSet = new HashSet<>();
    private List<String> procedures = new LinkedList<>();
    private Map<Integer, String> lexemeTable;
    private LexemeList lexemeList;
    private Boolean ax = true, expression = false;
    private int statementCount = 1;

    {
        ParseSig parseSig = ParseSig.getInstance();
        LexemeTable.invertLexemesMap(LexemeTable.getLexemesMap());
        lexemeTable = LexemeTable.getLexemesCodes();
        try {
            lexemeList = new LexemeList(parseSig.getSigFile(new FileInputStream("src/main/resources/source.sig")));
        } catch (IOException e) {
            System.out.println("Can't open file in Generator.java");
            e.printStackTrace();
        }
    }

    void setFileOut(String path) {
        try {
            this.fileOut = new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeFile() {
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
                    fileOut.write("CODE SEGMENT" + "\n\n");
                    statementList(child);
                    fileOut.write("CODE ENDS" + "\n");
                } else {
                    program(child);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void statementList(Node child) throws IOException {

        for (Node node : child.getChildren()) {
            if ("<empty>".equals(node.getRule())) {
                break;
            } else if ("<statement>".equals(node.getRule())) {
                statement(node);
            } else if ("<statement-list>".equals(node.getRule())) {
                statementList(node);
            } else {
                System.out.println("Error");
            }
        }

    }

    private void statement(Node node) throws IOException {
        fileOut.write("\t; statement #" + statementCount++ + "\n");
        ax = true;
        expression(node.getChildren().get(2));
        String variableIdentifier = variableIdentifier(node.getChildren().get(0));
        if (ax) {
            fileOut.write("\tRST\n");
            fileOut.write("\tMOV " + variableIdentifier + ", AX" + "\n\n");
            ax = false;
        } else {
            fileOut.write("\tRST\n");
            fileOut.write("\tMOV " + variableIdentifier + ", BX" + "\n\n");
            ax = true;
        }
    }

    private String variableIdentifier(Node node) {
        return node.getChildren().get(0).getChildren().get(0).getRule();
    }

    private String expression(Node node) throws IOException {
        summand(node.getChildren().get(0));
        summandList(node.getChildren().get(1));
        expression = false;
        return "";
    }

    private void summand(Node node) throws IOException {
        String multiplier = multiplier(node.getChildren().get(0));

        if (!ax) {
            fileOut.write("\tPUSH AX\n\n ");
            ax = true;
        }
        fileOut.write("\tMOV AX, " + multiplier + "\n");
        ax = false;

        multipliersList(node.getChildren().get(1));


    }

    private String multiplier(Node node) throws IOException {

        if ("<unsigned-integer>".equals(node.getChildren().get(0).getRule()))
            return node.getChildren().get(0).getChildren().get(0).getRule();
        else if ("<variable-identifier>".equals(node.getChildren().get(0).getRule())) {
            String var = node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getRule();
            checkIfVarDeclared(var);
            return var;
        } else {
            String expression = expression(node.getChildren().get(1));
            this.expression = true;
            return expression;
        }

    }

    private void multipliersList(Node node) throws IOException {
        if ("<empty>".equals(node.getChildren().get(0).getRule())) {
            return;
        }

        String multiplicationInstruction = multiplicationInstruction(node.getChildren().get(0));
        String multiplier = multiplier(node.getChildren().get(1));

        if ("/".equals(multiplicationInstruction)) {
            if (!expression) {
                fileOut.write("\tMOV BX, " + multiplier + "\n");
                fileOut.write("\tDIV BX" + "\n");
                fileOut.write("\tPUSH AX" + "\n\n");
                ax = true;
            }else {
                fileOut.write("\tPOP BX\n");
                fileOut.write("\tPOP AX\n");
                fileOut.write("\tDIV BX" + "\n");
                fileOut.write("\tPUSH AX" + "\n\n");
            }

        } else if ("*".equals(multiplicationInstruction)) {
            if (!expression) {
                fileOut.write("\tMOV BX, " + multiplier + "\n");
                fileOut.write("\tMUL BX" + "\n");
                fileOut.write("\tPUSH AX" + "\n\n");
                ax = true;
            } else{
                fileOut.write("\tPOP BX\n");
                fileOut.write("\tPOP AX\n");
                fileOut.write("\tMUL BX" + "\n");
                fileOut.write("\tPUSH AX" + "\n\n");
            }
        }
        multipliersList(node.getChildren().get(2));

        expression = false;
    }

    private String multiplicationInstruction(Node node) {
        return node.getChildren().get(0).getRule();
    }

    private void summandList(Node node) throws IOException {
        ax = true;
        summand(node.getChildren().get(1));
        String addInstruction = addInstruction(node.getChildren().get(0));

        if ("+".equals(addInstruction)) {
            fileOut.write("\tPOP BX\n");
            fileOut.write("\tPOP AX\n");
            fileOut.write("\tADD AX, BX" + "\n");
            fileOut.write("\tPUSH AX\n\n");
            expression = false;
        } else if ("-".equals(addInstruction)) {
            fileOut.write("\tPOP BX\n");
            fileOut.write("\tPOP AX\n");
            fileOut.write("\tSUB AX, BX" + "\n");
            fileOut.write("\tPUSH AX\n\n");
            expression = false;
        }

    }

    private String addInstruction(Node node) {
        return node.getChildren().get(0).getRule();
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
        Node identifier = child.getChildren().get(0)
                .getChildren().get(0)
                .getChildren().get(0);
        int type = Integer.parseInt(child.getChildren().get(2)
                .getData());

        if (Integer.valueOf(identifier.getData()) >= 1000) {
            try {
                checkDeclarVarDuplication(identifier.getRule());
                //identSet.add(identifier.getRule());
                fileOut.write("\t" + identifier.getRule());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (type == 404) {
            try {
                fileOut.write("\tDW\t?" + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void variableDeclaration(Node child) {
        try {
            fileOut.write("DATA SEGMENT" + "\n");
            program(child);
            fileOut.write("DATA ENDS" + "\n\n");
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

    private void checkIfVarDeclared(String var){
        if (!identSet.contains(var)){
            System.out.println("Semantic error: the " + var + " not defined!\n");
            System.exit(0);
        }
    }

    private void checkDeclarVarDuplication(String var) {
        if (identSet.contains(var)) {
            System.out.println("Semantic error: Identifier "
                    + var + " is already defined");
            System.exit(0);
        } else {
            identSet.add(var);
        }
    }

}
