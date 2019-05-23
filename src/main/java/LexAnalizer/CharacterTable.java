package LexAnalizer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CharacterTable {
    public static Integer[] symbolCategory = new Integer[128];

    private static void initSymbolCategory(){
        for (int i = 0; i < symbolCategory.length; i++) symbolCategory[i] = 6;
    }

    private static Integer[] fillSymbolCategory() {
        initSymbolCategory();
        for (int i = 0; i < symbolCategory.length; i++) {
            if (i >= 8 && i <= 13 || i == 32) symbolCategory[i] = 0;
            if (i >= 48 && i < 58) symbolCategory[i] = 1;
            if ((i > 64 && i < 91) || (i > 96 && i < 123)) symbolCategory[i] = 2;
            if (i == 41 ||i == 59 || i == 61)
                symbolCategory[i] = 3;
            if (i == 40 || i == 42 || i == 43 ||
                    i == 45 || i == 46 || i == 47) symbolCategory[i] = 3;
            if (i == 58) symbolCategory[i] = 4;
            //if (i == 40 || i == 42) symbolCategory[i] = 5;
        }
        return symbolCategory;
    }

    private static void writeSymbolCategoryInFile(){
        FileFunctions.writeInFile("src/main/resources/symbolCategory.txt", symbolCategory);
    }

    public static void createFileSymbolCategory(){
        initSymbolCategory();
        symbolCategory = fillSymbolCategory();

        writeSymbolCategoryInFile();
    }

    public static void getSymbolCategory(){
        try {
            symbolCategory = FileFunctions.readFromFile("src/main/resources/symbolCategory.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
