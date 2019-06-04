package SyntaxAnalizer;

import LexAnalizer.CharacterTable;
import LexAnalizer.Lexeme;
import LexAnalizer.ParseSig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Grammar {
    private String constBuffer = null;
    private LexemeList lexemeList;
    private List<Lexeme> buffer = new LinkedList<>();

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
        Lexeme lexeme = lexemeList.pop();
        Node procedureIdentifier = procedureIdentifier();
        Lexeme lexeme1 = lexemeList.pop();
        Node block = block();
        Lexeme lexeme2 = lexemeList.pop();

        if (lexeme == null || !"PROGRAM".equals(lexeme.lexeme.toUpperCase())) {
            Service.throwError("'PROGRAM' expected");
        } else if (procedureIdentifier == null) {
            Service.throwError("Procedure identifier expected");
        } else if (lexeme1 == null || !";".equals(lexeme1.lexeme)) {
            Service.throwError("';' expected");
        } else if (block == null) {
            Service.throwError("Block expected");
        } else if (lexeme2 == null || !".".equals(lexeme2.lexeme)) {
            Service.throwError("'.' expected");
        } else {
            return Node.newBuilder()
                    .setRule("<program>")
                    .addChildren(procedureIdentifier)
                    .addChildren(block)
                    .build();
        }

        return null;
    }

    private Node block() {
        Node variableDeclaration = variableDeclaration();
        Lexeme lexeme = lexemeList.pop();
        Node statementList = statementList();
        Lexeme lexeme1 = lexemeList.pop();

        if (variableDeclaration == null) {
            Service.throwError("Variable declaration expected");
        } else if (lexeme == null) {
            Service.throwError("'BEGIN' expected");
        } else if (!"BEGIN".equals(lexeme.lexeme.toUpperCase())) {
            Service.throwError("'BEGIN' expected");
        } else if (statementList == null) {
            Service.throwError("Statement list expected");
        } else if (lexeme1 == null) {
            Service.throwError("'END' expected");
        } else if (!"END".equals(lexeme1.lexeme.toUpperCase())) {
            Service.throwError("'END' expected");
        } else {
            return Node.newBuilder()
                    .setRule("<block>")
                    .addChildren(variableDeclaration)
                    .addChildren(statementList)
                    .build();
        }

        return null;
    }

    public Node variableDeclaration() {
        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null)
            if (!"VAR".equals(lexeme.lexeme.toUpperCase())) {
                Service.throwError("'VAR' expected");
            } else {
                return Node.newBuilder()
                        .setRule("<variable-declaration>")
                        .addChildren(declarationList())
                        .build();
            }

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

        if (nodesBuffer.size() != 0)
            for (int i = 0; i < nodesBuffer.size(); i++) {
                node.addChildren(nodesBuffer.get(i));
            }
        else return empty();

        return node;

    }

    public Node declaration() {
        Node node = variableIdentifier();

        Lexeme lexeme = lexemeList.pop();

        if ("BEGIN".equals(lexeme.lexeme.toUpperCase())){
            lexemeList.addFirst(lexeme);
            return null;
        }

        if (lexeme != null)
            if (!":".equals(lexeme.lexeme)) {
                Service.throwError("':' expected");
            } else if (!"INTEGER".equals(lexemeList.pop().lexeme.toUpperCase())) {
                Service.throwError("'INTEGER' expected");
            } else if (!";".equals(lexemeList.pop().lexeme)) {
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
        List<Node> nodesBuffer = new LinkedList<>();
        Node statement = null;
        while (true) {
            if ((statement = statement()) == null) break;
            nodesBuffer.add(statement);
        }

        Node node = Node.newBuilder()
                .setRule("<statement-list>")
                .build();

        if (nodesBuffer.size() != 0)
            for (int i = 0; i < nodesBuffer.size(); i++) {
                node.addChildren(nodesBuffer.get(i));
            }
        else return empty();

        return node;
    }

    public Node statement() {
        Node variableIdentifier = variableIdentifier();

        Lexeme lexeme = lexemeList.pop();
        Node expression = expression();
        Lexeme nextLexeme = lexemeList.pop();

        if (lexeme != null) {
            if (variableIdentifier == null) {
                Service.throwError("Variable identifier expected");
            } else if (!":=".equals(lexeme.lexeme)) {
                Service.throwError("':=' expected");
            } else if (expression == null) {
                Service.throwError("Expression expected");
            } else if (nextLexeme != null)
                if (!";".equals(nextLexeme.lexeme)) {
                    Service.throwError("';' expected");
                } else {
                    return Node.newBuilder()
                            .setRule("<statement>")
                            .addChildren(variableIdentifier)
                            .addChildren(expression)
                            .build();
                }
        }


        return null;
    }

    private Node expression() {
        Node summand = summand();
        // Node summandsList = summandsList();

        Node node = Node.newBuilder()
                .setRule("<expression>")
                .build();

        if (summand == null) {
            Service.throwError("Summand expected");
            return null;
        }/* else if (summandsList == null) {
            Service.throwError("Summands list expected");
            return null;
        } */ else {
            node.addChildren(summand);
            //node.addChildren(summandsList);
        }

        return node;
    }

    private Node summandsList() {
        Node addInstructions = addInstructions();
        Node summand = summand();

        Node node = Node.newBuilder()
                .setRule("<summands-list>")
                .build();

        while (true)
            if (addInstructions == null) {
                Service.throwError("Add instruction expected");
                break;
            } else if (summand == null) {
                Service.throwError("Summand expected");
                break;
            } else {
                addInstructions = addInstructions();
                summand = summand();
                node.addChildren(addInstructions);
                node.addChildren(summand);
            }

        return node;
    }

    private Node addInstructions() {
        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null) {
            if (!"+".equals(lexeme.lexeme) || !"-".equals(lexeme.lexeme)) {
                Service.throwError("'+' or '-' expected");
            } else {
                return Node.newBuilder()
                        .setRule("<add-instruction>")
                        .setData(lexeme.lexeme)
                        .build();
            }
        }
        return null;
    }

    public Node summand() {
        Node multiplier = multiplier();
        Node multiplierList = multipliersList();


        if (multiplier == null) {
            Service.throwError("multiplier expected");
            return null;
        } else if (multiplierList == null) {
            Service.throwError("multipliers list expected");
            return null;
        } else {
            return Node.newBuilder()
                    .setRule("<summand>")
                    .addChildren(multiplier)
                    .addChildren(multiplierList)
                    .build();
        }
    }

    private Node multipliersList() {
        //List<Node> nodesBuffer = new LinkedList<>();
        Node multiplicationInstruction = multiplicationInstruction();
        Node multiplier = multiplier();

        Node node = Node.newBuilder()
                .setRule("<multipliers-list>")
                .build();

        if (multiplicationInstruction == null) {
            return null;
        } else {
            node.addChildren(multiplicationInstruction);
        }

        if (multiplier == null) {
            Service.throwError("Multiplier expected");
            return null;
        } else {
            node.addChildren(multiplier);
        }

        return node;
    }

    private Node multiplicationInstruction() {
        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null) {
            if ("*".equals(lexeme.lexeme) || "/".equals(lexeme.lexeme)) {
                return Node.newBuilder()
                        .setRule("<multiplication-instruction>")
                        .setData(lexeme.lexeme)
                        .build();
            } else {
                Service.throwError("'*' or '/' expected");
                lexemeList.addFirst(lexeme);
            }
        }
        return null;
    }

    private Node multiplier() {
        Node variableIdentifier = variableIdentifier();
        Node unsignedInteger = null;
        if (variableIdentifier == null)
            unsignedInteger = unsignedInteger();

        if (variableIdentifier != null) {
            return Node.newBuilder()
                    .setRule("<multiplier>")
                    .addChildren(variableIdentifier)
                    .build();
        } else if (unsignedInteger == null) {
            Service.throwError("Variable or constant expected");
        } else {
            return Node.newBuilder()
                    .setRule("<multiplier>")
                    .addChildren(unsignedInteger)
                    .build();
        }

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
        Node identifier = identifier();

        if (identifier == null) {
            Service.throwError("Identifier expected");
        } else return Node.newBuilder()
                .setRule("<procedure-identifier>")
                .addChildren(identifier)
                .build();

        return null;
    }

    private Node identifier() {
        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null) {
            if (lexeme.lexemeCode
                    >= 1000) {
                return Node.newBuilder()
                        .setRule("<identifier>")
                        .setData(lexeme.lexeme)
                        .build();
            } else {
                //Service.throwError("identifier expected");
                lexemeList.addFirst(lexeme);
                return null;
            }
        } else {
            //Service.throwError("identifier expected");
            return null;
        }
    }

    private Node unsignedInteger() {
        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null) {
            if (lexeme.lexemeCode >= 500) {
                return Node.newBuilder()
                        .setRule("<unsigned-integer>")
                        .setData(lexeme.lexeme)
                        .build();
            } else {
                lexemeList.addFirst(lexeme);
                return null;
            }
        } else {
            return null;
        }
    }

    private Node empty() {
        return Node.newBuilder()
                .setRule("<empty>")
                .build();
    }

}
