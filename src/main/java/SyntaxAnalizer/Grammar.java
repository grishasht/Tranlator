package SyntaxAnalizer;

import LexAnalizer.CharacterTable;
import LexAnalizer.Lexeme;
import LexAnalizer.ParseSig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Grammar {
    private LexemeList lexemeList;
    private Map<String, Integer> lexemeTable;

    {
        CharacterTable.getSymbolCategory();
        ParseSig parseSig = ParseSig.getInstance();
        LexemeTable.setLexemesMap(parseSig.getTable());
        lexemeTable = LexemeTable.getLexemesMap();
        try {
            lexemeList = new LexemeList(parseSig.getSigFile(new FileInputStream("src/main/resources/source.sig")));
        } catch (IOException e) {
            System.out.println("Can't open file in Grammar.java");
            e.printStackTrace();
        }
    }

    public Node signalProgramm(){
        return Node.newBuilder()
                .setRule("<signal-program>")
                .addChildren(program())
                .build();
    }

    public Node program() {
        Lexeme lexeme = lexemeList.pop();
        Node procedureIdentifier = procedureIdentifier();
        Lexeme lexeme1 = lexemeList.pop();
        Node block = block();
        Lexeme lexeme2 = lexemeList.pop();

        if (lexeme == null || !"PROGRAM".equals(lexeme.lexeme.toUpperCase())) {
            Service.throwError("'PROGRAM' expected." + Service.lineColumnError(lexemeList));
        } else if (procedureIdentifier == null) {
            Service.throwError("Procedure identifier expected." + Service.lineColumnError(lexemeList));
        } else if (lexeme1 == null || !";".equals(lexeme1.lexeme)) {
            Service.throwError("';' expected." + Service.lineColumnError(lexemeList));
        } else if (block == null) {
            Service.throwError("Block expected." + Service.lineColumnError(lexemeList));
        } else if (lexeme2 == null || !".".equals(lexeme2.lexeme)) {
            Service.throwError("'.' expected." + Service.lineColumnError(lexemeList));
        } else {
            return Node.newBuilder()
                    .setRule("<program>")
                    .addChildren(terminal("PROGRAM"))
                    .addChildren(procedureIdentifier)
                    .addChildren(terminal(";"))
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
            Service.throwError("Variable declaration expected." + Service.lineColumnError(lexemeList));
        } else if (lexeme == null) {
            Service.throwError("'BEGIN' expected." + Service.lineColumnError(lexemeList));
        } else if (!"BEGIN".equals(lexeme.lexeme.toUpperCase())) {
            lexemeList.addFirst(lexeme);
            Service.throwError("'BEGIN' expected." + Service.lineColumnError(lexemeList));
        } else if (statementList == null) {
            Service.throwError("Statement list expected." + Service.lineColumnError(lexemeList));
        } else if (lexeme1 == null) {
            lexemeList.addFirst(lexeme1);
            Service.throwError("'END' expected." + Service.lineColumnError(lexemeList));
        } else if (!"END".equals(lexeme1.lexeme.toUpperCase())) {
            Service.throwError("'END' expected." + Service.lineColumnError(lexemeList));
        } else {
            return Node.newBuilder()
                    .setRule("<block>")
                    .addChildren(variableDeclaration)
                    .addChildren(terminal("BEGIN"))
                    .addChildren(statementList)
                    .addChildren(terminal("END"))
                    .build();
        }

        return null;
    }

    public Node variableDeclaration() {
        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null)
            if (!"VAR".equals(lexeme.lexeme.toUpperCase())) {
                Service.throwError("'VAR' expected." + Service.lineColumnError(lexemeList));
            } else {
                return Node.newBuilder()
                        .setRule("<variable-declaration>")
                        .addChildren(terminal("VAR"))
                        .addChildren(declarationList())
                        .build();
            }

        return null;
    }

    public Node declarationList() {
        Node declaration = declaration();

        if (declaration == null) {
            return Node.newBuilder()
                    .setRule("<declaration-list>")
                    .addChildren(empty())
                    .build();
        } else {
            return Node.newBuilder()
                    .setRule("<declaration-list>")
                    .addChildren(declaration)
                    .addChildren(declarationList())
                    .build();
        }
    }

    public Node declaration() {
        Node node = variableIdentifier();

        Lexeme lexeme = lexemeList.pop();

        if ("BEGIN".equals(lexeme.lexeme.toUpperCase())) {
            lexemeList.addFirst(lexeme);
            return null;
        }

        if (lexeme != null)
            if (!":".equals(lexeme.lexeme)) {
                Service.throwError("':' expected." + Service.lineColumnError(lexemeList));
            } else if (!"INTEGER".equals(lexemeList.pop().lexeme.toUpperCase())) {
                Service.throwError("'INTEGER' expected." + Service.lineColumnError(lexemeList));
            } else if (!";".equals(lexemeList.pop().lexeme)) {
                Service.throwError("';' expected." + Service.lineColumnError(lexemeList));
            } else {
                return Node.newBuilder()
                        .setRule("<declaration>")
                        .addChildren(node)
                        .addChildren(terminal(":"))
                        .addChildren(terminal("INTEGER"))
                        .addChildren(terminal(";"))
                        .build();
            }

        return null;
    }

    private Node statementList() {
        Node statement = statement();
        if (statement == null) {
            return Node.newBuilder()
                    .setRule("<statement-list>")
                    .addChildren(empty())
                    .build();
        } else return Node.newBuilder()
                .setRule("<statement-list>")
                .addChildren(statement)
                .addChildren(statementList())
                .build();
    }

    public Node statement() {
        Node variableIdentifier = variableIdentifier();

        Lexeme lexeme = lexemeList.pop();
        if ("END".equals(lexeme.lexeme.toUpperCase())) {
            lexemeList.addFirst(lexeme);
            return null;
        }
        Node expression = expression();
        Lexeme nextLexeme = lexemeList.pop();
        if ("END".equals(nextLexeme.lexeme.toUpperCase())) {
            lexemeList.addFirst(nextLexeme);
            return null;
        }

        if (lexeme != null) {
            if (variableIdentifier == null) {
                Service.throwError("Variable identifier expected." + Service.lineColumnError(lexemeList));
            } else if (!":=".equals(lexeme.lexeme)) {
                Service.throwError("':=' expected." + Service.lineColumnError(lexemeList));
            } else if (expression == null) {
                Service.throwError("Expression expected." + Service.lineColumnError(lexemeList));
            } else if (nextLexeme != null)
                if (!";".equals(nextLexeme.lexeme)) {
                    Service.throwError("';' expected." + Service.lineColumnError(lexemeList));
                } else {
                    return Node.newBuilder()
                            .setRule("<statement>")
                            .addChildren(variableIdentifier)
                            .addChildren(terminal(":="))
                            .addChildren(expression)
                            .addChildren(terminal(";"))
                            .build();
                }
        }


        return null;
    }

    private Node expression() {
        Node summand = summand();
        Node summandsList = summandsList();

        Node node = Node.newBuilder()
                .setRule("<expression>")
                .build();

        if (summand == null) {
            Service.throwError("Summand expected." + Service.lineColumnError(lexemeList));
            return null;
        } else if (summandsList == null) {
            Service.throwError("Summands list expected." + Service.lineColumnError(lexemeList));
            return null;
        } else {
            node.addChildren(summand);
            node.addChildren(summandsList);
        }

        return node;
    }

    private Node summandsList() {
        Node addInstructions = addInstructions();
        Node summand = summand();


        if (addInstructions == null) {
            //Service.throwError("Add instruction expected");
            return null;
        } else if (summand == null) {
            Service.throwError("Summand expected." + Service.lineColumnError(lexemeList));
            return null;
        } else {
            return Node.newBuilder()
                    .setRule("<summands-list>")
                    .addChildren(addInstructions)
                    .addChildren(summand)
                    .addChildren(summandsList())
                    .build();
        }

    }

    private Node addInstructions() {
        Lexeme lexeme = lexemeList.pop();


        if (lexeme != null) {
            if ("+".equals(lexeme.lexeme)) {
                return Node.newBuilder()
                        .setRule("<add-instruction>")
                        .addChildren(terminal("+"))
                        .build();

            } else if ("-".equals(lexeme.lexeme)) {
                return Node.newBuilder()
                        .setRule("<add-instruction>")
                        .addChildren(terminal("-"))
                        .build();
            } else {
                lexemeList.addFirst(lexeme);
            }
        }
        return null;
    }

    public Node summand() {
        Node multiplier = multiplier();
        Node multiplierList = multipliersList();


        if (multiplier == null) {
            // Service.throwError("multiplier expected");
            return null;
        } else if (multiplierList == null) {
            Service.throwError("Multipliers list expected." + Service.lineColumnError(lexemeList));
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
            return Node.newBuilder()
                    .setRule("<multipliers-list>")
                    .addChildren(empty())
                    .build();
        } else {
            node.addChildren(multiplicationInstruction);
        }

        if (multiplier == null) {
            Service.throwError("Multiplier expected." + Service.lineColumnError(lexemeList));
            return null;
        } else {
            node.addChildren(multiplier);
            node.addChildren(multipliersList());
        }

        return node;
    }

    private Node multiplicationInstruction() {
        Lexeme lexeme = lexemeList.pop();

        if (lexeme != null) {
            if ("*".equals(lexeme.lexeme) || "/".equals(lexeme.lexeme)) {
                return Node.newBuilder()
                        .setRule("<multiplication-instruction>")
                        .addChildren(terminal(lexeme.lexeme))
                        .build();
            } else {
                lexemeList.addFirst(lexeme);
//                Service.throwError("'*' or '/' expected");
            }
        }
        return null;
    }

    private Node multiplier() {
        Node variableIdentifier = variableIdentifier();
        Node unsignedInteger = null;
        if (variableIdentifier == null)
            unsignedInteger = unsignedInteger();
        Lexeme lexeme = null;
        Node expression = null;

        if (variableIdentifier == null && unsignedInteger == null) {
            lexeme = lexemeList.pop();
            if (lexemeList == null) {
                Service.throwError("Multiplier expected." + Service.lineColumnError(lexemeList));
                return null;
            } else if ("(".equals(lexeme.lexeme)) {
                expression = expression();
                Lexeme lexeme1 = lexemeList.pop();
                if (expression == null || lexeme == null) {
                    Service.throwError("Multiplier expected." + Service.lineColumnError(lexemeList));
                    return null;
                } else if (")".equals(lexeme1.lexeme)) {
                    return Node.newBuilder()
                            .setRule("<multiplier>")
                            .addChildren(terminal("("))
                            .addChildren(expression)
                            .addChildren(terminal(")"))
                            .build();
                } else {
                    lexemeList.addFirst(lexeme1);
                    return null;
                }
            } else {
                lexemeList.addFirst(lexeme);
                return null;
            }
        }

        if (variableIdentifier != null) {
            return Node.newBuilder()
                    .setRule("<multiplier>")
                    .addChildren(variableIdentifier)
                    .build();
        } else if (unsignedInteger != null) {
//            Service.throwError("Variable or constant expected");
            return Node.newBuilder()
                    .setRule("<multiplier>")
                    .addChildren(unsignedInteger)
                    .build();
        } else {
            return Node.newBuilder()
                    .setRule("<multiplier>")
                    .addChildren(expression)
                    .build();
        }
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
            Service.throwError("Identifier expected." + Service.lineColumnError(lexemeList));
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
                        .addChildren(terminal(lexeme.lexeme))
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
                        .addChildren(terminal(lexeme.lexeme))
                        .build();
            } else {
                lexemeList.addFirst(lexeme);
                return null;
            }
        } else {
            return null;
        }
    }

    private Node terminal(String terminal) {
        return Node.newBuilder()
                .setRule(terminal)
                .setData(String.valueOf(lexemeTable.get(terminal)))
                .build();
    }

    private Node empty() {
        return Node.newBuilder()
                .setRule("<empty>")
                .build();
    }

}
