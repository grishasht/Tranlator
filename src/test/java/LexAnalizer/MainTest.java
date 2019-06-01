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
        List<Lexeme> result = parse.lexemsBuffer;
        List<Lexeme> expected = parse.getSigFile(fileInputStream);
        System.out.println(result.size());
        for (Lexeme i: parse.lexemsBuffer) {
            System.out.println("Lexeme code: " + i.lexemeCode +
                    " Row number: " + i.lexemeRow +
                    " Column number: " + i.lexemeColumn);
        }
        assertEquals(expected, result);
    }
}