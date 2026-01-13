package electronicstore.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

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

    public static Button createIconButton(String glyph, String tooltipText, double radius, String bgColor) {
        StackPane icon = createGlyphIcon(glyph, radius, bgColor);
        Button btn = new Button();
        btn.setGraphic(icon);
        btn.getStyleClass().add("icon-button");
        if (tooltipText != null && !tooltipText.isEmpty()) {
            Tooltip.install(btn, new Tooltip(tooltipText));
        }
        return btn;
    }

    public static StackPane createGlyphIcon(String glyph, double radius, String bgColor) {
        Circle circle = new Circle(radius, Color.web(bgColor));
        Label glyphLabel = new Label(glyph);
        glyphLabel.setTextFill(Color.WHITE);
        // size glyph to fit comfortably inside the circle
        double fontSize = Math.max(10, radius * 0.9);
        glyphLabel.setFont(Font.font(fontSize));
        StackPane stack = new StackPane(circle, glyphLabel);
        stack.setPrefSize(radius * 2.0, radius * 2.0);
        stack.setMaxSize(radius * 2.0, radius * 2.0);
        stack.setMinSize(radius * 2.0, radius * 2.0);
        stack.setAlignment(javafx.geometry.Pos.CENTER);
        stack.getStyleClass().add("branding-logo");
        return stack;
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
