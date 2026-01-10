package electronicstore.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.control.Label;

public class Branding {
    public static HBox createLargeBranding() {
        StackPane logo = createLogoIcon(26);
        Label name = new Label("Inventa");
        name.getStyleClass().add("branding-name");
        HBox box = new HBox(10, logo, name);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    public static Node createSmallIcon() {
        return createLogoIcon(10);
    }

    private static StackPane createLogoIcon(double radius) {
        Circle circle = new Circle(radius, Color.web("#2E86AB"));

        
        SVGPath cart = new SVGPath();
        cart.setContent("M3 3 L5 3 L7 9 L13 9 L14 6 L6 6 L5 3 M6 11 A1 1 0 1 0 6 13 A1 1 0 1 0 6 11 M11 11 A1 1 0 1 0 11 13 A1 1 0 1 0 11 11");
        cart.setFill(Color.WHITE);
        cart.setScaleX(radius / 8.0);
        cart.setScaleY(radius / 8.0);

        StackPane stack = new StackPane(circle, cart);
        stack.getStyleClass().add("branding-logo");
        return stack;
    }
}
