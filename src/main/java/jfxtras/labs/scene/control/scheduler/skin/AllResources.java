package jfxtras.labs.scene.control.scheduler.skin;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class AllResources {

    final private ObservableList<Scheduler.Resource> resources;
    final private ListChangeListener<Scheduler.Resource> listChangeListener= new ListChangeListener<Scheduler.Resource>() {

        @Override
        public void onChanged(Change<? extends Scheduler.Resource> c) {
            fireOnChangeListener();
        }
    };

    public AllResources(ObservableList<Scheduler.Resource> resources) {
        this.resources = resources;

        this.resources.addListener(new WeakListChangeListener<>(listChangeListener));
    }


    /**
     * fires when something changes in the events
     */
    public void addOnChangeListener(Runnable runnable) {
        this.runnables.add(runnable);
    }
    public void removeOnChangeListener(Runnable runnable) {
        this.runnables.remove(runnable);
    }
    private List<Runnable> runnables = new ArrayList<>();

    private void fireOnChangeListener() {
        for (Runnable runnable : runnables) {
            runnable.run();
        }
    }

    List<Scheduler.Resource> collectRegular() {
        return resources;
    }


}
