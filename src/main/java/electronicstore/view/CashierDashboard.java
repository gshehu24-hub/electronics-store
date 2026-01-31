package electronicstore.view;

import electronicstore.controller.CashierController;
import electronicstore.controller.LoginController;
import electronicstore.model.inventory.Item;
import electronicstore.model.transactions.Bill;
import electronicstore.model.transactions.BillItem;
import electronicstore.model.users.Cashier;
import electronicstore.view.LoginView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CashierDashboard {
    private Stage stage;
    private CashierController controller;
    private Cashier cashier;
    private TableView<BillItem> billTable;
    private ObservableList<BillItem> billData;
    private Label totalLabel;
    private TextField itemField;
    private TextField qtyField;

    public CashierDashboard(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
        this.controller = new CashierController(cashier);
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
        Label titleLabel = new Label("Cashier Dashboard");
        titleLabel.getStyleClass().addAll("label", "header");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = electronicstore.view.Branding.createIconButton("⏻", "Logout", 12, "#FF6B6B");
        logoutButton.setOnAction(e -> logout());

        topBar.getChildren().addAll(branding, titleLabel, spacer, logoutButton);
        mainLayout.setTop(topBar);

        VBox contentLayout = new VBox(15);
        try {
            cashier.loadInventory();
            cashier.loadTodayBills();
        } catch (Exception ignored) {}
        HBox itemSection = new HBox(15);
        itemSection.getStyleClass().add("hbox");
        itemSection.getStyleClass().add("card");
        itemSection.setPadding(new Insets(15));

        Label itemIdLabel = new Label("Item ID:");
        itemIdLabel.getStyleClass().add("label");

        itemField = new TextField();
        itemField.setPromptText("Enter Item ID");
        itemField.getStyleClass().add("text-field");

        Label qtyLabel = new Label("Quantity:");
        qtyLabel.getStyleClass().add("label");

        qtyField = new TextField();
        qtyField.setPromptText("Quantity");
        qtyField.getStyleClass().add("text-field");

        Button addItemButton = new Button("Add Item");
        addItemButton.getStyleClass().addAll("button", "success");
        addItemButton.setOnAction(e -> {
            String itemId = itemField.getText().trim();
            int qty = 1;
            try {
                qty = Integer.parseInt(qtyField.getText().trim());
            } catch (NumberFormatException ex) {
            }
            try {
                boolean added = controller.addItemToBill(itemId, qty);
                if (added) {
                    updateBillTable();
                    updateTotal();
                    itemField.clear();
                    qtyField.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Item not found.", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        });

        itemSection.getChildren().addAll(itemIdLabel, itemField, qtyLabel, qtyField, addItemButton);

        
        billTable = new TableView<>();
        billTable.getStyleClass().add("table-view");
        billTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        billData = FXCollections.observableArrayList();
        billTable.setItems(billData);

        TableColumn<BillItem, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getItem().getName()));

        TableColumn<BillItem, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<BillItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<BillItem, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getSubtotal()).asObject());

        TableColumn<BillItem, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setCellFactory(param -> new TableCell<BillItem, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.getStyleClass().addAll("button", "danger");
                deleteButton.setOnAction(event -> {
                    BillItem bi = getTableView().getItems().get(getIndex());
                    if (bi != null && bi.getItem() != null) {
                        bi.getItem().setQuantity(bi.getItem().getQuantity() + bi.getQuantity());
                    }
                    billData.remove(bi);
                    if (cashier.getCurrentBill() != null) {
                        cashier.getCurrentBill().getItems().remove(bi);
                    }
                    updateTotal();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        billTable.getColumns().addAll(itemNameCol, qtyCol, priceCol, subtotalCol, deleteCol);

        totalLabel = new Label("Total: $0.00");
        totalLabel.getStyleClass().add("label");
        totalLabel.getStyleClass().add("header");
        totalLabel.getStyleClass().add("success");

        Button generateBillButton = new Button("Generate Bill");
        generateBillButton.getStyleClass().addAll("button", "primary");
        generateBillButton.setOnAction(e -> {
            Bill generatedBill = controller.generateBill();
            if (generatedBill != null) {
                billData.clear();
                totalLabel.setText("Total: $0.00");
                showBillWindow(generatedBill);
            }
        });

        HBox bottomRow = new HBox();
        bottomRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);
        bottomRow.getChildren().addAll(totalLabel, bottomSpacer, generateBillButton);

        contentLayout.getChildren().addAll(itemSection, billTable, bottomRow);

        mainLayout.setCenter(contentLayout);

        Scene scene = new Scene(mainLayout, 900, 700);

        
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Cashier Dashboard");
        stage.setMaximized(true);
    }

    private void updateBillTable() {
        if (cashier.getCurrentBill() != null) {
            billData.setAll(cashier.getCurrentBill().getItems());
        }
    }

    private void showBillWindow(Bill bill) {
        Stage billStage = new Stage();
        billStage.setTitle("Bill - " + bill.getBillNumber());
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Inventa BILL");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label billInfoLabel = new Label("Bill Number: " + bill.getBillNumber() + 
                                       "\nDate: " + bill.getBillDate() + 
                                       "\nTime: " + bill.getBillTime() +
                                       "\nCashier: " + bill.getCashier().getName());
        
        
        TableView<BillItem> billItemsTable = new TableView<>();
        ObservableList<BillItem> billItemsData = FXCollections.observableArrayList(bill.getItems());
        billItemsTable.setItems(billItemsData);
        
        TableColumn<BillItem, String> itemNameCol = new TableColumn<>("Item");
        itemNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getItem().getName()));
        
        TableColumn<BillItem, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        TableColumn<BillItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<BillItem, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getSubtotal()).asObject());
        
        billItemsTable.getColumns().addAll(itemNameCol, qtyCol, priceCol, subtotalCol);
        billItemsTable.setPrefHeight(200);
        
        Label totalLabel = new Label("TOTAL: $" + String.format("%.2f", bill.calculateTotal()));
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label thankYouLabel = new Label("Thank you for shopping with us!");
        thankYouLabel.setStyle("-fx-font-style: italic;");
        
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> billStage.close());
        
        layout.getChildren().addAll(titleLabel, billInfoLabel, billItemsTable, totalLabel, thankYouLabel, closeButton);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        
        Scene scene = new Scene(layout, 500, 500);
        billStage.setScene(scene);
        billStage.show();
    }

    private void updateTotal() {
        double total = 0.0;
        for (BillItem item : billData) {
            total += item.getSubtotal();
        }
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    private void logout() {
        
        LoginController controller = new LoginController();
        new LoginView(stage, controller);
    }
}