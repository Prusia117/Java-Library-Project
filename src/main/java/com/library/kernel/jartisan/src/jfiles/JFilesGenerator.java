package com.library.kernel.jartisan.src.jfiles;

import com.library.kernel.jartisan.src.Colors;
import com.library.kernel.jartisan.src.jfiles.jtemplates.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class JFilesGenerator implements Colors {

    public static final List<String> J_SUPPORTED_FILE_TYPES = Arrays.asList("Entity", "Model", "Controller", "Migration");

    private static final HashMap<String, String> DIRECTORIES = new HashMap<String, String>() {{
        put("Entity", "entities");
        put("Model", "models");
        put("Controller", "controllers");
        put("Migration", "database/migrations");
    }};

    private static final HashMap<String, IJTemplate> CLASSES = new HashMap<String, IJTemplate>() {{
        put("Entity", new JEntityTemplate());
        put("Model", new JModelTemplate());
        put("Controller", new JControllerTemplate());
        put("Migration", new JMigrationTemplate());
    }};

    public static boolean generateNewFile(String type, String className){
        String filePath = "src/main/java/com/library/" + DIRECTORIES.get(type) + className + type +".java";

        String fileTemplate = CLASSES.get(type).getTemplate(className);
        boolean result = newFile(filePath, fileTemplate);
        return result;
    }

    private static boolean newFile(String path, String content){
        try {
            File file = new File(path);

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
