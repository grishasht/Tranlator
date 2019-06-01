package LexAnalizer;

public class Lexem {
    public String lexem;
    public Integer lexemCode;
    public Integer lexemRow;
    public Integer lexemColumn;

    public Lexem(String lexem, Integer lexemCode, Integer lexemRow, Integer lexemColumn) {
        this.lexem = lexem;
        this.lexemCode = lexemCode;
        this.lexemRow = lexemRow;
        this.lexemColumn = lexemColumn;
    }

    @Override
    public String toString() {
        return "Lexem: " + lexem +
                " Lexem code: " + lexemCode +
                " Row number: " + lexemRow +
                " Column number: " + lexemColumn;
    }
}
