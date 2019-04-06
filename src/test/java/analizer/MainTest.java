package analizer;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void shouldReturnCorrectCodedList() throws IOException {
        String fileName = "src/main/resources/src/p1.sig";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ParseSig parse = ParseSig.getInstance();
        List<Lexem> result = parse.getSigFile(fileInputStream);
        List<Lexem> expected = parse.getSigFile(fileInputStream);
        assertEquals(expected, result);
    }
}