package jfxtras.labs.scene.control.scheduler.skin;

import javafx.print.PrinterJob;
import javafx.scene.Node;
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.time.LocalDateTime;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public interface SchedulerSkin {
    /**
     * Complete refresh
     */
    void refresh();

    /**
     * Recreate the events
     */
    void setupEvents();

    /**
     * Recreate events only on participant ResourceBodyPanes
     */
    void setupParticularEvents(long oldResourceId, long newResourceId);

    /**
     *
     * @param x scene coordinate
     * @param y scene coordinate
     * @return a localDateTime equivalent of the click location, where a drop in the day section has nano seconds == 1, and a drop in a header (wholeday) section has nano seconds == 0
     */
    LocalDateTime convertClickInSceneToDateTime(double x, double y);

    long convertClickInSceneToResourceId(double x, double y);

    /**
     * Finds rendered node for event.  The node can be used as the owner for a popup.
     * or finding its x, y coordinates.
     *
     * @param event
     * @return rendered node that represents event
     */
    Node getNodeForPopup(Scheduler.Event event);

    public void print(PrinterJob job);
}
