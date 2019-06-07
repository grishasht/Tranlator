package CodeGenerator;

import SyntaxAnalizer.Node;

import java.io.FileWriter;
import java.io.IOException;

public class Generator {
    private FileWriter fileOut;
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

    public void program(Node node) throws IOException {
        for (Node child: node.getChildren()){

            if (child.getRule().equals("<procedure-identifier>")){
                procedureIdentifier(child);
            } else if (child.getRule().equals("<identifier>")){
              //  return identifier(child);
            } else if (child.getRule().equals("<variable-declaration>")){
                variableDeclaration(child);
            } else if (child.getRule().equals("<declaration>")){
                declaration(child);
            } else if (child.getRule().equals("<block>")){
                block(child);
            } else if (child.getRule().equals("<statement-list>")){
                fileOut.write("CODE SEGMENT" + '\n');
                statementList(child);
                fileOut.write("CODE ENDS" + '\n');
            } else {
                program(child);
            }

        }
    }

    private void statementList(Node child) {

    }

    private void block(Node child) {

    }

    private void declaration(Node child) {

    }

    private void variableDeclaration(Node child) {

    }

    private void identifier(Node child) {

    }

    private void procedureIdentifier(Node child) {

    }

}
