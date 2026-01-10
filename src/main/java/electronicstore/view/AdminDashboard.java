package electronicstore.view;

import electronicstore.controller.AdminController;
import electronicstore.controller.LoginController;
import electronicstore.model.exceptions.InvalidInputException;
import electronicstore.model.transactions.Report;
import electronicstore.model.users.AccessLevel;
import electronicstore.model.users.Administrator;
import electronicstore.model.users.User;
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
import javafx.scene.layout.HBox;import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AdminDashboard {
    private Stage stage;
    private AdminController controller;
    private Administrator admin;
    private TableView<User> employeeTable;
    private ObservableList<User> employeeData;
    private Label totalIncomeLabel;
    private Label totalCostsLabel;
    private Label netProfitLabel;
    private Label totalSalariesLabel;

    public AdminDashboard(Stage stage, Administrator admin) {
        this.stage = stage;
        this.admin = admin;
        this.controller = new AdminController(admin);
        initializeUI();
    }

    private void initializeUI() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-layout");
        mainLayout.setPadding(new Insets(20));

        
        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        topBar.setSpacing(15);

        
        HBox branding = electronicstore.view.Branding.createLargeBranding();
        branding.getStyleClass().add("dashboard-branding");
        
        Label titleLabel = new Label("Administrator Dashboard");
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

        VBox centerLayout = new VBox(20);
        centerLayout.getStyleClass().add("vbox");
        centerLayout.setPadding(new Insets(20));

        
        VBox employeeSection = new VBox(15);
        employeeSection.getStyleClass().add("card");

        Label employeeLabel = new Label("Employee Management");
        employeeLabel.getStyleClass().add("label");
        employeeLabel.getStyleClass().add("header");

        employeeTable = new TableView<>();
        employeeTable.getStyleClass().add("table-view");
        employeeData = FXCollections.observableArrayList(admin.getEmployeeList());
        employeeTable.setItems(employeeData);
        employeeTable.setPrefHeight(250);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(100);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<User, String> accessLevelCol = new TableColumn<>("Access Level");
        accessLevelCol.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));
        accessLevelCol.setPrefWidth(100);

        TableColumn<User, Double> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryCol.setPrefWidth(100);

        employeeTable.getColumns().addAll(usernameCol, nameCol, accessLevelCol, salaryCol);

        HBox employeeButtons = new HBox(15);
        employeeButtons.getStyleClass().add("hbox");

        Button addButton = new Button("Add Employee");
        addButton.getStyleClass().add("button");
        addButton.getStyleClass().add("success");
        addButton.setOnAction(e -> showAddEmployeeDialog());

        Button modifyButton = new Button("Modify Employee");
        modifyButton.getStyleClass().add("button");
        modifyButton.getStyleClass().add("warning");
        modifyButton.setOnAction(e -> showModifyEmployeeDialog());

        Button deleteButton = new Button("Delete Employee");
        deleteButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("danger");
        deleteButton.setOnAction(e -> deleteSelectedEmployee());

        employeeButtons.getChildren().addAll(addButton, modifyButton, deleteButton);

        employeeSection.getChildren().addAll(employeeLabel, employeeTable, employeeButtons);

        
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");

        
        VBox financialSection = new VBox(15);
        financialSection.getStyleClass().add("card");

        Label financialLabel = new Label("Financial Report");
        financialLabel.getStyleClass().add("label");
        financialLabel.getStyleClass().add("header");

        HBox dateBox = new HBox(15);
        dateBox.getStyleClass().add("hbox");
        dateBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label startLabel = new Label("Start Date:");
        startLabel.getStyleClass().add("label");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.getStyleClass().add("date-picker");

        Label endLabel = new Label("End Date:");
        endLabel.getStyleClass().add("label");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.getStyleClass().add("date-picker");

        Button generateReportButton = new Button("Generate Report");
        generateReportButton.getStyleClass().add("button");
        generateReportButton.getStyleClass().add("success");

        dateBox.getChildren().addAll(startLabel, startDatePicker, endLabel, endDatePicker, generateReportButton);

        HBox resultsBox = new HBox(25);
        resultsBox.getStyleClass().add("hbox");
        resultsBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        totalIncomeLabel = new Label("Total Income: $0.00");
        totalIncomeLabel.getStyleClass().add("label");
        totalIncomeLabel.getStyleClass().add("success");

        totalCostsLabel = new Label("Total Costs: $0.00");
        totalCostsLabel.getStyleClass().add("label");
        totalCostsLabel.getStyleClass().add("error");

        netProfitLabel = new Label("Net Profit: $0.00");
        netProfitLabel.getStyleClass().add("label");
        netProfitLabel.getStyleClass().add("success");

        totalSalariesLabel = new Label("Total Salaries: $0.00");
        totalSalariesLabel.getStyleClass().add("label");
        totalSalariesLabel.getStyleClass().add("warning");

        resultsBox.getChildren().addAll(totalIncomeLabel, totalCostsLabel, netProfitLabel, totalSalariesLabel);

        generateReportButton.setOnAction(e -> {
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();
            if (start != null && end != null) {
                Report report = admin.generateFinancialReport(start, end);
                totalIncomeLabel.setText("Total Income: $" + String.format("%.2f", report.getTotalIncome()));
                totalCostsLabel.setText("Total Costs: $" + String.format("%.2f", report.getTotalCosts()));
                netProfitLabel.setText("Net Profit: $" + String.format("%.2f", report.getNetProfit()));
                totalSalariesLabel.setText("Total Salaries: $" + String.format("%.2f", admin.getEmployeeList().stream().mapToDouble(User::getSalary).sum()));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select both start and end dates.");
                alert.showAndWait();
            }
        });

        financialSection.getChildren().addAll(financialLabel, dateBox, resultsBox);

        centerLayout.getChildren().addAll(employeeSection, separator, financialSection);

        mainLayout.setCenter(centerLayout);

        Scene scene = new Scene(mainLayout, 900, 700);

        
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Administrator Dashboard");
        stage.setResizable(true);
    }

    private void showAddEmployeeDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Employee");
        dialog.setHeaderText("Enter employee details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        DatePicker dobPicker = new DatePicker();
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");
        ComboBox<AccessLevel> accessLevelBox = new ComboBox<>();
        accessLevelBox.setItems(FXCollections.observableArrayList(AccessLevel.MANAGER, AccessLevel.CASHIER));
        accessLevelBox.setValue(AccessLevel.CASHIER);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Name:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("Date of Birth:"), 0, 3);
        grid.add(dobPicker, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Salary:"), 0, 6);
        grid.add(salaryField, 1, 6);
        grid.add(new Label("Access Level:"), 0, 7);
        grid.add(accessLevelBox, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String username = usernameField.getText().trim();
                    String password = passwordField.getText();
                    String name = nameField.getText().trim();
                    LocalDate dob = dobPicker.getValue();
                    String phone = phoneField.getText().trim();
                    String email = emailField.getText().trim();
                    double salary = Double.parseDouble(salaryField.getText().trim());
                    AccessLevel level = accessLevelBox.getValue();

                    
                    if (username.isEmpty()) {
                        throw new IllegalArgumentException("Username is required");
                    }
                    if (!Validator.validateUsername(username)) {
                        throw new IllegalArgumentException("Username must be at least 4 characters and contain only letters and numbers");
                    }
                    if (password.isEmpty()) {
                        throw new IllegalArgumentException("Password is required");
                    }
                    if (!Validator.validatePassword(password)) {
                        throw new IllegalArgumentException("Password must be at least 6 characters");
                    }
                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Name is required");
                    }
                    if (dob == null) {
                        throw new IllegalArgumentException("Date of birth is required");
                    }
                    if (phone.isEmpty()) {
                        throw new IllegalArgumentException("Phone number is required");
                    }
                    if (!Validator.validatePhoneNumber(phone)) {
                        throw new IllegalArgumentException("Phone number must be 10-15 digits");
                    }
                    if (email.isEmpty()) {
                        throw new IllegalArgumentException("Email is required");
                    }
                    if (!Validator.validateEmail(email)) {
                        throw new IllegalArgumentException("Invalid email format");
                    }
                    if (!Validator.validatePositiveNumber(salary)) {
                        throw new IllegalArgumentException("Salary must be a positive number");
                    }

                    User newUser = null;
                    if (level == AccessLevel.MANAGER) {
                        newUser = new electronicstore.model.users.Manager(username, password, name, dob, phone, email, salary);
                    } else if (level == AccessLevel.CASHIER) {
                        newUser = new electronicstore.model.users.Cashier(username, password, name, dob, phone, email, salary);
                    }

                    controller.addEmployee(newUser);
                    employeeData.add(newUser);
                    return addButtonType;
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error adding employee: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showModifyEmployeeDialog() {
        User selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select an employee to modify.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modify Employee");
        dialog.setHeaderText("Edit employee details");

        ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField(selected.getUsername());
        usernameField.setEditable(false); 
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New Password (leave empty to keep current)");
        TextField nameField = new TextField(selected.getName());
        DatePicker dobPicker = new DatePicker(selected.getDateOfBirth());
        TextField phoneField = new TextField(selected.getPhoneNumber());
        TextField emailField = new TextField(selected.getEmail());
        TextField salaryField = new TextField(String.valueOf(selected.getSalary()));
        ComboBox<AccessLevel> accessLevelBox = new ComboBox<>();
        accessLevelBox.setItems(FXCollections.observableArrayList(AccessLevel.MANAGER, AccessLevel.CASHIER));
        accessLevelBox.setValue(selected.getAccessLevel());

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Name:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("Date of Birth:"), 0, 3);
        grid.add(dobPicker, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Salary:"), 0, 6);
        grid.add(salaryField, 1, 6);
        grid.add(new Label("Access Level:"), 0, 7);
        grid.add(accessLevelBox, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                try {
                    String password = passwordField.getText().isEmpty() ? selected.getPassword() : passwordField.getText();
                    String name = nameField.getText().trim();
                    LocalDate dob = dobPicker.getValue();
                    String phone = phoneField.getText().trim();
                    String email = emailField.getText().trim();
                    double salary = Double.parseDouble(salaryField.getText().trim());
                    AccessLevel level = accessLevelBox.getValue();

                    
                    if (!passwordField.getText().isEmpty() && !Validator.validatePassword(password)) {
                        throw new IllegalArgumentException("Password must be at least 6 characters");
                    }
                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Name is required");
                    }
                    if (dob == null) {
                        throw new IllegalArgumentException("Date of birth is required");
                    }
                    if (phone.isEmpty()) {
                        throw new IllegalArgumentException("Phone number is required");
                    }
                    if (!Validator.validatePhoneNumber(phone)) {
                        throw new IllegalArgumentException("Phone number must be 10-15 digits");
                    }
                    if (email.isEmpty()) {
                        throw new IllegalArgumentException("Email is required");
                    }
                    if (!Validator.validateEmail(email)) {
                        throw new IllegalArgumentException("Invalid email format");
                    }
                    if (!Validator.validatePositiveNumber(salary)) {
                        throw new IllegalArgumentException("Salary must be a positive number");
                    }

                    User updatedUser = null;
                    if (level == AccessLevel.MANAGER) {
                        updatedUser = new electronicstore.model.users.Manager(selected.getUsername(), password, name, dob, phone, email, salary);
                    } else if (level == AccessLevel.CASHIER) {
                        updatedUser = new electronicstore.model.users.Cashier(selected.getUsername(), password, name, dob, phone, email, salary);
                    }

                    controller.modifyEmployee(selected.getUsername(), updatedUser);
                    int index = employeeData.indexOf(selected);
                    employeeData.set(index, updatedUser);
                    return modifyButtonType;
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Error modifying employee: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteSelectedEmployee() {
        User selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            admin.deleteEmployee(selected.getUsername());
            employeeData.remove(selected);
        }
    }

    private void resetDashboard() {
        
        totalIncomeLabel.setText("Total Income: $0.00");
        totalCostsLabel.setText("Total Costs: $0.00");
        netProfitLabel.setText("Net Profit: $0.00");
        totalSalariesLabel.setText("Total Salaries: $0.00");

        
        employeeData.clear();
        employeeData.addAll(admin.getEmployeeList());
    }

    private void logout() {
        
        LoginController controller = new LoginController();
        new LoginView(stage, controller);
    }
}