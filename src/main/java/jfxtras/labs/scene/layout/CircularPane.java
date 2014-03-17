package jfxtras.labs.scene.layout;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

// http://www.guigarage.com/2012/11/custom-ui-controls-with-javafx-part-2/

/**
 * This pane lays it childeren out in a circle.
 * 
 * In order to understand how to tune this pane, it is important to understand how it does its layouting.
 * Placing nodes on a circle in essence is not that difficult; a circle is 360 degrees, so each node is spaced 360 / n degrees apart, the real challenge is to determine how large the circle must be.
 * Nodes in JavaFX are rectangles with a width and a height, but for calculating in a circle this is impractical. 
 * So CircularPane treats nodes as circles, or for better visualization: as beads on a chain.
 * 
 * The first step is to determine how large a single bead is. This already is an interesting question.
 * A beat should encompass the contents of the rectangular node, but CircularPane does not know what exactly is drawn in the node.
 * It could be a simple flat or vertical line, where the encompassing circle's diameter is equal to the width or height (whichever is the largest). 
 * But if the contents is an X, then then encompassing circle's diameter is equal to the diagonal (Pythagoras).
 * Since CircularPane does not know, it has to assume the worst and use the diameter. 
 *
 * CircularPane segments the 360 degrees in equal parts; 360 / number of children. 
 * The largest bead determines the distance from the origin to where it fits in a segment, and this determines the size of the circle.   
 * 
 * @author Tom Eugelink
 *
 */
public class CircularPane extends Pane {

	/** Id */
	public CircularPane withId(String v) { setId(v); return this; }
	
	/** StartAngle in degrees: the startAngle is used to determine the starting position; default = 0 = north (top) */
	public ObjectProperty<Double> startAngleProperty() { return iStartAngleObjectProperty; }
	final private ObjectProperty<Double> iStartAngleObjectProperty = new SimpleObjectProperty<Double>(this, "startAngle", 0.0);
	public Double getStartAngle() { return iStartAngleObjectProperty.getValue(); }
	public void setStartAngle(Double value) { iStartAngleObjectProperty.setValue(value); }
	public CircularPane withStartAngle(Double value) { setStartAngle(value); return this; } 

	/** childrenAreCircular: if all childeren are circular, then we can use a different size */
	public ObjectProperty<Boolean> childrenAreCircularProperty() { return iChildrenAreCircularObjectProperty; }
	final private ObjectProperty<Boolean> iChildrenAreCircularObjectProperty = new SimpleObjectProperty<Boolean>(this, "childrenAreCircular", false);
	public Boolean getChildrenAreCircular() { return iChildrenAreCircularObjectProperty.getValue(); }
	public void setChildrenAreCircular(Boolean value) { iChildrenAreCircularObjectProperty.setValue(value); }
	public CircularPane withChildrenAreCircular(Boolean value) { setChildrenAreCircular(value); return this; } 

	/** debug: show debug hints */
	public ObjectProperty<Paint> showDebugProperty() { return iShowDebugObjectProperty; }
	final private ObjectProperty<Paint> iShowDebugObjectProperty = new SimpleObjectProperty<Paint>(this, "showDebug", null);
	public Paint getShowDebug() { return iShowDebugObjectProperty.getValue(); }
	public void setShowDebug(Paint value) { iShowDebugObjectProperty.setValue(value); }
	public CircularPane withShowDebug(Paint value) { setShowDebug(value); return this; } 

    @Override 
    protected double computeMinWidth(double height) {
    	List<Node> nodes = getManagedChildren();
    	int numberOfNodes = nodes.size() - nodeToBeadMap.size();
    	double lMaximumSize = 0;
    	if (getChildrenAreCircular()) {
    		lMaximumSize = determineMaxWidthHeight(nodes);
    	}
    	else {
    		lMaximumSize = determineMaxDiameter(nodes);
    	}
    	if (numberOfNodes == 0) {
    		return 0;
    	}
    	if (numberOfNodes == 1) {
    		return lMaximumSize;
    	}
    	if (numberOfNodes == 2) {
    		return 2 * lMaximumSize;
    	}
    	// determine the size of the circle where the centre of the bead would be placed on 
    	double lDiameter = lMaximumSize / Math.sin(2 * Math.PI / numberOfNodes / 2);
    	lDiameter += lMaximumSize; // but of course we need the outer circle
    	return lDiameter;
    }

    @Override 
    protected double computeMinHeight(double width) {
    	return computeMinWidth(-1);
    }

    @Override 
    protected double computePrefWidth(double height) {
    	return computeMinWidth(-1);
    }

