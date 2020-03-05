package demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class InnerBoundaryCollisionBehavior extends DynamicBehavior {
    Rectangle boundaryRectangle;

    public InnerBoundaryCollisionBehavior(double top, double left, double bottom, double right) {
        topWallBoundsY = top;
        bottomWallBoundsY = bottom;
        leftWallBoundsX = left;
        rightWallBoundsX = right;

        boundaryRectangle = new Rectangle();
        this.getChildren().add(boundaryRectangle);
        boundaryRectangle.setLayoutX(left);
        boundaryRectangle.setLayoutY(top);
        boundaryRectangle.setWidth(right - left);
        boundaryRectangle.setHeight(bottom - top);
        boundaryRectangle.setFill(Color.BLACK);
        boundaryRectangle.setStroke(Color.RED);
        boundaryRectangle.setStrokeWidth(1);
    }

    double topWallBoundsY = 0;
    double bottomWallBoundsY = 500;
    double leftWallBoundsX = 0;
    double rightWallBoundsX = 1000;
    double tolerance = 5;

    @Override
    public void addItem(DynamicItem item) {
        super.addItem(item);
        if (tolerance < item.getBoundWidth()) {
            tolerance = item.getBoundWidth();
        }
    }

    public void setVisualizeBoundary(boolean value) {
        boundaryRectangle.setVisible(value);
    }

    public void setCollisionBoundaryInset(int insetTop, int insetLeft, int insetBottom, int insetRight) {
        topWallBoundsY += insetTop;
        bottomWallBoundsY -= insetBottom;
        leftWallBoundsX += insetLeft;
        rightWallBoundsX -= insetRight;
    }

    private static Random random = new Random();


    boolean collidesOnLeftBounds(double minX, double maxX) {
        return (minX <= leftWallBoundsX && maxX > leftWallBoundsX);
    }

    boolean collidesOnRightBounds(double minX, double maxX) {
        return (maxX >= rightWallBoundsX && minX < rightWallBoundsX);
    }

    boolean collidesOnTopBounds(double minY, double maxY) {
        return minY <= topWallBoundsY && maxY > topWallBoundsY;
    }

    boolean collidesOnBottomBounds(double minY, double maxY) {
        return maxY >= bottomWallBoundsY && minY < bottomWallBoundsY;
    }

    /// Animate the items in the behavior
    public void animate(int timeElapsed) {
        // Get the bounds of each pair of items and calculate their new direction if they collide
        for (int i = 0; i < items.size(); i++) {
            DynamicItem item = items.get(i);
            if (item.isAnchored) { continue; }

            double minX = item.getBounds().getMinX();
            double minY = item.getBounds().getMinY();
            double maxX = item.getBounds().getMaxX();
            double maxY = item.getBounds().getMaxY();

            if (collidesOnLeftBounds(minX, maxX)) {
                item.setPosition(leftWallBoundsX, item.getBounds().getMinY());
                item.setVelocity(new CGVector(-item.velocity.dx, item.velocity.dy + random.nextDouble() * (random.nextBoolean() ? 1 : -1)));
            } else if (collidesOnRightBounds(minX, maxX)) {
                item.setPosition(rightWallBoundsX - tolerance, item.getBounds().getMinY());
                item.setVelocity(new CGVector(-item.velocity.dx, item.velocity.dy  + random.nextDouble() * (random.nextBoolean() ? 1 : -1)));
            } else if (collidesOnTopBounds(minY, maxY)) {
                item.setPosition(item.getBounds().getMinX(), topWallBoundsY);
                item.setVelocity(new CGVector(item.velocity.dx + random.nextDouble() * (random.nextBoolean() ? 1 : -1), -item.velocity.dy));
            } else if (collidesOnBottomBounds(minY, maxY)) {
                item.setPosition(item.getBounds().getMinX(), bottomWallBoundsY - tolerance);
                item.setVelocity(new CGVector(item.velocity.dx + random.nextDouble() * (random.nextBoolean() ? 1 : -1), -item.velocity.dy));
            }
        }
    }

}
