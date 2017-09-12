package jfxtras.labs.scene.control.scheduler.skin;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.scheduler.Scheduler;
import jfxtras.util.NodeUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class EventDurationDragger extends Rectangle {
    EventDurationDragger(EventRegularBodyPane eventPane, Scheduler.Event event, LayoutHelp layoutHelp)
    {
        // remember
        this.eventPane = eventPane;
        this.event = event;
        this.layoutHelp = layoutHelp;

        // bind: place at the right of the pane, 1/2 height of the pane, centered
        xProperty().bind( NodeUtil.snapXY(eventPane.widthProperty().subtract(5)) );
        yProperty().bind( NodeUtil.snapXY(eventPane.heightProperty().multiply(0.25)) );
        heightProperty().bind( eventPane.heightProperty().multiply(0.5) );
        setWidth(3);
        minimumWidth = ( ((SchedulerSkinAbstract<?>)layoutHelp.skin).getSnapToMinutes() * 60 * 1000) / layoutHelp.durationInMSPerPixelProperty.get();

        // styling
        getStyleClass().add("DurationDragger");

        // mouse
        layoutHelp.setupMouseOverAsBusy(this);
        setupMouseDrag();
    }
    private final EventRegularBodyPane eventPane;
    private final Scheduler.Event event;
    private final LayoutHelp layoutHelp;
    private double minimumWidth = 10;

    private void setupMouseDrag() {
        // start resize
        setOnMousePressed( (mouseEvent) -> {
            // only on primary
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
                return;
            }

            // we handle this event
            mouseEvent.consume();

            double lX = eventPane.getLayoutX();
            double lWidth = eventPane.getWidth();

            //TODO #0543 redesign
            // Calculating layoutX for event with earlier startDate than showed
            LocalDate minDate = ((ResourceBodyPane)eventPane.getParent()).minDateObjectProperty.get();
            boolean hasHiddenPart = event.getStartTime().toLocalDate().isBefore(minDate);
            if (hasHiddenPart) {
                // seconds passed from midnight to argument value
                LocalDateTime midnight = event.getStartTime().truncatedTo(ChronoUnit.DAYS);
                Duration duration = Duration.between(midnight, event.getStartTime());
                long secondsPassed = duration.getSeconds();

                int daysBetween = Period.between(event.getStartTime().toLocalDate(), minDate).getDays();
                long todayOffset = (long) (secondsPassed / (layoutHelp.durationInMSPerPixelProperty.get() / 1000));
                double ghostX = layoutHelp.dayWidthProperty.get() * daysBetween - todayOffset;

                lWidth += ghostX;
                lX += -ghostX;
            }

            // place a rectangle at exactly the same location as the eventPane
            setCursor(Cursor.H_RESIZE);
            resizeRectangle = new Rectangle(lX, eventPane.getLayoutY(), lWidth, eventPane.getHeight()); // the values are already snapped
            resizeRectangle.getStyleClass().add("GhostRectangle");
            ((Pane) eventPane.getParent()).getChildren().add(resizeRectangle);

            // place a text node at the bottom of the resize rectangle
            endTimeText = new Text(layoutHelp.timeDateTimeFormatter.format(event.getEndTime()));
            endTimeText.layoutYProperty().set(eventPane.getLayoutY());
            if (hasHiddenPart) {
                endTimeText.layoutXProperty().bind(resizeRectangle.widthProperty().add(lX));
            } else{
                endTimeText.layoutXProperty().bind(resizeRectangle.widthProperty().add(eventPane.getLayoutX()));
            }
            endTimeText.getStyleClass().add("GhostRectangleText");
            ((Pane) eventPane.getParent()).getChildren().add(endTimeText);
        });

        // visualize resize
        setOnMouseDragged( (mouseEvent) -> {
            // only on primary
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
                return;
            }

            // we handle this event
            mouseEvent.consume();

            //  calculate the number of pixels from on-screen nodeX (layoutX) to on-screen mouseX
            double lNodeScreenX = NodeUtil.screenX(eventPane);
            double lMouseX = mouseEvent.getScreenX();
            double lWidth = lMouseX - lNodeScreenX;

            //TODO #0543 redesign
            // Add to lWidth, hidden part of event. For event with earlier startDate than showed on the screen
            LocalDate minDate = ((ResourceBodyPane)eventPane.getParent()).minDateObjectProperty.get();
            if (event.getStartTime().toLocalDate().isBefore(minDate)) {
                // seconds passed from midnight to argument value
                LocalDateTime midnight = event.getStartTime().truncatedTo(ChronoUnit.DAYS);
                Duration duration = Duration.between(midnight, event.getStartTime());
                long secondsPassed = duration.getSeconds();

                int daysBetween = Period.between(event.getStartTime().toLocalDate(), minDate).getDays();
                long todayOffset = (long) (secondsPassed / (layoutHelp.durationInMSPerPixelProperty.get() / 1000));
                double ghostX = layoutHelp.dayWidthProperty.get() * daysBetween - todayOffset;

                lWidth += ghostX;
            }

            if (lWidth < minimumWidth) {
                lWidth = minimumWidth; // prevent underflow
            }
            resizeRectangle.setWidth( NodeUtil.snapWH(resizeRectangle.getLayoutX(), lWidth) );

            // show the current time in the label
            LocalDateTime endLocalDateTime = calculateEndDateTime();
            endTimeText.setText(layoutHelp.timeDateTimeFormatter.format(endLocalDateTime));
        });

        // end resize
        setOnMouseReleased( (mouseEvent) -> {
            // only on primary
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
                return;
            }

            LocalDateTime endLocalDateTime = calculateEndDateTime(); // must be done before the UI is reset and the event is consumed

            // we handle this event
            mouseEvent.consume();

            // reset ui
            setCursor(Cursor.HAND);
            ((Pane) eventPane.getParent()).getChildren().remove(resizeRectangle);
            resizeRectangle = null;
            ((Pane) eventPane.getParent()).getChildren().remove(endTimeText);
            endTimeText = null;

            // set the new enddate
            eventPane.event.setEndTime(endLocalDateTime);
            layoutHelp.callEventChangedCallback(event);

            // relayout the entire skin
            layoutHelp.skin.setupParticularEvents(event.getResourceId(), event.getResourceId());
        });
    }
    private Rectangle resizeRectangle = null;
    private Text endTimeText = null;

    private LocalDateTime calculateEndDateTime() {

        // calculate the new end datetime for the event (recalculating the duration)
        long ms = (long)(resizeRectangle.getWidth() * layoutHelp.durationInMSPerPixelProperty.get());
        LocalDateTime endLocalDateTime = eventPane.startDateTime.plusSeconds(ms / 1000);

        // round to X minutes accuracy
        endLocalDateTime = layoutHelp.roundTimeToNearestMinutes(endLocalDateTime, (int)((SchedulerSkinAbstract<?>)layoutHelp.skin).getSnapToMinutes());
        return endLocalDateTime;
    }
}
