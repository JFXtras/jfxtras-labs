package jfxtras.labs.scene.control.scheduler.skin;

import javafx.scene.layout.Pane;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class DragPane extends Pane{

    DragPane(LayoutHelp layoutHelp) {
        prefWidthProperty().bind(layoutHelp.skinnable.widthProperty()); // the drag pane is the same size as the whole skin
        prefHeightProperty().bind(layoutHelp.skinnable.heightProperty());
    }
}
