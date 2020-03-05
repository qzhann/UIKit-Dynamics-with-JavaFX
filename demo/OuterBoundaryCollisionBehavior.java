package demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

/*
 * OuterBoundaryCollisionBehavior defines a collision behavior whose collision boundary encompasses its items.
 */
public class OuterBoundaryCollisionBehavior extends DynamicBehavior {

    double topBoudaryY = 0;
    double bottomBoundaryY = 500;
    double leftBoundaryX = 0;
    double rightBoundaryX = 1000;
    double tolerance = 5;

    Rectangle boundaryRectangle;

    public OuterBoundaryCollisionBehavior(double top, double left, double bottom, double right) {
        super();

        boundaryRectangle = new Rectangle();
        this.getChildren().add(boundaryRectangle);
        setPosition(top, left, bottom, right);

        boundaryRectangle.setFill(Color.BLACK);
        boundaryRectangle.setStroke(Color.RED);
        boundaryRectangle.setStrokeWidth(1);
    }

    @Override
    public void addItem(DynamicItem item) {
        super.addItem(item);
        if (tolerance < item.getBoundWidth()) {
            tolerance = item.getBoundWidth();
        }
    }

    public void setPosition(double top, double left, double bottom, double right) {
        topBoudaryY = top;
        bottomBoundaryY = bottom;
        leftBoundaryX = left;
        rightBoundaryX = right;
        boundaryRectangle.setLayoutX(left);
        boundaryRectangle.setLayoutY(top);
        boundaryRectangle.setWidth(right - left);
        boundaryRectangle.setHeight(bottom - top);
    }

    public void setVisualizeBoundary(boolean value) {
        boundaryRectangle.setVisible(value);
    }

    public void setVisualizationStyle(Color color) {
        boundaryRectangle.setFill(color);
        boundaryRectangle.setStroke(color);
    }

    private static Random random = new Random();

    boolean collidesOnLeftBoundary(double minX, double minY, double maxX, double maxY) {
        double centerX = (maxX + minX) / 2;
        double centerY = (maxY + minY) / 2;
        return (maxX >= leftBoundaryX && minX < leftBoundaryX) && centerY >= topBoudaryY && centerY <= bottomBoundaryY;
    }

    boolean collidesOnRightBoundary(double minX, double minY, double maxX, double maxY) {
        double centerX = (maxX + minX) / 2;
        double centerY = (maxY + minY) / 2;
        return (minX <= rightBoundaryX && maxX > rightBoundaryX) && centerY >= topBoudaryY && centerY <= bottomBoundaryY;
    }

    boolean collidesOnTopBoundary(double minX, double minY, double maxX, double maxY) {
        double centerX = (maxX + minX) / 2;
        double centerY = (maxY + minY) / 2;
        return maxY >= topBoudaryY && minY < topBoudaryY && centerX >= leftBoundaryX && centerX <= rightBoundaryX;
    }

    boolean collidesOnBottomBoundary(double minX, double minY, double maxX, double maxY) {
        double centerX = (maxX + minX) / 2;
        double centerY = (maxY + minY) / 2;
        return minY <= bottomBoundaryY && maxY > bottomBoundaryY && centerX >= leftBoundaryX && centerX <= rightBoundaryX;
    }

    /// Animate the items in the behavior
    public void animate(int timeElapsed) {
        for (int i = 0; i < items.size(); i++) {
            DynamicItem item = items.get(i);
            if (item.isAnchored) { continue; }

            double minX = item.getBounds().getMinX();
            double minY = item.getBounds().getMinY();
            double maxX = item.getBounds().getMaxX();
            double maxY = item.getBounds().getMaxY();

            if (collidesOnLeftBoundary(minX, minY, maxX, maxY)) {
                item.setPosition(leftBoundaryX - tolerance, item.getBounds().getMinY());
                item.setVelocity(new CGVector(-item.velocity.dx, item.velocity.dy + random.nextDouble() * (random.nextBoolean() ? 1 : -1)));
            } else if (collidesOnRightBoundary(minX, minY, maxX, maxY)) {
                item.setPosition(rightBoundaryX, item.getBounds().getMinY());
                item.setVelocity(new CGVector(-item.velocity.dx, item.velocity.dy  + random.nextDouble() * (random.nextBoolean() ? 1 : -1)));
            } else if (collidesOnTopBoundary(minX, minY, maxX, maxY)) {
                item.setPosition(item.getBounds().getMinX(), topBoudaryY - tolerance);
                item.setVelocity(new CGVector(item.velocity.dx + random.nextDouble() * (random.nextBoolean() ? 1 : -1), -item.velocity.dy));
            } else if (collidesOnBottomBoundary(minX, minY, maxX, maxY)) {
                item.setPosition(item.getBounds().getMinX(), bottomBoundaryY);
                item.setVelocity(new CGVector(item.velocity.dx + random.nextDouble() * (random.nextBoolean() ? 1 : -1), -item.velocity.dy));
            }
        }
    }

}
