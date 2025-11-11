package com.library.applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeApplication extends Application {

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource("/I-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 870, 500);
        stage.setTitle("IniciO");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
