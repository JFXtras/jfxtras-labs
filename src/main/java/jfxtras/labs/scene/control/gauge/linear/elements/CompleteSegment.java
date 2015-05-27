package jfxtras.labs.scene.control.gauge.linear.elements;

import jfxtras.labs.scene.control.gauge.linear.LinearGauge;

public class CompleteSegment implements Segment {

	final private String id;
	final private LinearGauge<?> linearGauge;
	
	public CompleteSegment(LinearGauge<?> linearGauge, String id) {
		this.id = id;
		this.linearGauge = linearGauge;
	}
	
	public CompleteSegment(LinearGauge<?> linearGauge) {
		this.id = null;
		this.linearGauge = linearGauge;
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
