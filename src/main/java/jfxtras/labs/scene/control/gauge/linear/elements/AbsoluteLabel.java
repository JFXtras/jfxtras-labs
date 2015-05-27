package jfxtras.labs.scene.control.gauge.linear.elements;

public class AbsoluteLabel implements Label {

	final private String id;
	final private double value;

	public AbsoluteLabel(double value, String id) {
		this.id = id;
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Marker#getId()
	 */
	@Override
	public String getText() {
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
