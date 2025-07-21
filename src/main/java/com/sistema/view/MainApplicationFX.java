package com.sistema.view;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sistema.controller.MainController;
import com.sistema.model.Category;
import com.sistema.model.Discount;
import com.sistema.model.Order;
import com.sistema.model.Product;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApplicationFX extends Application {
    private MainController controller;
    private Stage primaryStage;
    private Scene loginScene, adminScene, customerScene;
    
    // Admin components
    private TableView<Product> adminProductTable;
    private TableView<Order> adminOrderTable;
    private TextField productNameField, productDescField, productPriceField, productStockField;
    private TextField discountNameField, discountValueField;
    private ComboBox<String> discountTypeCombo;
    
    // Customer components
    private TableView<Product> customerProductTable;
    private TableView<Map.Entry<Product, Integer>> cartTable;
    private TableView<Order> customerOrderTable;
    private Label cartTotalLabel;
    private ComboBox<String> paymentMethodCombo, shippingMethodCombo;
    private ComboBox<Discount> discountCombo;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new MainController();
        
        createLoginScene();
        createAdminScene();
        createCustomerScene();
        
        primaryStage.setTitle("Sistema de E-commerce - TechStore");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void createLoginScene() {
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(50));
        
        Label titleLabel = new Label("TechStore - Sistema de E-commerce");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Login form
        GridPane loginForm = new GridPane();
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setHgap(10);
        loginForm.setVgap(10);
          TextField emailField = new TextField();
        emailField.setPromptText("usuario@ejemplo.com");
        emailField.setPrefWidth(250);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña (mín. 6 caracteres)");
        passwordField.setPrefWidth(250);
        
        // Label para mostrar errores de validación
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);
        errorLabel.setPrefWidth(300);
        
        Button loginBtn = new Button("Iniciar Sesión");
        loginBtn.setPrefWidth(150);
        Button registerBtn = new Button("Registrarse como Cliente");
        registerBtn.setPrefWidth(150);
        
        loginForm.add(new Label("Email:"), 0, 0);
        loginForm.add(emailField, 1, 0);
        loginForm.add(new Label("Contraseña:"), 0, 1);
        loginForm.add(passwordField, 1, 1);
        loginForm.add(errorLabel, 0, 2, 2, 1); // Span 2 columns
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, registerBtn);
        
        loginBox.getChildren().addAll(titleLabel, loginForm, buttonBox);
        
        // Event handlers con validación mejorada
        loginBtn.setOnAction(e -> {
            errorLabel.setText(""); // Limpiar errores previos
            
            String email = emailField.getText();
            String password = passwordField.getText();
            
            // Validación con feedback detallado
            String loginResult = controller.loginWithValidation(email, password);
            
            if (loginResult == null) { // Login exitoso
                if (controller.isAdmin()) {
                    loadAdminData();
                    primaryStage.setScene(adminScene);
                } else {
                    loadCustomerData();
                    primaryStage.setScene(customerScene);
                }
                emailField.clear();
                passwordField.clear();
                errorLabel.setText("");
            } else {
                errorLabel.setText(loginResult);
                // Mantener el email si es válido para que el usuario no tenga que escribirlo de nuevo
                String emailValidation = controller.validateEmail(email);
                if (emailValidation != null) {
                    emailField.selectAll(); // Seleccionar todo el email para fácil corrección
                } else {
                    passwordField.selectAll(); // Si el email es válido, seleccionar contraseña
                }
            }
        });
        
        // Validación en tiempo real del email
        emailField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                String validation = controller.validateEmail(newText);
                if (validation != null) {
                    emailField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                } else {
                    emailField.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
                }
            } else {
                emailField.setStyle(""); // Reset style
            }
        });
        
        // Permitir login con Enter
        passwordField.setOnAction(e -> loginBtn.fire());
        emailField.setOnAction(e -> passwordField.requestFocus());
        
        registerBtn.setOnAction(e -> showRegisterDialog());
        
        loginScene = new Scene(loginBox, 400, 300);
    }

    private void createAdminScene() {
        TabPane adminTabs = new TabPane();
        
        // Tab 1: Gestión de Productos
        Tab productTab = new Tab("Productos");
        productTab.setClosable(false);
        productTab.setContent(createProductManagementPane());
        
        // Tab 2: Gestión de Pedidos
        Tab orderTab = new Tab("Pedidos");
        orderTab.setClosable(false);
        orderTab.setContent(createOrderManagementPane());
        
        // Tab 3: Gestión de Descuentos
        Tab discountTab = new Tab("Descuentos");
        discountTab.setClosable(false);
        discountTab.setContent(createDiscountManagementPane());
        
        adminTabs.getTabs().addAll(productTab, orderTab, discountTab);
        
        VBox adminRoot = new VBox();
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_RIGHT);
        headerBox.setPadding(new Insets(10));
        
        Button logoutBtn = new Button("Cerrar Sesión");
        logoutBtn.setOnAction(e -> {
            controller.logout();
            primaryStage.setScene(loginScene);
        });
        
        headerBox.getChildren().add(logoutBtn);
        adminRoot.getChildren().addAll(headerBox, adminTabs);
        
        adminScene = new Scene(adminRoot, 800, 600);
    }

    private VBox createProductManagementPane() {
        VBox productPane = new VBox(10);
        productPane.setPadding(new Insets(10));
        
        // Product form
        GridPane productForm = new GridPane();
        productForm.setHgap(10);
        productForm.setVgap(10);
        
        productNameField = new TextField();
        productDescField = new TextField();
        productPriceField = new TextField();
        productStockField = new TextField();
        
        productForm.add(new Label("Nombre:"), 0, 0);
        productForm.add(productNameField, 1, 0);
        productForm.add(new Label("Descripción:"), 0, 1);
        productForm.add(productDescField, 1, 1);
        productForm.add(new Label("Precio:"), 0, 2);
        productForm.add(productPriceField, 1, 2);
        productForm.add(new Label("Stock:"), 0, 3);
        productForm.add(productStockField, 1, 3);
        
        Button addProductBtn = new Button("Agregar Producto");
        Button updateProductBtn = new Button("Actualizar Producto");
        Button deleteProductBtn = new Button("Eliminar Producto");
        
        HBox productButtonBox = new HBox(10);
        productButtonBox.getChildren().addAll(addProductBtn, updateProductBtn, deleteProductBtn);
        
        // Product table
        adminProductTable = new TableView<>();
        setupProductTable(adminProductTable);
        
        // Event handlers
        addProductBtn.setOnAction(e -> addProduct());
        updateProductBtn.setOnAction(e -> updateProduct());
        deleteProductBtn.setOnAction(e -> deleteProduct());
        
        productPane.getChildren().addAll(
            new Label("Gestión de Productos"),
            productForm,
            productButtonBox,
            adminProductTable
        );
        
        return productPane;
    }

    private VBox createOrderManagementPane() {
        VBox orderPane = new VBox(10);
        orderPane.setPadding(new Insets(10));
        
        Label titleLabel = new Label("Gestión de Pedidos");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        adminOrderTable = new TableView<>();
        setupOrderTable(adminOrderTable);
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.setItems(FXCollections.observableArrayList(
            "pendiente", "procesando", "enviado", "entregado", "cancelado"
        ));
        
        Button updateStatusBtn = new Button("Actualizar Estado");
        updateStatusBtn.setOnAction(e -> {
            Order selectedOrder = adminOrderTable.getSelectionModel().getSelectedItem();
            String newStatus = statusCombo.getValue();
            if (selectedOrder != null && newStatus != null) {
                if (controller.updateOrderStatus(selectedOrder.getId(), newStatus)) {
                    loadAdminData();
                    showAlert("Éxito", "Estado del pedido actualizado");
                } else {
                    showAlert("Error", "No se pudo actualizar el estado");
                }
            }
        });
        
        HBox statusBox = new HBox(10);
        statusBox.getChildren().addAll(new Label("Nuevo Estado:"), statusCombo, updateStatusBtn);
        
        orderPane.getChildren().addAll(titleLabel, adminOrderTable, statusBox);
        return orderPane;
    }    private VBox createDiscountManagementPane() {
        VBox discountPane = new VBox(15);
        discountPane.setPadding(new Insets(10));
        
        Label titleLabel = new Label("Gestión de Descuentos");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Sección de búsqueda de productos
        VBox searchSection = createProductSearchSection();
        
        // Sección de creación de descuentos
        VBox discountCreationSection = createDiscountCreationSection();
        
        // Tabla de descuentos existentes
        VBox discountListSection = createDiscountListSection();
        
        discountPane.getChildren().addAll(titleLabel, searchSection, discountCreationSection, discountListSection);
        return discountPane;
    }
    
    private VBox createProductSearchSection() {
        VBox searchSection = new VBox(10);
        searchSection.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 10;");
        
        Label searchLabel = new Label("Buscar Productos para Aplicar Descuentos");
        searchLabel.setStyle("-fx-font-weight: bold;");
        
        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por nombre o descripción...");
        searchField.setPrefWidth(300);
        
        ComboBox<Category> categoryFilterCombo = new ComboBox<>();
        categoryFilterCombo.setPromptText("Filtrar por categoría");
        categoryFilterCombo.setItems(controller.getCategories());
        
        Button searchBtn = new Button("Buscar");
        Button clearBtn = new Button("Limpiar");
        
        searchBox.getChildren().addAll(
            new Label("Búsqueda:"), searchField,
            new Label("Categoría:"), categoryFilterCombo,
            searchBtn, clearBtn
        );
        
        // Tabla de resultados de búsqueda
        TableView<Product> searchResultsTable = new TableView<>();
        setupProductTable(searchResultsTable);
        searchResultsTable.setPrefHeight(200);
        
        // Eventos
        searchBtn.setOnAction(e -> {
            String searchText = searchField.getText();
            Category selectedCategory = categoryFilterCombo.getValue();
            Integer categoryId = selectedCategory != null ? selectedCategory.getId() : null;
            
            if (searchText.isEmpty() && categoryId == null) {
                searchResultsTable.setItems(controller.getProducts());
            } else {
                searchResultsTable.setItems(controller.searchProducts(searchText, categoryId, null, null));
            }
        });
        
        clearBtn.setOnAction(e -> {
            searchField.clear();
            categoryFilterCombo.setValue(null);
            searchResultsTable.setItems(controller.getProducts());
        });
        
        // Cargar todos los productos inicialmente
        searchResultsTable.setItems(controller.getProducts());
        
        searchSection.getChildren().addAll(searchLabel, searchBox, searchResultsTable);
        return searchSection;
    }
    
    private VBox createDiscountCreationSection() {
        VBox creationSection = new VBox(10);
        creationSection.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 10;");
        
        Label creationLabel = new Label("Crear Nuevo Descuento");
        creationLabel.setStyle("-fx-font-weight: bold;");
        
        GridPane discountForm = new GridPane();
        discountForm.setHgap(10);
        discountForm.setVgap(10);
        
        discountNameField = new TextField();
        discountTypeCombo = new ComboBox<>();
        discountTypeCombo.setItems(FXCollections.observableArrayList("porcentaje", "fijo"));
        discountValueField = new TextField();
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(2);
        
        ComboBox<String> aplicableCombo = new ComboBox<>();
        aplicableCombo.setItems(FXCollections.observableArrayList("general", "producto", "categoria"));
        aplicableCombo.setValue("general");
        
        TextField objetoIdField = new TextField();
        objetoIdField.setPromptText("ID del producto o categoría");
        objetoIdField.setDisable(true);
        
        // Habilitar/deshabilitar campo según selección
        aplicableCombo.setOnAction(e -> {
            String selected = aplicableCombo.getValue();
            objetoIdField.setDisable("general".equals(selected));
            if ("producto".equals(selected)) {
                objetoIdField.setPromptText("ID del producto");
            } else if ("categoria".equals(selected)) {
                objetoIdField.setPromptText("ID de la categoría");
            }
        });
        
        discountForm.add(new Label("Nombre:"), 0, 0);
        discountForm.add(discountNameField, 1, 0);
        discountForm.add(new Label("Tipo:"), 0, 1);
        discountForm.add(discountTypeCombo, 1, 1);
        discountForm.add(new Label("Valor:"), 0, 2);
        discountForm.add(discountValueField, 1, 2);
        discountForm.add(new Label("Aplicable para:"), 0, 3);
        discountForm.add(aplicableCombo, 1, 3);
        discountForm.add(new Label("ID Objeto:"), 0, 4);
        discountForm.add(objetoIdField, 1, 4);
        discountForm.add(new Label("Descripción:"), 0, 5);
        discountForm.add(descriptionArea, 1, 5);
        
        HBox buttonBox = new HBox(10);
        Button addGeneralBtn = new Button("Agregar Descuento General");
        Button addSpecificBtn = new Button("Agregar Descuento Específico");
        
        addGeneralBtn.setOnAction(e -> addGeneralDiscount(descriptionArea.getText()));
        addSpecificBtn.setOnAction(e -> addSpecificDiscount(aplicableCombo.getValue(), objetoIdField.getText(), descriptionArea.getText()));
        
        buttonBox.getChildren().addAll(addGeneralBtn, addSpecificBtn);
        
        creationSection.getChildren().addAll(creationLabel, discountForm, buttonBox);
        return creationSection;
    }
    
    private VBox createDiscountListSection() {
        VBox listSection = new VBox(10);
        listSection.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 10;");
        
        Label listLabel = new Label("Descuentos Existentes");
        listLabel.setStyle("-fx-font-weight: bold;");
        
        TableView<Discount> discountTable = new TableView<>();
        setupDiscountTable(discountTable);
        
        HBox actionBox = new HBox(10);
        Button toggleStatusBtn = new Button("Activar/Desactivar");
        Button viewDetailsBtn = new Button("Ver Detalles");
        
        toggleStatusBtn.setOnAction(e -> {
            Discount selected = discountTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (controller.toggleDiscountStatus(selected.getId())) {
                    loadDiscountData(discountTable);
                    showAlert("Éxito", "Estado del descuento actualizado");
                }
            }
        });
        
        viewDetailsBtn.setOnAction(e -> {
            Discount selected = discountTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String details = controller.getDiscountDetails(selected);
                showAlert("Detalles del Descuento", details);
            }
        });
        
        actionBox.getChildren().addAll(toggleStatusBtn, viewDetailsBtn);
        
        listSection.getChildren().addAll(listLabel, discountTable, actionBox);
        
        // Cargar datos iniciales
        loadDiscountData(discountTable);
        
        return listSection;
    }
    
    private void setupDiscountTable(TableView<Discount> table) {
        TableColumn<Discount, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Discount, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Discount, String> typeCol = new TableColumn<>("Tipo");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("tipoDescuento"));
        
        TableColumn<Discount, Double> valueCol = new TableColumn<>("Valor");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("valor"));
        
        TableColumn<Discount, String> applicableCol = new TableColumn<>("Aplicable Para");
        applicableCol.setCellValueFactory(new PropertyValueFactory<>("aplicablePara"));
        
        TableColumn<Discount, Boolean> activeCol = new TableColumn<>("Activo");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("activo"));
        
        table.getColumns().addAll(idCol, nameCol, typeCol, valueCol, applicableCol, activeCol);
    }
    
    private void loadDiscountData(TableView<Discount> table) {
        table.setItems(FXCollections.observableArrayList(controller.getAllDiscounts()));
    }

    private void createCustomerScene() {
        TabPane customerTabs = new TabPane();
        
        // Tab 1: Catálogo de Productos
        Tab catalogTab = new Tab("Catálogo");
        catalogTab.setClosable(false);
        catalogTab.setContent(createCatalogPane());
        
        // Tab 2: Carrito de Compras
        Tab cartTab = new Tab("Carrito");
        cartTab.setClosable(false);
        cartTab.setContent(createCartPane());
        
        // Tab 3: Mis Pedidos
        Tab ordersTab = new Tab("Mis Pedidos");
        ordersTab.setClosable(false);
        ordersTab.setContent(createCustomerOrdersPane());
        
        customerTabs.getTabs().addAll(catalogTab, cartTab, ordersTab);
        
        VBox customerRoot = new VBox();
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_RIGHT);
        headerBox.setPadding(new Insets(10));
        
        Button logoutBtn = new Button("Cerrar Sesión");
        logoutBtn.setOnAction(e -> {
            controller.logout();
            primaryStage.setScene(loginScene);
        });
        
        headerBox.getChildren().add(logoutBtn);
        customerRoot.getChildren().addAll(headerBox, customerTabs);
        
        customerScene = new Scene(customerRoot, 800, 600);
    }    private VBox createCatalogPane() {
        VBox catalogPane = new VBox(15);
        catalogPane.setPadding(new Insets(10));
        
        Label titleLabel = new Label("Catálogo de Productos");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Sección de búsqueda para clientes
        VBox searchSection = createCustomerSearchSection();
        
        customerProductTable = new TableView<>();
        setupProductTableWithDiscounts(customerProductTable);
        
        HBox actionBox = new HBox(10);
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, 1);
        Button addToCartBtn = new Button("Agregar al Carrito");
        Button applyDiscountBtn = new Button("Ver Descuentos");
        
        addToCartBtn.setOnAction(e -> {
            Product selectedProduct = customerProductTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                int quantity = quantitySpinner.getValue();
                if (controller.addToCart(selectedProduct, quantity)) {
                    showAlert("Éxito", "Producto agregado al carrito");
                } else {
                    showAlert("Error", "No se pudo agregar al carrito");
                }
            }
        });
        
        applyDiscountBtn.setOnAction(e -> {
            Product selectedProduct = customerProductTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                showProductDiscounts(selectedProduct);
            }
        });
        
        actionBox.getChildren().addAll(
            new Label("Cantidad:"), quantitySpinner, 
            addToCartBtn, applyDiscountBtn
        );
        
        catalogPane.getChildren().addAll(titleLabel, searchSection, customerProductTable, actionBox);
        
        return catalogPane;
    }
    
    private VBox createCustomerSearchSection() {
        VBox searchSection = new VBox(10);
        searchSection.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-padding: 10;");
        
        Label searchLabel = new Label("Buscar Productos");
        searchLabel.setStyle("-fx-font-weight: bold;");
        
        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por nombre o descripción...");
        searchField.setPrefWidth(250);
        
        ComboBox<Category> categoryCombo = new ComboBox<>();
        categoryCombo.setPromptText("Categoría");
        categoryCombo.setItems(controller.getCategories());
        
        TextField minPriceField = new TextField();
        minPriceField.setPromptText("Precio mín.");
        minPriceField.setPrefWidth(80);
        
        TextField maxPriceField = new TextField();
        maxPriceField.setPromptText("Precio máx.");
        maxPriceField.setPrefWidth(80);
        
        Button searchBtn = new Button("Buscar");
        Button clearBtn = new Button("Limpiar");
        
        searchBox.getChildren().addAll(
            searchField, categoryCombo, 
            new Label("$"), minPriceField, 
            new Label("-"), maxPriceField,
            searchBtn, clearBtn
        );
        
        // Eventos de búsqueda
        searchBtn.setOnAction(e -> performCustomerSearch(
            searchField.getText(),
            categoryCombo.getValue(),
            minPriceField.getText(),
            maxPriceField.getText()
        ));
        
        clearBtn.setOnAction(e -> {
            searchField.clear();
            categoryCombo.setValue(null);
            minPriceField.clear();
            maxPriceField.clear();
            customerProductTable.setItems(controller.getProducts());
        });
        
        // Búsqueda en tiempo real
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                customerProductTable.setItems(controller.getProducts());
            } else {
                customerProductTable.setItems(controller.smartSearch(newText));
            }
        });
        
        searchSection.getChildren().addAll(searchLabel, searchBox);
        return searchSection;
    }
    
    private void setupProductTableWithDiscounts(TableView<Product> table) {
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        
        TableColumn<Product, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nameCol.setPrefWidth(200);
        
        TableColumn<Product, String> descCol = new TableColumn<>("Descripción");
        descCol.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        descCol.setPrefWidth(250);
        
        TableColumn<Product, Double> priceCol = new TableColumn<>("Precio");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("precio"));
        priceCol.setPrefWidth(80);
        
        TableColumn<Product, String> discountCol = new TableColumn<>("Mejor Descuento");
        discountCol.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            Discount bestDiscount = controller.getBestDiscount(product);
            if (bestDiscount != null) {
                String discountText = bestDiscount.getValor() + 
                    (bestDiscount.getTipoDescuento().equals("porcentaje") ? "%" : "$") + 
                    " OFF";
                return new javafx.beans.property.SimpleStringProperty(discountText);
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        discountCol.setPrefWidth(100);
        
        TableColumn<Product, Double> finalPriceCol = new TableColumn<>("Precio Final");
        finalPriceCol.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            Discount bestDiscount = controller.getBestDiscount(product);
            double finalPrice = controller.calculateDiscountedPrice(product, bestDiscount);
            return new javafx.beans.property.SimpleDoubleProperty(finalPrice).asObject();
        });
        finalPriceCol.setPrefWidth(100);
        
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(60);
        
        table.getColumns().addAll(idCol, nameCol, descCol, priceCol, discountCol, finalPriceCol, stockCol);
    }
    
    private void performCustomerSearch(String searchText, Category category, String minPriceText, String maxPriceText) {
        try {
            Integer categoryId = category != null ? category.getId() : null;
            Double minPrice = minPriceText.isEmpty() ? null : Double.parseDouble(minPriceText);
            Double maxPrice = maxPriceText.isEmpty() ? null : Double.parseDouble(maxPriceText);
            
            ObservableList<Product> results = controller.searchProducts(searchText, categoryId, minPrice, maxPrice);
            customerProductTable.setItems(results);
        } catch (NumberFormatException e) {
            showAlert("Error", "Verifique los valores de precio");
        }
    }
    
    private void showProductDiscounts(Product product) {
        List<Discount> applicableDiscounts = controller.getApplicableDiscounts(product);
        
        if (applicableDiscounts.isEmpty()) {
            showAlert("Descuentos", "No hay descuentos disponibles para este producto.");
            return;
        }
        
        StringBuilder message = new StringBuilder();
        message.append("Descuentos disponibles para: ").append(product.getNombre()).append("\n\n");
        message.append("Precio original: $").append(String.format("%.2f", product.getPrecio())).append("\n\n");
        
        for (Discount discount : applicableDiscounts) {
            double discountedPrice = controller.calculateDiscountedPrice(product, discount);
            double savings = product.getPrecio() - discountedPrice;
            
            message.append("• ").append(discount.getNombre()).append("\n");
            message.append("  Descuento: ").append(discount.getValor());
            if (discount.getTipoDescuento().equals("porcentaje")) {
                message.append("%");
            } else {
                message.append("$");
            }
            message.append("\n");
            message.append("  Precio con descuento: $").append(String.format("%.2f", discountedPrice)).append("\n");
            message.append("  Ahorro: $").append(String.format("%.2f", savings)).append("\n\n");        }
        
        showAlert("Descuentos Disponibles", message.toString());
    }

    private VBox createCartPane() {
        VBox cartPane = new VBox(10);
        cartPane.setPadding(new Insets(10));
        
        Label titleLabel = new Label("Carrito de Compras");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        cartTable = new TableView<>();
        setupCartTable();
        
        cartTotalLabel = new Label("Total: $0.00");
        cartTotalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Checkout form
        GridPane checkoutForm = new GridPane();
        checkoutForm.setHgap(10);
        checkoutForm.setVgap(10);
        
        paymentMethodCombo = new ComboBox<>();
        paymentMethodCombo.setItems(FXCollections.observableArrayList(
            "Tarjeta de Crédito", "Tarjeta de Débito", "PayPal", "Transferencia Bancaria"
        ));
        
        shippingMethodCombo = new ComboBox<>();
        shippingMethodCombo.setItems(FXCollections.observableArrayList(
            "Envío Estándar", "Envío Express", "Recojo en Tienda"
        ));
        
        discountCombo = new ComboBox<>();
        
        checkoutForm.add(new Label("Método de Pago:"), 0, 0);
        checkoutForm.add(paymentMethodCombo, 1, 0);
        checkoutForm.add(new Label("Método de Envío:"), 0, 1);
        checkoutForm.add(shippingMethodCombo, 1, 1);
        checkoutForm.add(new Label("Descuento:"), 0, 2);
        checkoutForm.add(discountCombo, 1, 2);
        
        Button removeFromCartBtn = new Button("Remover del Carrito");
        Button checkoutBtn = new Button("Finalizar Compra");
        Button refreshCartBtn = new Button("Actualizar Carrito");
        
        removeFromCartBtn.setOnAction(e -> removeFromCart());
        checkoutBtn.setOnAction(e -> checkout());
        refreshCartBtn.setOnAction(e -> loadCartData());
        
        HBox cartButtonBox = new HBox(10);
        cartButtonBox.getChildren().addAll(removeFromCartBtn, refreshCartBtn, checkoutBtn);
        
        cartPane.getChildren().addAll(
            titleLabel, cartTable, cartTotalLabel,
            new Label("Finalizar Compra"), checkoutForm, cartButtonBox
        );
        
        return cartPane;
    }

    private VBox createCustomerOrdersPane() {
        VBox ordersPane = new VBox(10);
        ordersPane.setPadding(new Insets(10));
        
        Label titleLabel = new Label("Mis Pedidos");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        customerOrderTable = new TableView<>();
        setupOrderTable(customerOrderTable);
        
        ordersPane.getChildren().addAll(titleLabel, customerOrderTable);
        return ordersPane;
    }

    private void setupProductTable(TableView<Product> table) {
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Product, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Product, String> descCol = new TableColumn<>("Descripción");
        descCol.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        TableColumn<Product, Double> priceCol = new TableColumn<>("Precio");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        table.getColumns().addAll(idCol, nameCol, descCol, priceCol, stockCol);
    }    private void setupOrderTable(TableView<Order> table) {
        TableColumn<Order, Integer> idCol = new TableColumn<>("ID Pedido");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);
        
        TableColumn<Order, Integer> customerIdCol = new TableColumn<>("ID Cliente");
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        customerIdCol.setPrefWidth(80);
        
        TableColumn<Order, String> customerNameCol = new TableColumn<>("Nombre Cliente");
        customerNameCol.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            // Buscar el nombre del cliente usando el controller
            String customerName = controller.getCustomerNameById(order.getClienteId());
            return new javafx.beans.property.SimpleStringProperty(customerName != null ? customerName : "Cliente #" + order.getClienteId());
        });
        customerNameCol.setPrefWidth(150);
        
        TableColumn<Order, String> statusCol = new TableColumn<>("Estado");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        statusCol.setPrefWidth(100);
        
        TableColumn<Order, String> paymentCol = new TableColumn<>("Método Pago");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        paymentCol.setPrefWidth(120);
        
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalCol.setPrefWidth(80);
        
        table.getColumns().addAll(idCol, customerIdCol, customerNameCol, statusCol, paymentCol, totalCol);
    }

    private void setupCartTable() {
        TableColumn<Map.Entry<Product, Integer>, String> productCol = new TableColumn<>("Producto");
        productCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKey().getNombre()));
        
        TableColumn<Map.Entry<Product, Integer>, Double> priceCol = new TableColumn<>("Precio");
        priceCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getKey().getPrecio()).asObject());
        
        TableColumn<Map.Entry<Product, Integer>, Integer> quantityCol = new TableColumn<>("Cantidad");
        quantityCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getValue()).asObject());
        
        TableColumn<Map.Entry<Product, Integer>, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> {
            double subtotal = cellData.getValue().getKey().getPrecio() * cellData.getValue().getValue();
            return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();
        });
        
        cartTable.getColumns().addAll(productCol, priceCol, quantityCol, subtotalCol);
    }    private void showRegisterDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registro de Cliente");
        dialog.setHeaderText("Complete sus datos para crear una cuenta");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField emailField = new TextField();
        emailField.setPromptText("usuario@ejemplo.com");
        emailField.setPrefWidth(250);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mínimo 6 caracteres");
        passwordField.setPrefWidth(250);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Nombre completo");
        nameField.setPrefWidth(250);
        
        TextField addressField = new TextField();
        addressField.setPromptText("Dirección completa");
        addressField.setPrefWidth(250);
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("Teléfono de contacto");
        phoneField.setPrefWidth(250);
        
        // Label para errores de validación
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);
        errorLabel.setPrefWidth(400);
        
        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Nombre:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("Dirección:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Teléfono:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(errorLabel, 0, 5, 2, 1); // Span 2 columns
        
        // Validación en tiempo real del email
        emailField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                String validation = controller.validateEmail(newText);
                if (validation != null) {
                    emailField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                } else {
                    emailField.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
                }
            } else {
                emailField.setStyle("");
            }
        });
        
        // Validación de contraseña
        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                if (newText.length() < 6) {
                    passwordField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                } else {
                    passwordField.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
                }
            } else {
                passwordField.setStyle("");
            }
        });
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Personalizar el botón OK
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Registrarse");
        
        // Manejar el resultado del diálogo
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String registrationResult = controller.registerCustomerWithValidation(
                emailField.getText(),
                passwordField.getText(),
                nameField.getText(),
                addressField.getText(),
                phoneField.getText()
            );
            
            if (registrationResult == null) {
                showAlert("¡Éxito!", "Cliente registrado correctamente.\nYa puede iniciar sesión con su email.");
            } else {
                // Mostrar error y volver a abrir el diálogo con los datos
                showAlert("Error de Validación", registrationResult);
                
                // Reabrir el diálogo manteniendo los datos válidos
                showRegisterDialogWithData(
                    emailField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    registrationResult
                );
            }
        }
    }
    
    private void showRegisterDialogWithData(String email, String password, String name, String address, String phone, String errorMessage) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registro de Cliente - Corregir Errores");
        dialog.setHeaderText("Por favor corrija los errores indicados");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField emailField = new TextField(email);
        emailField.setPromptText("usuario@ejemplo.com");
        emailField.setPrefWidth(250);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setText(password);
        passwordField.setPromptText("Mínimo 6 caracteres");
        passwordField.setPrefWidth(250);
        
        TextField nameField = new TextField(name);
        nameField.setPromptText("Nombre completo");
        nameField.setPrefWidth(250);
        
        TextField addressField = new TextField(address);
        addressField.setPromptText("Dirección completa");
        addressField.setPrefWidth(250);
        
        TextField phoneField = new TextField(phone);
        phoneField.setPromptText("Teléfono de contacto");
        phoneField.setPrefWidth(250);
        
        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);
        errorLabel.setPrefWidth(400);
        
        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Nombre:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("Dirección:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Teléfono:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(errorLabel, 0, 5, 2, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Registrarse");
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String registrationResult = controller.registerCustomerWithValidation(
                emailField.getText(),
                passwordField.getText(),
                nameField.getText(),
                addressField.getText(),
                phoneField.getText()
            );
            
            if (registrationResult == null) {
                showAlert("¡Éxito!", "Cliente registrado correctamente.\nYa puede iniciar sesión con su email.");
            } else {
                showAlert("Error de Validación", registrationResult);
                // Recursivamente mostrar el diálogo hasta que sea exitoso o se cancele
                showRegisterDialogWithData(
                    emailField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    registrationResult
                );
            }
        }
    }

    private void addProduct() {
        try {
            String name = productNameField.getText();
            String desc = productDescField.getText();
            double price = Double.parseDouble(productPriceField.getText());
            int stock = Integer.parseInt(productStockField.getText());
            
            if (controller.addProduct(name, desc, price, stock, 1, "")) {
                clearProductForm();
                loadAdminData();
                showAlert("Éxito", "Producto agregado correctamente");
            } else {
                showAlert("Error", "No se pudo agregar el producto");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Verifique los datos numéricos");
        }
    }

    private void updateProduct() {
        Product selectedProduct = adminProductTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                selectedProduct.setNombre(productNameField.getText());
                selectedProduct.setDescripcion(productDescField.getText());
                selectedProduct.setPrecio(Double.parseDouble(productPriceField.getText()));
                selectedProduct.setStock(Integer.parseInt(productStockField.getText()));
                
                if (controller.updateProduct(selectedProduct)) {
                    clearProductForm();
                    loadAdminData();
                    showAlert("Éxito", "Producto actualizado correctamente");
                } else {
                    showAlert("Error", "No se pudo actualizar el producto");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Verifique los datos numéricos");
            }
        }
    }

    private void deleteProduct() {
        Product selectedProduct = adminProductTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            if (controller.deleteProduct(selectedProduct.getId())) {
                clearProductForm();
                loadAdminData();
                showAlert("Éxito", "Producto eliminado correctamente");
            } else {
                showAlert("Error", "No se pudo eliminar el producto");
            }
        }
    }

    private void addDiscount() {
        try {
            String name = discountNameField.getText();
            String type = discountTypeCombo.getValue();
            double value = Double.parseDouble(discountValueField.getText());
            
            if (controller.addDiscount(name, type, value, "Descuento " + name)) {
                clearDiscountForm();
                showAlert("Éxito", "Descuento agregado correctamente");
            } else {
                showAlert("Error", "No se pudo agregar el descuento");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Verifique el valor del descuento");
        }
    }

    private void removeFromCart() {
        Map.Entry<Product, Integer> selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (controller.removeFromCart(selectedItem.getKey())) {
                loadCartData();
                showAlert("Éxito", "Producto removido del carrito");
            } else {
                showAlert("Error", "No se pudo remover el producto");
            }
        }
    }

    private void checkout() {
        String paymentMethod = paymentMethodCombo.getValue();
        String shippingMethod = shippingMethodCombo.getValue();
        Discount selectedDiscount = discountCombo.getValue();
        
        if (paymentMethod != null && shippingMethod != null) {
            Integer discountId = selectedDiscount != null ? selectedDiscount.getId() : null;
            
            if (controller.createOrder(paymentMethod, shippingMethod, discountId)) {
                loadCartData();
                showAlert("Éxito", "Pedido creado correctamente");
            } else {
                showAlert("Error", "No se pudo procesar el pedido");
            }
        } else {
            showAlert("Error", "Seleccione método de pago y envío");
        }
    }

    private void loadAdminData() {
        adminProductTable.setItems(controller.getProducts());
        adminOrderTable.setItems(FXCollections.observableArrayList(controller.getAllOrders()));
    }

    private void loadCustomerData() {
        customerProductTable.setItems(controller.getProducts());
        loadCartData();
        customerOrderTable.setItems(FXCollections.observableArrayList(controller.getMyOrders()));
        discountCombo.setItems(FXCollections.observableArrayList(controller.getAvailableDiscounts()));
    }

    private void loadCartData() {
        Map<Product, Integer> cart = controller.getCart();
        ObservableList<Map.Entry<Product, Integer>> cartItems = FXCollections.observableArrayList(cart.entrySet());
        cartTable.setItems(cartItems);
        cartTotalLabel.setText(String.format("Total: $%.2f", controller.getCartTotal()));
    }

    private void clearProductForm() {
        productNameField.clear();
        productDescField.clear();
        productPriceField.clear();
        productStockField.clear();
    }

    private void clearDiscountForm() {
        discountNameField.clear();
        discountTypeCombo.setValue(null);
        discountValueField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Métodos para gestión avanzada de descuentos
    
    private void addGeneralDiscount(String description) {
        try {
            String name = discountNameField.getText();
            String type = discountTypeCombo.getValue();
            double value = Double.parseDouble(discountValueField.getText());
            
            if (controller.addDiscount(name, type, value, description)) {
                clearDiscountForm();
                showAlert("Éxito", "Descuento general agregado correctamente");
            } else {
                showAlert("Error", "No se pudo agregar el descuento");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Verifique el valor del descuento");
        }
    }
    
    private void addSpecificDiscount(String aplicablePara, String objetoIdText, String description) {
        try {
            String name = discountNameField.getText();
            String type = discountTypeCombo.getValue();
            double value = Double.parseDouble(discountValueField.getText());
            
            if ("general".equals(aplicablePara)) {
                addGeneralDiscount(description);
                return;
            }
            
            int objetoId = Integer.parseInt(objetoIdText);
            boolean success = false;
            
            if ("producto".equals(aplicablePara)) {
                success = controller.addProductDiscount(name, type, value, description, objetoId);
            } else if ("categoria".equals(aplicablePara)) {
                success = controller.addCategoryDiscount(name, type, value, description, objetoId);
            }
            
            if (success) {
                clearDiscountForm();
                showAlert("Éxito", "Descuento específico agregado correctamente");
            } else {
                showAlert("Error", "No se pudo agregar el descuento. Verifique que el ID existe.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Verifique los valores numéricos");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
