package com.library.applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksCRUDApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BooksCRUDApplication.class.getResource("/BooksCRUDView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 870, 900);
        stage.setTitle("Agregar Libro");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}