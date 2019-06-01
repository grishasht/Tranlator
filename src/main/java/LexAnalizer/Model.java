package LexAnalizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Model {
    protected Integer countLines;
    protected Integer countColumns;
    protected Integer constantCode = 500;
    protected Integer identifierCode = 1000;
    protected String buffer;
    protected Queue<Character> extraChar = new ArrayDeque<>();
    protected List<Lexeme> lexemsBuffer = new LinkedList<>();
    protected HashMap<String, Integer> table = new HashMap<>();

    public void setExtraChar(Character extraChar) {
        this.extraChar.add(extraChar);
    }

    public Character getExtraChar() {
        return extraChar.remove();
    }

    public HashMap<String, Integer> getTable() {
        return table;
    }

    public void setCountLines(Integer countLines) {
        this.countLines = countLines;
    }

    public void setCountColumns(Integer countColumns) {
        this.countColumns = countColumns;
    }

    public void setBuffer(int intCh) {
        this.buffer = "" + (char)intCh;
    }

    protected void setTable() throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader("src/main/resources/codeTable.csv"));
        String line = buffer.readLine();
        while (line != null) {
            String tmp1 = "";
            String tmp2 = "";
            for (int i = 0; line.charAt(i) != '&'; i++)
                tmp1 += line.charAt(i);
            for (int i = line.length() - 1; line.charAt(i) != '&'; i--)
                tmp2 += line.charAt(i);

            tmp2 = new StringBuilder(tmp2).reverse().toString();
            table.put(tmp1, Integer.parseInt(tmp2));
            line = buffer.readLine();
        }
    }

}
