package com.library.controllers;

import com.library.entities.UserEntity;
import com.library.models.UserModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class UsersCRUDFormController {

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

        colPassword.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().user_password));

        cargarTabla();

        tablaUsuarios.setOnMouseClicked(event -> cargarSeleccionado());
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
        txtPassword.setText(seleccionado.user_password);
    }

    // ============================================================
    // CRUD
    // ============================================================

    @FXML
    private void onAgregar() {

        boolean ok = userModel.save(
                txtNombre.getText(),
                txtCorreo.getText(),
                txtPassword.getText()
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
        seleccionado.user_password = txtPassword.getText();

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