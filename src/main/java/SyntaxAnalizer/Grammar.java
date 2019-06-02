package SyntaxAnalizer;

import LexAnalizer.CharacterTable;
import LexAnalizer.ParseSig;

import java.io.FileInputStream;
import java.io.IOException;

public class Grammar {
    private LexemeList lexemeList;

    {
        CharacterTable.getSymbolCategory();
        ParseSig parseSig = ParseSig.getInstance();
        LexemeTable.setLexemesMap(parseSig.getTable());
        try {
            lexemeList = new LexemeList(parseSig.getSigFile(new FileInputStream("src/main/resources/tests/p1.sig")));
        } catch (IOException e) {
            System.out.println("Can't open file in Grammar.java");
            e.printStackTrace();
        }
    }

    public Node program() {

        return null;
    }

    private Node block() {
        return null;
    }

    private Node variableDeclaration() {
        return null;
    }

    private Node declarationList() {
        return null;
    }

    public Node declaration() {
        Node node = variableIdentifier();

        if (!":".equals(lexemeList.pop().lexeme)){
            Service.throwError("':' expected");
        }else if(!"INTEGER".equals(lexemeList.pop().lexeme.toUpperCase())){
            Service.throwError("'INTEGER' expected");
        }else if(!";".equals(lexemeList.pop().lexeme.toUpperCase())){
            Service.throwError("';' expected");
        }else{
            return Node.newBuilder()
                    .setRule("<declaration>")
                    .addChildren(node)
                    .build();
        }

        return null;
    }

    private Node statementList() {
        return null;
    }

    private Node statement() {
        return null;
    }

    private Node expression() {
        return null;
    }

    private Node summandsList() {
        return null;
    }

    private Node addInstructions() {
        return null;
    }

    private Node summand() {
        return null;
    }

    private Node multipliersList() {
        return null;
    }

    private Node multiplicationInstruction() {
        return null;
    }

    private Node multiplier() {
        return null;
    }

    private Node variableIdentifier() {
        return Node.newBuilder()
                .setRule("<variable-identifier>")
                .addChildren(identifier())
                .build();
    }

    private Node procedureIdentifier() {
        return null;
    }

    private Node identifier() {
//        lexemeList.getLexemes().forEach(System.out::println);
        String lexeme = lexemeList.pop().lexeme;

        if (Service.getLexemeCode(lexeme)
                >= 1000 && lexeme != null){
            return Node.newBuilder()
                    .setRule("<identifier>")
                    .setData(lexeme)
                    .build();
        } else {
            Service.throwError("identifier expected");
            return null;
        }
    }

}
