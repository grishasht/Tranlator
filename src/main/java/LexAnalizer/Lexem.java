package LexAnalizer;

public class Lexem {
    public Integer lexem;
    public Integer lexemRow;
    public Integer lexemColumn;

    public Lexem(Integer lexem, Integer lexemRow, Integer lexemColumn) {
        this.lexem = lexem;
        this.lexemRow = lexemRow;
        this.lexemColumn = lexemColumn;
    }

    @Override
    public String toString() {
        return "Lexem{" +
                "lexem = " + lexem +
                ", lexemRow = " + lexemRow +
                ", lexemColumn = " + lexemColumn +
                '}';
    }
}
