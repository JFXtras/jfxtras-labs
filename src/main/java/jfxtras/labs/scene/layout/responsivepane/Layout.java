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
	public Layout(Diagonal sizeAtLeast, Node root) {
		setSizeAtLeast(sizeAtLeast);
		setRoot(root);
	}
	
	/**
	 * 
	 */
	public Layout(Diagonal sizeAtLeast, Orientation orientation, Node root) {
		setSizeAtLeast(sizeAtLeast);
		setOrientation(orientation);
		setRoot(root);
	}
	
	/** Root */
	public ObjectProperty<Node> rootProperty() { return rootProperty; }
	final private SimpleObjectProperty<Node> rootProperty = new SimpleObjectProperty<>(this, "root", null);
	public Node getRoot() { return rootProperty.getValue(); }
	public void setRoot(Node value) { rootProperty.setValue(value); }
	public Layout withRoot(Node value) { setRoot(value); return this; } 

	/** sizeAtLeast */
	public ObjectProperty<Diagonal> sizeAtLeastProperty() { return sizeAtLeastProperty; }
	final private SimpleObjectProperty<Diagonal> sizeAtLeastProperty = new SimpleObjectProperty<>(this, "sizeAtLeast", Diagonal.inches(0.0));
	public Diagonal getSizeAtLeast() { return sizeAtLeastProperty.getValue(); }
	public void setSizeAtLeast(Diagonal value) { sizeAtLeastProperty.setValue(value); }
	public Layout withSizeAtLeast(Diagonal value) { setSizeAtLeast(value); return this; }

	/** Orientation */
	public ObjectProperty<Orientation> orientationProperty() { return orientationProperty; }
	final private SimpleObjectProperty<Orientation> orientationProperty = new SimpleObjectProperty<>(this, "orientation", null);
	public Orientation getOrientation() { return orientationProperty.getValue(); }
	public void setOrientation(Orientation value) { orientationProperty.setValue(value); }
	public Layout withOrientation(Orientation value) { setOrientation(value); return this; }
	
	public String toString() {
		return getSizeAtLeast() + (getOrientation() == null ? "" : "-" + getOrientation());
	}
}