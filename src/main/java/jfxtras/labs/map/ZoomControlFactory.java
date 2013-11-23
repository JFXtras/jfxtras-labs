/**
 * ZoomControlFactory.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
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

	private static final int GAP = 5;

	private static final String ZOOM_LEVEL = "Zoom to level ";

	protected Zoomable zoomable;

	private ZoomSlider zoomSlider;

	private Button zoomInButton;

	private Button zoomOutButton;
	
	private boolean ignore;

	public Pane create(Zoomable zoomable) {
		
		this.zoomable = zoomable;

		zoomSlider = new ZoomSlider(zoomable);
		
		zoomSlider.valueProperty().addListener(new ZoomSliderChangeListener(zoomable));
		
		zoomable.minZoomProperty().addListener(new ZoomMinChangeListener());
		zoomable.maxZoomProperty().addListener(new ZoomMaxChangeListener());

		ZoomButtonFactory zoomButtonFactory = new ZoomInButtonFactory(zoomable);
		zoomInButton = zoomButtonFactory.create();

		zoomButtonFactory = new ZoomOutButtonFactory(zoomable);
		zoomOutButton = zoomButtonFactory.create();
		
		setTooltip(zoomable.zoomProperty().get());
		updateButtons(zoomable.zoomProperty().get());
		
		zoomable.zoomProperty().addListener(new ZoomChangeListener());

		Pane pane = new VBox(GAP);
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
	
	private void updateButtons(int zoom) {
		zoomOutButton.setDisable(!(zoom > zoomable.minZoomProperty().get()));
		zoomInButton.setDisable(!(zoom < zoomable.maxZoomProperty().get()));
	}

	private class ZoomChangeListener implements ChangeListener<Number> {

		private static final double ZOOM_DIFF = .01;
		
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			int zoom = newValue.intValue();
			
			setTooltip(zoom);
			
			updateButtons(zoom);

			if (Math.abs(zoomSlider.getValue() - zoom) > ZOOM_DIFF) {
				ignore = true;
				zoomSlider.setValue(zoom);
				ignore = false;
			}
		}
	}
	
	private class ZoomMinChangeListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> ov,
				Number oldVal, Number newVal) {
			ignore = true;
			double min = newVal.doubleValue();
			zoomSlider.adjustScale(min, zoomSlider.getMax());
			zoomSlider.setMin(min);
			ignore = false;
		}
	}
	
	private class ZoomMaxChangeListener implements ChangeListener<Number> {
		
		@Override
		public void changed(ObservableValue<? extends Number> ov,
				Number oldVal, Number newVal) {
			ignore = true;
			double max = newVal.doubleValue();
			zoomSlider.adjustScale(zoomSlider.getMin(), max);
			zoomSlider.setMax(max);
			ignore = false;
		}
	}
	
	private class ZoomSliderChangeListener implements ChangeListener<Number> {
		
		private Zoomable zoomable;
		
		public ZoomSliderChangeListener(Zoomable zoomable) {
			this.zoomable = zoomable;
		}
		
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
            if(!ignore){
            	int newZoom = newVal.intValue();
 				zoomable.zoomProperty().set(newZoom);
            }
        }
	}

}
