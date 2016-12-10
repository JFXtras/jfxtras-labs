package jfxtras.labs.scene.layout.responsivepane;

import javafx.scene.Scene;

public class Width extends Size {
	
	public final static Width ZERO = new Width(0.0000000000, Unit.INCH);
	
	Unit unit;	
	double value;

	/**
	 * 
	 * @param value
	 * @param unit
	 */
	Width(double value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}
	
	// ========================================================================================================================================================================================================
	// Actual relevant size
	
	/**
	 * Convert the width to inches
	 * @param responsivePane 
	 * @return
	 */
	double toInches(ResponsivePane responsivePane) {
		
		// convert width to diagonal by using the current size of the pane
		Scene lScene = responsivePane.getScene();
		double lHeightInInches = lScene.getHeight() / responsivePane.determinePPI();
		double lWidthInInches = unit.toInches(value);		
		double lDiagonalInInches = Math.sqrt( (lWidthInInches * lWidthInInches) + (lHeightInInches + lHeightInInches));
		return lDiagonalInInches;
	}


	// ========================================================================================================================================================================================================
	// CONVENIENCE
	
	static public Width inches(double v) {
		return new Width(v, Unit.INCH);
	}
	
	// ========================================================================================================================================================================================================
	// FXML
	
	/**
	 * @param s
	 * @return
	 */
	static public Width valueOf(String s) {
		if (s.endsWith(Unit.INCH.suffix)) {
			return inches(Double.parseDouble(s.substring(0, s.length() - Unit.INCH.suffix.length())));
		}
		throw new IllegalArgumentException("Don't know how to parse '" + s + "'");
	}
	
	
	// ========================================================================================================================================================================================================
	// SUPPORT
	
	public String toString() {
		return "width=" + value + unit.suffix;
	}
}
