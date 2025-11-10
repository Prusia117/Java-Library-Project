package com.library.kernel.jartisan.src.jfiles.jtemplates;

public final class JModelTemplate implements IJTemplate  {

    @Override
    public String getTemplate(String className) {

        return "package com.library.models;\n\n" +
                "import java.util.ArrayList;\n\n" +
                "import com.library.kernel.kernelSql.orm.repository.Repository;\n\n" +
                "import com.library.entities." + className + "Entity;\n\n" +
                "import com.library.kernel.framework.IModel;\n\n" +
                "public final class " + className + "Model implements IModel{\n" +
                "\n" +
                "\tRepository<" + className + "Entity> " + className.toLowerCase() + "Repositrory = new Repository<" + className + "Entity>(" + className + "Entity.class);\n" +
                "\n" +
                "\tpublic ArrayList<" + className + "Entity> getAll(){\n" +
                "\t\tArrayList<" + className + "Entity> entityList = " + className.toLowerCase() + "Repositrory.getAll();\n" +
                "\t\treturn entityList;\n" +
                "\t}\n\n" +
                "\t@Override\n" +
                "\tpublic ArrayList<" + className + "Entity> getById(String id){\n" +
                "\t\tArrayList<" + className + "Entity> entityList = " + className.toLowerCase() + "Repositrory.getById(id);\n" +
                "\t\treturn entityList;\n" +
                "\t}\n\n" +
                "\t@Override\n" +
                "\tpublic boolean save(){\n" +
                "\t\t" + className + "Entity entity = new " + className + "Entity();\n" +
                "\t\treturn " + className.toLowerCase() + "Repositrory" + ".save(entity)" + ";\n" +
                "\t}\n\n" +
                "\t@Override\n" +
                "\tpublic boolean update(String id){\n" +
                "\t\t" + className + "Entity entity = new " + className + "Entity();\n" +
                "\t\treturn " + className.toLowerCase() + "Repositrory" + ".update(entity, id)" + ";\n" +
                "\t}\n\n" +
                "\t@Override\n" +
                "\tpublic boolean delete(String id){\n" +
                "\t\treturn " + className.toLowerCase() + "Repositrory" + ".delete(id)" + ";\n" +
                "\t}\n\n" +
                "}\n";

    }
}


