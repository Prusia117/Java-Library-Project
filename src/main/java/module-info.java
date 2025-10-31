module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.library;


    opens com.library to javafx.fxml;
    exports com.library;
}
