package com.sistema;

import com.sistema.model.User;
import com.sistema.model.dao.UserDAOImpl;
import com.sistema.view.LoginViewFX;
import com.sistema.view.MainAdminViewFX;
import com.sistema.view.MainCustomerViewFX;
import com.sistema.view.RegisterViewFX;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Instancias de las vistas JavaFX
        LoginViewFX loginView = new LoginViewFX();
        RegisterViewFX registerView = new RegisterViewFX();

        // Instancia del DAO
        UserDAOImpl userDAO = new UserDAOImpl();

        // Listener para login
        loginView.getLoginButton().setOnAction(e -> {
            String email = loginView.getUsuario();
            String password = loginView.getPassword();
            User user = userDAO.findByEmailAndPassword(email, password);
            if (user != null) {
                loginView.close();
                if ("admin".equals(user.getRole())) {
                    new MainAdminViewFX().show();
                } else {
                    new MainCustomerViewFX().show();
                }
            } else {
                loginView.showError("Usuario o contraseña incorrectos.");
            }
        });

        // Listener para mostrar la ventana de registro
        loginView.getRegisterButton().setOnAction(e -> {
            loginView.close();
            registerView.show();
        });

        // Listener para registro
        registerView.getRegisterButton().setOnAction(e -> {
    String email = registerView.getUsuario();
    String password = registerView.getPassword();
    String nombre = registerView.getNombre();
    String direccion = registerView.getDireccion(); // Debes tener este método en RegisterViewFX
    String telefono = registerView.getTelefono();   // Debes tener este método en RegisterViewFX

    if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
        registerView.showError("Todos los campos son obligatorios.");
        return;
    }
    User user = new User(0, email, password, "cliente");
    if (userDAO.registerUserAndCustomer(user, nombre, direccion, telefono)) {
        registerView.showMessage("¡Registro exitoso!");
        registerView.close();
        loginView.show();
    } else {
        registerView.showError("Error al registrar usuario.");
    }
});

        loginView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}