package jfxtras.labs.scene.control.gauge.linear.elements;

import jfxtras.labs.scene.control.gauge.linear.LinearGauge;

public class PercentSegment implements Segment {

	final private String id;
	final private double minPercent;
	final private double maxPercent;
	final private LinearGauge<?> linearGauge;

	public PercentSegment(LinearGauge<?> linearGauge, double minPercent, double maxPercent, String id) {
		this.id = id;
		this.minPercent = minPercent;
		this.maxPercent = maxPercent;
		this.linearGauge = linearGauge;
	}
	
	public PercentSegment(LinearGauge<?> linearGauge, double minPercent, double maxPercent) {
		this.id = null;
		this.minPercent = minPercent;
		this.maxPercent = maxPercent;
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
		double minValue = linearGauge.getMinValue();
		double maxValue = linearGauge.getMaxValue();
		return minValue + ((maxValue - minValue) * minPercent / 100.0);
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Segment#getMax()
	 */
	@Override
	public double getMaxValue() {
		double minValue = linearGauge.getMinValue();
		double maxValue = linearGauge.getMaxValue();
		return minValue + ((maxValue - minValue) * maxPercent / 100.0);
	}
}
