package jfxtras.labs.scene.control.scheduler.skin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jfxtras.util.NodeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class DayBodyPane extends Pane {

    /**
     *
     */
    public DayBodyPane(LocalDate localDate, AllEvents allEvents, LayoutHelp layoutHints) {
        this.localDateObjectProperty.set(localDate);
        this.allEvents = allEvents;
        this.layoutHelp = layoutHints;
        construct();
    }

    final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
    final AllEvents allEvents;
    final LayoutHelp layoutHelp;

    /**
     *
     */
    private void construct() {

        // for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
        getStyleClass().add("Day");
        setId("DayBodyPane" + localDateObjectProperty.get()); // for testing

        // react to changes in the appointments
/*        allEvents.addOnChangeListener(() -> {
            setupAppointments();
        });
        setupAppointments();

        // change the layout related to the size
        widthProperty().addListener((observable) -> {
            relayout();
        });
        heightProperty().addListener((observable) -> {
            relayout();
        });*/

//        setupMouseDrag();

        // for testing
        localDateObjectProperty.addListener((observable) -> {
            setId("DayBody" + localDateObjectProperty.get());
        });
        setId("DayBody" + localDateObjectProperty.get());
    }

    /**
     * @param x scene coordinate
     * @param y scene coordinate
     * @return a localDateTime where nano seconds == 1
     */
    LocalDateTime convertClickInSceneToDateTime(double x, double y) {
        Rectangle r = new Rectangle(NodeUtil.sceneX(this), NodeUtil.sceneY(this), this.getWidth(), this.getHeight());
        if (r.contains(x, y)) {
            LocalDate localDate = localDateObjectProperty.get();
            double lHeightOffset = (y - r.getY());
            int ms = (int) (lHeightOffset * layoutHelp.durationInMSPerPixelProperty.get());
            LocalDateTime localDateTime = localDate.atStartOfDay().plusSeconds(ms / 1000);
            localDateTime = localDateTime.withNano(EventAbstractPane.DRAG_DAY); // we abuse the nano second to deviate body panes from header panes
            return localDateTime;
        }
        return null;
    }

}
