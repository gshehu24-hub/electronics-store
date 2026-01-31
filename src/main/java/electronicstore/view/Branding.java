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
        Circle circle = new Circle(radius, Color.web("#667eea"));

        SVGPath bolt = new SVGPath();
        bolt.setContent("M13 2 L5 10 L9 10 L7 18 L15 10 L11 10 Z");
        bolt.setFill(Color.web("#fbbf24"));
        bolt.setScaleX(radius / 10.0);
        bolt.setScaleY(radius / 10.0);

        StackPane stack = new StackPane(circle, bolt);
        stack.getStyleClass().add("branding-logo");
        return stack;
    }
}
