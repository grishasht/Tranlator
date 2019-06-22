package CodeGenerator;

import SyntaxAnalizer.Grammar;

import java.io.IOException;

public class Main {

    public static final String FILE_PATH = "src/main/resources/PROG.ASM";

    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar();
        Generator generator = new Generator();
        generator.setFileOut(FILE_PATH);
        generator.program(grammar.signalProgram());
        generator.closeFile();
    }
}
