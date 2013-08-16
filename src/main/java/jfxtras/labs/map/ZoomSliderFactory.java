package jfxtras.labs.map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;

/**
 * Factory for the zoom slider.
 *
 * @author Mario Schroeder
 */
public class ZoomSliderFactory {

    private Zoomable zoomable;

    public ZoomSliderFactory(Zoomable zoomable) {
        this.zoomable = zoomable;
    }

    protected Slider create() {
        Slider slider = new Slider();
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1.0);
        
        slider.setValue(zoomable.zoomProperty().get());
        slider.setMin(zoomable.getMinZoom());
        slider.setMax(zoomable.getMaxZoom());

        slider.setPrefSize(30, 150);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                zoomable.zoomProperty().set(new_val.intValue());
            }
        });

        return slider;
    }
}