    @Override 
    protected double computePrefHeight(double width) {
    	return computeMinHeight(-1);
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

    		// remove all beads
    		getChildren().removeAll(nodeToBeadMap.values());
    		nodeToBeadMap.clear();
    		
	    	// get the nodes we need to render
	    	List<Node> nodes = getManagedChildren();
	    	if (nodes.size() == 0) {
	    		// nothing to do
	    		return;
	    	}
	    	int numberOfNodes = nodes.size() - nodeToBeadMap.size();
	    	
	    	double lMaximumSize = 0;
	    	if (getChildrenAreCircular()) {
	    		lMaximumSize = determineMaxWidthHeight(nodes);
	    	}
	    	else {
	    		lMaximumSize = determineMaxDiameter(nodes);
	    	}
	    	double lBeadWidth = lMaximumSize; 
	    	
	    	// layout
	    	double lPaneWidth = getWidth() - lMaximumSize; // must allow for room to the left and right  
	    	double lPaneHeight = getHeight() - lMaximumSize;  // must allow for room to the left and right
	    	double lAngleStep = -2 * Math.PI / numberOfNodes;
	    	double lAngle = Math.PI - degreesToRadials(getStartAngle());
	    	for (final Node lNode : nodes) {
	    		// beads are not laid out directly, through their associated node
	    		if (lNode instanceof Bead) {
	    			continue;
	    		}
	    		
	    		Bounds lBounds = lNode.getBoundsInParent();
	    		 
	    		final double lX = (lPaneWidth / 2) // from the center 
	    				        + (lPaneWidth / 2 * Math.sin(lAngle)) // determine the position on the chain
	    				        ;
	    		final double lY = (lPaneHeight / 2) 
	    				        + (lPaneHeight / 2 * Math.cos(lAngle)) 
	    				        ;
	    		
	    		// place a bead to show where this node should be
	    		if (getShowDebug() != null) {
		    		Bead lBead = new Bead();
		    		lBead.setRadius(lBeadWidth / 2);
		    		nodeToBeadMap.put(lNode, lBead);
		    		lBead.setLayoutX(lX + (lBeadWidth / 2));
		    		lBead.setLayoutY(lY + (lBeadWidth / 2));
		    		getChildren().add(lBead);
	    		}	    		

	    		// now place the node
	    		double lW = calculateNodeWidth(lNode);
	    		double lH = calculateNodeHeight(lNode);
	    		lNode.resize(lW, lH);
	    		
	    		// place on the right spot immediately
	    		lNode.setLayoutX( lX 
	    				       + ((lBeadWidth - lW) / 2) 
	    				       - (lNode instanceof Parent ? 0 : lBounds.getMinX()) // correct primitives with their bounds (primitives have their centre at 0,0, so minX is < 0) 
	    				       ); 
	    		lNode.setLayoutY( lY 
	    				        + ((lBeadWidth - lH) / 2) 
	    				        - (lNode instanceof Parent ? 0 : lBounds.getMinY()) // correct primitives with their bounds (primitives have their centre at 0,0, so minX is < 0) 
	    				        ); 
	    		

	    		// animate from the center
//	    		final double lXi = (lPaneWidth / 2) + (lMaximumSize / 2);
//	    		final double lYi = (lPaneWidth / 2) + (lMaximumSize / 2);
//	    		if (initial) {
//		    		lNode.setLayoutX( lXi );
//		    		lNode.setLayoutY( lYi );
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
//				    		lNode.setLayoutX( lX + ((lXi - lX) * (1-progress)));
//				    		lNode.setLayoutY( lY + ((lYi - lY) * (1-progress)));
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
	Map<Node, Bead> nodeToBeadMap = new WeakHashMap<>();
    boolean initial = true;
    
    private class Bead extends Circle {
    	public Bead() {
    		super();
    		setFill(null);
    		setStroke(getShowDebug());
    	}
    }
    
	private double determineMaxWidthHeight(List<Node> nodes) {
		double lMaximumSize = 0; 
		for (Node lNode : nodes) {
			if (lNode instanceof Bead) {
				continue;
			}
			double lWidth = calculateNodeWidth(lNode);
			if (lWidth > lMaximumSize) {
				lMaximumSize = lWidth;
			}
			double lHeight = calculateNodeHeight(lNode);
			if (lHeight > lMaximumSize) {
				lMaximumSize = lHeight;
			}
		}
		return lMaximumSize;
	}
    
	private double determineMaxDiameter(List<Node> nodes) {
		double lMaximumSize = 0; 
		for (Node lNode : nodes) {
			if (lNode instanceof Bead) {
				continue;
			}
			double lWidth = calculateNodeWidth(lNode);
			double lHeight = calculateNodeHeight(lNode);
			double lSize = Math.sqrt( (lWidth * lWidth) + (lHeight * lHeight) );
			if (lSize > lMaximumSize) {
				lMaximumSize = lSize;
			}
		}
		return lMaximumSize;
	}
    
    private double calculateNodeWidth(Node n) {
    	return n.prefWidth(-1);
    }
    
    private double calculateNodeHeight(Node n) {
    	return n.prefHeight(-1);
    }
    
    private double degreesToRadials(double d) {
    	return d / 360 * 2 * Math.PI;
    }
}
