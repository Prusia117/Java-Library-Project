package com.library.controllers;

import com.library.entities.BookEntity;
import com.library.models.BookModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class BooksCRUDFormController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnio;
    @FXML private TextField txtCategoria;
    @FXML private ImageView imagePreview;

    @FXML private TableView<BookEntity> tablaLibros;
    @FXML private TableColumn<BookEntity, String> colTitulo;
    @FXML private TableColumn<BookEntity, String> colAutor;
    @FXML private TableColumn<BookEntity, Integer> colAnio;
    @FXML private TableColumn<BookEntity, String> colCategoria;

    private String rutaImagen = null;
    private String rutaPDF = null;

    private final BookModel bookModel = new BookModel();

    @FXML
    public void initialize() {

        colTitulo.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().book_title));

        colAutor.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().book_author));

        colAnio.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().book_year).asObject());

        colCategoria.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().book_category));

        cargarTabla();

        tablaLibros.setOnMouseClicked(event -> cargarSeleccionado());
    }

    private void cargarTabla() {
        ArrayList<BookEntity> lista = bookModel.getAll();
        tablaLibros.setItems(FXCollections.observableArrayList(lista));
    }

    private void cargarSeleccionado() {
        BookEntity seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        txtTitulo.setText(seleccionado.book_title);
        txtAutor.setText(seleccionado.book_author);
        txtAnio.setText(String.valueOf(seleccionado.book_year));
        txtCategoria.setText(seleccionado.book_category);

        if (seleccionado.book_image_path != null) {
            File img = new File("src/main/resources" + seleccionado.book_image_path);
            if (img.exists()) {
                imagePreview.setImage(new Image(img.toURI().toString()));
            }
        }

        rutaImagen = seleccionado.book_image_path;
        rutaPDF = seleccionado.book_file_path;
    }

    // =======================================================
    // COPIAR ARCHIVOS A RESOURCES
    // =======================================================

    private String copiarArchivo(File origen, String destinoCarpeta) {
        try {
            String fileName = origen.getName();
            String rutaFinal = destinoCarpeta + fileName;

            Path destino = Path.of("src/main/resources" + rutaFinal);

            Files.createDirectories(destino.getParent());
            Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

            return rutaFinal;  // Lo que se guarda en BD
        }
        catch (Exception e) {
            alerta("Error", "No se pudo copiar el archivo:\n" + e.getMessage());
            return null;
        }
    }

    // =======================================================
    // SELECCIONAR IMAGEN
    // =======================================================

    @FXML
    private void onSeleccionarImagen() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png")
        );
        File file = chooser.showOpenDialog(null);

        if (file != null) {

            // COPIAR A RESOURCES/img/Imagenes/
            String ruta = copiarArchivo(file, "/img/Imagenes/");
            if (ruta != null) {
                rutaImagen = ruta;
                imagePreview.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    // =======================================================
    // SELECCIONAR PDF
    // =======================================================

    @FXML
    private void onSeleccionarPDF() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            // COPIAR A RESOURCES/img/Pdfs/
            String ruta = copiarArchivo(file, "/img/Pdfs/");
            if (ruta != null) {
                rutaPDF = ruta;
                alerta("PDF seleccionado", "Se guardó una copia del PDF:\n" + file.getName());
            }
        }
    }

    // =======================================================
    // AGREGAR
    // =======================================================

    @FXML
    private void onAgregar() {

        if (txtAnio.getText().isEmpty()) {
            alerta("Error", "El campo Año no puede estar vacío.");
            return;
        }

        int year;

        try {
            year = Integer.parseInt(txtAnio.getText());
        } catch (Exception e) {
            alerta("Error", "El año debe ser un número válido.");
            return;
        }

        boolean ok = bookModel.save(
                txtTitulo.getText(),
                txtAutor.getText(),
                year,
                txtCategoria.getText(),
                rutaPDF,
                rutaImagen
        );

        if (!ok) {
            alerta("Error", "No se pudo guardar el libro");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Libro agregado correctamente");
    }

    // =======================================================
    // ACTUALIZAR
    // =======================================================

    @FXML
    private void onActualizar() {
        BookEntity seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Advertencia", "Selecciona un libro para actualizar");
            return;
        }

        seleccionado.book_title = txtTitulo.getText();
        seleccionado.book_author = txtAutor.getText();
        seleccionado.book_year = Integer.parseInt(txtAnio.getText());
        seleccionado.book_category = txtCategoria.getText();
        seleccionado.book_image_path = rutaImagen;
        seleccionado.book_file_path = rutaPDF;

        boolean ok = bookModel.update(seleccionado.book_id.toString());

        if (!ok) {
            alerta("Error", "No se pudo actualizar el libro");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Libro actualizado correctamente");
    }

    // =======================================================
    // ELIMINAR
    // =======================================================

    @FXML
    private void onEliminar() {
        BookEntity seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Advertencia", "Selecciona un libro para eliminar");
            return;
        }

        boolean ok = bookModel.delete(seleccionado.book_id.toString());

        if (!ok) {
            alerta("Error", "No se pudo eliminar el libro");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Libro eliminado correctamente");
    }

    // =======================================================
    // LIMPIAR
    // =======================================================

    private void limpiar() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtCategoria.clear();
        imagePreview.setImage(null);

        rutaImagen = null;
        rutaPDF = null;
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
