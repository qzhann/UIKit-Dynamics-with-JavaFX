package demo;

/*
 * Push Behavior defines how to animate the translational change of position of its items. API Style of references Apple UIPushBehavior.
 */

public class PushBehavior extends DynamicBehavior {
    enum PushType {
        INSTANTANEOUS, CONTINUOUS;
    }

    PushType pushType;

    /// Animate the items in the behavior
    public void animate(int timeElapsed) {
        for (int i = 0; i < items.size(); i++) {
            if (pushType == PushType.INSTANTANEOUS) {
                items.get(i).animateTranslationUsingCurrentVelocity(timeElapsed);
            }
        }
    }

    public PushBehavior(PushType type) {
        pushType = type;
    }
}
