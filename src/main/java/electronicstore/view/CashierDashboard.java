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
    private TableView<Bill> todayBillsTable;
    private ObservableList<Bill> todayBillsData;
    private Label billsCountLabel;
    private TextField itemSearchField;
    private TextField quantityField;

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

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.getStyleClass().add("warning");
        resetButton.setOnAction(e -> resetDashboard());

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button");
        logoutButton.getStyleClass().add("danger");
        logoutButton.setOnAction(e -> logout());

        topBar.getChildren().addAll(branding, titleLabel, spacer, resetButton, logoutButton);
        mainLayout.setTop(topBar);

        VBox contentLayout = new VBox(15);
        contentLayout.getStyleClass().add("vbox");
        contentLayout.setPadding(new Insets(20));

        
        HBox itemSection = new HBox(15);
        itemSection.getStyleClass().add("hbox");
        itemSection.getStyleClass().add("card");
        itemSection.setPadding(new Insets(15));

        Label itemIdLabel = new Label("Item ID:");
        itemIdLabel.getStyleClass().add("label");

        itemSearchField = new TextField();
        itemSearchField.setPromptText("Enter Item ID");
        itemSearchField.getStyleClass().add("text-field");

        Label qtyLabel = new Label("Quantity:");
        qtyLabel.getStyleClass().add("label");

        quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        quantityField.getStyleClass().add("text-field");

        Button addItemButton = new Button("Add Item");
        addItemButton.getStyleClass().add("button");
        addItemButton.getStyleClass().add("success");

        itemSection.getChildren().addAll(itemIdLabel, itemSearchField, qtyLabel, quantityField, addItemButton);

        
        billTable = new TableView<>();
        billTable.getStyleClass().add("table-view");
        billData = FXCollections.observableArrayList();
        billTable.setItems(billData);
        billTable.setPrefHeight(200);

        TableColumn<BillItem, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getItem().getName()));

        TableColumn<BillItem, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<BillItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceAtSale"));

        TableColumn<BillItem, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getSubtotal()).asObject());

        TableColumn<BillItem, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setCellFactory(param -> new TableCell<BillItem, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    BillItem item = getTableView().getItems().get(getIndex());
                    billData.remove(item);
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
        generateBillButton.getStyleClass().add("button");
        generateBillButton.getStyleClass().add("success");
        generateBillButton.setOnAction(e -> {
            Bill generatedBill = controller.generateBill();
            if (generatedBill != null) {
                updateTodayBillsTable();
                billData.clear();
                totalLabel.setText("Total: $0.00");
                showBillWindow(generatedBill);
            }
        });

        
        todayBillsTable = new TableView<>();
        todayBillsTable.getStyleClass().add("table-view");
        todayBillsData = FXCollections.observableArrayList(cashier.viewTodayBills());
        todayBillsTable.setItems(todayBillsData);
        todayBillsTable.setPrefHeight(200);

        TableColumn<Bill, String> billNumCol = new TableColumn<>("Bill Number");
        billNumCol.setCellValueFactory(new PropertyValueFactory<>("billNumber"));

        TableColumn<Bill, Double> billTotalCol = new TableColumn<>("Total");
        billTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        todayBillsTable.getColumns().addAll(billNumCol, billTotalCol);

        billsCountLabel = new Label("Total Bills Today: " + cashier.getTotalBillsCount());
        billsCountLabel.getStyleClass().add("label");
        billsCountLabel.getStyleClass().add("header");

        Label todaysBillsLabel = new Label("Today's Bills:");
        todaysBillsLabel.getStyleClass().add("label");
        todaysBillsLabel.getStyleClass().add("header");

        contentLayout.getChildren().addAll(itemSection, billTable, totalLabel, generateBillButton,
                                        todaysBillsLabel, todayBillsTable, billsCountLabel);

        mainLayout.setCenter(contentLayout);

        Scene scene = new Scene(mainLayout, 900, 700);

        
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Cashier Dashboard");
    }

    private void updateBillTable() {
        if (cashier.getCurrentBill() != null) {
            billData.setAll(cashier.getCurrentBill().getItems());
        }
    }

    private void updateTodayBillsTable() {
        todayBillsData.setAll(cashier.viewTodayBills());
        billsCountLabel.setText("Total Bills Today: " + cashier.getTotalBillsCount());
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
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceAtSale"));
        
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
        double total = billData.stream().mapToDouble(BillItem::getSubtotal).sum();
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    private void resetDashboard() {
        
        billData.clear();
        totalLabel.setText("Total: $0.00");

        
        itemSearchField.clear();
        quantityField.clear();

        
        todayBillsData.clear();
        todayBillsData.addAll(cashier.viewTodayBills());
        billsCountLabel.setText("Total Bills Today: " + cashier.getTotalBillsCount());
    }

    private void logout() {
        
        LoginController controller = new LoginController();
        new LoginView(stage, controller);
    }
}