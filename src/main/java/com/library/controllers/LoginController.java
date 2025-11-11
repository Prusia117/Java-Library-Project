package com.library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField campoVisible;
    @FXML private Button btnVerContrasena;
    @FXML private ImageView iconoOjo;
    @FXML private Button btIniciarSesion;
    @FXML private ImageView imgIniciarSesion;

    private boolean mostrando = false;

    @FXML
    public void initialize() {
        DropShadow sombra = new DropShadow();
        sombra.setRadius(20.0);
        sombra.setColor(Color.web("#00BFFF"));

        btIniciarSesion.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> imgIniciarSesion.setEffect(sombra));
        btIniciarSesion.addEventHandler(MouseEvent.MOUSE_EXITED, e -> imgIniciarSesion.setEffect(null));
    }

    @FXML
    private void togglePassword() {
        if (!mostrando) {
            campoVisible.setText(txtContrasena.getText());
            campoVisible.setVisible(true);
            campoVisible.setManaged(true);
            txtContrasena.setVisible(false);
            txtContrasena.setManaged(false);
            iconoOjo.setImage(new Image(getClass().getResourceAsStream("/img/Botones/BNV.png")));
            mostrando = true;
        } else {
            txtContrasena.setText(campoVisible.getText());
            campoVisible.setVisible(false);
            campoVisible.setManaged(false);
            txtContrasena.setVisible(true);
            txtContrasena.setManaged(true);
            iconoOjo.setImage(new Image(getClass().getResourceAsStream("/img/Botones/BV.png")));
            mostrando = false;
        }
    }

    @FXML
    private void irAInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            Scene scene = new Scene(root, 870, 500);
            stage.setScene(scene);
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
