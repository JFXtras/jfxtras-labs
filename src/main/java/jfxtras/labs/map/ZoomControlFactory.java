package jfxtras.labs.map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This is the factory that creates two button and a slider, 
 * the default zoom control.
 * 
 * @author Mario Schroeder
 * 
 */
public class ZoomControlFactory {

	private static final String ZOOM_LEVEL = "Zoom to level ";

	protected Zoomable zoomable;

	private Slider zoomSlider;

	private Button zoomInButton;

	private Button zoomOutButton;

	public Pane create(Zoomable zoomable) {
		
		this.zoomable = zoomable;

		ZoomSliderFactory zoomSliderFactory = new ZoomSliderFactory(zoomable);
		zoomSlider = zoomSliderFactory.create();

		ZoomButtonFactory zoomButtonFactory = new ZoomInButtonFactory(zoomable);
		zoomInButton = zoomButtonFactory.create();

		zoomButtonFactory = new ZoomOutButtonFactory(zoomable);
		zoomOutButton = zoomButtonFactory.create();
		
		setTooltip(zoomable.zoomProperty().get());
		
		zoomable.zoomProperty().addListener(new ZoomChangeListener());

		Pane pane = new VBox();
		pane.getChildren().add(zoomInButton);
		pane.getChildren().add(zoomSlider);
		pane.getChildren().add(zoomOutButton);
		
		pane.setLayoutX(10);
		pane.setLayoutY(20);

		return pane;
	}

	private void setTooltip(int zoom) {
		zoomSlider.setTooltip(new Tooltip("Zoom level " + zoom));
		zoomInButton.setTooltip(new Tooltip(ZOOM_LEVEL + (zoom + 1)));
		zoomOutButton.setTooltip(new Tooltip(ZOOM_LEVEL + (zoom - 1)));
	}

	private class ZoomChangeListener implements ChangeListener<Number> {

		private static final double ZOOM_DIFF = .01;
		
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			int zoom = newValue.intValue();
			
			setTooltip(zoom);
			
			zoomOutButton.setDisable(!(zoom > zoomable.getMinZoom()));
			zoomInButton.setDisable(!(zoom < zoomable.getMaxZoom()));

			if (Math.abs(zoomSlider.getValue() - zoom) > ZOOM_DIFF) {
				zoomSlider.setValue(zoom);
			}
		}

	}

}
