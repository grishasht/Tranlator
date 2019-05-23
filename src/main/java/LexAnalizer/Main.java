package LexAnalizer;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        CharacterTable.createFileSymbolCategory();
        CharacterTable.getSymbolCategory();
        ParseSig parseSig = ParseSig.getInstance();
        parseSig.getSigFile(new FileInputStream("src/main/resources/source.sig"));
        parseSig.getTable();

        for (Lexem i: parseSig.lexemsBuffer) {
            System.out.println("Lexem code: " + i.lexem +
                    " Row number: " + i.lexemRow +
                    " Column number: " + i.lexemColumn);
        }
    }
}
