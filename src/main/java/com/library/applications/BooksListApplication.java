package com.library.applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksListApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage Stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BooksListView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 870, 500);
            Stage.setTitle("Lista de Libros");
            Stage.setScene(scene);
            Stage.show();


    }
}