package jfxtras.labs.scene.layout.responsivepane;

public class Width {
	
	public final static Width ZERO = new Width(0.0000000000, Unit.INCH);
	
	/**
	 * 
	 * @param value
	 * @param unit
	 */
	public Width(double value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}
	final Unit unit;	
	final double value;
	
	/**
	 * Convert the width to inches
	 * @return
	 */
	public double toInches() {
		return unit.toInches(value);
	}
	
	// ========================================================================================================================================================================================================
	// Unit
	
	enum Unit {
		INCH("in");
		
		private Unit(String suffix){
	        this.suffix = suffix;
	    }
	    private final String suffix;
	    
		public double toInches(double value) {
			return value;
		}	    
	}
	
	// ========================================================================================================================================================================================================
	// CONVENIENCE
	
	static public Width inches(double v) {
		return new Width(v, Unit.INCH);
	}
	
	static public Width valueOf(String s) {
		if (s.endsWith(Unit.INCH.suffix)) {
			return inches(Double.parseDouble(s.substring(0, s.length() - Unit.INCH.suffix.length())));
		}
		throw new IllegalArgumentException("Don't know how to parse '" + s + "'");
	}
	
	public String toString() {
		return value + unit.suffix;
	}
}
