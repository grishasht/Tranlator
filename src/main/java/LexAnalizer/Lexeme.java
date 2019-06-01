package LexAnalizer;

public class Lexeme {
    public String lexeme;
    public Integer lexemeCode;
    public Integer lexemeRow;
    public Integer lexemeColumn;

    public Lexeme(String lexeme, Integer lexemeCode, Integer lexemeRow, Integer lexemeColumn) {
        this.lexeme = lexeme;
        this.lexemeCode = lexemeCode;
        this.lexemeRow = lexemeRow;
        this.lexemeColumn = lexemeColumn;
    }

    @Override
    public String toString() {
        return "Lexeme: " + lexeme +
                " Lexeme code: " + lexemeCode +
                " Row number: " + lexemeRow +
                " Column number: " + lexemeColumn;
    }
}
