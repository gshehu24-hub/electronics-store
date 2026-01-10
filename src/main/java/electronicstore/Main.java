package electronicstore;

import electronicstore.controller.LoginController;
import electronicstore.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginController controller = new LoginController();
        new LoginView(primaryStage, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}