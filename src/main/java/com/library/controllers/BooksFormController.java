package com.library.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BooksFormController {

    @FXML
    private TextField txtTitulo, txtAutor, txtResena, txtFecha;

    @FXML
    private Button btnGuardar, btnImportarPDF, btnImportarIMG;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        txtFecha.textProperty().addListener((obs, oldText, newText) -> {
            String numeros = newText.replaceAll("[^0-9]", "");
            if (numeros.length() > 8) numeros = numeros.substring(0, 8);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numeros.length(); i++) {
                sb.append(numeros.charAt(i));
                if ((i == 1 || i == 3) && i != numeros.length() - 1) sb.append("/");
            }
            String formateado = sb.toString();
            if (!formateado.equals(newText)) txtFecha.setText(formateado);
        });

        DropShadow sombra = new DropShadow();
        sombra.setRadius(10);
        sombra.setOffsetY(3);
        sombra.setColor(Color.rgb(0, 0, 0, 0.4));

        addShadow(btnGuardar, sombra);
        addShadow(btnImportarPDF, sombra);
        addShadow(btnImportarIMG, sombra);
    }

    private void addShadow(Button btn, DropShadow sombra) {
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> btn.setEffect(sombra));
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> btn.setEffect(null));
    }

    @FXML
    private void onGuardarClick(ActionEvent event) {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String resena = txtResena.getText().trim();
        String fecha = txtFecha.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty() || resena.isEmpty() || fecha.isEmpty()) {
            lblMensaje.setText("Debes llenar todos los campos antes de guardar.");
            lblMensaje.setTextFill(Color.RED);
        } else {
            lblMensaje.setText("Libro guardado con éxito.");
            lblMensaje.setTextFill(Color.GREEN);
            System.out.println("Libro guardado: " + titulo + " - " + autor + " (" + fecha + ")");
        }
    }

    @FXML
    private void onImportarPDFClick(ActionEvent event) {
        importarArchivo("pdf", "*.pdf", "Seleccionar archivo PDF del libro", "Libro agregado al folder correctamente.");
    }

    @FXML
    private void onImportarIMGClick(ActionEvent event) {
        importarArchivo("Imagenes", "*.png", "Seleccionar imagen del libro", "Imagen importada correctamente.");
    }

    private void importarArchivo(String subcarpeta, String extension, String titulo, String mensajeExito) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titulo);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos " + subcarpeta, extension));

        Stage stage = (Stage) lblMensaje.getScene().getWindow();
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            try {
                File carpetaDestino = new File("Libros/" + subcarpeta);
                if (!carpetaDestino.exists()) carpetaDestino.mkdirs();

                Path destino = new File(carpetaDestino, archivoSeleccionado.getName()).toPath();
                Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                lblMensaje.setText(mensajeExito);
                lblMensaje.setTextFill(Color.GREEN);
            } catch (IOException e) {
                lblMensaje.setText("Error al guardar el archivo.");
                lblMensaje.setTextFill(Color.RED);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onSalirClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/E-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Libros");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
