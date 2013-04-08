package jfxtras.labs.map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;

/**
 * Factory for the zoom slider.
 *
 * @author Mario Schröder
 */
class ZoomSliderFactory {

    private MapControlable controlable;

    ZoomSliderFactory(MapControlable controlable) {
        this.controlable = controlable;
    }

    Slider create() {
        Slider slider = new Slider();
        slider.setOrientation(Orientation.VERTICAL);
        
        slider.setMin(ZoomBounds.Min.getValue());
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
