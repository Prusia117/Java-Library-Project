package src;

import java.io.File;
import java.io.PrintWriter;

import src.jtemplates.EntityTemplate;
import src.jtemplates.ModelTemplate;

public final class Jartisan implements Colors {

    public static void main(String[] args) {


        boolean confirmation = false;

        switch (args[0]) {
            case "Entity":
            case "Model":
                StringBuilder filePath = new StringBuilder("src/main/java/com/library/");
                switch (args[0]) {
                    case "Entity":
                        filePath.append("entities/");
                        break;
                    case "Model":
                        filePath.append("models/");
                        break;
                
                    default:
                        break;
                }
                filePath.append(args[1] + "Entity.java");

                confirmation = FilesGenerator.newFile(filePath, EntityTemplate.getTemplate(args[1]));


                break;
            default:
                break;
        }

        try {
            if(confirmation){
                System.out.println("[" + BLUE + "INFO" + RESET + "]: " + GREEN + "Jartisan Created " + BLUE + args[0] + GREEN + " Successfully" + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + "Jartisan Exception: " + e + RESET);
        }
    }
}
