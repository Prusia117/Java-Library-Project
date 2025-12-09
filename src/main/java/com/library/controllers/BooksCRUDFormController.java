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

    // Archivos temporales (NO copiados aún)
    private File archivoImagenTemporal = null;
    private File archivoPDFTemporal = null;

    // Rutas actuales del libro en edición
    private String rutaImagenActual = null;
    private String rutaPDFActual = null;

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

        // Guardar rutas actuales
        rutaImagenActual = seleccionado.book_image_path;
        rutaPDFActual = seleccionado.book_file_path;

        // Mostrar imagen actual
        if (seleccionado.book_image_path != null && !seleccionado.book_image_path.isEmpty()) {
            File img = new File("src/main/resources" + seleccionado.book_image_path);
            if (img.exists()) {
                imagePreview.setImage(new Image(img.toURI().toString()));
            }
        }

        // Limpiar archivos temporales
        archivoImagenTemporal = null;
        archivoPDFTemporal = null;
    }

    // =======================================================
    // VALIDACIÓN DE CAMPOS
    // =======================================================

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtTitulo.getText() == null || txtTitulo.getText().trim().isEmpty()) {
            errores.append("- El Título es obligatorio\n");
        }

        if (txtAutor.getText() == null || txtAutor.getText().trim().isEmpty()) {
            errores.append("- El Autor es obligatorio\n");
        }

        if (txtAnio.getText() == null || txtAnio.getText().trim().isEmpty()) {
            errores.append("- El Año es obligatorio\n");
        } else {
            try {
                int year = Integer.parseInt(txtAnio.getText().trim());
                if (year < 1000 || year > 9999) {
                    errores.append("- El Año debe ser un valor entre 1000 y 9999\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El Año debe ser un número válido\n");
            }
        }

        if (txtCategoria.getText() == null || txtCategoria.getText().trim().isEmpty()) {
            errores.append("- La Categoría es obligatoria\n");
        }

        if (errores.length() > 0) {
            alerta("Campos incompletos", errores.toString());
            return false;
        }

        return true;
    }

    // =======================================================
    // VALIDACIÓN DE ARCHIVOS PARA AGREGAR
    // =======================================================

    private boolean validarArchivosAgregar() {
        StringBuilder errores = new StringBuilder();

        if (archivoImagenTemporal == null) {
            errores.append("- Debes seleccionar una imagen\n");
        }

        if (archivoPDFTemporal == null) {
            errores.append("- Debes seleccionar un archivo PDF\n");
        }

        if (errores.length() > 0) {
            alerta("Archivos requeridos", errores.toString());
            return false;
        }

        return true;
    }
    // =======================================================
    // COPIAR ARCHIVOS A RESOURCES
    // =======================================================

    private String copiarArchivo(File origen, String destinoCarpeta) {
        try {
            long timestamp = System.currentTimeMillis();
            String fileName = timestamp + "_" + origen.getName();
            String rutaFinal = destinoCarpeta + fileName;

            Path destino = Path.of("src/main/resources" + rutaFinal);

            Files.createDirectories(destino.getParent());
            Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

            return rutaFinal;
        }
        catch (Exception e) {
            alerta("Error", "No se pudo copiar el archivo:\n" + e.getMessage());
            return null;
        }
    }

    // =======================================================
    // SELECCIONAR IMAGEN (solo selección, NO copia)
    // =======================================================

    @FXML
    private void onSeleccionarImagen() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar Imagen");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png")
        );
        archivoImagenTemporal = chooser.showOpenDialog(null);

        if (archivoImagenTemporal != null) {
            // Solo preview, NO copiar aún
            imagePreview.setImage(new Image(archivoImagenTemporal.toURI().toString()));
        }
    }

    // =======================================================
    // SELECCIONAR PDF (solo selección, NO copia)
    // =======================================================

    @FXML
    private void onSeleccionarPDF() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar PDF");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );

        archivoPDFTemporal = chooser.showOpenDialog(null);

        if (archivoPDFTemporal != null) {
            alerta("PDF seleccionado", "PDF: " + archivoPDFTemporal.getName());
        }
    }


    // =======================================================
    // AGREGAR - CON VALIDACIÓN DE ARCHIVOS
    // =======================================================

    @FXML
    private void onAgregar() {
        // VALIDAR CAMPOS PRIMERO
        if (!validarCampos()) {
            return;
        }

        // VALIDAR QUE SE HAYAN SELECCIONADO ARCHIVOS
        if (!validarArchivosAgregar()) {
            return;
        }

        int year = Integer.parseInt(txtAnio.getText().trim());

        // COPIAR ARCHIVOS (ya validados que existen)
        String rutaImagenFinal = copiarArchivo(archivoImagenTemporal, "/img/Imagenes/");
        if (rutaImagenFinal == null) {
            alerta("Error", "No se pudo copiar la imagen");
            return;
        }

        String rutaPDFFinal = copiarArchivo(archivoPDFTemporal, "/img/Pdfs/");
        if (rutaPDFFinal == null) {
            alerta("Error", "No se pudo copiar el PDF");
            return;
        }

        // CREAR ENTITY CON LAS RUTAS REALES
        BookEntity nuevoLibro = new BookEntity();
        nuevoLibro.book_title = txtTitulo.getText().trim();
        nuevoLibro.book_author = txtAutor.getText().trim();
        nuevoLibro.book_year = year;
        nuevoLibro.book_category = txtCategoria.getText().trim();
        nuevoLibro.book_file_path = rutaPDFFinal;
        nuevoLibro.book_image_path = rutaImagenFinal;

        // GUARDAR LA ENTIDAD
        boolean ok = bookModel.save(nuevoLibro);

        if (!ok) {
            alerta("Error", "No se pudo guardar el libro");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Libro agregado correctamente");
    }

    // =======================================================
    // ACTUALIZAR - UNA SOLA ACTUALIZACIÓN
    // =======================================================

    @FXML
    private void onActualizar() {
        BookEntity seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Advertencia", "Selecciona un libro para actualizar");
            return;
        }

        // VALIDAR CAMPOS PRIMERO
        if (!validarCampos()) {
            return;
        }

        // Actualizar campos básicos
        seleccionado.book_title = txtTitulo.getText().trim();
        seleccionado.book_author = txtAutor.getText().trim();
        seleccionado.book_year = Integer.parseInt(txtAnio.getText().trim());
        seleccionado.book_category = txtCategoria.getText().trim();

        // COPIAR ARCHIVOS NUEVOS PRIMERO (si existen)
        if (archivoImagenTemporal != null) {
            String nuevaRutaImagen = copiarArchivo(archivoImagenTemporal, "/img/Imagenes/");
            if (nuevaRutaImagen != null) {
                seleccionado.book_image_path = nuevaRutaImagen;
            } else {
                alerta("Error", "No se pudo copiar la nueva imagen");
                return;
            }
        } else {
            // Mantener imagen actual
            seleccionado.book_image_path = rutaImagenActual;
        }

        if (archivoPDFTemporal != null) {
            String nuevaRutaPDF = copiarArchivo(archivoPDFTemporal, "/img/Pdfs/");
            if (nuevaRutaPDF != null) {
                seleccionado.book_file_path = nuevaRutaPDF;
            } else {
                alerta("Error", "No se pudo copiar el nuevo PDF");
                return;
            }
        } else {
            // Mantener PDF actual
            seleccionado.book_file_path = rutaPDFActual;
        }

        // UNA SOLA ACTUALIZACIÓN EN BD CON TODOS LOS DATOS
        boolean ok = bookModel.update(seleccionado, seleccionado.book_id.toString());

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

        // Confirmación de eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de eliminar el libro \"" + seleccionado.book_title + "\"?");

        if (confirmacion.showAndWait().get() != ButtonType.OK) {
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

    @FXML
    private void limpiar() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtCategoria.clear();
        imagePreview.setImage(null);

        archivoImagenTemporal = null;
        archivoPDFTemporal = null;
        rutaImagenActual = null;
        rutaPDFActual = null;

        tablaLibros.getSelectionModel().clearSelection();
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}