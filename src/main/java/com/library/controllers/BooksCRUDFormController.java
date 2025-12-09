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

        // Se enlazan los campos EXACTOS del BookEntity
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

        if (seleccionado.book_image_path != null && !seleccionado.book_image_path.isEmpty()) {
            File img = new File(seleccionado.book_image_path);
            if (img.exists()) {
                imagePreview.setImage(new Image(img.toURI().toString()));
            }
        }

        rutaImagen = seleccionado.book_image_path;
    }

    @FXML
    private void onSeleccionarImagen() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            rutaImagen = file.getAbsolutePath();
            imagePreview.setImage(new Image(file.toURI().toString()));
        }
    }
    @FXML
    private void onSeleccionarPDF() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            rutaPDF = file.getAbsolutePath();
            alerta("PDF seleccionado", "Se cargó el PDF:\n" + file.getName());
        }
    }


    @FXML
    private void onAgregar() {
        String yearText = txtAnio.getText();

        if (yearText == null || yearText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Faltan datos");
            alert.setContentText("El campo Año está vacío. Ingresa un número.");
            alert.show();
            return;
        }

        int year = Integer.parseInt(yearText);

        boolean ok = bookModel.save(
                txtTitulo.getText(),
                txtAutor.getText(),
                year,
                txtCategoria.getText(),
                rutaPDF,           //book_file_path
                rutaImagen         //book_image_path
        );


        if (!ok) {
            alerta("Error", "No se pudo guardar el libro");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Libro agregado correctamente");
    }

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

        boolean ok = bookModel.update(seleccionado.book_id.toString());

        if (!ok) {
            alerta("Error", "No se pudo actualizar el libro");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Libro actualizado correctamente");
    }

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

    private void limpiar() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtCategoria.clear();
        imagePreview.setImage(null);
        rutaImagen = null;
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}