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
    	return computePrefWidth(0);
    }

    @Override 
    protected double computeMinHeight(double width) {
    	return computePrefHeight(0);
    }

    @Override 
    protected double computePrefWidth(double height) {
    	double lMaximumSize = determineMaxSize(getManagedChildren());
    	double lSize = 5 * lMaximumSize;
    	return lSize;
    }

    @Override 
    protected double computePrefHeight(double width) {
    	double lMaximumSize = determineMaxSize(getManagedChildren());
    	double lSize = 5 * lMaximumSize;
    	return lSize;
    }

    @Override 
    protected double computeMaxWidth(double height) {
    	return computePrefWidth(0);
    }

    @Override 
    protected double computeMaxHeight(double width) {
    	return computePrefHeight(0);
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
	    		final double lX = (lPaneWidth / 2) + (lPaneWidth / 2 * Math.sin(lAngle));
	    		final double lY = (lPaneHeight / 2) + (lPaneHeight / 2 * Math.cos(lAngle));
	    		double lW = calculateNodeWidth(lNode);
	    		double lH = calculateNodeHeight(lNode);
	    		lNode.resize(lW, lH);
	    		lNode.setLayoutX(lX);
	    		lNode.setLayoutY(lY);
	    		
	    		
//	    		if (initial) {
//		    		lNode.setLayoutX( (lPaneWidth / 2) );
//		    		lNode.setLayoutY( (lPaneHeight / 2) );
//		    		initial = false;
//	    		}
//
//	    		// has the X and Y changed?
//	    		if ( (Math.abs(lNode.getLayoutX() - lX) > 0.001)
//	    		  || (Math.abs(lNode.getLayoutY() - lY) > 0.001)
//	    		   ) {
//	    			   
//		    		// while the animation is running, don't touch the children
//		        	layingoutChilderen.addAndGet(1);
//		    		new Transition() {
//		    			
//		    			{
//		    				setCycleDuration(Duration.millis(500));
//		    				setAutoReverse(false);
//		    				setCycleCount(1);
//		    				setOnFinished( (event) -> {
//		    			    	layingoutChilderen.addAndGet(-1);
//		    				});
//		    			}
//						
//						@Override
//						protected void interpolate(double progress) {
//				    		lNode.setLayoutX(lX * progress);
//				    		lNode.setLayoutY(lY * progress);
//						}
//					}.playFromStart();
//	    		}
	    		   
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
