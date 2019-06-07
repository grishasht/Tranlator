package CodeGenerator;

import SyntaxAnalizer.Grammar;

public class Main {

    public static final String FILE_PATH = "src/main/resources/PROG.ASM";

    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        Generator generator = new Generator();
        generator.setFileOut(FILE_PATH);

    }
}
