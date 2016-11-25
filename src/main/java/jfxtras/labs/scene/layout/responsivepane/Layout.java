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
	
	public Layout() {
		
	}
	
	/**
	 * 
	 */
	public Layout(Double widthInInchesAtLeast, Node root) {
		setWidthInInchesAtLeast(widthInInchesAtLeast);
		setRoot(root);
	}
	
	/** Root */
	public ObjectProperty<Node> rootProperty() { return rootProperty; }
	final private SimpleObjectProperty<Node> rootProperty = new SimpleObjectProperty<>(this, "root", null);
	public Node getRoot() { return rootProperty.getValue(); }
	public void setRoot(Node value) { rootProperty.setValue(value); }
	public Layout withRoot(Node value) { setRoot(value); return this; } 

	/** WidthInInchesAtLeast (in inches) */
	public ObjectProperty<Double> widthInInchesAtLeastProperty() { return widthInInchesAtLeastProperty; }
	final private SimpleObjectProperty<Double> widthInInchesAtLeastProperty = new SimpleObjectProperty<>(this, "widthInInchesAtLeast", 0.0);
	public Double getWidthInInchesAtLeast() { return widthInInchesAtLeastProperty.getValue(); }
	public void setWidthInInchesAtLeast(Double value) { widthInInchesAtLeastProperty.setValue(value); }
	public Layout withWidthInInchesAtLeast(Double value) { setWidthInInchesAtLeast(value); return this; } 
}