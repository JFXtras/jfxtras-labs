package jfxtras.labs.scene.control.scheduler.skin;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.css.converters.DoubleConverter;
import jfxtras.labs.scene.control.scheduler.Scheduler;
import jfxtras.util.NodeUtil;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public abstract class SchedulerSkinAbstract<T> extends SkinBase<Scheduler> implements SchedulerSkin {
    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */

    protected final Scheduler control;
    AllEvents events;
    AllResources resources;

    protected BorderPane borderPane = null;
    private WeekHeaderPane weekHeaderPane = null;
    private ScrollPane weekScrollPane = null;
    private WeekBodyPane weekBodyPane = null;

    private LayoutHelp layoutHelp = new LayoutHelp(getSkinnable(), this);

    protected SchedulerSkinAbstract(Scheduler control) {
        super(control);
        this.control = control;
        construct();
    }

    /**
     * Reconstruct the UI part
     */
    protected void reconstruct() {
        weekBodyPane.reconstruct();
        weekHeaderPane.reconstruct();

        // initial setup
        refresh();
    }

    /*
     * construct the component
     */
    private void construct() {
        resources = new AllResources(getSkinnable().resources());
        events = new AllEvents(getSkinnable().events());

        // setup component
        createNodes();

        // react to changes in the locale
        getSkinnable().localeProperty().addListener(localeInvalidationListener);

        // react to changes in the displayed calendar
        getSkinnable().displayedLocalDateTime().addListener(displayedDateTimeChangeListener);

        // react to changes in the appointments
        getSkinnable().events().addListener(eventListChangeListener);

        // react to changes in the resources
        getSkinnable().resources().addListener(resourceListChangeListener);

        // clean up removed appointments from eventNodeMap
        getSkinnable().events().addListener(eventNodeMapCleanUpListChangeListener);

        // initial setup
        refresh();
    }

    private InvalidationListener localeInvalidationListener = (observable) -> {
        refresh();
    };
    private ChangeListener<? super LocalDateTime> displayedDateTimeChangeListener = (observable, oldSelection, newSelection) -> {
        assignDateToDayAndHeaderPanes();
        scrollWeekpaneToShowDisplayedTime();
        setupEvents();
    };
    private ListChangeListener<Scheduler.Event> eventListChangeListener = (changes) -> {
        if (changes.next()) {
            if (changes.wasRemoved()) {
                changes.getRemoved().forEach(c-> setupParticularEvents(c.getResourceId(), c.getResourceId()));
                return;
            }
        }
        setupEvents();
    };
    private ListChangeListener<Scheduler.Event> eventNodeMapCleanUpListChangeListener = (changes) -> {
        while (changes.next()) {
            if (changes.wasRemoved()) {
                changes.getRemoved().stream().forEach(a -> eventNodeMap().remove(System.identityHashCode(a)));
            }
        }
    };
    private ListChangeListener<Scheduler.Resource> resourceListChangeListener = (changes) -> {
        weekBodyPane.reconstruct();
        setupEvents();
        layoutHelp.resourcesCountProperty.set(resources.collectRegular().size());
    };


    /**
     *
     */
    public void dispose() {

        // remove listeners
        getSkinnable().localeProperty().removeListener(localeInvalidationListener);
        getSkinnable().displayedLocalDateTime().removeListener(displayedDateTimeChangeListener);
        getSkinnable().events().removeListener(eventListChangeListener);
        getSkinnable().events().removeListener(eventNodeMapCleanUpListChangeListener);

        // reset style classes
        getSkinnable().getStyleClass().clear();
        getSkinnable().getStyleClass().add(Scheduler.class.getSimpleName());

        // continue
        super.dispose();
    }

    /**
     * Assign a calendar to each day, so it knows what it must draw.
     */
    private void assignDateToDayAndHeaderPanes() {
        // assign it to each day pane
        int i = 0;
        List<LocalDate> lLocalDates = determineDisplayedLocalDates();
        for (LocalDate lLocalDate : lLocalDates) {
            // set the calendar
            DayBodyPane lDayPane = weekBodyPane.dayBodyPanes.get(i);
            lDayPane.localDateObjectProperty.set(lLocalDate);
            DayHeaderPane lDayHeaderPane = weekHeaderPane.dayHeaderPanes.get(i);
            lDayHeaderPane.localDateObjectProperty.set(lLocalDate);
            i++;
        }

        for (ResourceBodyPane lResourceBodyPane : weekBodyPane.resourceBodyPanes) {
            lResourceBodyPane.setDisplayedLocalDates(lLocalDates);
        }

        // place the now line
        nowUpdateRunnable.run();

        // tell the control what range is displayed, so it can update the appointments
        LocalDate lStartLocalDate = lLocalDates.get(0);
        LocalDate lEndLocalDate = lLocalDates.get(lLocalDates.size() - 1);
        if (getSkinnable().getLocalDateTimeRangeCallback() != null) {
            Scheduler.LocalDateTimeRange lRange = new Scheduler.LocalDateTimeRange(lStartLocalDate.atStartOfDay(), lEndLocalDate.plusDays(1).atStartOfDay());
            getSkinnable().getLocalDateTimeRangeCallback().call(lRange);
        }
/*        if (getSkinnable().getCalendarRangeCallback() != null) {
            Agenda.CalendarRange lRange = new Agenda.CalendarRange( DateTimeToCalendarHelper.createCalendarFromLocalDate(lStartLocalDate, TimeZone.getDefault(), Locale.getDefault()), DateTimeToCalendarHelper.createCalendarFromLocalDate(lEndLocalDate, TimeZone.getDefault(), Locale.getDefault()));
            getSkinnable().getCalendarRangeCallback().call(lRange);
        }*/
    }

    /**
     *
     */
    private void refreshLocale() {
        // create the formatter to use
        Locale locale = getSkinnable().getLocale();
        layoutHelp.dayOfWeekDateFormat = new SimpleDateFormat("E", locale);
        layoutHelp.dayOfWeekDateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("E").toFormatter(locale);
        layoutHelp.dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
        layoutHelp.dateDateTimeFormatter = new DateTimeFormatterBuilder().appendLocalized(FormatStyle.SHORT, null).toFormatter(locale);

        // assign weekend of weekday class
        for (DayBodyPane lDayBodyPane : weekBodyPane.dayBodyPanes) {
            String lWeekendOrWeekday = isWeekend(lDayBodyPane.localDateObjectProperty.get()) ? "weekend" : "weekday";
            lDayBodyPane.getStyleClass().removeAll("weekend", "weekday");
            lDayBodyPane.getStyleClass().add(lWeekendOrWeekday);
        }
        for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes) {
            String lWeekendOrWeekday = isWeekend(lDayHeaderPane.localDateObjectProperty.get()) ? "weekend" : "weekday";
            lDayHeaderPane.getStyleClass().removeAll("weekend", "weekday");
            lDayHeaderPane.getStyleClass().add(lWeekendOrWeekday);
        }
    }

    /**
     * Have all days reconstruct the appointments
     */
    public void setupEvents() {
        for (ResourceBodyPane lResource : weekBodyPane.resourceBodyPanes) {
            lResource.setupEvents();
        }
        calculateSizes(); // must be done after setting up the panes
        nowUpdateRunnable.run(); // set the history
    }

    /**
     * Re-render only changed resources
     *
     * @param oldResourceId
     * @param newResourceId
     */
    public void setupParticularEvents(long oldResourceId, long newResourceId) {
        List<ResourceBodyPane> resourceBodyPanes = weekBodyPane.resourceBodyPanes.stream()
                .filter(c -> c.resource.getId().equals(oldResourceId) || c.resource.getId().equals(newResourceId))
                .collect(Collectors.toList());
        resourceBodyPanes.forEach(c -> c.setupEvents());

        calculateSizes(); // must be done after setting up the panes
        nowUpdateRunnable.run(); // set the history
    }

    /**
     *
     */
    public void refresh() {
        assignDateToDayAndHeaderPanes();
        refreshLocale();
        setupEvents();
        nowUpdateRunnable.run();
    }

    /**
     *
     */
    @Override
    public Pane getNodeForPopup(Scheduler.Event event) {
        return eventNodeMap.get(System.identityHashCode(event));
    }

    final private Map<Integer, Pane> eventNodeMap = new HashMap<>();

    Map<Integer, Pane> eventNodeMap() {
        return eventNodeMap;
    }

    // ==================================================================================================================
    // StyleableProperties

    /**
     * snapToMinutes
     * I am clueless why the Integer version of this property gets a double pushed in (which results in a ClassCastException)
     */
    // TBEERNOT: reattempt converting this to Integer
    public final ObjectProperty<Double> snapToMinutesProperty() {
        return snapToMinutesProperty;
    }

    private ObjectProperty<Double> snapToMinutesProperty = new SimpleStyleableObjectProperty<Double>(StyleableProperties.SNAPTOMINUTES_CSSMETADATA, StyleableProperties.SNAPTOMINUTES_CSSMETADATA.getInitialValue(null));

    public final void setSnapToMinutes(double value) {
        snapToMinutesProperty().set(value);
    }

    public final double getSnapToMinutes() {
        return snapToMinutesProperty.get().intValue();
    }

    public final T withSnapToMinutes(double value) {
        setSnapToMinutes(value);
        return (T) this;
    }

    // -------------------------

    private static class StyleableProperties {
        private static final CssMetaData<Scheduler, Double> SNAPTOMINUTES_CSSMETADATA = new CssMetaDataForSkinProperty<Scheduler, SchedulerSkinAbstract<?>, Double>("-fxx-snap-to-minutes", DoubleConverter.getInstance(), 0.0) {
            @Override
            protected ObjectProperty<Double> getProperty(SchedulerSkinAbstract<?> s) {
                return s.snapToMinutesProperty;
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(SNAPTOMINUTES_CSSMETADATA);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * This method should delegate to {@link Node#getClassCssMetaData()} so that
     * a Node's CssMetaData can be accessed without the need for reflection.
     *
     * @return The CssMetaData associated with this node, which may include the
     * CssMetaData of its super classes.
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }


    // ==================================================================================================================
    // DRAW

    /**
     * construct the nodes
     */
    private void createNodes() {
        // when switching skin, remove any old stuff
        getChildren().clear();
        if (borderPane != null) {
            layoutHelp.dragPane.getChildren().remove(borderPane);
        }

        // we use a borderpane
        borderPane = new BorderPane();
        borderPane.prefWidthProperty().bind(getSkinnable().widthProperty()); // the border pane is the same size as the whole skin
        borderPane.prefHeightProperty().bind(getSkinnable().heightProperty());
        getChildren().add(borderPane);

        // borderpane center
        weekBodyPane = new WeekBodyPane();
        weekScrollPane = new ScrollPane();
        weekScrollPane.setContent(weekBodyPane);
        weekScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        weekScrollPane.setFitToWidth(true);
        weekScrollPane.setPannable(false); // panning would conflict with creating a new event
        borderPane.setCenter(weekScrollPane);
        // bind to the scrollpane's viewport
        weekScrollPane.viewportBoundsProperty().addListener((observable) -> {
            calculateSizes();
            nowUpdateRunnable.run();
        });

        // borderpane top: header has to be created after the content, because there is a binding
        weekHeaderPane = new WeekHeaderPane(weekBodyPane); // must be done after the WeekBodyPane
        weekHeaderPane.setTranslateX(1); // correct for the scrollpane
        borderPane.setTop(weekHeaderPane);

        // the borderpane is placed in the drag pane, so DragPane can catch mouse events
        getChildren().remove(borderPane);
        layoutHelp.dragPane.getChildren().add(borderPane);
        getChildren().add(layoutHelp.dragPane);

        // style
        getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
    }


    /**
     *
     */
    private void scrollWeekpaneToShowDisplayedTime() {
        // calculate the offset of the displayed time from midnight
        LocalDateTime lDisplayedLocalDateTime = getSkinnable().displayedLocalDateTime().get();
        double lOffsetInMinutes = (lDisplayedLocalDateTime.getHour() * 60) + lDisplayedLocalDateTime.getMinute();

        // calculate the position of the scrollbar that matches that offset from midnight
        double lScrollRange = weekScrollPane.getVmax() - weekScrollPane.getVmin();
        double lValue = lScrollRange * lOffsetInMinutes / (24.0 * 60.0);
        weekScrollPane.setVvalue(lValue);
    }

    // ==================================================================================================================
    // PANES

    abstract protected List<LocalDate> determineDisplayedLocalDates();

    /**
     * Responsible for rendering the day headers within the week
     */
    class WeekHeaderPane extends Pane {
        final List<DayHeaderPane> dayHeaderPanes = new ArrayList<DayHeaderPane>();

        public WeekHeaderPane(WeekBodyPane weekBodyPane) {
            construct();
        }

        private void construct() {
            // Grouping month and year
            GroupedHeaderPane groupedHeaderPane = new GroupedHeaderPane(determineDisplayedLocalDates(), layoutHelp);
            groupedHeaderPane.prefHeightProperty().bind(layoutHelp.groupedDateHeaderPaneProperty);

            getChildren().add(groupedHeaderPane);

            // one day header pane per day body pane
            for (DayBodyPane dayBodyPane : weekBodyPane.dayBodyPanes) {
                // create pane
                DayHeaderPane lDayHeader = new DayHeaderPane(dayBodyPane.localDateObjectProperty.get(), events, layoutHelp); // associate with a day, so we can use its administration. This needs only be done once

                // layout in relation to day panes
                lDayHeader.layoutXProperty().bind(dayBodyPane.layoutXProperty()); // same x position as the body
                lDayHeader.layoutYProperty().bind(heightProperty().divide(2));
                lDayHeader.prefWidthProperty().bind(dayBodyPane.prefWidthProperty()); // same width as the body
                lDayHeader.prefHeightProperty().bind(heightProperty().divide(2)); // same height as the week pane
                getChildren().add(lDayHeader);

                // remember
                dayHeaderPanes.add(lDayHeader);
            }

            prefWidthProperty().bind(weekBodyPane.widthProperty()); // same width as the weekpane
            prefHeightProperty().bind(layoutHelp.headerHeightProperty);
        }

        private void reconstruct() {
            dayHeaderPanes.clear();
            getChildren().clear();
            construct();
        }
    }

    /**
     * Responsible for rendering the days within the week
     */
    class WeekBodyPane extends Pane {
        final List<DayBodyPane> dayBodyPanes = new ArrayList<DayBodyPane>();
        final List<ResourceBodyPane> resourceBodyPanes = new ArrayList<ResourceBodyPane>();

        public WeekBodyPane() {
            getStyleClass().add("Week");
            construct();
        }

        private void construct() {
//            getChildren().add(new TimeScale24Hour(this, layoutHelp));
//            getChildren().add(new ResourceHeaderPane(this, layoutHelp, resources, events));

            int i = 0;
            for (LocalDate localDate : determineDisplayedLocalDates()) {
                DayBodyPane lDayPane = new DayBodyPane(localDate, events, layoutHelp);
                lDayPane.layoutXProperty().bind(layoutHelp.dayWidthProperty.multiply(i).add(layoutHelp.dayFirstColumnXProperty));
                lDayPane.layoutYProperty().set(0.0);
                lDayPane.prefWidthProperty().bind(layoutHelp.dayWidthProperty);
                lDayPane.prefHeightProperty().bind(layoutHelp.dayHeightProperty);
                getChildren().add(lDayPane);

                // remember
                dayBodyPanes.add(lDayPane);
                localDate = localDate.plusDays(1);
                i++;
            }

            int j = 0;
            for (Scheduler.Resource lResource : resources.collectRegular()) {
                ResourceHeaderPane resourceHeaderPane = new ResourceHeaderPane(lResource, layoutHelp);
                resourceHeaderPane.layoutXProperty().setValue(0.0);
                resourceHeaderPane.layoutYProperty().bind(NodeUtil.snapXY(layoutHelp.resourceHeightProperty.multiply(j)));

                resourceHeaderPane.prefWidthProperty().bind(layoutHelp.timeWidthProperty);
                resourceHeaderPane.prefHeightProperty().bind(layoutHelp.resourceHeightProperty);
                getChildren().add(resourceHeaderPane);

                ResourceBodyPane resourceBodyPane = new ResourceBodyPane(determineDisplayedLocalDates(), lResource, events, layoutHelp);
                resourceBodyPane.layoutXProperty().bind(layoutHelp.timeWidthProperty);
                resourceBodyPane.layoutYProperty().bind(NodeUtil.snapXY(layoutHelp.resourceHeightProperty.multiply(j)));

                resourceBodyPane.prefWidthProperty().bind(this.widthProperty().subtract(layoutHelp.timeWidthProperty));
                resourceBodyPane.prefHeightProperty().bind(layoutHelp.resourceHeightProperty);
                getChildren().add(resourceBodyPane);

                // remember
                resourceBodyPanes.add(resourceBodyPane);
                j++;
            }


        }

        void reconstruct() {
            dayBodyPanes.clear();
            getChildren().clear();
            construct();
        }
    }

    // ==================================================================================================================
    // NOW

    final Rectangle nowLine = new Rectangle(0, 0, 0, 0);

    /**
     * This is implemented as a runnable so it can be called from a timer, but also directly
     */
    Runnable nowUpdateRunnable = new Runnable() {
        {
            nowLine.getStyleClass().add("Now");
            nowLine.setHeight(20);
            nowLine.setWidth(3);

        }

        @Override
        public void run() {
            //  get now
            LocalDateTime lNow = LocalDateTime.now();
            LocalDate lToday = lNow.toLocalDate();


            // see if we are displaying now (this has to do with the fact that now may slide in or out of the view)
            // check all days
            boolean lFound = false;
            for (ResourceBodyPane lResourceBodyPane : weekBodyPane.resourceBodyPanes) {
                if (weekBodyPane.resourceBodyPanes.get(0) == lResourceBodyPane) {
                    // today
                    if (lResourceBodyPane.getStyleClass().contains("today") == false) {
                        lResourceBodyPane.getStyleClass().add("today");
                    }
                    lFound = true;

                    // add if not present
                    if (weekBodyPane.getChildren().contains(nowLine) == false) {
                        weekBodyPane.getChildren().add(nowLine); // this will remove the now line from another day
                        nowLine.xProperty().setValue(lResourceBodyPane.getCurrentTimeLocationInScene());
                    }

                    // place it
                    int lOffsetY = (lNow.getHour() * 60) + lNow.getMinute();
                    nowLine.setX(lResourceBodyPane.getCurrentTimeLocationInScene());
                    if (nowLine.heightProperty().isBound() == false) {
                        nowLine.heightProperty().bind(layoutHelp.dayHeightProperty);
                    }
                }


            }

            // if cannot be placed, remove
            if (lFound == false) {
                weekBodyPane.getChildren().remove(nowLine);
            }

            // history
            for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes) {
                for (Node lNode : lDayHeaderPane.getChildren()) {
                    if (lNode instanceof EventAbstractPane) {
                        ((EventAbstractPane) lNode).determineHistoryVisualizer(lNow);
                    }
                }
            }
            for (DayBodyPane lDayBodyPane : weekBodyPane.dayBodyPanes) {
                for (Node lNode : lDayBodyPane.getChildren()) {
                    if (lNode instanceof EventAbstractPane) {
                        ((EventAbstractPane) lNode).determineHistoryVisualizer(lNow);
                    }
                }
            }
        }
    };

    /**
     * This timer takes care of updating NOW
     */
    jfxtras.animation.Timer nowTimer = new jfxtras.animation.Timer(nowUpdateRunnable)
            .withCycleDuration(new Duration(60 * 1000)) // every minute
            .withDelay(new Duration(((60 - LocalDateTime.now().getSecond()) * 1000) - (LocalDateTime.now().getNano() / 1000000))) // trigger exactly on each new minute
            .start();


    // ==================================================================================================================
    // SUPPORT

    /**
     * check if a certain weekday name is a certain day-of-the-week
     */
    private boolean isWeekend(LocalDate localDate) {
        return (localDate.getDayOfWeek() == DayOfWeek.SATURDAY) || (localDate.getDayOfWeek() == DayOfWeek.SUNDAY);
    }


    /**
     * These values can not be determined by binding them to other values, because their calculation is too complex
     */
    private void calculateSizes() {
        // header
/*        int lMaxOfWholeDayAppointments = 0;
        for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes) {
            int lNumberOfWholeDayAppointments = lDayHeaderPane.getNumberOfWholeDayAppointments();
            lMaxOfWholeDayAppointments = Math.max(lMaxOfWholeDayAppointments, lNumberOfWholeDayAppointments);
        }*/
//        layoutHelp.highestNumberOfWholedayAppointmentsProperty.set(lMaxOfWholeDayAppointments);

        // day columns
        if (weekScrollPane.viewportBoundsProperty().get() != null) {
            layoutHelp.dayWidthProperty.set((weekScrollPane.viewportBoundsProperty().get().getWidth() - layoutHelp.timeWidthProperty.get()) / determineDisplayedLocalDates().size());
            layoutHelp.resourceWidthProperty.set((weekScrollPane.viewportBoundsProperty().get().getWidth() - layoutHelp.timeWidthProperty.get()));
        }

        // hour height
        double lScrollbarSize = new ScrollBar().getWidth();
        layoutHelp.resourceHeightProperty.set(layoutHelp.textHeightProperty.get() * 2 + 10); // 10 is padding
        if (weekScrollPane.viewportBoundsProperty().get() != null && (weekScrollPane.viewportBoundsProperty().get().getHeight() - lScrollbarSize) > layoutHelp.resourceHeightProperty.get() * 24) {
            // if there is more room than absolutely required, let the height grow with the available room
            layoutHelp.resourceHeightProperty.set((weekScrollPane.viewportBoundsProperty().get().getHeight() - lScrollbarSize) / 24);
        }
    }


    /**
     * @param x scene coordinate
     * @param y scene coordinate
     */
    public LocalDateTime convertClickInSceneToDateTime(double x, double y) {

        for (ResourceBodyPane lResourcePane : weekBodyPane.resourceBodyPanes) {
            LocalDateTime lLocalDateTime = lResourcePane.convertClickInSceneToDateTime(x, y);
            if (lLocalDateTime != null) {
                return lResourcePane.convertClickInSceneToDateTime(x, y);
            }
        }
        return null;
    }

    public long convertClickInSceneToResourceId(double x, double y) {
        for (ResourceBodyPane lResourcePane : weekBodyPane.resourceBodyPanes) {
            LocalDateTime lLocalDateTime = lResourcePane.convertClickInSceneToDateTime(x, y);
            if (lLocalDateTime != null) {
                return lResourcePane.convertClickInSceneToResourceId(x, y);
            }
        }
        return 0;
    }


    // ==================================================================================================================
    // Print

    /**
     * Prints the current skin using the given printer job.
     * <p>This method does not modify the state of the job, nor does it call
     * {@link PrinterJob#endJob}, so the job may be safely reused afterwards.
     *
     * @param job printer job used for printing
     * @since JavaFX 8.0
     */
    public void print(PrinterJob job) {
        float width = 5000;
        float height = 5000;

        // we use a borderpane
        BorderPane borderPane = new BorderPane();
        borderPane.prefWidthProperty().set(width);
        borderPane.prefHeightProperty().set(height);

        // borderpane center
        WeekBodyPane weekBodyPane = new WeekBodyPane();
        borderPane.setCenter(weekBodyPane);

        // borderpane top: header has to be created after the content, because there is a binding
        WeekHeaderPane weekHeaderPane = new WeekHeaderPane(weekBodyPane); // must be done after the WeekBodyPane
        borderPane.setTop(weekHeaderPane);

        // style
        borderPane.getStyleClass().add(Scheduler.class.getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
        borderPane.getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control

        // scale to match page
        PageLayout pageLayout = job.getJobSettings().getPageLayout();
        double scaleX = pageLayout.getPrintableWidth() / borderPane.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / borderPane.getBoundsInParent().getHeight();
        scaleY *= 0.9; // for some reason the height doesn't fit
        borderPane.getTransforms().add(new Scale(scaleX, scaleY));

        // print
        job.printPage(pageLayout, borderPane);
    }

}
