package jfxtras.labs.scene.control.gauge.linear;

public interface Marker {

	double getValue();

	default String getId() {
		return null;
	}
}