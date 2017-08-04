package jfxtras.labs.scene.control.scheduler.skin;

import javafx.scene.CacheHint;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.scheduler.Scheduler;
import jfxtras.util.NodeUtil;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class ResourceHeaderPane extends Pane {
    final Scheduler.Resource resource;
    private final LayoutHelp layoutHelp;

    public ResourceHeaderPane(Scheduler.Resource resource, LayoutHelp layoutHelp) {
        this.resource = resource;
        this.layoutHelp = layoutHelp;

        setMouseTransparent(true);

        setCacheHint(CacheHint.QUALITY);
        setCache(true);
        setCacheShape(true);

        construct();
    }

    private void construct() {
        // for debugging setStyle("-fx-border-color:BLUE;-fx-border-width:4px;");
//        setStyle("-fx-border-color:BLUE;-fx-border-width:4px;");
        {
            setId("ResourceHeaderPane" + resource.getId()); // for testing

            Text t = new Text(resource.getName());
            t.xProperty().bind(layoutHelp.timeWidthProperty.subtract(t.getBoundsInParent().getWidth()).subtract(layoutHelp.timeColumnWhitespaceProperty.get() / 2));
//        t.yProperty().bind(layoutHelp.resourceHeightProperty.multiply(lHour));
            t.setTranslateY(t.getBoundsInParent().getHeight()); // move it under the line
            t.getStyleClass().add("HourLabel");
            t.setFontSmoothingType(FontSmoothingType.LCD);
            getChildren().add(t);
        }

        {
            Line l = new Line(0, 10, 100, 10);
            l.setId("bottomLine" + resource.getId());
            l.getStyleClass().add("HalfHourLine");
            l.startXProperty().set(0.0);
            l.endXProperty().bind(NodeUtil.snapXY(layoutHelp.resourceWidthProperty).add(layoutHelp.timeWidthProperty));
            l.startYProperty().bind(NodeUtil.snapXY(layoutHelp.resourceHeightProperty.multiply(1)));
            l.endYProperty().bind(NodeUtil.snapXY(l.startYProperty()));
            getChildren().add(l);
        }
    }


}
