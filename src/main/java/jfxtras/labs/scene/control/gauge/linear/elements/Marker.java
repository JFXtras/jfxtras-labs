package jfxtras.labs.scene.control.gauge.linear.elements;

public interface Marker {

	double getValue();

	default String getId() {
		return null;
	}
}