package demo;

import java.util.Random;

/*
 * CGVector defines a vector with x and y components. API Style of references Apple CGVector.
 */
public class CGVector {
    private static Random random = new Random();
    double dx = (random.nextDouble() * 1 + 2) * (random.nextBoolean() ? 1 : -1);
    double dy = (random.nextDouble() * 1 + 2) * (random.nextBoolean() ? 1 : -1);
    double getSpeed() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public CGVector() { }
    public CGVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public CGVector cappedVelocity(double cap1, double cap2) {
        double maxCap = Math.max(cap1, cap2);
        double minCap = Math.min(cap1, cap2);
        return this.minCappedVelocity(minCap).maxCappedVelocity(maxCap);
    }

    public CGVector maxCappedVelocity(double cap) {
        boolean xPositive = this.dx > 0;
        boolean yPositive = this.dy > 0;

        double cappeddx = Math.min(cap, Math.abs(this.dx));
        double cappeddy = Math.min(cap, Math.abs(this.dy));

        CGVector capped = new CGVector(cappeddx * (xPositive ? 1 : -1), cappeddy * (yPositive ? 1 : -1));
        return capped;
    }

    public CGVector minCappedVelocity(double cap) {
        boolean xPositive = this.dx > 0;
        boolean yPositive = this.dy > 0;

        double cappeddx = Math.max(cap, Math.abs(this.dx));
        double cappeddy = Math.max(cap, Math.abs(this.dy));

        CGVector capped = new CGVector(cappeddx * (xPositive ? 1 : -1), cappeddy * (yPositive ? 1 : -1));
        return capped;
    }
}
