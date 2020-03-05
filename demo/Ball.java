package demo;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends DynamicItem {
    Circle circle;
    boolean isBlue;

    Ball(double x, double y, boolean isBlue) {
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.isBlue = isBlue;
        circle = new Circle();
        circle.setCenterX(5);
        circle.setCenterY(5);
        circle.setRadius(5);
        circle.setFill(isBlue? Color.BLUE : Color.RED);
        this.getChildren().add(circle);
    }

    @Override
    void setPosition(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    @Override
    Bounds getBounds() {
        return this.getBoundsInParent();
    }
}
