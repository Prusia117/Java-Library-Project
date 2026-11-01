package com.library.controllers;

import com.library.entities.UserEntity;
import com.library.kernel.framework.auth.PasswordHasher;
import com.library.models.UserModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UsersCRUDController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;


    @FXML private TableView<UserEntity> tablaUsuarios;
    @FXML private TableColumn<UserEntity, Integer> colId;
    @FXML private TableColumn<UserEntity, String> colNombre;
    @FXML private TableColumn<UserEntity, String> colCorreo;
    @FXML private TableColumn<UserEntity, String> colPassword;

    private final UserModel userModel = new UserModel();

    @FXML
    public void initialize() {

        // Se enlazan los campos EXACTOS del UserEntity
        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().user_id).asObject());

        colNombre.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().user_name));

        colCorreo.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().user_mail));

        // Solo se guarda el hash, así que no se muestra
        colPassword.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty("••••••••"));

        cargarTabla();

        tablaUsuarios.setOnMouseClicked(event -> cargarSeleccionado());
    }

    @FXML
    private void regresarAHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtNombre.getScene().getWindow();
            
            // Restablecer tamaños para Home
            stage.setMinWidth(870);
            stage.setMinHeight(500);
            stage.setWidth(870);
            stage.setHeight(500);
            
            stage.setScene(new Scene(root, 870, 500));
            stage.setTitle("Biblioteca Digital");
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void cargarTabla() {
        ArrayList<UserEntity> lista = userModel.getAll();
        tablaUsuarios.setItems(FXCollections.observableArrayList(lista));
    }

    private void cargarSeleccionado() {
        UserEntity seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        txtNombre.setText(seleccionado.user_name);
        txtCorreo.setText(seleccionado.user_mail);
        // Vacío = conservar la contraseña actual al actualizar
        txtPassword.clear();
    }

    // ============================================================
    // CRUD
    // ============================================================

    @FXML
    private void onAgregar() {

        UserEntity userEntity = new UserEntity();
        userEntity.user_name = txtNombre.getText();
        userEntity.user_mail = txtCorreo.getText();
        if (txtPassword.getText().isBlank()) {
            alerta("Advertencia", "La contraseña es obligatoria");
            return;
        }
        userEntity.user_password = PasswordHasher.hash(txtPassword.getText());

        boolean ok = userModel.save(
                userEntity
        );

        if (!ok) {
            alerta("Error", "No se pudo guardar el usuario");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Usuario agregado correctamente");
    }

    @FXML
    private void onActualizar() {

        UserEntity seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Advertencia", "Selecciona un usuario para actualizar");
            return;
        }

        seleccionado.user_name = txtNombre.getText();
        seleccionado.user_mail = txtCorreo.getText();
        // null hace que el UPDATE omita la columna y conserve el hash actual
        seleccionado.user_password = txtPassword.getText().isBlank()
                ? null
                : PasswordHasher.hash(txtPassword.getText());

        boolean ok = userModel.update(seleccionado, seleccionado.user_id.toString());

        if (!ok) {
            alerta("Error", "No se pudo actualizar el usuario");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Usuario actualizado correctamente");
    }

    @FXML
    private void onEliminar() {

        UserEntity seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Advertencia", "Selecciona un usuario para eliminar");
            return;
        }

        boolean ok = userModel.delete(seleccionado.user_id.toString());

        if (!ok) {
            alerta("Error", "No se pudo eliminar el usuario");
            return;
        }

        limpiar();
        cargarTabla();
        alerta("Éxito", "Usuario eliminado correctamente");
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtCorreo.clear();
        txtPassword.clear();
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}