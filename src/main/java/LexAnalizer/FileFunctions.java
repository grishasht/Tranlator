package LexAnalizer;

import java.io.*;

public class FileFunctions {

    public static Integer[] readFromFile(String filePath) throws IOException {
        BufferedReader buffReader = new BufferedReader(new FileReader(filePath));
        String str = buffReader.readLine();
        Integer[] buffer = new Integer[str.length()];

        for (int i = 0; i < str.length(); i++) {
            buffer[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
        }
        buffReader.close();

        return buffer;
    }

    public static void writeInFile(String filePath, Object[] buffer) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Object i : buffer)
            writer.print(i);
        writer.close();
    }
}
