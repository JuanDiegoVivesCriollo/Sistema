package com.sistema.view;

import java.util.List;

import com.sistema.model.Customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainCustomerViewFX extends Stage implements CustomerView {
    private final Button btnCatalogo = new Button("Ver Catálogo");
    private final Button btnCarrito = new Button("Ver Carrito");
    private final Button btnPedidos = new Button("Mis Pedidos");
    private final Button btnNotificaciones = new Button("Notificaciones");
    private final Label lblBienvenida = new Label("Bienvenido, Cliente");

    public MainCustomerViewFX() {
        setTitle("Panel de Cliente - TechMarket");

        lblBienvenida.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox menu = new VBox(15, btnCatalogo, btnCarrito, btnPedidos, btnNotificaciones);
        menu.setAlignment(Pos.CENTER);

        VBox root = new VBox(30, lblBienvenida, menu);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        setScene(new Scene(root, 400, 350));

        // Ejemplo de listeners (agrega tu lógica real aquí)
        btnCatalogo.setOnAction(e -> showMessage("Mostrar catálogo de productos"));
        btnCarrito.setOnAction(e -> showMessage("Mostrar carrito de compras"));
        btnPedidos.setOnAction(e -> showMessage("Mostrar historial de pedidos"));
        btnNotificaciones.setOnAction(e -> showMessage("Mostrar notificaciones"));
    }

    @Override
    public void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public void displayCustomerList(List<Customer> customers) {
        // Aquí puedes implementar la lógica para mostrar la lista de clientes si lo necesitas
        showMessage("Clientes: " + customers.size());
    }
}