package src.jtemplates;

public final class ControllerTemplate {

    public static String getTemplate(String className) {

        return "package com.library.controllers;\n\n" +
                "import javafx.fxml.FXML;\n\n" +
                "public class " + className + "Controller {\n" +
                "\n" +
                "}\n";

    }

}
