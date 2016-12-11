package jfxtras.labs.scene.layout.responsivepane;

/**
 * 
 */
public enum Device {
	DESKTOP,
	TABLET,
	PHONE;
	
	// ========================================================================================================================================================================================================
	// CONVENIENCE
	
	public Size size() {
		return DeviceSize.of(this);
	}
}