package demo;

import javafx.scene.layout.Pane;
import java.util.ArrayList;

/*
 * Dynamic Behavior defines how its items should interact with the environment and and each other. API style references Apple UIDyanamicBehavior.
 */
public abstract class DynamicBehavior extends Pane {
    protected ArrayList<DynamicItem> items = new ArrayList<DynamicItem>();

    public void addItem(DynamicItem item) {
        items.add(item);
    }

    public abstract void animate(int timeElapsed);
}