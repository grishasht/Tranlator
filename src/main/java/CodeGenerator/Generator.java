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
    private Boolean ax = false, bx = true;

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

    private String statementList(Node child) throws IOException {

        for (Node node: child.getChildren()){
            if (!"<empty>".equals(node.getRule())){
                statementList(node);
            }
            if (node.getRule() != null){
                if ("<identifier>".equals(node.getRule())
                        || "<unsigned-integer>".equals(node.getRule())){
                    if (ax == false){
                        fileOut.write( "MOV AX " + node.getChildren().get(0).getRule() + '\n');
                        ax = true;
                    } else {
                        fileOut.write( "MOV BX " + node.getChildren().get(0).getRule() + '\n');
                        ax = false;
                    }
                }
            }
        }

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
        Node identifier = child.getChildren().get(0)
                .getChildren().get(0)
                .getChildren().get(0);
        Integer type = Integer.valueOf(child.getChildren().get(2)
                .getData());

        if (Integer.valueOf(identifier.getData()) >= 1000){
            try {
                fileOut.write("\t" + identifier.getRule());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (type == 404){
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
