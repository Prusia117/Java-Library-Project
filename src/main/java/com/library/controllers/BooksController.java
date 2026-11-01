package com.library.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
        imageView.fitWidthProperty().bind(scrollPane.widthProperty().subtract(40));
        pdfContainer.getChildren().add(imageView);
        updateNavigationButtons();
    }

    // MÉTODO PÚBLICO PARA RECIBIR LA RUTA
    public void setPdfFilePath(String rutaPdf) {
        // Cargar el PDF en un hilo separado para evitar congelamiento
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Cerramos el documento anterior si existe
                    if (currentDocument != null) {
                        currentDocument.close();
                    }

                    File archivoLocal = new File(rutaPdf);

                    if (archivoLocal.exists()) {
                        // CASO A: Ruta absoluta del disco
                        System.out.println("Cargando PDF local: " + rutaPdf);
                        currentDocument = Loader.loadPDF(archivoLocal);
                    } else {
                        // CASO B: Recurso interno
                        System.out.println("Buscando PDF en recursos: " + rutaPdf);
                        String rutaRecurso = rutaPdf.startsWith("/") ? rutaPdf : "/" + rutaPdf;

                        try (InputStream stream = getClass().getResourceAsStream(rutaRecurso)) {
                            if (stream != null) {
                                currentDocument = Loader.loadPDF(stream.readAllBytes());
                            } else {
                                Platform.runLater(() -> 
                                    showError("PDF no encontrado", "No se encontró el archivo: " + rutaPdf)
                                );
                                return null;
                            }
                        }
                    }

                    // Si llegamos aquí, el PDF cargó correctamente
                    pdfRenderer = new PDFRenderer(currentDocument);
                    totalPages = currentDocument.getNumberOfPages();
                    currentPage = 0;

                    // Renderizar en el hilo de JavaFX
                    Platform.runLater(() -> {
                        renderCurrentPage();
                        lblPageInfo.setText("PDF cargado - Página 1 de " + totalPages);
                    });

                } catch (IOException e) {
                    Platform.runLater(() -> showError("Error al leer el PDF", e.getMessage()));
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Ejecutar la tarea en un hilo en segundo plano
        new Thread(loadTask).start();
        
        // Mostrar mensaje de carga
        lblPageInfo.setText("Cargando PDF...");
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

        // Renderizar en hilo separado
        Task<BufferedImage> renderTask = new Task<>() {
            @Override
            protected BufferedImage call() throws Exception {
                return pdfRenderer.renderImageWithDPI(currentPage, 150);
            }
        };

        renderTask.setOnSucceeded(event -> {
            BufferedImage bufferedImage = renderTask.getValue();
            imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            lblPageInfo.setText(String.format("Página %d de %d", currentPage + 1, totalPages));
            updateNavigationButtons();
        });

        renderTask.setOnFailed(event -> {
            showError("Error visualizando página", renderTask.getException().getMessage());
        });

        new Thread(renderTask).start();
    }

    private void updateNavigationButtons() {
        btnPreviousPage.setDisable(currentPage <= 0);
        btnNextPage.setDisable(currentPage >= totalPages - 1 || totalPages == 0);
        if (totalPages == 0) lblPageInfo.setText("No hay PDF cargado");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Cerrar el documento al cerrar la ventana
    public void cleanup() {
        try {
            if (currentDocument != null) {
                currentDocument.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}