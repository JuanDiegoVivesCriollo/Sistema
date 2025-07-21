package com.sistema;

import com.sistema.util.DataInitializer;
import com.sistema.view.MainApplicationFX;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Inicializar datos de ejemplo (opcional)
        try {
            DataInitializer.initializeData();
            System.out.println("Datos de ejemplo inicializados correctamente.");
        } catch (Exception e) {
            System.out.println("Los datos ya existen o error al inicializar: " + e.getMessage());
        }
        
        // Lanzar la aplicación principal
        MainApplicationFX mainApp = new MainApplicationFX();
        mainApp.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}