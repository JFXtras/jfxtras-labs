package jfxtras.labs.scene.control.gauge.linear;

public class PercentMarker implements Marker {

	final private String id;
	final private double valuePercent;
	final private LinearGauge<?> linearGauge;

	public PercentMarker(LinearGauge<?> linearGauge, double valuePercent, String id) {
		this.id = id;
		this.valuePercent = valuePercent;
		this.linearGauge = linearGauge;
	}
	
	public PercentMarker(LinearGauge<?> linearGauge, double valuePercent) {
		this.id = null;
		this.valuePercent = valuePercent;
		this.linearGauge = linearGauge;
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
		double minValue = linearGauge.getMinValue();
		double maxValue = linearGauge.getMaxValue();
		return minValue + ((maxValue - minValue) * valuePercent / 100.0);
	}
}
