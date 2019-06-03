package SyntaxAnalizer;

import LexAnalizer.CharacterTable;
import LexAnalizer.Lexeme;
import LexAnalizer.ParseSig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

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

    public Node declarationList() {
        List<Node> nodesBuffer = new LinkedList<>();
        Node declaration = null;
        while (true) {
            if ((declaration = declaration()) == null) break;
            nodesBuffer.add(declaration);
        }

        Node node = Node.newBuilder()
                .setRule("<declaration-list>")
                .build();

        for (int i = 0; i < nodesBuffer.size(); i++) {
            node.addChildren(nodesBuffer.get(i));
        }

        return node;

    }

    public Node declaration() {
        Node node = variableIdentifier();

        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null)
        if (!":".equals(lexeme.lexeme)) {
            Service.throwError("':' expected");
        } else if (!"INTEGER".equals(lexemeList.pop().lexeme.toUpperCase())) {
            Service.throwError("'INTEGER' expected");
        } else if (!";".equals(lexemeList.pop().lexeme.toUpperCase())) {
            Service.throwError("';' expected");
        } else {
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
        Node identifier = identifier();
        if (identifier != null)
            return Node.newBuilder()
                    .setRule("<variable-identifier>")
                    .addChildren(identifier)
                    .build();
        else return null;
    }

    private Node procedureIdentifier() {
        return null;
    }

    private Node identifier() {
//        lexemeList.getLexemes().forEach(System.out::println);
        Lexeme lexeme = lexemeList.pop();
        String lexemeName;
        if (lexeme != null) lexemeName = lexeme.lexeme;
        else lexemeName = "";

        if (lexeme != null
                && Service.getLexemeCode(lexemeName)
                >= 1000) {
            return Node.newBuilder()
                    .setRule("<identifier>")
                    .setData(lexemeName)
                    .build();
        } else {
            Service.throwError("identifier expected");
            return null;
        }
    }

}
