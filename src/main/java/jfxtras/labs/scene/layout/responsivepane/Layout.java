package jfxtras.labs.scene.layout.responsivepane;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 *
 */
@DefaultProperty("root")
public class Layout {
	
	/**
	 * 
	 */
	public Layout() {
		
	}
	
	/**
	 * 
	 */
	public Layout(Width widthAtLeast, Node root) {
		setWidthAtLeast(widthAtLeast);
		setRoot(root);
	}
	
	/**
	 * 
	 */
	public Layout(Width widthAtLeast, Orientation orientation, Node root) {
		setWidthAtLeast(widthAtLeast);
		setOrientation(orientation);
		setRoot(root);
	}
	
	/** Root */
	public ObjectProperty<Node> rootProperty() { return rootProperty; }
	final private SimpleObjectProperty<Node> rootProperty = new SimpleObjectProperty<>(this, "root", null);
	public Node getRoot() { return rootProperty.getValue(); }
	public void setRoot(Node value) { rootProperty.setValue(value); }
	public Layout withRoot(Node value) { setRoot(value); return this; } 

	/** WidthAtLeast */
	public ObjectProperty<Width> widthAtLeastProperty() { return widthAtLeastProperty; }
	final private SimpleObjectProperty<Width> widthAtLeastProperty = new SimpleObjectProperty<>(this, "widthAtLeast", Width.inches(0.0));
	public Width getWidthAtLeast() { return widthAtLeastProperty.getValue(); }
	public void setWidthAtLeast(Width value) { widthAtLeastProperty.setValue(value); }
	public Layout withWidthAtLeast(Width value) { setWidthAtLeast(value); return this; }

	/** Orientation */
	public ObjectProperty<Orientation> orientationProperty() { return orientationProperty; }
	final private SimpleObjectProperty<Orientation> orientationProperty = new SimpleObjectProperty<>(this, "orientation", null);
	public Orientation getOrientation() { return orientationProperty.getValue(); }
	public void setOrientation(Orientation value) { orientationProperty.setValue(value); }
	public Layout withOrientation(Orientation value) { setOrientation(value); return this; }
	
	public String toString() {
		return getWidthAtLeast() + (getOrientation() == null ? "" : "-" + getOrientation());
	}
}