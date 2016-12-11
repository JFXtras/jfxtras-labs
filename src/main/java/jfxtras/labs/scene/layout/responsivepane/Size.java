package jfxtras.labs.scene.layout.responsivepane;

public abstract class Size {

	public final static Diagonal ZERO = new Diagonal(0.0000000000, Unit.INCH);	

	abstract double toInches(ResponsivePane responsivePane);
	
	// ========================================================================================================================================================================================================
	// FXML
	
	/**
	 * @param s
	 * @return
	 */
	static public Size valueOf(String s) {
		
		// width if proper prefix
		if (s.startsWith("w:")) {
			return Width.valueOf(s.substring("w:".length()));
		}
		if (s.startsWith("width:")) {
			return Width.valueOf(s.substring("width:".length()));
		}
		
		// if numeric, it must be a diagonal
		if (Character.isDigit(s.charAt(0))) {
			return Diagonal.valueOf(s);
		}
		
		// else it is a device
		return DeviceSize.valueOf(s);
	}
}
