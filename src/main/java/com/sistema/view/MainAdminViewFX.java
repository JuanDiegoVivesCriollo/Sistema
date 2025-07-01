package com.sistema.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainAdminViewFX extends Stage {
    public MainAdminViewFX() {
        setTitle("Panel de Administrador");
        VBox vbox = new VBox(20, new Label("Bienvenido, Administrador"));
        vbox.setStyle("-fx-padding: 40; -fx-alignment: center;");
        setScene(new Scene(vbox, 400, 200));
    }
}