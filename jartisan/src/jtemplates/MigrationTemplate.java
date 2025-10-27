package src.jtemplates;

public final class MigrationTemplate {

    public static String getTemplate(String className) {

        return "package com.library.database.migrations;\n\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import com.library.kernel.migrator.Migrator;\n" +
                "import com.library.kernel.migrator.core.ddl.table.Schema;\n\n" +
                "public class " + className + "Migration implements Migrator {\n" +
                "\n" +
                "\t@Override\n" +
                "\tpublic ArrayList<Schema> up() {\n" +
                "\t\treturn new ArrayList<Schema>(List.of(" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\t));\n" +
                "\t}\n" +
                "\n" +
                "\t@Override\n" +
                "\tpublic void rollback() {\n" +
                "\t\t\n" +
                "\t}\n" +
                "}\n";

    }

}
