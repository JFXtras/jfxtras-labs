package jfxtras.labs.scene.control.gauge.linear;

public class AbsoluteMarker implements Marker {

	final private String id;
	final private double value;

	public AbsoluteMarker(double value, String id) {
		this.id = id;
		this.value = value;
	}
	
	public AbsoluteMarker(double value) {
		this.id = null;
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Marker#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Marker#getValue()
	 */
	@Override
	public double getValue() {
		return value;
	}
	
}
