package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import javafx.scene.control.SkinBase;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;

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
}
