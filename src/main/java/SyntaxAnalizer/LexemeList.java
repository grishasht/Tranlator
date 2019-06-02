package SyntaxAnalizer;

import LexAnalizer.Lexeme;

import java.util.LinkedList;
import java.util.List;

public class LexemeList {
    private List<Lexeme> lexemes;

    public LexemeList(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public Lexeme pop(){
        return (Lexeme) ((LinkedList) lexemes).pollFirst();
    }

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

}
