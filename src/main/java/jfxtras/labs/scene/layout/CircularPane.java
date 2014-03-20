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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


/**
 * This pane lays it children out in a circle.
 * 
 * In order to understand how to tune this pane, it is important to understand how it does its places its children.
 * Placing nodes on a circle in essence is not that difficult; a circle is 360 degrees, so each node is spaced 360 / n degrees apart, the real challenge is to determine how large the circle must be.
 * Nodes in JavaFX are rectangles with a width and a height, but for calculating in a circle the rectangular shape is impractical. 
 * So CircularPane treats its child nodes as circles, or for better visualization: as beads on a chain.
 * 
 * The first step is to determine how large a single bead is. This already is an interesting question.
 * A beat should encompass the contents of the node, but CircularPane does not know what exactly is drawn in the node.
 * It could be a simple flat or vertical line, where the encompassing circle's diameter is equal to the width or height (whichever is the largest). 
 * But if the contents is an X or a rectangle, then then encompassing circle's diameter is equal to the diagonal (Pythagoras).
 * Since CircularPane does not know, it has to assume the worst and use the diameter.
 * But the childrenAreCircles property allows the user to inform CircularPane than all the children are circular (or smaller), so it can then use the width or height to calculate the encompassing circle.  
 *
 * CircularPane segments the 360 degrees in equal parts; 360 / number of children. 
 * The largest bead determines the distance from the origin to where it fits in a segment, and this determines the size of the circle.
 * By setting a debug color, the beads will be drawn.
 * 
 * @author Tom Eugelink
 *
 */
public class CircularPane extends Pane {

	private enum SIZE { MIN, PREF, MAX }

	
	// ==========================================================================================================================================================================================================================================
	// properties

	/** Id */
	public CircularPane withId(String v) { setId(v); return this; }
	
	/** StartAngle in degrees: the startAngle is used to determine the starting position; default = 0 = north (top) */
	public ObjectProperty<Double> startAngleProperty() { return startAngleObjectProperty; }
	final private ObjectProperty<Double> startAngleObjectProperty = new SimpleObjectProperty<Double>(this, "startAngle", 0.0) {
		public void set(Double v) {
			if (v == null) {
				throw new IllegalArgumentException(getName() + " cannot be null");
			}
			if (v < 0.0 || v > 359.0) {
				throw new IllegalArgumentException(getName() + " must be between 0 and 359");
			}
			super.set(v);
		}
	};
	public Double getStartAngle() { return startAngleObjectProperty.getValue(); }
	public void setStartAngle(Double value) { startAngleObjectProperty.setValue(value); }
	public CircularPane withStartAngle(Double value) { setStartAngle(value); return this; } 

	/** arc in degrees: the arc is used to determine the eind position; default = 360 = north (top) */
	public ObjectProperty<Double> arcProperty() { return arcObjectProperty; }
	final private ObjectProperty<Double> arcObjectProperty = new SimpleObjectProperty<Double>(this, "arc", 360.0) {
		public void set(Double v) {
			if (v == null) {
				throw new IllegalArgumentException(getName() + " cannot be null");
			}
			if (v < 1.0 || v > 360.0) {
				throw new IllegalArgumentException(getName() + " must be between 1 and 360");
			}
			super.set(v);
		}
	};
	public Double getArc() { return arcObjectProperty.getValue(); }
	public void setArc(Double value) { arcObjectProperty.setValue(value); }
	public CircularPane withArc(Double value) { setArc(value); return this; } 

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

	
	// ==========================================================================================================================================================================================================================================
	// computer size
	
    @Override 
    protected double computeMinWidth(double height) {
    	return computeChainDiameter(SIZE.MIN);
    }

    @Override 
    protected double computeMinHeight(double width) {
    	return computeChainDiameter(SIZE.MIN);
    }

    @Override 
    protected double computePrefWidth(double height) {
    	return computeChainDiameter(SIZE.PREF);
    }

    @Override 
    protected double computePrefHeight(double width) {
    	return computeChainDiameter(SIZE.PREF);
    }

    @Override 
    protected double computeMaxWidth(double height) {
    	// In 'normal' layout logic these computed sizes would denote an ability or preference.
    	// Min would indicate the minimal size the node or layout is able to render itself, 
    	// Pref the preferred size, 
    	// and Max the maximum size a node is ABLE to render itself.
    	//
    	// If a node were given more space to render itself, without any further instructions from the user (layout / pane), it should still stick to its preferred size, because that is its preferred size after all.
    	//
    	// However, in JavaFX Max does not denote an ability, but an intent / preference.
    	// If a node indicates it has a max of, say, Double.MAX_VALUE (as CircularPane should do, because it is able to render itself on that size),
    	// the Pane implementations in JavaFX will actually make CircularPane grow into additional space, instead of keeping its preferred size.
    	// In my opinion this is wrong, but unfortunately the way things are in JavaFX.
    	// 
    	// Therefore the Max size is the preferred size. 
    	return computeChainDiameter(SIZE.PREF);
    }

