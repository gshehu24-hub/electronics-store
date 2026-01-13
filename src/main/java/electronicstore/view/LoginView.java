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
        mainContainer.getStyleClass().add("login-container");

        
        VBox loginForm = new VBox(20);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.getStyleClass().add("login-form");
        loginForm.setMaxWidth(400);
        loginForm.setMaxHeight(350);

        
        HBox branding = electronicstore.view.Branding.createLargeBranding();
        branding.getStyleClass().add("login-title");

        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("grid-pane");
        grid.setHgap(15);
        grid.setVgap(15);

        Label userNameLabel = new Label("Username:");
        userNameLabel.getStyleClass().add("label");
        grid.add(userNameLabel, 0, 0);

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("text-field");
        grid.add(usernameField, 1, 0);

        Label pwLabel = new Label("Password:");
        pwLabel.getStyleClass().add("label");
        grid.add(pwLabel, 0, 1);

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("text-field");
        grid.add(passwordField, 1, 1);

        
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().addAll("button", "primary");
        loginButton.setOnAction(e -> handleLogin());

        
        errorLabel = new Label();
        errorLabel.getStyleClass().add("label");
        errorLabel.getStyleClass().add("error");

        loginForm.getChildren().addAll(branding, grid, loginButton, errorLabel);
        mainContainer.getChildren().add(loginForm);

        Scene scene = new Scene(mainContainer, 600, 500);

        
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Inventa - Login");
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