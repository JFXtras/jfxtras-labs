package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import javafx.scene.control.SkinBase;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.labs.scene.control.gauge.linear.Marker;
import jfxtras.labs.scene.control.gauge.linear.Segment;

/**
 * 
 */
public class LinearGaugeSkin<C extends LinearGauge<?>> extends SkinBase<C> {

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LinearGaugeSkin(C control) {
		super(control);
	}

	// ==================================================================================================================
	// VALIDATE
	
	protected String validateValue() {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();
 		double controlValue = getSkinnable().getValue();
		if (controlMinValue > controlMaxValue) {
			return String.format("Min-value (%f) cannot be greater than max-value (%f)", controlMinValue, controlMaxValue);
		}
		if (controlMinValue > controlValue) {
			return String.format("Min-value (%f) cannot be greater than the value (%f)", controlMinValue, controlValue);
		}
		if (controlValue > controlMaxValue) {
			return String.format("Value (%f) cannot be greater than max-value (%f)", controlValue, controlMaxValue);
		}
		return null;
	}
	
	protected String validateSegments() {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();

 		for (Segment segment : getSkinnable().segments()) {
 	 		double segmentMinValue = segment.getMinValue();
 	 		double segmentMaxValue = segment.getMaxValue();
			if (segmentMinValue < controlMinValue) {
				return String.format("Segments min-value (%f) cannot be less than the controls min-value (%f)", segmentMinValue, controlMinValue);
			}
			if (segmentMaxValue > controlMaxValue) {
				return String.format("Segments max-value (%f) cannot be greater than the controls max-value (%f)", segmentMaxValue, controlMaxValue);
			}
 		}
 		return null;
	}
	
	protected String validateMarkers() {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();

 		for (Marker marker : getSkinnable().markers()) {
 	 		double markerValue = marker.getValue();
			if (markerValue < controlMinValue) {
				return String.format("Marker value (%f) cannot be less than the controls min-value (%f)", markerValue, controlMinValue);
			}
			if (markerValue > controlMaxValue) {
				return String.format("Marker max-value (%f) cannot be greater than the controls max-value (%f)", markerValue, controlMaxValue);
			}
 		}
 		return null;
	}
}
