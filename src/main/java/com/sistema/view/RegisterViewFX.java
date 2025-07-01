package com.sistema.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterViewFX extends Stage {
    private final TextField userField = new TextField();
    private final PasswordField passField = new PasswordField();
    private final TextField nameField = new TextField();
    private final Button registerButton = new Button("Registrar");
    private final TextField direccionField = new TextField();
    private final TextField telefonoField = new TextField();

   public RegisterViewFX() {
    setTitle("Registro");

    Label title = new Label("Crear Cuenta");
    title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

    VBox vbox = new VBox(10,
        title,
        new Label("Usuario:"),
        userField,
        new Label("Contraseña:"),
        passField,
        new Label("Nombre:"),
        nameField,
        new Label("Dirección:"),
        direccionField,
        new Label("Teléfono:"),
        telefonoField,
        registerButton // <-- Solo una vez aquí
    );
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(30));

    Scene scene = new Scene(vbox, 320, 370);
    setScene(scene);
}

    public String getUsuario() {
        return userField.getText();
    }

    public String getPassword() {
        return passField.getText();
    }

    public String getNombre() {
        return nameField.getText();
    }
    public String getDireccion() {
        return direccionField.getText();
    }

    public String getTelefono() {
        return telefonoField.getText();
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}