package jfxtras.labs.scene.control.scheduler.skin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class DayHeaderPane extends Pane {


    public DayHeaderPane(LocalDate localDate, AllEvents allEvents, LayoutHelp layoutHelp) {
        this.localDateObjectProperty.set(localDate);
        this.allEvents = allEvents;
        this.layoutHelp = layoutHelp;
        construct();
    }

    final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
    final AllEvents allEvents;
    final LayoutHelp layoutHelp;

    private void construct() {

        // for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
        getStyleClass().add("DayHeader");

        // set day label
        dayText = new Text("?");
        dayText.getStyleClass().add("DayLabel");
        dayText.setX(layoutHelp.paddingProperty.get()); // align left
        dayText.setY(dayText.prefHeight(0));
        getChildren().add(dayText);

        // clip the visible part
        Rectangle lClip = new Rectangle(0, 0, 0, 0);
        lClip.widthProperty().bind(widthProperty().subtract(layoutHelp.paddingProperty.get()));
        lClip.heightProperty().bind(heightProperty());
        dayText.setClip(lClip);

        // react to changes in the calendar by updating the label
        localDateObjectProperty.addListener((observable) -> {
            setLabel();
        });
        setLabel();

        // react to changes in the events
        /*allEvents.addOnChangeListener(() -> {
            setupAppointments();
        });*/
//        setupAppointments();

        // setup the create event
//        setupMouse();
    }

    private void setLabel() {
        String lLabel = localDateObjectProperty.get().format(layoutHelp.dayOfWeekDateTimeFormatter)
                + " "
                + localDateObjectProperty.get().format(layoutHelp.dateDateTimeFormatter);
        dayText.setText(lLabel);

        // for testing
        setId("DayHeader" + localDateObjectProperty.get());
    }

    private Text dayText = new Text("?");

    /**
     *
     */

    final private List<Scheduler.Event> events = new ArrayList<>();

    /**
     * So the out view knows how much room (height) we need
     *
     * @return
     */
    public int getNumberOfWholeDayAppointments() {
        return events.size();
    }

}
