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
    private List<String> registers = new ArrayList<>();
    private Boolean ax = true;

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
                    fileOut.write("CODE SEGMENT" + "\n\n");
                    statementList(child);
                    fileOut.write("\nCODE ENDS" + "\n");
                } else {
                    program(child);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private String statementList(Node child) throws IOException {

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

        return null;
    }

    private void statement(Node node) throws IOException {
        String expression = expression(node.getChildren().get(2));
        String variableIdentifier = variableIdentifier(node.getChildren().get(0));

        if (ax) {
            fileOut.write("\tMOV " + variableIdentifier + ", CX" + "\n\n");
            ax = false;
        } else {
            fileOut.write("\tMOV " + variableIdentifier + ", CX" + "\n\n");
            ax = true;
        }
    }

    private String variableIdentifier(Node node) {
        return node.getChildren().get(0).getChildren().get(0).getRule();
    }

    private String expression(Node node) throws IOException {
        String summand = summand(node.getChildren().get(0));
        String summandList = summandList(node.getChildren().get(1));
        return "";
    }

    private String summand(Node node) throws IOException {
        String multiplier = multiplier(node.getChildren().get(0));

        if (!ax) {
            fileOut.write("\tMOV DX, AX\n");
            ax = true;
        }
        fileOut.write("\tMOV AX, " + multiplier + "\n");
        ax = false;

        String multipliersList = multipliersList(node.getChildren().get(1));



        return "";
    }

    private String multiplier(Node node) throws IOException {

        if ("<unsigned-integer>".equals(node.getChildren().get(0).getRule()))
            return node.getChildren().get(0).getChildren().get(0).getRule();
        else if ("<variable-identifier>".equals(node.getChildren().get(0).getRule())) {
            return node.getChildren().get(0).getChildren().get(0).getChildren().get(0).getRule();
        } else {
            return expression(node.getChildren().get(1));
        }

    }

    private String multipliersList(Node node) throws IOException {
        if ("<empty>".equals(node.getChildren().get(0).getRule())){
            return "empty";
        }

        String multiplicationInstruction = multiplicationInstruction(node.getChildren().get(0));
        String multiplier = multiplier(node.getChildren().get(1));

        if ("/".equals(multiplicationInstruction)){
            fileOut.write("\tMOV BX, " + multiplier + "\n");
            fileOut.write("\tDIV BX" + "\n");
        } else if ("*".equals(multiplicationInstruction)){
            fileOut.write("\tMOV BX, " + multiplier + "\n");
            fileOut.write("\tMUL BX" + "\n");
        }
        multipliersList(node.getChildren().get(2));

        return multiplier;
    }

    private String multiplicationInstruction(Node node) {
        return node.getChildren().get(0).getRule();
    }

    private String summandList(Node node) throws IOException {

        fileOut.write("\tMOV CX, AX" + "\n\n");
        ax = true;
        String summand = summand(node.getChildren().get(1));
        String addInstruction = addInstruction(node.getChildren().get(0));

        if ("+".equals(addInstruction)){
            fileOut.write("\tADD CX, AX" + "\n");
        } else if ("-".equals(addInstruction)){
            fileOut.write("\tSUB CX, AX" + "\n");
        }

        return "";
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
        Integer type = Integer.valueOf(child.getChildren().get(2)
                .getData());

        if (Integer.valueOf(identifier.getData()) >= 1000) {
            try {
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

    private void checkDeclarVarDuplication(String var, int identCode) {
        if (identSet.contains(var)) {
            System.out.println("Semantic error: Identifier \""
                    + lexemeTable.get(identCode) + "\""
                    + " is already defined");
        } else {
            identSet.add(var);
        }
    }

}
