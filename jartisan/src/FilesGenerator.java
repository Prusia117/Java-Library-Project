package src;

import java.io.File;
import java.io.PrintWriter;

public class FilesGenerator implements Colors  {
    public static boolean newFile(StringBuilder path, String content){
        try {
            File file = new File(path.toString());

            file.createNewFile();
            PrintWriter output = new PrintWriter(file);
            output.println(content);
            output.close();
            return file.exists();
        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Jartisan Exception: " + e + RESET);
            return false;
        }
    }
}
