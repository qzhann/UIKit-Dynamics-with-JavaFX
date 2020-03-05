package demo;

import javafx.geometry.Bounds;
import javafx.scene.Group;

import java.util.Random;

/*
 * Dynamic Item defines the way that an item interacts with other items and inside behaviors. API Style of references Apple UIDynamicItem.
 */
public abstract class DynamicItem extends Group {
    boolean isAnchored = false;
    static double maxVelocity = 2;
    static double minVelocity = 0.5;
    CGVector velocity = new CGVector();
    static Random random = new Random();

    double getX() {
        return getBounds().getMinX();
    }

    double getY() {
        return getBounds().getMinY();
    }

    void setVelocity(CGVector velocity) {
        // Cap the velocity
        this.velocity = velocity.cappedVelocity(maxVelocity, minVelocity);
    }

    abstract void setPosition(double x, double y);

    abstract Bounds getBounds();

    double getBoundWidth() {
        return getBounds().getWidth();
    }
    double getBoundHeight() {
        return getBounds().getHeight();
    }


    boolean boundsWillCollideWith(DynamicItem other) {
        return getBounds().intersects(other.getBounds());
    }

    void animateTranslationUsingCurrentVelocity(int timeElapsed) {
        if (isAnchored) { return; }

        double x = getBounds().getMinX();
        x += velocity.dx * timeElapsed;
        double y = getY();
        y += velocity.dy * timeElapsed;
        this.setPosition(x, y);
    }
}
