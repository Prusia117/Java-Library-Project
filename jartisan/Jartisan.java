package jartisan;

import java.io.File;
import java.io.PrintWriter;

import com.library.kernel.classes.Colors;

public final class Jartisan implements Colors {
    public static void main(String[] args) {

        StringBuilder fileContent = new StringBuilder();
        StringBuilder filePath = new StringBuilder("src/main/java/com/library/");

        switch (args[0]) {
            case "Entity":
                fileContent.append(
                        "package com.library.entities;\n\n" +
                                "import com.library.kernel.orm.Entity;\n\n" +
                                "import java.util.ArrayList;\n\n" +
                                "import java.util.HashMap;\n\n" +
                                "public final class " + args[1] + "Entity implements Entity {\n" +
                                "\n" +
                                "\tprivate String tableName = \"\";\n" +
                                "\tprivate String primaryKey = \"\";\n" +
                                "\tprivate boolean hasAutoIncrement = true; \n" +
                                "\tprivate HashMap<Class<?>, String> relationships = new HashMap<Class<?>, String>() {{}};\n" +
                                "\n\n" +
                                "\tpublic ArrayList<Entity> entityRelationships;\n" +
                                "\n" +
                                "\tpublic String getPrimaryKey() {\n" +
                                "\t\treturn primaryKey;\n" +
                                "\t}\n" +
                                "\n" +
                                "\tpublic String getTableName() {\n" +
                                "\t\treturn tableName;\n" +
                                "\t}\n" +
                                "\n" +
                                "\tpublic boolean getAutoIncrement() {\n" +
                                "\t\treturn hasAutoIncrement;\n" +
                                "\t}\n" +
                                "\n" +
                                "\tpublic HashMap<Class<?>, String> getRelationships(){\n" +
                                "\t\treturn relationships;\n" +
                                "\t}\n" +
                                "\n" +
                                "\tpublic void setEntityRelationships(ArrayList<Entity> entities){\n" +
                                "\t\tthis.entityRelationships = entities;\n" +
                                "\t}\n" +
                                "}\n"
                                );
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
            System.out.println(GREEN + "Jartisan Created " + BLUE + args[0] + GREEN + " Successfully" + RESET);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(RED + "Jartisan Exception: " + e + RESET);
        }
    }
}
