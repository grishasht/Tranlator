package LexAnalizer;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void shouldReturnCorrectCodedList() throws IOException {
        CharacterTable.createFileSymbolCategory();
        String fileName = "src/main/resources/tests/p1.sig";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ParseSig parse = ParseSig.getInstance();
        parse.getSigFile(fileInputStream);
        parse.getTable();
        List<Lexem> result = parse.lexemsBuffer;
        List<Lexem> expected = parse.getSigFile(fileInputStream);
        System.out.println(result.size());
        for (Lexem i: parse.lexemsBuffer) {
            System.out.println("Lexem code: " + i.lexem +
                    " Row number: " + i.lexemRow +
                    " Column number: " + i.lexemColumn);
        }
        assertEquals(expected, result);
    }
}