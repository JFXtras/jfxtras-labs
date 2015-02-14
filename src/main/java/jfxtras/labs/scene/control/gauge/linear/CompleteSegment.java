package jfxtras.labs.scene.control.gauge.linear;

public class CompleteSegment implements Segment {

	public CompleteSegment(String id, LinearGauge linearGauge) {
		this.id = id;
		this.linearGauge = linearGauge;
	}
	final private String id;
	final private LinearGauge linearGauge;
	
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
		return linearGauge.getMinValue();
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Segment#getMax()
	 */
	@Override
	public double getMaxValue() {
		return linearGauge.getMaxValue();
	}
}
