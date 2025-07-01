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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginViewFX extends Stage {
    private final TextField userField = new TextField();
    private final PasswordField passField = new PasswordField();
    private final Button loginButton = new Button("Ingresar");
    private final Button registerButton = new Button("Registrar");

    public LoginViewFX() {
        setTitle("Iniciar Sesión");

        Label title = new Label("Bienvenido");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10,
            title,
            new Label("Usuario:"),
            userField,
            new Label("Contraseña:"),
            passField,
            buttonBox
        );
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));

        Scene scene = new Scene(vbox, 320, 260);
        setScene(scene);
    }

    public String getUsuario() {
        return userField.getText();
    }

    public String getPassword() {
        return passField.getText();
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}