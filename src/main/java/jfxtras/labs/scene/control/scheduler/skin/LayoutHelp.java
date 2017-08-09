package jfxtras.labs.scene.control.scheduler.skin;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * This class is not a class but a data holder, a record, all fields are accessed directly.
 * Its methods are utility methods, which normally would be statics in a util class.
 *
 * @author Tom Eugelink
 */
public class LayoutHelp {


    public LayoutHelp(Scheduler skinnable, SchedulerSkin skin) {
        this.skinnable = skinnable;
        this.skin = skin;
        dragPane = new DragPane(this);

        // header
        titleDateTimeHeightProperty.bind(textHeightProperty.multiply(1.5));
        appointmentHeaderPaneHeightProperty.bind(textHeightProperty.add(5)); // not sure why the 5 is needed
        headerHeightProperty.bind(highestNumberOfWholedayAppointmentsProperty.multiply(appointmentHeaderPaneHeightProperty).add(titleDateTimeHeightProperty));

        // day columns
        dayFirstColumnXProperty.bind(timeWidthProperty);
        dayContentWidthProperty.bind(dayWidthProperty.subtract(10)); // the 10 is a margin at the right so that there is always room to start a new event

        // hour height
        dayHeightProperty.bind(resourceHeightProperty.multiply(resourcesCountProperty));
        durationInMSPerPixelProperty.bind(msPerDayProperty.divide(dayWidthProperty));

        // generic
        Text textHeight = new Text("X");
        textHeight.getStyleClass().add("Scheduler");
        textHeightProperty.set(textHeight.getBoundsInParent().getHeight());

        // time column
        Text textWidth = new Text("Resource name");
        textWidth.getStyleClass().add("Scheduler");
        timeWidthProperty.bind(timeColumnWhitespaceProperty.add(textWidth.getBoundsInParent().getWidth()));
    }

    final Scheduler skinnable;
    final SchedulerSkin skin;
    final DragPane dragPane;

    final DoubleProperty msPerDayProperty = new SimpleDoubleProperty(24 * 60 * 60 * 1000);

    final IntegerProperty highestNumberOfWholedayAppointmentsProperty = new SimpleIntegerProperty(0);

    final DoubleProperty paddingProperty = new SimpleDoubleProperty(3);
    final DoubleProperty timeColumnWhitespaceProperty = new SimpleDoubleProperty(10);
    final DoubleProperty wholedayAppointmentFlagpoleWidthProperty = new SimpleDoubleProperty(5);
    final DoubleProperty textHeightProperty = new SimpleDoubleProperty(0);
    final DoubleProperty titleDateTimeHeightProperty = new SimpleDoubleProperty(0);
    final DoubleProperty headerHeightProperty = new SimpleDoubleProperty(0);
    final DoubleProperty appointmentHeaderPaneHeightProperty = new SimpleDoubleProperty(0);
    final DoubleProperty timeWidthProperty = new SimpleDoubleProperty(0);
    final DoubleProperty dayFirstColumnXProperty = new SimpleDoubleProperty(0);
    final DoubleProperty dayWidthProperty = new SimpleDoubleProperty(0);
    final DoubleProperty dayContentWidthProperty = new SimpleDoubleProperty(0);
    final DoubleProperty dayHeightProperty = new SimpleDoubleProperty(0);
    final DoubleProperty durationInMSPerPixelProperty = new SimpleDoubleProperty(0);
    final DoubleProperty resourceHeightProperty = new SimpleDoubleProperty(0);

    final DoubleProperty resourceWidthProperty = new SimpleDoubleProperty(0);
    final IntegerProperty resourcesCountProperty = new SimpleIntegerProperty(0);


    SimpleDateFormat dayOfWeekDateFormat = new SimpleDateFormat("E", Locale.getDefault());
    DateTimeFormatter dayOfWeekDateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("E").toFormatter(Locale.getDefault());
    SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.getDefault());
    DateTimeFormatter dateDateTimeFormatter = new DateTimeFormatterBuilder().appendLocalized(FormatStyle.SHORT, null).toFormatter(Locale.getDefault());
    final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    DateTimeFormatter timeDateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("MM/dd/yy HH:mm").toFormatter(Locale.getDefault());

    /**
     * I have no clue why the wholeday event header needs an additional 10.0 px X offset in right-to-left mode
     */
    void clip(Pane pane, Text text, DoubleBinding width, DoubleBinding height, boolean mirrorWidth, double additionMirorXOffset) {
        Rectangle lClip = new Rectangle(0, 0, 0, 0);
        if (mirrorWidth && skinnable.getNodeOrientation().equals(NodeOrientation.RIGHT_TO_LEFT)) {
            lClip.xProperty().bind(pane.widthProperty().multiply(-1.0).add(text.getBoundsInParent().getWidth()).add(additionMirorXOffset));
        }
        lClip.widthProperty().bind(width);
        lClip.heightProperty().bind(height);
        text.setClip(lClip);
    }

    /**
     *
     */
    void setupMouseOverAsBusy(final Node node) {
        // play with the mouse pointer to show something can be done here
        node.setOnMouseEntered((mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                node.setCursor(Cursor.HAND);
                mouseEvent.consume();
            }
        });
        node.setOnMouseExited((mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                node.setCursor(Cursor.DEFAULT);
                mouseEvent.consume();
            }
        });
    }

    /**
     * @param localDateTime
     * @param minutes
     * @return
     */
    LocalDateTime roundTimeToNearestMinutes(LocalDateTime localDateTime, int minutes) {
        System.err.println(" localDateTime: " + localDateTime + " minutes: " + minutes );
        if (minutes >= 1440) {
            return roundTimeToNearestDays(localDateTime, minutes);
        }

        localDateTime = localDateTime.withSecond(0).withNano(0);
        int lMinutes = localDateTime.getMinute() % minutes;
        if (lMinutes < (minutes / 2)) {
            localDateTime = localDateTime.plusMinutes(-1 * lMinutes);
        } else {
            localDateTime = localDateTime.plusMinutes(minutes - lMinutes);
        }
        return localDateTime;
    }

    private LocalDateTime roundTimeToNearestDays(LocalDateTime localDateTime, int minutes) {
        if (localDateTime.getHour() > 12) {
            return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1);
        } else {
            return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        }

    }

    /**
     * Has the client added a callback to process the change?
     *
     * @param event
     */
    void callEventChangedCallback(Scheduler.Event event) {
        // ignore temp appointments
        if (!(event instanceof EventAbstractPane.EventImplForDrag)) {
            Callback<Scheduler.Event, Void> lChangedCallback = skinnable.getAppointmentChangedCallback();
            if (lChangedCallback != null) {
                lChangedCallback.call(event);
            }
        }
    }
}
