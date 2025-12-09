package com.library.controllers;

import com.library.entities.BookEntity;
import com.library.models.BookModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class BooksListController {

    @FXML
    private FlowPane contenedorLibros;

    private final BookModel bookModel = new BookModel();

    @FXML
    public void initialize() {
        cargarLibros();
    }

    private void cargarLibros() {
        ArrayList<BookEntity> libros = bookModel.getAll();

        for (BookEntity libro : libros) {
            VBox tarjeta = crearTarjetaLibro(libro);
            contenedorLibros.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaLibro(BookEntity libro) {
        VBox tarjeta = new VBox(12);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);" +
                "-fx-padding: 20;" +
                "-fx-cursor: hand;"
        );
        tarjeta.setPrefWidth(200);
        tarjeta.setPrefHeight(320);

        // Imagen
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        if (libro.book_image_path != null && !libro.book_image_path.isEmpty()) {
            File imgFile = new File("src/main/resources" + libro.book_image_path);
            if (imgFile.exists()) {
                imageView.setImage(new Image(imgFile.toURI().toString()));
            }
        }

        // Título
        Label titulo = new Label(libro.book_title);
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        titulo.setWrapText(true);
        titulo.setMaxWidth(180);
        titulo.setAlignment(Pos.CENTER);

        // Autor
        Label autor = new Label(libro.book_author);
        autor.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        // Botón Ver Detalles
        Button btnDetalles = new Button("Ver Detalles");
        btnDetalles.setStyle(
                "-fx-background-color: #3498db;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 15 8 15;"
        );
        
        btnDetalles.setOnAction(event -> abrirDetalles(libro));

        tarjeta.getChildren().addAll(imageView, titulo, autor, btnDetalles);

        return tarjeta;
    }

    private void abrirDetalles(BookEntity libro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BooksDetailsView.fxml"));
            Parent root = loader.load();

            // Pasar el libro al controlador de detalles
            BooksDetailsController controller = loader.getController();
            controller.setLibro(libro);

            // Obtener el Stage actual y cerrarlo
            Stage stageActual = (Stage) contenedorLibros.getScene().getWindow();
            
            // Crear nueva ventana para detalles
            Stage stageDetalles = new Stage();
            stageDetalles.setTitle("Detalles del Libro");
            stageDetalles.setScene(new Scene(root, 900, 700));
            stageDetalles.show();
            
            // Cerrar la ventana de lista de libros
            stageActual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void regresarAHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contenedorLibros.getScene().getWindow();
            
            // Restablecer tamaños para Home
            stage.setMinWidth(870);
            stage.setMinHeight(500);
            stage.setWidth(870);
            stage.setHeight(500);
            
            stage.setScene(new Scene(root, 870, 500));
            stage.setTitle("Biblioteca Digital");
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}