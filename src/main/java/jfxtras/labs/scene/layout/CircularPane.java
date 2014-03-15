package jfxtras.labs.scene.layout;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

// http://www.guigarage.com/2012/11/custom-ui-controls-with-javafx-part-2/
public class CircularPane extends Pane {

	/** StartAngle: the startAngle is used to determine the starting position; default = 0 = north (top) */
	public ObjectProperty<Double> startAngleProperty() { return iStartAngleObjectProperty; }
	final private ObjectProperty<Double> iStartAngleObjectProperty = new SimpleObjectProperty<Double>(this, "startAngle", 0.0);
	public Double getStartAngle() { return iStartAngleObjectProperty.getValue(); }
	public void setStartAngle(Double value) { iStartAngleObjectProperty.setValue(value); }
	public CircularPane withStartAngle(Double value) { setStartAngle(value); return this; } 

	public CircularPane withId(String v) { setId(v); return this; }
	
    @Override 
    protected double computeMinWidth(double height) {
    	return computePrefWidth(-1);
    }

    @Override 
    protected double computeMinHeight(double width) {
    	return computePrefHeight(-1);
    }

    @Override 
    protected double computePrefWidth(double height) {
//    	double lMaximumSize = determineMaxSize(getManagedChildren());
//    	double lSize = 5 * lMaximumSize;
//    	return lSize;
    	
    	List<Node> nodes = getManagedChildren();
    	double lMaximumSize = determineMaxSize(nodes);
    	if (nodes.size() == 0) {
    		return 0;
    	}
    	if (nodes.size() == 1) {
    		return lMaximumSize;
    	}
    	if (nodes.size() == 2) {
    		return 2 * lMaximumSize;
    	}
//    	double lDiameter = lMaximumSize / Math.sin( 2*Math.PI / nodes.size() );
//    	lDiameter += lMaximumSize;
    	
    	double lDiameter = ((2 * nodes.size() * lMaximumSize) / (2 * Math.PI)) + lMaximumSize;
    	
    	return lDiameter;
    }

    @Override 
    protected double computePrefHeight(double width) {
//    	double lMaximumSize = determineMaxSize(getManagedChildren());
//    	double lSize = 5 * lMaximumSize;
//    	return lSize;
    	return computePrefWidth(-1);
    }

    @Override 
    protected double computeMaxWidth(double height) {
    	return computePrefWidth(-1);
    }

    @Override 
    protected double computeMaxHeight(double width) {
    	return computePrefHeight(-1);
    }

    @Override 
    protected void layoutChildren() {
    	if (layingoutChilderen.get() > 0) {
    		return;
    	}    	
    	layingoutChilderen.addAndGet(1);
    	try {
    		
	    	// get the nodes we need to render
	    	List<Node> nodes = getManagedChildren();
	    	if (nodes.size() == 0) {
	    		// nothing to do
	    		return;
	    	}
	    	
	    	double lMaximumSize = determineMaxSize(nodes);
	    	
	    	// layout
	    	double lPaneWidth = getWidth() - lMaximumSize;
	    	double lPaneHeight = getHeight() - lMaximumSize;
	    	double lAngleStep = -2 * Math.PI / nodes.size();
	    	double lAngle = Math.PI - getStartAngle();
	    	for (final Node lNode : nodes) {
	    		final double lX = (lPaneWidth / 2) + (lMaximumSize / 2) + (lPaneWidth / 2 * Math.sin(lAngle));
	    		final double lY = (lPaneHeight / 2) + (lMaximumSize / 2) + (lPaneHeight / 2 * Math.cos(lAngle));
	    		double lW = calculateNodeWidth(lNode);
	    		double lH = calculateNodeHeight(lNode);
	    		lNode.resize(lW, lH);
//	    		lNode.setLayoutX(lX);
//	    		lNode.setLayoutY(lY);
	    		
	    		
	    		final double lXi = (lPaneWidth / 2) + (lMaximumSize / 2);
	    		final double lYi = (lPaneWidth / 2) + (lMaximumSize / 2);
	    		if (initial) {
		    		lNode.setLayoutX( lXi );
		    		lNode.setLayoutY( lYi );
		    		initial = false;
	    		}

	    		// has the X and Y changed?
	    		if ( (Math.abs(lNode.getLayoutX() - lX) > 0.001)
	    		  || (Math.abs(lNode.getLayoutY() - lY) > 0.001)
	    		   ) {
	    			   
		    		// while the animation is running, don't touch the children
		        	layingoutChilderen.addAndGet(1);
		    		new Transition() {
		    			
		    			{
		    				setCycleDuration(Duration.millis(500));
		    				setAutoReverse(false);
		    				setCycleCount(1);
		    				setOnFinished( (event) -> {
		    			    	layingoutChilderen.addAndGet(-1);
		    				});
		    			}
						
						@Override
						protected void interpolate(double progress) {
				    		lNode.setLayoutX( lX + ((lXi - lX) * (1-progress)));
				    		lNode.setLayoutY( lY + ((lYi - lY) * (1-progress)));
						}
					}.playFromStart();
	    		}
	    		   
				// next
	        	lAngle += lAngleStep;
	    	}
    	}
    	finally {
        	layingoutChilderen.addAndGet(-1);
    	}
    }
    private AtomicInteger layingoutChilderen = new AtomicInteger(0);
    boolean initial = true;
    
	private double determineMaxSize(List<Node> nodes) {
		double lMaximumSize = 0; // diameter of the emcompassing circle
		for (Node lNode : nodes) {
			double lWidth = calculateNodeWidth(lNode);
			double lHeight = calculateNodeHeight(lNode);
			double lSize = Math.sqrt( (lWidth * lWidth) + (lHeight * lHeight) );
			if (lSize > lMaximumSize) {
				lMaximumSize = lSize;
			}
		}
//		System.out.println(getId() + " lMaximumSize= " + lMaximumSize);
		return lMaximumSize;
	}
    
    private double calculateNodeWidth(Node n) {
    	return n.prefWidth(-1);
    }
    
    private double calculateNodeHeight(Node n) {
    	return n.prefHeight(-1);
    }
}
