package electronicstore.view;

import electronicstore.controller.LoginController;
import electronicstore.model.users.AccessLevel;
import electronicstore.model.users.Administrator;
import electronicstore.model.users.Cashier;
import electronicstore.model.users.Manager;
import electronicstore.model.users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class LoginView {
    private Stage stage;
    private LoginController controller;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    public LoginView(Stage stage, LoginController controller) {
        this.stage = stage;
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        
        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background: linear-gradient(to bottom right, #667eea, #764ba2);");

        VBox loginCard = new VBox(25);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setMaxWidth(420);
        loginCard.setMaxHeight(450);
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 50; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 10);");

        HBox branding = electronicstore.view.Branding.createLargeBranding();
        
        Label welcomeLabel = new Label("Welcome Back");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label subtitleLabel = new Label("Sign in to continue");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");

        VBox fieldsBox = new VBox(15);
        fieldsBox.setAlignment(Pos.CENTER);

        VBox usernameBox = new VBox(8);
        Label userNameLabel = new Label("Username");
        userNameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #334155;");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-pref-height: 45; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #e2e8f0; -fx-border-width: 1; -fx-background-color: #f8fafc; -fx-font-size: 14px;");
        usernameField.setPrefWidth(300);
        usernameBox.getChildren().addAll(userNameLabel, usernameField);

        VBox passwordBox = new VBox(8);
        Label pwLabel = new Label("Password");
        pwLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #334155;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-pref-height: 45; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #e2e8f0; -fx-border-width: 1; -fx-background-color: #f8fafc; -fx-font-size: 14px;");
        passwordField.setPrefWidth(300);
        passwordBox.getChildren().addAll(pwLabel, passwordField);

        fieldsBox.getChildren().addAll(usernameBox, passwordBox);

        Button loginButton = new Button("Sign In");
        loginButton.setPrefWidth(300);
        loginButton.setPrefHeight(45);
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");
        loginButton.setOnAction(e -> handleLogin());

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 13px;");

        loginCard.getChildren().addAll(branding, welcomeLabel, subtitleLabel, fieldsBox, loginButton, errorLabel);
        mainContainer.getChildren().add(loginCard);

        Scene scene = new Scene(mainContainer, 600, 500);

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Inventa - Login");
        stage.setMaximized(true);
        stage.show();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = controller.authenticate(username, password);
            if (user != null) {
                openDashboard(user);
            } else {
                errorLabel.setText("Invalid credentials");
            }
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void openDashboard(User user) {
        if (user.getAccessLevel() == AccessLevel.ADMINISTRATOR) {
            new AdminDashboard(stage, (Administrator) user);
        } else if (user.getAccessLevel() == AccessLevel.MANAGER) {
            new ManagerDashboard(stage, (Manager) user);
        } else if (user.getAccessLevel() == AccessLevel.CASHIER) {
            new CashierDashboard(stage, (Cashier) user);
        }
    }
}