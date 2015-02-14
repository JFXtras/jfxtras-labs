package jfxtras.labs.scene.control.gauge.linear;

public class AbsoluteSegment implements Segment {

	final private String id;
	final private double min;
	final private double max;

	public AbsoluteSegment(String id, double min, double max) {
		this.id = id;
		this.min = min;
		this.max = max;
	}
	
	public AbsoluteSegment(double min, double max) {
		this.id = null;
		this.min = min;
		this.max = max;
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Segment#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Segment#getMin()
	 */
	@Override
	public double getMinValue() {
		return min;
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Segment#getMax()
	 */
	@Override
	public double getMaxValue() {
		return max;
	}
}
