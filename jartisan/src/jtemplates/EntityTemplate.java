package src.jtemplates;

public final class EntityTemplate {

    public static String getTemplate(String className) {

        return "package com.library.entities;\n\n" +
                "import com.library.kernel.orm.Entity;\n\n" +
                "import java.util.ArrayList;\n\n" +
                "import java.util.HashMap;\n\n" +
                "public final class " + className + "Entity implements Entity {\n" +
                "\n" +
                "\tprivate String tableName = \"\";\n\n" +
                "\tprivate String primaryKey = \"\";\n\n" +
                "\tprivate boolean hasAutoIncrement = true; \n\n" +
                "\tprivate HashMap<Class<?>, String> relationships = null /*new HashMap<Class<?>, String>() {{}}*/;\n\n" +
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
                "}\n";

    }

}
