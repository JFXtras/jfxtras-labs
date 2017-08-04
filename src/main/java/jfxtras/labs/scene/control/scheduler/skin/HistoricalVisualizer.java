package jfxtras.labs.scene.control.scheduler.skin;

import javafx.scene.shape.Rectangle;

/**
 * @author Tom Eugelink
 */
public class HistoricalVisualizer extends Rectangle {

    HistoricalVisualizer(EventAbstractPane pane) // TBEE: pane must implement something to show its begin and end datetime, so this class knowns how to render itself?
    {
        // 100% overlay the pane
        setMouseTransparent(true);
        xProperty().set(0);
        yProperty().set(0);
        widthProperty().bind(pane.prefWidthProperty());
        heightProperty().bind(pane.prefHeightProperty());
        setVisible(false);
        getStyleClass().add("History");
    }
}
