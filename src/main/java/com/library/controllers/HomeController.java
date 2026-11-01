package com.library.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private void irALibros(ActionEvent event) throws IOException {
        cambiarEscena(event, "/BooksListView.fxml", 870, 800);
    }

    @FXML
    private void irACRUDLibros(ActionEvent event) throws IOException {
        cambiarEscena(event, "/BooksCRUDView.fxml", 1000, 750);
    }

    @FXML
    private void irACRUDUsuarios(ActionEvent event) throws IOException {
        cambiarEscena(event, "/UsersCRUDView.fxml", 700, 650);
    }

    @FXML
    private void salir() {
        Platform.exit();
    }

    @FXML
    private void mostrarAcercaDe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Biblioteca Digital");
        alert.setContentText("Sistema de gestión de biblioteca\nVersión 1.0\n\n© 2025 Todos los derechos reservados");
        alert.showAndWait();
    }

    // Método auxiliar mejorado para cambiar de escena con control de tamaño
    private void cambiarEscena(ActionEvent event, String fxmlPath, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Establecer tamaños mínimos y preferidos
        stage.setMinWidth(width * 0.8);  // Mínimo 80% del tamaño preferido
        stage.setMinHeight(height * 0.8);
        
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        
        // Ajustar el tamaño de la ventana
        stage.setWidth(width);
        stage.setHeight(height);
        
        // Centrar la ventana
        stage.centerOnScreen();
        
        stage.show();
    }
}