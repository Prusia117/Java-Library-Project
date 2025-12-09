package com.library.controllers;

import com.library.entities.BookEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksDetailsController {

    @FXML private ImageView imgPortadaDetalle; // Debe coincidir con el FXML
    @FXML private Label lblDescripcionDetalle; // Debe coincidir con el FXML

    private BookEntity libroActual; // Aquí guardaremos el libro que estamos viendo

    // Este método lo llama la lista para "inyectar" el libro
    public void setLibroActual(BookEntity libro) {
        this.libroActual = libro;
        actualizarVista();
    }

    private void actualizarVista() {
        if (libroActual != null) {
            lblDescripcionDetalle.setText("Libro: " + libroActual.book_title + "\nAutor: " + libroActual.book_author + "\nAño: " + libroActual.book_year + "\nCategoria : " + libroActual.book_category);

            // --- CORRECCIÓN DE IMAGEN DE DETALLE ---
            try {
                String ruta = libroActual.book_image_path;
                if (ruta != null) {
                    java.io.File archivoLocal = new java.io.File(ruta);

                    if (archivoLocal.exists()) {
                        // Cargar desde C:\...
                        imgPortadaDetalle.setImage(new Image(archivoLocal.toURI().toString()));
                    } else {
                        // Cargar desde recursos internos
                        var stream = getClass().getResourceAsStream(ruta);
                        if (stream != null) {
                            imgPortadaDetalle.setImage(new Image(stream));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error cargando imagen detalle");
            }
        }
    }

    @FXML
    void abrirLibroBlaze(ActionEvent event) {
        if (libroActual != null && libroActual.book_file_path != null) {
            // Usamos la ruta dinámica del libro actual
            abrirVisorPDF(libroActual.book_file_path);
        } else {
            System.out.println("Este libro no tiene ruta de PDF asignada.");
        }
    }

    private void abrirVisorPDF(String rutaPdf) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BooksView.fxml"));
            Parent root = loader.load();

            BooksController controller = loader.getController();

            // PASO CLAVE: Pasamos la ruta tal cual viene de la BD (C:\... o /img/...)
            controller.setPdfFilePath(rutaPdf);

            Stage stage = new Stage();
            stage.setTitle("Lector");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}