package src;

import java.io.File;
import java.io.PrintWriter;

import src.jtemplates.EntityTemplate;
import src.jtemplates.ModelTemplate;
import src.jtemplates.ControllerTemplate;
import src.jtemplates.MigrationTemplate;

public final class Jartisan implements Colors {

    public static void main(String[] args) {

        try {
            boolean result = false;
            switch (args[0]) {
                case "Entity":
                case "Model":
                case "Controller":
                case "Migration":
                    StringBuilder filePath = new StringBuilder("src/main/java/com/library/");
                    String fileTemplate = null;
                    switch (args[0]) {
                        case "Entity":
                            filePath.append("entities/" + args[1] + "Entity.java");
                            fileTemplate = EntityTemplate.getTemplate(args[1]);
                            break;
                        case "Model":
                            filePath.append("models/" + args[1] + "Model.java");
                            fileTemplate = ModelTemplate.getTemplate(args[1]);
                            break;
                        case "Controller":
                            filePath.append("controllers/" + args[1] + "Controller.java");
                            fileTemplate = ControllerTemplate.getTemplate(args[1]);
                            break;
                        case "Migration":
                            filePath.append("database/migrations/" + args[1] + "Migration.java");
                            fileTemplate = MigrationTemplate.getTemplate(args[1]);
                            break;
                        default:
                            break;
                    }

                    result = FilesGenerator.newFile(filePath, fileTemplate);

                    break;
                default:
                    break;
            }

            if (result) {
                System.out.println("[" + BLUE + "INFO" + RESET + "]: " + GREEN + "Jartisan Created " + BLUE + args[0]
                        + GREEN + " Successfully" + RESET);
            }
        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Jartisan Exception: " + e + RESET);
        }
    }
}
