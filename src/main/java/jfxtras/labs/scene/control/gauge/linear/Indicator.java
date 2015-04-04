package jfxtras.labs.scene.control.gauge.linear;

public class Indicator {
	
	final private int idx;
	final private String id;

	public Indicator(int idx, String id) {
		this.idx = idx;
		if (id == null) {
			throw new IllegalArgumentException("An id is mandatory");
		}
		this.id = id;
	}

	public int getIdx() {
		return idx;
	}

	public String getId() {
		return id;
	}	
}
