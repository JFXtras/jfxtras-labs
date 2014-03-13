package jfxtras.labs.scene.layout;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class RadialPane extends Pane {

	/** StartAngle: the startAngle is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Double> startAngleProperty() { return iStartAngleObjectProperty; }
	final private ObjectProperty<Double> iStartAngleObjectProperty = new SimpleObjectProperty<Double>(this, "startAngle", 1.0);
	public Double getStartAngle() { return iStartAngleObjectProperty.getValue(); }
	public void setStartAngle(Double value) { iStartAngleObjectProperty.setValue(value); }
	public RadialPane withStartAngle(Double value) { setStartAngle(value); return this; } 


    @Override 
    protected double computeMinWidth(double height) {
    	return 50.0;
    }

    @Override 
    protected double computeMinHeight(double width) {
    	return 50.0;
    }

    @Override 
    protected double computePrefWidth(double height) {
    	return 100.0;
    }

    @Override 
    protected double computePrefHeight(double width) {
    	return 100.0;
    }

    @Override 
    protected void layoutChildren() {
    	layingoutChilderen++;
    	try {
	    	// get the nodes we need to render
	    	List<Node> nodes = getManagedChildren();
	    	if (nodes.size() == 0) {
	    		// nothing to do
	    		return;
	    	}
	    	
	    	// prepare layout
	    	double lMaximumSize = 0; // either width or height
	    	for (Node lNode : nodes) {
	    		double lWidth = calculateNodeWidth(lNode);
	    		if (lWidth > lMaximumSize) {
	    			lMaximumSize = lWidth;
	    		}
	    		double lHeight = calculateNodeHeight(lNode);
	    		if (lHeight > lMaximumSize) {
	    			lMaximumSize = lHeight;
	    		}
	    	}
	    	System.out.println("lMaximumSize= " + lMaximumSize);
	    	
	    	// layout
	    	double lPaneWidth = getWidth() - lMaximumSize;
	    	double lPaneHeight = getHeight() - lMaximumSize;
	    	double lAngleStep = -2 * Math.PI / nodes.size();
	    	double lAngle = Math.PI - getStartAngle();
	    	for (Node lNode : nodes) {
//	    		double lX = (lPaneWidth / 2) + (((lPaneWidth / 2) - lMaximumSize) * Math.sin(lAngle));
//	    		double lY = (lPaneHeight / 2) + (((lPaneHeight / 2) - lMaximumSize) * Math.cos(lAngle));
	    		double lX = (lPaneWidth / 2) + (lPaneWidth / 2 * Math.sin(lAngle));
	    		double lY = (lPaneHeight / 2) + (lPaneHeight / 2 * Math.cos(lAngle));
	    		setBounds(lNode, lX, lY, calculateNodeWidth(lNode), calculateNodeHeight(lNode));
	        	
	        	lAngle += lAngleStep;
	    	}
    	}
    	finally {
    		layingoutChilderen--;
    	}
    }
    int layingoutChilderen = 0;
    
    private void setBounds(Node n, double x, double y, double width, double height) {
		// for debugging 
		System.out.println(n + " setBound x="  + x + ",y=" + y + " / w=" + width + ",h=" + height + " / resizable=" + n.isResizable());
		n.resizeRelocate(x, y, width, height);
	}

    private double calculateNodeWidth(Node n) {
//    	Bounds lBounds = n.getLayoutBounds();
//    	return lBounds.getMinX() + lBounds.getWidth(); // TODO: how to properly determine the width of a node?
    	return n.prefWidth(-1);
    }
    
    private double calculateNodeHeight(Node n) {
//    	Bounds lBounds = n.getLayoutBounds();
//    	return lBounds.getMinY() + lBounds.getHeight(); // TODO: how to properly determine the height of a node?
    	return n.prefHeight(-1);
    }
}
