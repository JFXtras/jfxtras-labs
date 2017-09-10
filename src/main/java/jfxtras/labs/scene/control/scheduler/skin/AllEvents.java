package jfxtras.labs.scene.control.scheduler.skin;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import jfxtras.labs.scene.control.scheduler.Scheduler.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class AllEvents {
    /**
     *
     */
    public AllEvents(ObservableList<Event> events) {
        this.events = events;

        events.addListener(new WeakListChangeListener<>(listChangeListener));

    }

    final private ObservableList<Event> events;
    final private ListChangeListener<Event> listChangeListener = new ListChangeListener<Event>() {
        @Override
        public void onChanged(javafx.collections.ListChangeListener.Change<? extends Event> changes) {
            fireOnChangeListener();
        }
    };

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
//            runnable.run();
            Platform.runLater(runnable);
        }
    }

    /**
     * @param resourceId
     * @return
     */
    public List<Event> collectRegularForResourceAndDates(long resourceId, LocalDate minDate, LocalDate maxDate) {
        List<Event> collectedEvents =  events.parallelStream().filter(c ->
                c.getResourceId().equals(resourceId)
    &&(  ( (c.getStartTime().toLocalDate().isAfter(minDate) || c.getStartTime().toLocalDate().isEqual(minDate))
                        && (c.getStartTime().toLocalDate().isBefore(maxDate) || c.getStartTime().toLocalDate().isEqual(maxDate))  )

    || ((c.getEndTime().toLocalDate().isBefore(maxDate) || c.getEndTime().toLocalDate().isEqual(maxDate))
                        && (c.getEndTime().toLocalDate().isAfter(minDate) || c.getEndTime().toLocalDate().isEqual(minDate))))   )
                .collect(Collectors.toList());
/*
        // scan all events and filter the ones for this resource
        for (Event lEvent : events) {
            if (lEvent.getResourceId().equals(resourceId)) {
                collectedEvents.add(lEvent);
            }

        }*/
        return collectedEvents;
    }
}