    @Override 
    protected double computeMaxHeight(double width) {
    	// For an explanation, see computerMaxWidth
    	return computeChainDiameter(SIZE.PREF);
    }

	
	// ==========================================================================================================================================================================================================================================
	// layout

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
	    		lMaximumSize = determineBeadDiameterUsingWidthOrHeight(nodes, SIZE.PREF);
	    	}
	    	else {
	    		lMaximumSize = determineBeadDiameterUsingTheDiagonal(nodes, SIZE.PREF);
	    	}
	    	double lBeadWidth = lMaximumSize; 
	    	
	    	// layout
	    	double lPaneWidth = getWidth() - lMaximumSize; // must allow for room to the left and right  
	    	double lPaneHeight = getHeight() - lMaximumSize;  // must allow for room to the left and right
	    	double lAngleStep = getArc() / numberOfNodes;
	    	double lAngle = 180 - getStartAngle();
	    	for (final Node lNode : nodes) {
	    		// beads are not laid out directly, through their associated node
	    		if (lNode instanceof Bead) {
	    			continue;
	    		}
	    		
	    		Bounds lBounds = lNode.layoutBoundsProperty().get();
	    		 
	    		final double lX = (lPaneWidth / 2) // from the center
	    				        + (lPaneWidth / 2 * Math.sin(degreesToRadials(lAngle))) // determine the position on the chain
	    				        ;
	    		final double lY = (lPaneHeight / 2) 
	    				        + (lPaneHeight / 2 * Math.cos(degreesToRadials(lAngle))) 
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
	    		double lW = calculateNodeWidth(lNode, SIZE.PREF);
	    		double lH = calculateNodeHeight(lNode, SIZE.PREF);
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
	private final Map<Node, Bead> nodeToBeadMap = new WeakHashMap<>();
//    private boolean initial = true;

	
	// ==========================================================================================================================================================================================================================================
	// support
    
    private class Bead extends Circle {
    	public Bead() {
    		super();
    		setFill(null);
    		setStroke(getShowDebug());
    	}
    }
    
    private double computeChainDiameter(SIZE size) {
    	List<Node> nodes = getManagedChildren();
    	nodes.removeAll(nodeToBeadMap.values());
    	int numberOfNodes = nodes.size();
    	double lMaximumSize = 0;
    	if (getChildrenAreCircular()) {
    		lMaximumSize = determineBeadDiameterUsingWidthOrHeight(nodes, size);
    	}
    	else {
    		lMaximumSize = determineBeadDiameterUsingTheDiagonal(nodes, size);
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
    	// determine the size of the circle where the center of the bead would be placed on (Daan's formula) 
    	double lDiameter = lMaximumSize / Math.sin(degreesToRadials(360 / (numberOfNodes * 360 / getArc()) / 2));
    	lDiameter += lMaximumSize; // but of course we need the outer circle
    	return lDiameter;
    }

	private double determineBeadDiameterUsingWidthOrHeight(List<Node> nodes, SIZE size) {
		double lMaximumSize = 0; 
		for (Node lNode : nodes) {
			if (lNode instanceof Bead) {
				continue;
			}
			double lWidth = calculateNodeWidth(lNode, size);
			if (lWidth > lMaximumSize) {
				lMaximumSize = lWidth;
			}
			double lHeight = calculateNodeHeight(lNode, size);
			if (lHeight > lMaximumSize) {
				lMaximumSize = lHeight;
			}
		}
		return lMaximumSize;
	}
    
	private double determineBeadDiameterUsingTheDiagonal(List<Node> nodes, SIZE size) {
		double lMaximumSize = 0; 
		for (Node lNode : nodes) {
			if (lNode instanceof Bead) {
				continue;
			}
			double lWidth = calculateNodeWidth(lNode, size);
			double lHeight = calculateNodeHeight(lNode, size);
			double lSize = Math.sqrt( (lWidth * lWidth) + (lHeight * lHeight) );
			if (lSize > lMaximumSize) {
				lMaximumSize = lSize;
			}
		}
		return lMaximumSize;
	}
    
    private double calculateNodeWidth(Node n, SIZE size) {
    	if (size == SIZE.MIN) {
    		return n.minWidth(-1);
    	}
    	if (size == SIZE.MAX) {
    		return n.maxWidth(-1);
    	}
    	return n.prefWidth(-1);
    }
    
    private double calculateNodeHeight(Node n, SIZE size) {
    	if (size == SIZE.MIN) {
    		return n.minHeight(-1);
    	}
    	if (size == SIZE.MAX) {
    		return n.maxHeight(-1);
    	}
    	return n.prefHeight(-1);
    }
    
    private double degreesToRadials(double d) {
    	return d / 360 * 2 * Math.PI;
    }
    
    private double marginLeft() {
        double startAngle = getStartAngle();
        double arc = getArc();
        double endAngle = startAngle + arc;
        double chainWidth = computeChainDiameter(SIZE.PREF);
        double chainCenterX = chainWidth / 2;
        
        // if left most point of circle is part of the arc, then the margin is 0
        if (startAngle <= 270 &&  endAngle >= 270) {
            return 0;
        }
        
        // determine X of both angles
        final double lStartX = chainCenterX + (chainWidth / 2 * Math.sin(degreesToRadials(startAngle)));
        final double lEndX = chainCenterX + (chainWidth / 2 * Math.sin(degreesToRadials(endAngle)));
        
        // what is the lowest value
        double lX = chainCenterX;
        lX = Math.min(lStartX, lX);
        lX = Math.min(lEndX, lX);
        return lX;
    }

}
