package com.library.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BooksController {

    @FXML private Button btnPreviousPage;
    @FXML private Button btnNextPage;
    @FXML private Label lblPageInfo;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox pdfContainer;

    private PDDocument currentDocument;
    private PDFRenderer pdfRenderer;
    private int currentPage = 0;
    private int totalPages = 0;
    private ImageView imageView;

    @FXML
    public void initialize() {
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        pdfContainer.getChildren().add(imageView);
        updateNavigationButtons();


    }

    // MÉTODO PÚBLICO PARA RECIBIR LA RUTA ---
    public void setPdfFilePath(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            loadPDF(file);
        } else {
            showError("Archivo no encontrado",
                    "No se pudo encontrar el archivo PDF en:\n" + path);
        }
    }
    

    @FXML
    protected void onPreviousPageClick() {
        if (currentPage > 0) {
            currentPage--;
            renderCurrentPage();
        }
    }

    @FXML
    protected void onNextPageClick() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            renderCurrentPage();
        }
    }

    private void loadPDF(File file) {
        try {
            if (currentDocument != null) currentDocument.close();

            currentDocument = Loader.loadPDF(file);
            pdfRenderer = new PDFRenderer(currentDocument);
            totalPages = currentDocument.getNumberOfPages();
            currentPage = 0;
            renderCurrentPage();
        } catch (IOException e) {
            showError("Error al cargar el PDF", e.getMessage());
        }
    }

    private void renderCurrentPage() {
        if (pdfRenderer == null || currentDocument == null) return;
        try {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(currentPage, 150);
            imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            lblPageInfo.setText(String.format("Página %d de %d", currentPage + 1, totalPages));
            updateNavigationButtons();
        } catch (IOException e) {
            showError("Error renderizando", e.getMessage());
        }
    }

    private void updateNavigationButtons() {
        btnPreviousPage.setDisable(currentPage <= 0);
        btnNextPage.setDisable(currentPage >= totalPages - 1 || totalPages == 0);
        if (totalPages == 0) lblPageInfo.setText("No hay PDF cargado");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
