package jfxtras.labs.scene.control.scheduler.skin;

import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.labs.scene.control.scheduler.Scheduler;
import jfxtras.util.NodeUtil;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
abstract class EventAbstractPane extends Pane {

    final protected Scheduler.Event event;
    final protected LayoutHelp layoutHelp;
    final protected HistoricalVisualizer historyVisualizer;
    final protected EventMenu eventMenu;

    EventAbstractPane(Scheduler.Event event, LayoutHelp layoutHelp) {
        this.event = event;
        this.layoutHelp = layoutHelp;
        eventMenu = new EventMenu(this, event, layoutHelp);

        // for debugging setStyle("-fx-border-color:PINK;-fx-border-width:1px;");
        getStyleClass().add("Event");
        getStyleClass().add("group5");

        // historical visualizer
        historyVisualizer = new HistoricalVisualizer(this);
        getChildren().add(historyVisualizer);

        // tooltip
        Tooltip tooltip = new Tooltip(
                "Start time: \n" + event.getStartTime().toString()
                        + "\nEnd Time:\n" + event.getEndTime().toString());
        tooltip.setFont(new Font(12));
        if (event.getId() != null) {
            Tooltip.install(this, tooltip);
        }

        // dragging
        setupDragging();

        // react to changes in the selected events
        layoutHelp.skinnable.selectedEvents().addListener(new WeakListChangeListener<>(listChangeListener));
    }


    final private ListChangeListener<Scheduler.Event> listChangeListener = new ListChangeListener<Scheduler.Event>() {
        @Override
        public void onChanged(javafx.collections.ListChangeListener.Change<? extends Scheduler.Event> changes) {
            setOrRemoveSelected();
        }
    };

    /**
     *
     */
    private void setOrRemoveSelected() {
        // remove class if not selected
        if (getStyleClass().contains(SELECTED) == true // visually selected
                && layoutHelp.skinnable.selectedEvents().contains(event) == false // but no longer in the selected collection
                ) {
            getStyleClass().remove(SELECTED);
        }

        // add class if selected
        if (getStyleClass().contains(SELECTED) == false // visually not selected
                && layoutHelp.skinnable.selectedEvents().contains(event) == true // but still in the selected collection
                ) {
            getStyleClass().add(SELECTED);
        }
    }

    private static final String SELECTED = "Selected";

    /**
     * @param now
     */
    void determineHistoryVisualizer(LocalDateTime now) {
        historyVisualizer.setVisible(event.getStartTime().isBefore(now));
    }

    /**
     *
     */
    private void setupDragging() {
        // start drag
        setOnMousePressed((mouseEvent) -> {
            // action without select: middle button
            if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
                handleAction();
                return;
            }
            // popup: right button
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                eventMenu.showMenu(mouseEvent);
                return;
            }
            // only on primary
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
                return;
            }

            // we handle this event
            mouseEvent.consume();

            // if this an action
            if (mouseEvent.getClickCount() > 1) {
                handleAction();
                return;
            }

            // is dragging allowed
            if (layoutHelp.skinnable.getAllowDragging() == false) {
                handleSelect(mouseEvent);
                return;
            }

