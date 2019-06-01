package LexAnalizer;

import java.io.*;
import java.util.List;

public class ParseSig extends Model {
    private Integer columnNumber;

    private static class SingletonHolder {
        private static final ParseSig INSTANCE = new ParseSig();
    }

    public static ParseSig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void throwError(int intCh, String errType) {
        System.out.println("Lex error: " + errType + ". Line "
                + countLines + "; Column " + countColumns
                + ". Symbol: " + (char) intCh +
                ", it's ASCII code: " + intCh);
    }

    private String getDigit(int intCh, Reader reader) throws IOException {
        setBuffer(intCh);
        do {
            intCh = reader.read();
            char symbol = (char) intCh;
            Integer[] symbCat = CharacterTable.symbolCategory;
            countColumns++;
            if (symbCat[intCh] == 2 ||
                    symbCat[intCh] == 6) {
                throwError(intCh, "incorrect constant");
                break;
            } else if (symbCat[intCh] == 1) buffer += symbol;
            else break;
        } while (!Character.isWhitespace(intCh));
        setExtraChar((char) intCh);
        table.put(buffer, constantCode++);
        return buffer;
    }

    private String getLetter(int intCh, Reader reader) throws IOException {
        setBuffer(intCh);

        do {
            intCh = reader.read();
            if (intCh == -1) break;
            Integer[] symbCat = CharacterTable.symbolCategory;
            char symbol = (char) intCh;
            countColumns++;
            if (symbCat[intCh] == 6) {
                throwError(intCh, "incorrect identifier");
                break;
            } else if (symbCat[intCh] == 2 || symbCat[intCh] == 1) buffer += symbol;
            else {
                extraChar.add((char) intCh);
                break;
            }
        } while (!Character.isWhitespace(intCh));
        return buffer;
    }

    private String getOneSymbolDivider(int intCh, Reader reader) throws IOException {
        buffer = "";

        if ((char) intCh == '(') {
            char prevIntCh = (char) intCh;
            intCh = reader.read();
            char currCh = (char) intCh;
            if (currCh == '*') {
                skipComment(reader);
            }
            if (CharacterTable.symbolCategory[intCh] == 3
                    || CharacterTable.symbolCategory[intCh] == 2
                    || CharacterTable.symbolCategory[intCh] == 1
                    || CharacterTable.symbolCategory[intCh] == 0
                    || CharacterTable.symbolCategory[intCh] == 6) {
                if (currCh != '*')
                    setBuffer(prevIntCh);
                if (currCh != '*')
                    extraChar.add(currCh);
            }

        } else if ((char) intCh == ')') {
            char prevIntCh = (char) intCh;
            intCh = reader.read();
            char currCh = (char) intCh;
            if (CharacterTable.symbolCategory[intCh] == 3
                    || CharacterTable.symbolCategory[intCh] == 2
                    || CharacterTable.symbolCategory[intCh] == 1
                    || CharacterTable.symbolCategory[intCh] == 0
                    || CharacterTable.symbolCategory[intCh] == 6) {
                setBuffer(prevIntCh);
                extraChar.add(currCh);
            }
        } else setBuffer(intCh);
        return buffer;
    }

    private void skipComment(Reader reader) throws IOException {
        char prev;
        char next = ' ';
        while (true) {
            prev = next;
            next = (char) reader.read();
            countColumns++;
            if (next == '\uFFFF' || next == '.') {
                throwError(next, "end of file");
                break;
            }
            if (prev == '*' && next == ')') break;
        }
    }

    private String getMultiSymbolDivider(int intCh, Reader reader) throws IOException {
        setBuffer(intCh);

        intCh = reader.read();
        char ch = (char) intCh;
        countColumns++;

        if (ch == '=') buffer += ch;

        return buffer;
    }

    private String switcher(Reader reader, Integer[] symbCat, int intCh) throws IOException {
        String out = "";
        if (intCh != -1)
            if (symbCat[intCh] != null)
                switch (symbCat[intCh]) {
                    case 6:
                        throwError(intCh, "prohibited symbol");
                        break;
                    case 1:
                        out = getDigit(intCh, reader);
                        break;
                    case 2:
                        out = getLetter(intCh, reader);
                        break;
                    case 3:
                        out = getOneSymbolDivider(intCh, reader);
                        break;
                    case 4:
                        out = getMultiSymbolDivider(intCh, reader);
                        break;
                    case 0:
                        break;
                    default:
                        throw new IllegalArgumentException("No such symbol!");
                }
        return out;
    }

    private void fillLexemeList(String lexeme) {
        if (!lexeme.equals("")) {
            if (table.containsKey(lexeme)) {
                lexemsBuffer.add(new Lexeme(lexeme, table.get(lexeme), countLines, columnNumber));
            } else {
                table.put(lexeme, ++identifierCode);
                lexemsBuffer.add(new Lexeme(lexeme, table.get(lexeme), countLines, columnNumber));
            }
        }
    }

    public List<Lexeme> getSigFile(InputStream in) throws IOException {
        String s = "source.sig", t = "testFile";
        //InputStream in = new FileInputStream("src/main/resources/" + s);
        Reader reader = new InputStreamReader(in, "US-ASCII");
        setTable();
        int intCh;
        setCountLines(1);
        setCountColumns(1);

        Integer[] symbCat = CharacterTable.symbolCategory;
        String lexem = "";

        intCh = reader.read();
        while (true) {
            char ch = (char) intCh;
            if (extraChar.isEmpty()) {
                columnNumber = countColumns;
                lexem = switcher(reader, symbCat, intCh);
                fillLexemeList(lexem);
            } else {
                while (!extraChar.isEmpty()) {
                    columnNumber = countColumns;
                    lexem = switcher(reader, symbCat, (int) getExtraChar());
                    fillLexemeList(lexem);
                }
            }

            if (extraChar.isEmpty()) intCh = reader.read();
            if (intCh == -1 || (char) intCh == '.') break;

            countColumns++;
            if (ch == '\n') {
                countLines++;
                countColumns = 0;
            }
        }
        //System.out.println(table);
        return lexemsBuffer;
    }
}
