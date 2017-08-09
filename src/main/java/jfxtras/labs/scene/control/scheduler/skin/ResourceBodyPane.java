package jfxtras.labs.scene.control.scheduler.skin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jfxtras.labs.scene.control.scheduler.Scheduler;
import jfxtras.util.NodeUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class ResourceBodyPane extends Pane {

    final AllEvents allEvents;
    final LayoutHelp layoutHelp;
    final Scheduler.Resource resource;
    List<LocalDate> displayedLocalDates;
    ObjectProperty<LocalDate> minDateObjectProperty = new SimpleObjectProperty<>();
    ObjectProperty<LocalDate> maxDateObjectProperty = new SimpleObjectProperty<>();


    public ResourceBodyPane(List<LocalDate> displayedLocalDates, Scheduler.Resource resource, AllEvents allEvents, LayoutHelp layoutHelp) {
        this.allEvents = allEvents;
        this.layoutHelp = layoutHelp;
        this.resource = resource;
        this.displayedLocalDates = displayedLocalDates;

        setDisplayedLocalDates(displayedLocalDates);

        construct();
    }

    void setDisplayedLocalDates(List<LocalDate> displayedLocalDates) {
        this.displayedLocalDates = displayedLocalDates;

        calculateMinMaxDates();
    }

    ;

    private void calculateMinMaxDates() {
        final Comparator<LocalDate> comp = (p1, p2) -> p1.compareTo(p2);
        minDateObjectProperty.setValue(displayedLocalDates.stream().min(comp).get());
        maxDateObjectProperty.setValue(displayedLocalDates.stream().max(comp).get());
    }

    private void construct() {
        // for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
//        setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
        setId("ResourceBodyPane" + resource.getId()); // for testing

        allEvents.addOnChangeListener(() -> {
//            setupEvents();
        });
        setupEvents();

        widthProperty().addListener((observable -> {
            relayout();
        }));
        heightProperty().addListener((observable -> {
            relayout();
        }));

        setupMouseDrag();

/*        setCacheHint(CacheHint.QUALITY);
        setCache(true);
        setCacheShape(true);*/
    }

    private Rectangle resizeRectangle = null;
    private boolean dragged = false;

    private void setupMouseDrag() {
        // start new appointment
        setOnMousePressed((mouseEvent) -> {
            // only on primary
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
                return;
            }
            // if there is no one to handle the result, don't even bother
            if (layoutHelp.skinnable.newEventCallbackProperty().get() == null) {
                return;
            }

            // show the rectangle
            setCursor(Cursor.H_RESIZE);
            double lX = NodeUtil.snapXY(mouseEvent.getScreenX() - NodeUtil.screenX(ResourceBodyPane.this));
            resizeRectangle = new Rectangle(lX, 0, 10, layoutHelp.resourceHeightProperty.get());
            resizeRectangle.getStyleClass().add("GhostRectangle");
            getChildren().add(resizeRectangle);

            // this event should not be processed by the appointment area
            mouseEvent.consume();
            dragged = false;
            layoutHelp.skinnable.selectedEvents().clear();
        });
        // visualize resize
        setOnMouseDragged((mouseEvent) -> {
            if (resizeRectangle == null) {
                return;
            }

            // - calculate the number of pixels from onscreen nodeY (layoutY) to onscreen mouseY
            double lWidth = mouseEvent.getScreenX() - NodeUtil.screenX(resizeRectangle);
            if (lWidth < 5) {
                lWidth = 5;
            }
            resizeRectangle.setWidth(lWidth);

            // no one else
            mouseEvent.consume();
            dragged = true;
        });
        // end resize
        setOnMouseReleased((mouseEvent) -> {
            if (resizeRectangle == null) {
                return;
            }

            // no one else
            mouseEvent.consume();

            // reset ui
            setCursor(Cursor.HAND);
            getChildren().remove(resizeRectangle);

            // must have dragged (otherwise it is considered an "unselect all" action)
            if (dragged == false) {
                return;
            }


            LocalDateTime lStartDateTime = convertClickInSceneToDateTime(mouseEvent.getX(), mouseEvent.getY());
            if (lStartDateTime != null) {
                lStartDateTime = lStartDateTime.plusSeconds((int) (resizeRectangle.getX() * layoutHelp.durationInMSPerPixelProperty.get() / 1000));
                lStartDateTime = layoutHelp.roundTimeToNearestMinutes(lStartDateTime, (int) ((SchedulerSkinAbstract<?>) layoutHelp.skin).getSnapToMinutes());

                // calculate the new end date for the appointment (recalculating the duration)
                LocalDateTime lEndDateTime = lStartDateTime.plusSeconds((int) (resizeRectangle.getWidth() * layoutHelp.durationInMSPerPixelProperty.get() / 1000));
                lEndDateTime = layoutHelp.roundTimeToNearestMinutes(lEndDateTime, (int) ((SchedulerSkinAbstract<?>) layoutHelp.skin).getSnapToMinutes());

                // ask the control to create a new appointment (null may be returned)
                Scheduler.Event lEvent = null;
                if (layoutHelp.skinnable.newEventCallbackProperty().get() != null) {
                    lEvent = layoutHelp.skinnable.newEventCallbackProperty().get().call(new Scheduler.LocalDateTimeRange(lStartDateTime, lEndDateTime));
                }

                if (lEvent != null) {
                    layoutHelp.skinnable.events().add(lEvent); // the appointments collection is listened to, so they will automatically be refreshed
                }
            }

            // calculate the starttime
/*
            LocalDateTime lStartDateTime = localDateObjectProperty.get().atStartOfDay();
            lStartDateTime = lStartDateTime.plusSeconds( (int)(resizeRectangle.getX() * layoutHelp.durationInMSPerPixelProperty.get() / 1000) );
            lStartDateTime = layoutHelp.roundTimeToNearestMinutes(lStartDateTime, (int)((SchedulerSkinAbstract<?>)layoutHelp.skin).getSnapToMinutes());

            // calculate the new end date for the appointment (recalculating the duration)
            LocalDateTime lEndDateTime = lStartDateTime.plusSeconds( (int)(resizeRectangle.getWidth() * layoutHelp.durationInMSPerPixelProperty.get() / 1000) );
            lEndDateTime = layoutHelp.roundTimeToNearestMinutes(lEndDateTime, (int)((SchedulerSkinAbstract<?>)layoutHelp.skin).getSnapToMinutes());
*/

            // clean up
            resizeRectangle = null;


/*            if (layoutHelp.skinnable.createAppointmentCallbackProperty().get() != null) {
                lEvent = layoutHelp.skinnable.createAppointmentCallbackProperty().get().call(new Agenda.CalendarRange(DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lStartDateTime, TimeZone.getDefault(), Locale.getDefault()), DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lEndDateTime, TimeZone.getDefault(), Locale.getDefault())));
            }*/

        });
    }

    private void relayout() {

        double lNumberOfPixelsPerMinute = layoutHelp.resourceWidthProperty.get() / (24 * 60 * displayedLocalDates.size());

        for (EventAbstractTrackedPane lEventAbstractTrackedPane : trackedEventBodyPanes) {

            // for this pane specifically
            double lNumberOfTracks = (double) lEventAbstractTrackedPane.clusterOwner.clusterTracks.size();
            double lTrackHeight = layoutHelp.resourceHeightProperty.get() / lNumberOfTracks;
            double lTrackIdx = (double) lEventAbstractTrackedPane.clusterTrackIdx;


            double lY = (lTrackHeight * lTrackIdx);
            lEventAbstractTrackedPane.setLayoutY(NodeUtil.snapXY(lY));

            // the Y is determined by the start time in minutes projected onto the total day height (being 24 hours)
//            int lStartOffsetInMinutes = displayedLocalDates.size() * ((lEventAbstractTrackedPane.startDateTime.getHour() * 60) + lEventAbstractTrackedPane.startDateTime.getMinute());
//            double lX = lNumberOfPixelsPerMinute * lStartOffsetInMinutes;
            double lX = getPositionByLocalDateTime(lEventAbstractTrackedPane.startDateTime);
            lEventAbstractTrackedPane.setLayoutX(NodeUtil.snapXY(lX));

            // the width is the remaining width (subtracting the wholeday appointments) divided by the number of tracks in the cluster
            double lH = lTrackHeight;
            // all but the most right appointment get 50% extra width, so they underlap the next track
            if (lTrackIdx < lNumberOfTracks - 1) {
                lH *= 1.75;
            }
            lEventAbstractTrackedPane.setPrefHeight(NodeUtil.snapWH(lEventAbstractTrackedPane.getLayoutY(), lH));

            // the height is determined by the duration projected against the total dayHeight (being 24 hours)
            double lW;

            long lWidthInMinutes = lEventAbstractTrackedPane.durationInMS / 1000 / 60;
            lW = lNumberOfPixelsPerMinute * lWidthInMinutes;

            // if start date of event < that minimal displayed date, then subtract appropriate amount of width
            if  (lEventAbstractTrackedPane.startDateTime.isBefore(minDateObjectProperty.get().atStartOfDay())) {
                Duration duration = Duration.between(lEventAbstractTrackedPane.startDateTime, minDateObjectProperty.get().atStartOfDay());
                long seconds = duration.getSeconds();
                lW -= (seconds / 60) * lNumberOfPixelsPerMinute;
            }

            // the width has a minimum size, in order to be able to render sensibly
            if (lW < 2 * layoutHelp.paddingProperty.get()) {
                lW = 2 * layoutHelp.paddingProperty.get();
            }

            lEventAbstractTrackedPane.setPrefWidth(NodeUtil.snapWH(lEventAbstractTrackedPane.getLayoutX(), lW));
        }
    }

    final private List<Scheduler.Event> regularEvents = new ArrayList<>();
    final private List<EventRegularBodyPane> regularEventBodyPanes = new ArrayList<>();

    void setupEvents() {
        setupRegularEvents();

        trackedEventBodyPanes.clear();
        trackedEventBodyPanes.addAll(regularEventBodyPanes);
        List<? extends EventAbstractTrackedPane> determineTracks = EventRegularBodyPane.determineTracks(trackedEventBodyPanes);
        // add the appointments to the pane in the correct order, so they overlap nicely
        getChildren().removeAll(determineTracks);
        getChildren().addAll(determineTracks);

        relayout();

    }

    final List<EventAbstractTrackedPane> trackedEventBodyPanes = new ArrayList<>();

    private void setupRegularEvents() {
        regularEvents.clear();
        regularEvents.addAll(allEvents.collectRegularForResourceAndDates(resource.getId(), minDateObjectProperty.get(), maxDateObjectProperty.get()));

        // remove all events
        getChildren().removeAll(regularEventBodyPanes);
        regularEventBodyPanes.clear();

        // for all regular events of this resource, create a header event pane
        int lCnt = 0;
        for (Scheduler.Event lEvent : regularEvents) {
            EventRegularBodyPane lEventPane = new EventRegularBodyPane(lEvent, layoutHelp);
            regularEventBodyPanes.add(lEventPane);
            ((SchedulerSkinAbstract<Scheduler.Event>) layoutHelp.skin).eventNodeMap().put(System.identityHashCode(lEvent), lEventPane);
            lEventPane.setId(lEventPane.getClass().getSimpleName() + resource.getId() + "/" + lCnt); // for testing

            lCnt++;
        }
    }


    /**
     * @param x
     * @param y
     * @return
     */
    LocalDateTime convertClickInSceneToDateTime(double x, double y) {
        Rectangle r = new Rectangle(sceneX(this), sceneY(this), this.getWidth(), this.getHeight());
        if (r.contains(x, y)) {
            double lWidthOffset = (x - r.getX());

            if (lWidthOffset > layoutHelp.dayWidthProperty.get()) {
                int dayCountOffset = (int) (lWidthOffset / layoutHelp.dayWidthProperty.get());
                double pixelsOffsetOfOneDay = lWidthOffset - (dayCountOffset * layoutHelp.dayWidthProperty.get());
                int ms = (int) (pixelsOffsetOfOneDay * layoutHelp.durationInMSPerPixelProperty.get());
                LocalDate day = displayedLocalDates.get(0).plusDays(dayCountOffset);
                LocalDateTime localDateTime = day.atStartOfDay().plusSeconds(ms / 1000L);
                localDateTime = localDateTime.withNano(EventAbstractPane.DRAG_DAY); // we abuse the nano second to deviate body panes from header panes

                return localDateTime;
            } else {
                int ms = (int) (lWidthOffset * layoutHelp.durationInMSPerPixelProperty.get());
                LocalDateTime localDateTime = displayedLocalDates.get(0).atStartOfDay().plusSeconds(ms / 1000);
                localDateTime = localDateTime.withNano(EventAbstractPane.DRAG_DAY); // we abuse the nano second to deviate body panes from header panes

                return localDateTime;
            }
        }
        return null;
    }

    /**
     * For now line
     *
     * @return
     */
    int getCurrentTimeLocationInScene() {
        // seconds passed from midnight
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.truncatedTo(ChronoUnit.DAYS);
        Duration duration = Duration.between(midnight, now);
        long secondsPassed = duration.getSeconds();

        for (int i = 0; i < displayedLocalDates.size(); i++) {
            if (displayedLocalDates.get(i).equals(LocalDate.now())) {
                int todayOffset = (int) (secondsPassed / (layoutHelp.durationInMSPerPixelProperty.get() / 1000));
                return (int) ((layoutHelp.dayWidthProperty.get() * i) + this.layoutXProperty().get()) + todayOffset;
            }
        }
        return 0;
    }

    /**
     * Determine layoutX by LocalDateTime
     *
     * @param localDateTime
     * @return
     */
    double getPositionByLocalDateTime(LocalDateTime localDateTime) {
        // seconds passed from midnight to argument value
        LocalDateTime midnight = localDateTime.truncatedTo(ChronoUnit.DAYS);
        Duration duration = Duration.between(midnight, localDateTime);
        long secondsPassed = duration.getSeconds();

/*        if (localDateTime.toLocalDate().isBefore(minDateObjectProperty.get())) {
            int daysBetween = Period.between(localDateTime.toLocalDate(), minDateObjectProperty.get()).getDays();
            long todayOffset = (long) (secondsPassed / (layoutHelp.durationInMSPerPixelProperty.get() / 1000));
            return -((layoutHelp.dayWidthProperty.get() * daysBetween - todayOffset) );
        }*/

        for (int i = 0; i < displayedLocalDates.size(); i++) {
            if (displayedLocalDates.get(i).getDayOfYear() == localDateTime.getDayOfYear()) {
                long todayOffset = (long) (secondsPassed / (layoutHelp.durationInMSPerPixelProperty.get() / 1000));
                return (layoutHelp.dayWidthProperty.get() * i) + todayOffset;
            }
        }

        return 0.0;
    }


    private double sceneX(Node node) {
        return node.localToScene(node.getBoundsInLocal()).getMinX() + node.getScene().getX();
    }

    private double sceneY(Node node) {
        return node.localToScene(node.getBoundsInLocal()).getMinY() + node.getScene().getY() - layoutHelp.headerHeightProperty.get();
    }

    public String toString() {
        return "Resource[x=" + this.getLayoutX() + ", y=" + this.getLayoutY() + ", width=" + this.widthProperty().getValue() + ", height=" + this.heightProperty().getValue() + "]";
    }

    public long convertClickInSceneToResourceId(double x, double y) {
        Rectangle r = new Rectangle(sceneX(this), sceneY(this), this.getWidth(), this.getHeight());
        if (r.contains(x, y)) {
            return this.resource.getId();
        }

        return 0;
    }
}
