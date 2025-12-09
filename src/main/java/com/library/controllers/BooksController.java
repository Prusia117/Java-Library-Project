package com.library.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.Loader; // Importante: Clase para cargar en PDFBox 3.x
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    public void setPdfFilePath(String rutaPdf) {
        try {
            // Cerramos el documento anterior si existe para liberar memoria
            if (currentDocument != null) currentDocument.close();

            // 1. Lógica Híbrida: Intentar cargar como archivo local (C:\...)
            File archivoLocal = new File(rutaPdf);

            if (archivoLocal.exists()) {
                // CASO A: Es una ruta absoluta del disco
                System.out.println("Cargando PDF local: " + rutaPdf);
                // Loader.loadPDF(File) es muy eficiente para archivos locales
                currentDocument = Loader.loadPDF(archivoLocal);
            } else {
                // CASO B: No existe en disco, intentamos como Recurso (/img/Pdfs/...)
                System.out.println("Buscando PDF en recursos: " + rutaPdf);

                // Aseguramos que la ruta empiece con "/" para buscar en resources
                String rutaRecurso = rutaPdf.startsWith("/") ? rutaPdf : "/" + rutaPdf;

                try (InputStream stream = getClass().getResourceAsStream(rutaRecurso)) {
                    if (stream != null) {
                        // En PDFBox 3.0, para cargar un Stream usamos bytes
                        currentDocument = Loader.loadPDF(stream.readAllBytes());
                    } else {
                        showError("PDF no encontrado", "No se encontró el archivo ni en disco ni en recursos:\n" + rutaPdf);
                        return;
                    }
                }
            }

            // Si llegamos aquí, el PDF cargó correctamente
            pdfRenderer = new PDFRenderer(currentDocument);
            totalPages = currentDocument.getNumberOfPages();
            currentPage = 0;
            renderCurrentPage();

        } catch (IOException e) {
            showError("Error al leer el PDF", e.getMessage());
            e.printStackTrace();
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



    private void renderCurrentPage() {
        if (pdfRenderer == null || currentDocument == null) return;
        try {
            // Renderizamos la página actual a una imagen (150 DPI es buena calidad)
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(currentPage, 150);
            imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

            lblPageInfo.setText(String.format("Página %d de %d", currentPage + 1, totalPages));
            updateNavigationButtons();
        } catch (IOException e) {
            showError("Error visualizando página", e.getMessage());
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