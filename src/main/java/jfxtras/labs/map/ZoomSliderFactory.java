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

    private MapControlable controlable;

    public ZoomSliderFactory(MapControlable controlable) {
        this.controlable = controlable;
    }

    protected Slider create() {
        Slider slider = new Slider();
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1.0);
        
        slider.setMin(controlable.getTileSource().getMinZoom());
        slider.setMax(controlable.getTileSource().getMaxZoom());
        slider.setValue(ZoomBounds.Min.getValue());

        slider.setPrefSize(30, 150);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                controlable.setZoom(new_val.intValue());
            }
        });

        return slider;
    }
}
