package com.library.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksDetailsController {

    @FXML
    private Button btnLeerBlaze;

    @FXML
    void abrirLibroBlaze(ActionEvent event) {
        // Ruta específica para ESTE libro (Blaze)
        String rutaDelPdf = "C:\\dev\\Java-Library-Project\\src\\main\\resources\\img\\Pdfs\\Autoinventario de escucha_JJVM.pdf";

        abrirVisorPDF(rutaDelPdf);
    }

    // Método genérico para abrir el visor
    private void abrirVisorPDF(String rutaPdf) {
        try {
            // 1. Cargar el FXML del visor de libros
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BooksView.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador del visor (BooksController)
            BooksController controller = loader.getController();

            // 3. Pasar la ruta del PDF al controlador
            controller.setPdfFilePath(rutaPdf);

            // 4. Crear y mostrar la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Lector de Libros");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

            // Opcional: Cerrar la ventana de detalles actual si lo deseas
            // ((Stage) btnLeerBlaze.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
