package com.library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

import com.library.applications.LoginApplication;

public class SplashController {

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private ImageView imgIniciarSesion;

    @FXML
    private void initialize() {
        DropShadow sombra = new DropShadow();
        sombra.setRadius(20.0);
        sombra.setColor(Color.web("#00BFFF"));

        btnInfo.setOnMouseEntered((MouseEvent e) -> {
            btnInfo.setEffect(sombra);
            btnInfo.setScaleX(1.1);
            btnInfo.setScaleY(1.1);
        });

        btnInfo.setOnMouseExited((MouseEvent e) -> {
            btnInfo.setEffect(null);
            btnInfo.setScaleX(1.0);
            btnInfo.setScaleY(1.0);
        });
        btnInfo.setOnAction(e -> abrirVentanaIS());

        Image normal = new Image(getClass().getResourceAsStream("/img/Botones/BPIS.png"));
        Image hover = new Image(getClass().getResourceAsStream("/img/Botones/BLPIS.png"));

        btnIniciarSesion.setOnMouseEntered(e -> {
            imgIniciarSesion.setImage(hover);
            imgIniciarSesion.setFitWidth(200);
            imgIniciarSesion.setFitHeight(100);
        });

        btnIniciarSesion.setOnMouseExited(e -> {
            imgIniciarSesion.setImage(normal);
            imgIniciarSesion.setFitWidth(190);
            imgIniciarSesion.setFitHeight(95);
        });

        btnIniciarSesion.setOnAction(e -> abrirVentanaIS());
    }

    @FXML
    private void abrirVentanaIS() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/IS-view.fxml"));
            Scene scene = new Scene(loader.load(), 870, 500);
            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("IniciarSesión");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarError("Error al abrir la ventana de Inicio de Sesión: " + ex.getMessage());
        }
    }


    @FXML
    private void mostrarError(String msg) {
        System.err.println(msg);
    }
}



