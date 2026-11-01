module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires javafx.swing;
    requires org.apache.pdfbox;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    exports com.library.applications;
    exports com.library.controllers;

    // Muy importante: abrirlos a javafx.fxml
    opens com.library.applications to javafx.fxml;
    opens com.library.controllers to javafx.fxml;

}
