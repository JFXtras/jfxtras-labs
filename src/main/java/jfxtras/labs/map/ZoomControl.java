package jfxtras.labs.map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/**
 * This is the default zoom control
 * 
 * @author Mario Schroeder
 * 
 */
public class ZoomControl extends VBox {
	
	protected static final double ZOOM_DIFF = .01;
	
	protected Zoomable zoomable;
	
	private Slider zoomSlider;

	private Button zoomInButton;

	private Button zoomOutButton;

	public ZoomControl(Zoomable zoomable) {
		this.zoomable = zoomable;
		
		ZoomSliderFactory zoomSliderFactory = new ZoomSliderFactory(zoomable);
		zoomSlider = zoomSliderFactory.create();

		ZoomButtonFactory zoomButtonFactory = new ZoomInButtonFactory(zoomable);
		zoomInButton = zoomButtonFactory.create();

		zoomButtonFactory = new ZoomOutButtonFactory(zoomable);
		zoomOutButton = zoomButtonFactory.create();

		getChildren().add(zoomInButton);
		getChildren().add(zoomSlider);
		getChildren().add(zoomOutButton);
		
		zoomable.zoomProperty().addListener(new ZoomChangeListener());

	}

	private class ZoomChangeListener implements ChangeListener<Number>{

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                Number newValue) {
			int zoom = newValue.intValue();
			zoomSlider.setTooltip(new Tooltip("Zoom level " + zoom));
			zoomInButton.setTooltip(new Tooltip("Zoom to level " + (zoom + 1)));
			zoomOutButton.setTooltip(new Tooltip("Zoom to level " + (zoom - 1)));
			zoomOutButton.setDisable(!(zoom > zoomable.getMinZoom()));
			zoomInButton.setDisable(!(zoom < zoomable.getMaxZoom()));
			
			if (Math.abs(zoomSlider.getValue() - zoom) > ZOOM_DIFF) {
				zoomSlider.setValue(zoom);
			}
		}
		
	}


}
