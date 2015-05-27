package jfxtras.labs.scene.control.gauge.linear.elements;

public interface Segment {

	double getMinValue();
	double getMaxValue();

	default String getId() {
		return null;
	}
}