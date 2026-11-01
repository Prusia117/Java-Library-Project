package com.library.controllers;

import com.library.entities.BookEntity;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BooksDetailsController {

    @FXML private ImageView imagenLibro;
    @FXML private Label lblTitulo;
    @FXML private Label lblAutor;
    @FXML private Label lblAnio;
    @FXML private Label lblCategoria;
    @FXML private Button btnLeerPDF;

    private BookEntity libro;

    public void setLibro(BookEntity libro) {
        this.libro = libro;
        cargarDatos();
    }

    private void cargarDatos() {
        if (libro == null) return;

        lblTitulo.setText(libro.book_title);
        lblAutor.setText(libro.book_author);
        lblAnio.setText(String.valueOf(libro.book_year));
        lblCategoria.setText(libro.book_category);

        // Cargar imagen
        if (libro.book_image_path != null && !libro.book_image_path.isEmpty()) {
            File imgFile = new File("src/main/resources" + libro.book_image_path);
            if (imgFile.exists()) {
                imagenLibro.setImage(new Image(imgFile.toURI().toString()));
            }
        }

        // Deshabilitar botón si no hay PDF
        if (libro.book_file_path == null || libro.book_file_path.isEmpty()) {
            btnLeerPDF.setDisable(true);
        }
    }

    @FXML
    private void abrirPDF() {
        if (libro.book_file_path == null || libro.book_file_path.isEmpty()) {
            mostrarAlerta("PDF no disponible", "Este libro no tiene un archivo PDF asociado.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Verificar que el archivo existe
            File pdfFile = new File("src/main/resources" + libro.book_file_path);
            if (!pdfFile.exists()) {
                mostrarAlerta("Error", "El archivo PDF no se encontró en: " + libro.book_file_path, Alert.AlertType.ERROR);
                return;
            }

            // Cargar el visor de PDF en un hilo separado para evitar congelamiento
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/BooksView.fxml"));
                    Parent root = loader.load();

                    // Pasar la ruta del PDF al controlador
                    BooksController controller = loader.getController();
                    controller.setPdfFilePath(pdfFile.getAbsolutePath());

                    // Crear nueva ventana para el visor
                    Stage pdfStage = new Stage();
                    pdfStage.setTitle("Leyendo: " + libro.book_title);
                    pdfStage.setScene(new Scene(root, 900, 700));
                    pdfStage.show();

                } catch (IOException e) {
                    mostrarAlerta("Error", "No se pudo abrir el visor de PDF: " + e.getMessage(), Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void descargarPDF() {
        if (libro.book_file_path == null || libro.book_file_path.isEmpty()) {
            mostrarAlerta("PDF no disponible", "Este libro no tiene un archivo PDF asociado.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Archivo fuente en resources
            File pdfOrigen = new File("src/main/resources" + libro.book_file_path);
            
            if (!pdfOrigen.exists()) {
                mostrarAlerta("Error", "El archivo PDF no se encontró.", Alert.AlertType.ERROR);
                return;
            }

            // Crear FileChooser para que el usuario elija dónde guardar
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar PDF");
            
            // Nombre sugerido para el archivo
            String nombreSugerido = libro.book_title.replaceAll("[^a-zA-Z0-9.-]", "_") + ".pdf";
            fileChooser.setInitialFileName(nombreSugerido);
            
            // Filtro para archivos PDF
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
            );

            // Mostrar diálogo para elegir ubicación
            File archivoDestino = fileChooser.showSaveDialog(lblTitulo.getScene().getWindow());

            if (archivoDestino != null) {
                // Copiar el archivo
                Files.copy(
                    pdfOrigen.toPath(),
                    archivoDestino.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                );

                mostrarAlerta(
                    "Descarga exitosa", 
                    "El PDF se ha guardado en:\n" + archivoDestino.getAbsolutePath(),
                    Alert.AlertType.INFORMATION
                );
            }

        } catch (IOException e) {
            mostrarAlerta("Error al descargar", "No se pudo guardar el PDF: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void regresarALista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BooksListView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) lblTitulo.getScene().getWindow();
            
            // Ajustar tamaños para lista
            stage.setMinWidth(870);
            stage.setMinHeight(800);
            stage.setWidth(870);
            stage.setHeight(800);
            
            stage.setScene(new Scene(root, 870, 800));
            stage.setTitle("Lista de Libros");
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}