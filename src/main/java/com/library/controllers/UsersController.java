package com.library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class UsersController {

    @FXML private StackPane stackPane;
    @FXML private ImageView fondo;
    @FXML private TextField nombreField;
    @FXML private TextField fechaField;
    @FXML private TextField correoField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView verIcono;
    @FXML private Label mensajeLabel;
    @FXML private Label salirLink;
    @FXML private Button irAtras;
    @FXML private Button guardarBtn;

    private boolean mostrando = false;
    private TextField textPasswordField = new TextField();
    private int contador = 0;

    @FXML
    public void initialize() {
        configurarFecha();
        fondo.fitWidthProperty().bind(stackPane.widthProperty());
        fondo.fitHeightProperty().bind(stackPane.heightProperty());

        aplicarEfectoSombra(guardarBtn);
        aplicarEfectoSombra(irAtras);

        salirLink.setOnMouseEntered(e -> salirLink.setStyle("-fx-text-fill: #6a0dad; -fx-underline: true;"));
        salirLink.setOnMouseExited(e -> salirLink.setStyle("-fx-text-fill: #4b0082; -fx-underline: false;"));
        salirLink.setOnMouseClicked(e -> irAEAplicacion());
    }

    private void configurarFecha() {
        fechaField.textProperty().addListener((obs, oldValue, newValue) -> {
            String clean = newValue.replaceAll("[^\\d]", "");
            if (clean.length() > 8) clean = clean.substring(0, 8);
            StringBuilder sb = new StringBuilder(clean);
            if (clean.length() > 4) sb.insert(4, '/');
            if (clean.length() > 2) sb.insert(2, '/');
            fechaField.setText(sb.toString());
            fechaField.positionCaret(sb.length());
        });
    }

    private void aplicarEfectoSombra(Button boton) {
        DropShadow sombra = new DropShadow();
        sombra.setRadius(20.0);
        sombra.setColor(Color.web("#00BFFF"));

        boton.setOnMouseEntered((MouseEvent e) -> {
            boton.setEffect(sombra);
            boton.setScaleX(1.1);
            boton.setScaleY(1.1);
        });

        boton.setOnMouseExited((MouseEvent e) -> {
            boton.setEffect(null);
            boton.setScaleX(1.0);
            boton.setScaleY(1.0);
        });
    }

    @FXML
    private void togglePassword() {
        if (!mostrando) {
            textPasswordField.setText(passwordField.getText());
            textPasswordField.setPrefWidth(passwordField.getPrefWidth());
            ((javafx.scene.layout.HBox) verIcono.getParent()).getChildren().set(0, textPasswordField);
            verIcono.setImage(new Image(getClass().getResourceAsStream("/img/Botones/BNV.png")));
            mostrando = true;
        } else {
            passwordField.setText(textPasswordField.getText());
            ((javafx.scene.layout.HBox) verIcono.getParent()).getChildren().set(0, passwordField);
            verIcono.setImage(new Image(getClass().getResourceAsStream("/img/Botones/BV.png")));
            mostrando = false;
        }
    }

    @FXML
    private void guardarDatos() {
        if (nombreField.getText().isEmpty() ||
                fechaField.getText().isEmpty() ||
                correoField.getText().isEmpty() ||
                passwordField.getText().isEmpty()) {

            mensajeLabel.setText("Falta algún cuadro por llenar");
            salirLink.setVisible(false);
        } else {
            contador++;
            mensajeLabel.setText("Se ha guardado con éxito el número " + contador + ". ¿Quisieras salir?");
            salirLink.setVisible(true);
        }
    }

    @FXML
    private void irAtras() {
        irAEAplicacion();
    }

    private void irAEAplicacion() {
        contador = 0;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/E-view.fxml"));
            Pane root = loader.load();
            Stage stage = (Stage) stackPane.getScene().getWindow();
            Scene scene = new Scene(root, 870, 500);
            stage.setScene(scene);
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
