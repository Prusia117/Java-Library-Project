package com.library.kernel.jartisan.src.jfiles.jtemplates;

public final class JControllerTemplate implements IJTemplate {

    @Override
    public String getTemplate(String className) {

        return "package com.library.controllers;\n\n" +
                "import javafx.fxml.FXML;\n\n" +
                "public class " + className + "Controller {\n" +
                "\n" +
                "}\n";

    }

}
