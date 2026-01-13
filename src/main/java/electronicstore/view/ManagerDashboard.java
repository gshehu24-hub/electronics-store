package electronicstore.view;

import electronicstore.controller.LoginController;
import electronicstore.controller.ManagerController;
import electronicstore.model.inventory.Category;
import electronicstore.model.inventory.Item;
import electronicstore.model.inventory.Sector;
import electronicstore.model.inventory.Supplier;
import electronicstore.model.users.Manager;
import electronicstore.model.utilities.IDGenerator;
import electronicstore.model.utilities.Validator;
import electronicstore.view.LoginView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ManagerDashboard {
    private Stage stage;
    private ManagerController controller;
    private Manager manager;
    private TabPane tabPane;

    public ManagerDashboard(Stage stage, Manager manager) {
        this.stage = stage;
        this.manager = manager;
        this.controller = new ManagerController(manager);
        manager.loadInventory(); 
        initializeUI();
    }

    private void initializeUI() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-layout");

        
        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        topBar.setSpacing(15);
        topBar.setPadding(new Insets(15));

        
        HBox branding = electronicstore.view.Branding.createLargeBranding();
        branding.getStyleClass().add("dashboard-branding");
        Label titleLabel = new Label("Manager Dashboard");
        titleLabel.getStyleClass().addAll("label", "header");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = electronicstore.view.Branding.createIconButton("⏻", "Logout", 12, "#FF6B6B");
        logoutButton.setOnAction(e -> logout());

        topBar.getChildren().addAll(branding, titleLabel, spacer, logoutButton);
        mainLayout.setTop(topBar);

        tabPane = new TabPane();
        tabPane.getStyleClass().add("tab-pane");

        
        Tab inventoryTab = new Tab("Inventory");
        VBox inventoryLayout = createInventoryTab();
        inventoryTab.setContent(inventoryLayout);

        
        Tab suppliersTab = new Tab("Suppliers");
        VBox suppliersLayout = createSuppliersTab();
        suppliersTab.setContent(suppliersLayout);

        
        Tab alertsTab = new Tab("Stock Alerts");
        VBox alertsLayout = createAlertsTab();
        alertsTab.setContent(alertsLayout);

        
        Tab statsTab = new Tab("Statistics");
        VBox statsLayout = createStatisticsTab();
        statsTab.setContent(statsLayout);

        
        inventoryTab.setGraphic(electronicstore.view.Branding.createSmallIcon());
        suppliersTab.setGraphic(electronicstore.view.Branding.createSmallIcon());
        alertsTab.setGraphic(electronicstore.view.Branding.createSmallIcon());
        statsTab.setGraphic(electronicstore.view.Branding.createSmallIcon());

        tabPane.getTabs().addAll(inventoryTab, suppliersTab, alertsTab, statsTab);
        mainLayout.setCenter(tabPane);

        Scene scene = new Scene(mainLayout, 900, 700);

        
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Manager Dashboard");
    }

    private VBox createInventoryTab() {
        VBox layout = new VBox(15);
        layout.getStyleClass().add("vbox");
        layout.setPadding(new Insets(20));

        TableView<Item> itemTable = new TableView<>();
        itemTable.getStyleClass().add("table-view");
        ObservableList<Item> itemData = FXCollections.observableArrayList(manager.getSectors().stream()
                .flatMap(sector -> sector.getItems().stream()).toList());
        itemTable.setItems(itemData);
        itemTable.setPrefHeight(350);

        TableColumn<Item, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        idCol.setPrefWidth(80);

        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Item, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setPrefWidth(80);

        TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        priceCol.setPrefWidth(80);

        TableColumn<Item, Double> discountCol = new TableColumn<>("Discount %");
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discountPercent"));
        discountCol.setPrefWidth(100);

        itemTable.getColumns().addAll(idCol, nameCol, qtyCol, priceCol, discountCol);

        HBox buttons = new HBox(15);
        buttons.getStyleClass().add("hbox");

        Button addItemButton = new Button("Add Item");
        addItemButton.getStyleClass().addAll("button", "primary", "success");

        Button updateItemButton = new Button("Update Item");
        updateItemButton.getStyleClass().addAll("button", "ghost");

        Button applyDiscountButton = new Button("Apply Discount");
        applyDiscountButton.getStyleClass().addAll("button");

        Button deleteItemButton = new Button("Delete Item");
        deleteItemButton.getStyleClass().addAll("button", "danger");

        addItemButton.setOnAction(e -> showAddItemDialog(itemData));
        updateItemButton.setOnAction(e -> showUpdateItemDialog(itemTable.getSelectionModel().getSelectedItem(), itemData));
        applyDiscountButton.setOnAction(e -> showApplyDiscountDialog(itemTable.getSelectionModel().getSelectedItem(), itemData));
        deleteItemButton.setOnAction(e -> deleteSelectedItem(itemTable.getSelectionModel().getSelectedItem(), itemData));

        buttons.getChildren().addAll(addItemButton, updateItemButton, applyDiscountButton, deleteItemButton);

        layout.getChildren().addAll(itemTable, buttons);
        return layout;
    }

    private void showAddItemDialog(ObservableList<Item> itemData) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Item");
        dialog.setHeaderText("Enter item details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Item Name");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category Name");
        TextField supplierField = new TextField();
        supplierField.setPromptText("Supplier Name");
        TextField sectorField = new TextField();
        sectorField.setPromptText("Sector Name");
        DatePicker purchaseDatePicker = new DatePicker();
        TextField purchasePriceField = new TextField();
        purchasePriceField.setPromptText("Purchase Price");
        TextField sellingPriceField = new TextField();
        sellingPriceField.setPromptText("Selling Price");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Supplier:"), 0, 2);
        grid.add(supplierField, 1, 2);
        grid.add(new Label("Sector:"), 0, 3);
        grid.add(sectorField, 1, 3);
        grid.add(new Label("Purchase Date:"), 0, 4);
        grid.add(purchaseDatePicker, 1, 4);
        grid.add(new Label("Purchase Price:"), 0, 5);
        grid.add(purchasePriceField, 1, 5);
        grid.add(new Label("Selling Price:"), 0, 6);
        grid.add(sellingPriceField, 1, 6);
        grid.add(new Label("Quantity:"), 0, 7);
        grid.add(quantityField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String catName = categoryField.getText().trim();
                    String supName = supplierField.getText().trim();
                    String secName = sectorField.getText().trim();
                    LocalDate purchaseDate = purchaseDatePicker.getValue();
                    double purchasePrice = Double.parseDouble(purchasePriceField.getText().trim());
                    double sellingPrice = Double.parseDouble(sellingPriceField.getText().trim());
                    int quantity = Integer.parseInt(quantityField.getText().trim());

                    if (name.isEmpty() || catName.isEmpty() || supName.isEmpty() || secName.isEmpty() ||
                        purchaseDate == null || !Validator.validatePositiveNumber(purchasePrice) ||
                        !Validator.validatePositiveNumber(sellingPrice) || !Validator.validateQuantity(quantity)) {
                        throw new IllegalArgumentException("Invalid input");
                    }

                    
                    Category category = manager.getSectors().stream()
                        .flatMap(s -> s.getItems().stream())
                        .map(Item::getCategory)
                        .filter(c -> c.getCategoryName().equals(catName))
                        .findFirst().orElse(null);
                    if (category == null) {
                        category = new Category(IDGenerator.generateCategoryID(), catName, "");
                    }

                    
                    Supplier supplier = manager.getSuppliers().stream()
                        .filter(s -> s.getName().equals(supName))
                        .findFirst().orElse(null);
                    if (supplier == null) {
                        supplier = new Supplier(IDGenerator.generateSupplierID(), supName, "", "", "", "");
                        manager.addSupplier(supplier);
                    }

                    
                    Sector sector = manager.getSectors().stream()
                        .filter(s -> s.getSectorName().equals(secName))
                        .findFirst().orElse(null);
                    if (sector == null) {
                        sector = new Sector(IDGenerator.generateSectorID(), secName, "");
                        manager.getSectors().add(sector);
                    }

                    Item newItem = new Item(IDGenerator.generateItemID(), name, category, supplier, sector,
                                            purchaseDate, purchasePrice, sellingPrice, quantity);
                    controller.addItem(newItem);
                    sector.getItems().add(newItem);
                    itemData.add(newItem);
                    return addButtonType;
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error adding item: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showUpdateItemDialog(Item selected, ObservableList<Item> itemData) {
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an item to update.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Item");
        dialog.setHeaderText("Update item quantity");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField quantityField = new TextField(String.valueOf(selected.getQuantity()));
        quantityField.setPromptText("New Quantity");

        grid.add(new Label("Quantity:"), 0, 0);
        grid.add(quantityField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    int newQty = Integer.parseInt(quantityField.getText().trim());
                    if (!Validator.validateQuantity(newQty)) {
                        throw new IllegalArgumentException("Invalid quantity");
                    }
                    selected.setQuantity(newQty);
                    itemData.set(itemData.indexOf(selected), selected);
                    manager.saveInventory();
                    return updateButtonType;
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error updating item: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showApplyDiscountDialog(Item selected, ObservableList<Item> itemData) {
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an item to apply discount.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Apply Discount");
        dialog.setHeaderText("Enter discount percentage");

        ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField discountField = new TextField();
        discountField.setPromptText("Discount %");

        grid.add(new Label("Discount %:"), 0, 0);
        grid.add(discountField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == applyButtonType) {
                try {
                    double discount = Double.parseDouble(discountField.getText().trim());
                    if (discount < 0 || discount > 100) {
                        throw new IllegalArgumentException("Invalid discount");
                    }
                    controller.applyDiscount(selected.getItemID(), discount);
                    itemData.set(itemData.indexOf(selected), selected);
                    return applyButtonType;
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error applying discount: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteSelectedItem(Item selected, ObservableList<Item> itemData) {
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an item to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Item");
        confirmation.setHeaderText("Are you sure you want to delete this item?");
        confirmation.setContentText("Item: " + selected.getName() + " (ID: " + selected.getItemID() + ")");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.deleteItem(selected.getItemID());
                    itemData.remove(selected);
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error deleting item: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private void showAddSupplierDialog(ObservableList<Supplier> supplierData) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Supplier");
        dialog.setHeaderText("Add a new supplier");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Supplier Name");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact Person");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Contact Person:"), 0, 1);
        grid.add(contactField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String contact = contactField.getText().trim();
                    String email = emailField.getText().trim();
                    String phone = phoneField.getText().trim();
                    String address = addressField.getText().trim();

                    if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        throw new IllegalArgumentException("All fields are required");
                    }

                    if (!Validator.validateEmail(email)) {
                        throw new IllegalArgumentException("Invalid email format");
                    }

                    if (!Validator.validatePhoneNumber(phone)) {
                        throw new IllegalArgumentException("Invalid phone format");
                    }

                    Supplier newSupplier = new Supplier(IDGenerator.generateSupplierID(), name, contact, email, phone, address);
                    controller.addSupplier(newSupplier);
                    supplierData.add(newSupplier);
                    return addButtonType;
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error adding supplier: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showUpdateSupplierDialog(Supplier selected, ObservableList<Supplier> supplierData) {
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a supplier to update.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Supplier");
        dialog.setHeaderText("Update supplier information");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(selected.getName());
        TextField contactField = new TextField(selected.getContactPerson());
        TextField emailField = new TextField(selected.getEmail());
        TextField phoneField = new TextField(selected.getPhoneNumber());
        TextField addressField = new TextField(selected.getAddress());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Contact Person:"), 0, 1);
        grid.add(contactField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String contact = contactField.getText().trim();
                    String email = emailField.getText().trim();
                    String phone = phoneField.getText().trim();
                    String address = addressField.getText().trim();

                    if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        throw new IllegalArgumentException("All fields are required");
                    }

                    if (!Validator.validateEmail(email)) {
                        throw new IllegalArgumentException("Invalid email format");
                    }

                    if (!Validator.validatePhoneNumber(phone)) {
                        throw new IllegalArgumentException("Invalid phone format");
                    }

                    selected.setName(name);
                    selected.setContactPerson(contact);
                    selected.setEmail(email);
                    selected.setPhoneNumber(phone);
                    selected.setAddress(address);

                    controller.updateSupplier(selected);
                    supplierData.set(supplierData.indexOf(selected), selected);
                    return updateButtonType;
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error updating supplier: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    private void deleteSelectedSupplier(Supplier selected, ObservableList<Supplier> supplierData) {
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a supplier to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Supplier");
        confirmation.setHeaderText("Are you sure you want to delete this supplier?");
        confirmation.setContentText("Supplier: " + selected.getName() + " (ID: " + selected.getSupplierID() + ")");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.deleteSupplier(selected.getSupplierID());
                    supplierData.remove(selected);
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error deleting supplier: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
    private VBox createSuppliersTab() {
        VBox layout = new VBox(15);
        layout.getStyleClass().add("vbox");
        layout.setPadding(new Insets(20));

        TableView<Supplier> supplierTable = new TableView<>();
        supplierTable.getStyleClass().add("table-view");
        ObservableList<Supplier> supplierData = FXCollections.observableArrayList(manager.getSuppliers());
        supplierTable.setItems(supplierData);

        TableColumn<Supplier, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("supplierID"));

        TableColumn<Supplier, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Supplier, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));

        TableColumn<Supplier, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Supplier, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        supplierTable.getColumns().addAll(idCol, nameCol, contactCol, emailCol, phoneCol);

        HBox buttons = new HBox(15);
        buttons.getStyleClass().add("hbox");

        Button addSupplierButton = new Button("Add Supplier");
        addSupplierButton.getStyleClass().add("button");
        addSupplierButton.getStyleClass().add("success");

        Button updateSupplierButton = new Button("Update Supplier");
        updateSupplierButton.getStyleClass().add("button");
        updateSupplierButton.getStyleClass().add("warning");

        Button deleteSupplierButton = new Button("Delete Supplier");
        deleteSupplierButton.getStyleClass().add("button");
        deleteSupplierButton.getStyleClass().add("danger");

        addSupplierButton.setOnAction(e -> showAddSupplierDialog(supplierData));
        updateSupplierButton.setOnAction(e -> showUpdateSupplierDialog(supplierTable.getSelectionModel().getSelectedItem(), supplierData));
        deleteSupplierButton.setOnAction(e -> deleteSelectedSupplier(supplierTable.getSelectionModel().getSelectedItem(), supplierData));

        buttons.getChildren().addAll(addSupplierButton, updateSupplierButton, deleteSupplierButton);

        layout.getChildren().addAll(supplierTable, buttons);
        return layout;
    }

    private VBox createAlertsTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        ListView<Item> alertsList = new ListView<>();
        ObservableList<Item> alertsData = FXCollections.observableArrayList(manager.checkStockAlerts());
        alertsList.setItems(alertsData);

        layout.getChildren().addAll(new Label("Items needing restock:"), alertsList);
        return layout;
    }

    private VBox createStatisticsTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        HBox dateBox = new HBox(10);
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        Button generateStatsButton = new Button("Generate Statistics");

        dateBox.getChildren().addAll(new Label("Start Date:"), startDatePicker,
                                     new Label("End Date:"), endDatePicker, generateStatsButton);

        TextArea statsArea = new TextArea();
        statsArea.setEditable(false);

        generateStatsButton.setOnAction(e -> {
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();
            if (start != null && end != null && !start.isAfter(end)) {
                StringBuilder stats = new StringBuilder();
                stats.append("Inventory Statistics from ").append(start).append(" to ").append(end).append("\n\n");

                int totalItems = 0;
                double totalValue = 0.0;
                int lowStockCount = 0;

                for (Sector sector : manager.getSectors()) {
                    for (Item item : sector.getItems()) {
                        if (!item.getPurchaseDate().isBefore(start) && !item.getPurchaseDate().isAfter(end)) {
                            totalItems += item.getQuantity();
                            totalValue += item.getSellingPrice() * item.getQuantity();
                        }
                        if (item.getQuantity() < 5) { 
                            lowStockCount++;
                        }
                    }
                }

                stats.append("Total Items in Stock: ").append(totalItems).append("\n");
                stats.append("Total Inventory Value: $").append(String.format("%.2f", totalValue)).append("\n");
                stats.append("Items with Low Stock (<5): ").append(lowStockCount).append("\n");
                stats.append("Number of Suppliers: ").append(manager.getSuppliers().size()).append("\n");

                statsArea.setText(stats.toString());
            } else {
                statsArea.setText("Please select valid start and end dates.");
            }
        });

        layout.getChildren().addAll(dateBox, statsArea);
        return layout;
    }

    // resetDashboard removed — functionality no longer exposed via UI

    private void logout() {
        
        LoginController controller = new LoginController();
        new LoginView(stage, controller);
    }
}