            // remember
            startX = mouseEvent.getX();
            startY = mouseEvent.getY();
            dragPickupDateTime = layoutHelp.skin.convertClickInSceneToDateTime(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            mouseActuallyHasDragged = false;
            dragging = true;
        });

        // visualize dragging
        setOnMouseDragged((mouseEvent) -> {
            if (dragging == false) {
                return;
            }

            // we handle this event
            mouseEvent.consume();

            // show the drag rectangle when we actually drag
            if (dragRectangle == null) {
                setCursor(Cursor.MOVE);
                // TODO: when dropping an event overlapping the day edge, the event is correctly(?) split in two. When dragging up such a splitted event the visualization does not match the actual time
                dragRectangle = new Rectangle(0, 0, NodeUtil.snapWH(0, getWidth()), NodeUtil.snapWH(0, getHeight()));
                dragRectangle.getStyleClass().add("GhostRectangle");
                layoutHelp.dragPane.getChildren().add(dragRectangle);

                // place a text node at the bottom of the resize rectangle
                startTimeText = new Text("...");
                startTimeText.getStyleClass().add("GhostRectangleText");
                if (showStartTimeText()) {
                    layoutHelp.dragPane.getChildren().add(startTimeText);
                }
                endTimeText = new Text("...");
                endTimeText.getStyleClass().add("GhostRectangleText");
                if (showEndTimeText()) {
                    layoutHelp.dragPane.getChildren().add(endTimeText);
                }
                // we use a clone for calculating the current time during the drag
                eventForDrag = new EventImplForDrag();
            }

            // move the drag rectangle
            double lX = NodeUtil.xInParent(this, layoutHelp.dragPane) + (mouseEvent.getX() - startX); // top-left of the original event pane + offset of drag
            double lY = NodeUtil.yInParent(this, layoutHelp.dragPane) + (mouseEvent.getY() - startY); // top-left of the original event pane + offset of drag
            dragRectangle.setX(NodeUtil.snapXY(lX));
            dragRectangle.setY(NodeUtil.snapXY(lY));
            startTimeText.layoutXProperty().set(dragRectangle.getX());
            startTimeText.layoutYProperty().set(dragRectangle.getY());
            endTimeText.layoutXProperty().set(dragRectangle.getX());
            endTimeText.layoutYProperty().set(dragRectangle.getY() + dragRectangle.getHeight() + endTimeText.getBoundsInParent().getHeight());
            mouseActuallyHasDragged = true;

            // update the start time
            eventForDrag.setStartTime(event.getStartTime());
            eventForDrag.setEndTime(event.getEndTime());
            // determine start and end DateTime of the drag
            LocalDateTime dragCurrentDateTime = layoutHelp.skin.convertClickInSceneToDateTime(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            long newResourceId = layoutHelp.skin.convertClickInSceneToResourceId(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            if (dragCurrentDateTime != null) { // not dropped somewhere outside
                handleDrag(eventForDrag, dragPickupDateTime, dragCurrentDateTime, newResourceId);
                startTimeText.setText(layoutHelp.timeDateTimeFormatter.format(eventForDrag.getStartTime()));
                endTimeText.setText(layoutHelp.timeDateTimeFormatter.format(eventForDrag.getEndTime()));
            }

        });

        // end drag
        setOnMouseReleased((mouseEvent) -> {
            if (dragging == false) {
                return;
            }

            // we handle this event
            mouseEvent.consume();
            dragging = false;

            // reset ui
            setCursor(Cursor.HAND);
            if (dragRectangle != null) {
                layoutHelp.dragPane.getChildren().remove(dragRectangle);
                layoutHelp.dragPane.getChildren().remove(startTimeText);
                layoutHelp.dragPane.getChildren().remove(endTimeText);
                dragRectangle = null;
                startTimeText = null;
                endTimeText = null;
                eventForDrag = null;
            }

            // if not dragged, then we're selecting
            if (mouseActuallyHasDragged == false) {
                handleSelect(mouseEvent);
                return;
            }

            // determine start and end DateTime of the drag
            LocalDateTime dragDropDateTime = layoutHelp.skin.convertClickInSceneToDateTime(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            long newResourceId = layoutHelp.skin.convertClickInSceneToResourceId(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            if (dragDropDateTime != null) { // not dropped somewhere outside
                System.err.println(" Drop resourceId " + newResourceId);
                Long oldResourceId = Long.valueOf(event.getResourceId());
                handleDrag(event, dragPickupDateTime, dragDropDateTime, newResourceId);
                layoutHelp.skin.setupParticularEvents(oldResourceId, newResourceId);
            }
        });
    }

    private boolean dragging = false;
    private Rectangle dragRectangle = null;
    private double startX = 0;
    private double startY = 0;
    private LocalDateTime dragPickupDateTime;
    private boolean mouseActuallyHasDragged = false;
    private final int roundToMinutes = 5;
    private Text startTimeText = null;
    private Text endTimeText = null;
    private Scheduler.Event eventForDrag = null;

    public static class EventImplForDrag extends Scheduler.EventImpl {

    }

    protected boolean showStartTimeText() {
        return true;
    }

    protected boolean showEndTimeText() {
        return true;
    }

    /**
     *
     */
    private void handleDrag(Scheduler.Event event, LocalDateTime dragPickupDateTime, LocalDateTime dragDropDateTime, long newResourceId) {

        // drag start
        boolean dragPickupInDayBody = dragInDayBody(dragPickupDateTime);
        dragPickupDateTime = layoutHelp.roundTimeToNearestMinutes(dragPickupDateTime, roundToMinutes);

        // drag end
        boolean dragDropInDayBody = dragInDayBody(dragDropDateTime);
        dragDropDateTime = layoutHelp.roundTimeToNearestMinutes(dragDropDateTime, roundToMinutes);

        // if dragged from day to day or header to header
        if ((dragPickupInDayBody && dragDropInDayBody)) {
            // simply add the duration
            boolean changed = false;
            Duration duration = Duration.between(dragPickupDateTime, dragDropDateTime);
            if (event.getStartTime() != null) {
                event.setStartTime(event.getStartTime().plus(duration));
                changed = true;
            }
            if (event.getEndTime() != null) {
                event.setEndTime(event.getEndTime().plus(duration));
                changed = true;
            }
            if (newResourceId != -1) {
                event.setResourceId(newResourceId);
            }
            if (changed) {
                layoutHelp.callEventChangedCallback(event);
            }
        }
    }


    /**
     *
     */
    private void handleSelect(MouseEvent mouseEvent) {
        // if not shift pressed, clear the selection
        if (mouseEvent.isShiftDown() == false && mouseEvent.isControlDown() == false) {
            layoutHelp.skinnable.selectedEvents().clear();
        }

        // add to selection if not already added
        if (layoutHelp.skinnable.selectedEvents().contains(event) == false) {
            layoutHelp.skinnable.selectedEvents().add(event);
        }
        // pressing control allows to toggle
        else if (mouseEvent.isControlDown()) {
            layoutHelp.skinnable.selectedEvents().remove(event);
        }
    }

    /**
     *
     */
    private void handleAction() {
        // has the client registered an action
        Callback<Scheduler.Event, Void> lCallback = layoutHelp.skinnable.getActionCallback();
        if (lCallback != null) {
            lCallback.call(event);
            return;
        }
    }

    /**
     * @param localDateTime
     * @return
     */
    private boolean dragInDayBody(LocalDateTime localDateTime) {
        return localDateTime.getNano() == DRAG_DAY;
    }


    static final int DRAG_DAY = 1;

    /**
     *
     */
    public String toString() {
        return "event=" + event.getStartTime() + "-" + event.getEndTime()
                + ";"
                + "sumary=" + event.getText()
                ;
    }
}
