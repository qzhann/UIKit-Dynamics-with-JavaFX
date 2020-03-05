package demo;

import javafx.animation.AnimationTimer;
import java.util.ArrayList;

/*
 * Dynamic Animator manages the start and pause of the animation Timer. API Style of references Apple UIDynamicAnimator.
 */
public class DynamicAnimator {
    private ArrayList<DynamicBehavior> behaviors = new ArrayList<DynamicBehavior>();
    private boolean isFirstTimeAnimation = true;
    private long previousTime = 0;
    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (isFirstTimeAnimation) {
                previousTime = now;
                isFirstTimeAnimation = false;
                return;
            }

            int timeElapsed = (int)(now - previousTime) / 10000000;
            for (DynamicBehavior behavior: behaviors) {
                behavior.animate(timeElapsed);
            }

            previousTime = now;
        }
    };

    public void start() {
        animationTimer.start();
    }

    public void pause() {
        animationTimer.stop();
    }

    public void addBehavior(DynamicBehavior behavior) {
        behaviors.add(behavior);
    }

    public void removeBehavior(DynamicBehavior behavior) {
        behaviors.remove(behavior);
    }
}
