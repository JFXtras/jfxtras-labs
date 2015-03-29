package jfxtras.labs.scene.control.gauge.linear;

import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.gauge.linear.BasicArcGaugeSkin;
import jfxtras.scene.control.ListSpinner;

/**
 * = BasicArcGauge
 * 
 *  
 * == Disclaimer
 * This is a blatant but approved visual copy of Gerrit Grunwald's Enzo RadialSteel (https://bitbucket.org/hansolo/enzo/src).
 * Gerrit describes the Enzo library in his blog like this: _all controls in that library are made for my personal demos and are not production ready._
 * This JFXtras control is supposed to be production ready. 
 */
public class BasicArcGauge extends LinearGauge<BasicArcGauge> {
	
	// ==================================================================================================================
	// LinearGauge

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet() {
		return ListSpinner.class.getResource("/jfxtras/labs/internal/scene/control/gauge/linear/" + BasicArcGauge.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new BasicArcGaugeSkin(this); 
	}
}
