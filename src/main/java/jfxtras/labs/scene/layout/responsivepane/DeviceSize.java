package jfxtras.labs.scene.layout.responsivepane;

public class DeviceSize extends Size {

	/**
	 * 
	 * @param device
	 */
	DeviceSize(String device) {
		this.device = device;
	}
	final String device;
	
	// ========================================================================================================================================================================================================
	// Actual relevant size
	
	/**
	 * Convert the width to inches
	 * @param responsivePane 
	 * @return
	 */
	double toInches(ResponsivePane responsivePane) {
		size = responsivePane.getDeviceSize(device);
		if (size == null) {
			throw new IllegalStateException("Device '" + device + "' is not defined in responsive Pane");
		}
		return size.toInches(responsivePane);
	}
	private Size size; // for logging in toString

	
	// ========================================================================================================================================================================================================
	// CONVENIENCE
	
	static public DeviceSize of(Device v) {
		return new DeviceSize(v.toString());
	}
	
	static public DeviceSize of(String v) {
		return new DeviceSize(v);
	}
	
	
	// ========================================================================================================================================================================================================
	// FXML
	
	/**
	 * @param s
	 * @return
	 */
	static public DeviceSize valueOf(String s) {
		return of(s);
	}
	
	
	// ========================================================================================================================================================================================================
	// SUPPORT
	
	public String toString() {
		return size + " (" + device + ")";
	}
}
