package LexAnalizer;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CharacterTable.createFileSymbolCategory();
        CharacterTable.getSymbolCategory();
        ParseSig parseSig = ParseSig.getInstance();
        parseSig.getSigFile(new FileInputStream("src/main/resources/source.sig"));
//        parseSig.getSigFile(new FileInputStream("src/main/resources/tests/p1.sig"));
        parseSig.getTable();

        parseSig.lexemsBuffer.forEach(System.out::println);
    }
}
