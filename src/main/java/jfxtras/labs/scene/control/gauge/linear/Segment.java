package jfxtras.labs.scene.control.gauge.linear;

public class Segment {

	public Segment(String id, double min, double max) {
		this.id = id;
		this.min = min;
		this.max = max;
	}
	final private String id;
	final private double min;
	final private double max;
	
	public String getId() {
		return id;
	}
	
	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}
}
