package com.library.controllers;

import com.library.entities.BookEntity;
import com.library.models.BookModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BooksListController implements Initializable {

    @FXML
    private FlowPane contenedorLibros; // Enlazado al FlowPane que acabamos de crear

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Instanciar tu modelo
        BookModel bookModel = new BookModel();

        // 2. Obtener la lista real de la BD
        ArrayList<BookEntity> listaDeLibros = bookModel.getAll();

        // 3. Generar la vista para cada libro
        if (listaDeLibros != null) {
            for (BookEntity libro : listaDeLibros) {
                VBox tarjeta = crearTarjetaLibro(libro);
                contenedorLibros.getChildren().add(tarjeta);
            }
        }
    }

    private VBox crearTarjetaLibro(BookEntity libro) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #4E342E; -fx-border-width: 2; -fx-padding: 15; -fx-background-color: white;");
        card.setPrefWidth(200);
        card.setAlignment(Pos.CENTER);


        // --- IMAGEN (Lógica corregida para rutas C:\...) ---
        ImageView imageView = new ImageView();
        try {
            String ruta = libro.book_image_path;

            Image imagenFinal = null;



            if (ruta != null && !ruta.isEmpty()) {

                String rutaAbsoluta = ruta.startsWith("/") ? ruta : "/" + ruta;



                InputStream stream = getClass().getResourceAsStream(rutaAbsoluta);



                if (stream != null) {

                    imagenFinal = new Image(stream);
                    imageView.setImage(imagenFinal);

                    System.out.println("Imagen cargada correctamente desde recursos: " + rutaAbsoluta);

                } else {

                    System.err.println("No jala la CHingadera " + rutaAbsoluta);

                }

            }

        } catch (Exception e) {
            System.out.println("Error al procesar la ruta de imagen: " + libro.book_title);
            e.printStackTrace();
        }

        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // --- Título ---
        Label lblTitulo = new Label(libro.book_title); // Usamos tu campo 'book_title'
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        lblTitulo.setWrapText(true);

        // --- Botón Detalles ---
        Button btnDetalles = new Button("Detalles");
        btnDetalles.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;");

        // Acción: Abrir detalles pasando ESTE libro específico
        btnDetalles.setOnAction(event -> abrirDetallesDelLibro(libro));

        card.getChildren().addAll(imageView, lblTitulo, btnDetalles);
        return card;
    }

    private void abrirDetallesDelLibro(BookEntity libro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BooksDetailsView.fxml"));
            Parent root = loader.load();

            // Pasamos el libro al controlador de detalles
            BooksDetailsController detailsController = loader.getController();
            detailsController.setLibroActual(libro);

            Stage stage = new Stage();
            stage.setTitle("Detalles: " + libro.book_title);
            stage.setScene(new Scene(root, 870, 500));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}