package src;

import java.io.File;
import java.io.PrintWriter;
import src.templates.EntityTemplate;

public final class Jartisan {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CIAN = "\u001B[36m";
    public static void main(String[] args) {

        StringBuilder fileContent = new StringBuilder();
        StringBuilder filePath = new StringBuilder("src/main/java/com/library/");

        switch (args[0]) {
            case "Entity":
                fileContent.append(EntityTemplate.getTemplate(args[1]));
                filePath.append("entities/" + args[1] + "Entity.java");
                break;

            default:
                break;
        }

        File file = new File(filePath.toString());
        try {
            file.createNewFile();
            PrintWriter output = new PrintWriter(file);
            output.println(fileContent);
            output.close();
            System.out.println("[" + BLUE + "INFO" + RESET + "]: " + GREEN + "Jartisan Created " + BLUE + args[0] + GREEN + " Successfully" + RESET);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(RED + "Jartisan Exception: " + e + RESET);
        }
    }
}
