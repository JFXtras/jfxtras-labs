package jfxtras.labs.scene.control.gauge.linear;

public interface Segment {

	double getMinValue();
	double getMaxValue();

	default String getId() {
		return null;
	}
}