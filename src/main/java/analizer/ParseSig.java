package analizer;

import java.io.*;
import java.util.List;

public class ParseSig extends Model{

    private static class SingletonHolder{
        private static final ParseSig INSTANCE = new ParseSig();
    }

    public static ParseSig getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private void throwError(int intCh, String errType){
        System.out.println("Lex error: " + errType + ". Line "
                + countLines + "; Column " + countColumns
                + ". Symbol: " + (char)intCh +
                ", it's ASCII code: " + intCh);
    }

    private String getDigit(int intCh, Reader reader) throws IOException {
        setBuffer(intCh);
        do{
            intCh = reader.read();
            char symbol = (char)intCh;
            Integer[] symbCat = CharacterTable.symbolCategory;
            countColumns++;
            if (symbCat[intCh] == 2 ||
                    symbCat[intCh] == 6) {
                throwError(intCh, "incorrect constant");
                break;
            }else if (symbCat[intCh] == 1) buffer += symbol;
            else break;
        }while(!Character.isWhitespace(intCh));
        setExtraChar((char)intCh);
        table.put(buffer, constantCode++);
        return buffer;
    }

    private String getLetter(int intCh, Reader reader) throws IOException {
        setBuffer(intCh);
        do{
            intCh = reader.read();
            if (intCh == -1) break;
            Integer[] symbCat = CharacterTable.symbolCategory;
            char symbol = (char)intCh;
            countColumns++;
            if (symbCat[intCh] == 6) {
                throwError(intCh, "incorrect identifier");
                break;
            }else if (symbCat[intCh] == 2 || symbCat[intCh] == 1) buffer += symbol;
            else {
                extraChar.add((char)intCh);
                break;
            }
        }while(!Character.isWhitespace(intCh));
        return buffer;
    }

    private String getOneSymbolDevider(int intCh, Reader reader) throws IOException {
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

        }else if ((char) intCh == ')') {
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
        }else setBuffer(intCh);
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

    private String getMultySymbolDevider(int intCh, Reader reader) throws IOException {
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
        if (symbCat[intCh]!= null)
        switch(symbCat[intCh]){
            case 6: throwError(intCh, "prohibited symbol"); break;
            case 1: out = getDigit(intCh, reader); break;
            case 2: out = getLetter(intCh, reader); break;
            case 3: out = getOneSymbolDevider(intCh, reader); break;
            case 4: out = getMultySymbolDevider(intCh, reader); break;
            case 0: break;
            default: throw new IllegalArgumentException("No such symbol!");
        }
        return out;
    }

    private void fillLexemList(String lexem){
        if (!lexem.equals("")) {
            if (table.containsKey(lexem)){
                lexemsBuffer.add(new Lexem(table.get(lexem), countLines, countColumns - lexem.length()));
            }else{
                table.put(lexem, identifierCode++);
                lexemsBuffer.add(new Lexem(table.get(lexem), countLines, countColumns - lexem.length()));
            }
        }
    }

    public List<Lexem> getSigFile(InputStream in) throws IOException {
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
                lexem = switcher(reader, symbCat, intCh);
                fillLexemList(lexem);
            }else {
                while (!extraChar.isEmpty()) {
                    lexem = switcher(reader, symbCat, (int) getExtraChar());
                    fillLexemList(lexem);
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
