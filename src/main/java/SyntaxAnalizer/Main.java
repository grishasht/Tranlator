package SyntaxAnalizer;

import LexAnalizer.CharacterTable;
import LexAnalizer.ParseSig;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CharacterTable.getSymbolCategory();
        ParseSig parseSig = ParseSig.getInstance();
        parseSig.getSigFile(new FileInputStream("src/main/resources/source.sig"));
               // .forEach(System.out::println);
        Lexeme.setLexemesMap(parseSig.getTable());
        Lexeme.print();
    }
}
