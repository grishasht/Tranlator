package SyntaxAnalizer;

import LexAnalizer.CharacterTable;
import LexAnalizer.ParseSig;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
//        ParseSig parseSig = ParseSig.getInstance();
//        LexemeList lexemeList = new LexemeList(
//                parseSig.getSigFile(new FileInputStream("src/main/resources/source.sig"))
//        );
//        lexemeList.getLexemes().forEach(System.out::println);
//        LexemeTable.setLexemesMap(parseSig.getTable());
//        LexemeTable.print();
        //List<Node> nodes = new LinkedList<>();
        // nodes.add(grammar.program());
        Grammar grammar = new Grammar();
        //System.out.println(grammar.program().toString());
        Service.printTree(grammar.program(), "");
    }
}
