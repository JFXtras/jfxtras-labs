package jfxtras.labs.scene.layout.responsivepane;

public class Diagonal {
	
	public final static Diagonal ZERO = new Diagonal(0.0000000000, Unit.INCH);
	
	Unit unit;	
	double value;
	String device;

	/**
	 * 
	 * @param value
	 * @param unit
	 */
	Diagonal(double value, Unit unit) {
		this.value = value;
		this.unit = unit;
		this.device = null;
	}
	/**
	 * 
	 * @param device
	 */
	Diagonal(String device) {
		this.value = 0.0;
		this.unit = null;
		this.device = device;
	}
	
	// ========================================================================================================================================================================================================
	// Actual relevant size
	
	/**
	 * Convert the width to inches
	 * @param responsivePane 
	 * @return
	 */
	double toInches(ResponsivePane responsivePane) {
		if (device != null && unit == null) {
			Diagonal lDiagonal = responsivePane.getDeviceSize(device);
			value = lDiagonal.value;
			unit = lDiagonal.unit;
		}
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
	
	static public Diagonal inches(double v) {
		return new Diagonal(v, Unit.INCH);
	}
	
	static public Diagonal device(Device v) {
		return new Diagonal(v.toString());
	}
	
	static public Diagonal device(String v) {
		return new Diagonal(v.toString());
	}
	
	
	// ========================================================================================================================================================================================================
	// FXML
	
	/**
	 * @param s
	 * @return
	 */
	static public Diagonal valueOf(String s) {
		if (s.endsWith(Unit.INCH.suffix)) {
			return inches(Double.parseDouble(s.substring(0, s.length() - Unit.INCH.suffix.length())));
		}
		throw new IllegalArgumentException("Don't know how to parse '" + s + "'");
	}
	
	
	// ========================================================================================================================================================================================================
	// SUPPORT
	
	public String toString() {
		return value + unit.suffix
		     + (device == null ? "" : " (" + device + ")");
	}
}
