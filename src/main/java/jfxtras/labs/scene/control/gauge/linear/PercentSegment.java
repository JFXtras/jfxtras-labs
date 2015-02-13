package jfxtras.labs.scene.control.gauge.linear;

public class PercentSegment implements Segment {

	public PercentSegment(String id, double minPercent, double maxPercent, LinearGauge linearGauge) {
		this.id = id;
		this.minPercent = minPercent;
		this.maxPercent = maxPercent;
		this.linearGauge = linearGauge;
	}
	final private String id;
	final private double minPercent;
	final private double maxPercent;
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
	public double getMin() {
		double minValue = linearGauge.getMinValue();
		double maxValue = linearGauge.getMaxValue();
		return minValue + ((maxValue - minValue) * minPercent / 100.0);
	}
	
	/* (non-Javadoc)
	 * @see jfxtras.labs.scene.control.gauge.linear.Segment#getMax()
	 */
	@Override
	public double getMax() {
		double minValue = linearGauge.getMinValue();
		double maxValue = linearGauge.getMaxValue();
		return minValue + ((maxValue - minValue) * maxPercent / 100.0);
	}
}
