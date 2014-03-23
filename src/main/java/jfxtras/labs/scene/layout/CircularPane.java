package jfxtras.labs.scene.layout;

import java.util.ArrayList;
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
 * This pane lays it children out in a circle or part of a circle (arc).
 * 
 * In order to understand how to use this pane, it is important to understand how it places its children.
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
 * The largest bead determines the distance from the origin to where it fits in a segment, and this determines the size of the chain.
 * 
 * By setting a debug color, the beads will be drawn and will clarify the layout.
 * 
 * @author Tom Eugelink
 *
 */
public class CircularPane extends Pane {

	private enum Size { MIN, PREF, MAX }

	
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
	private Double getStartAngle360() {
		return getStartAngle() % 360;
	}

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
    	ClipInfo lClipInfo = computePossiblyClippedWidth(Size.MIN);
		return lClipInfo.clippedSize;
    }

    @Override 
    protected double computeMinHeight(double width) {
    	ClipInfo lClipInfo = computePossiblyClippedHeight(Size.MIN);
		return lClipInfo.clippedSize;
    }

    @Override 
    protected double computePrefWidth(double height) {
    	prefWidthClipInfo = computePossiblyClippedWidth(Size.PREF);
		return prefWidthClipInfo.clippedSize;
    }
    private ClipInfo prefWidthClipInfo = null;

    @Override 
    protected double computePrefHeight(double width) {
    	prefHeightClipInfo = computePossiblyClippedHeight(Size.PREF);
		return prefHeightClipInfo.clippedSize;
    }
    private ClipInfo prefHeightClipInfo = null;

    @Override 
    protected double computeMaxWidth(double height) {
    	// In 'normal' layout logic these computed sizes would denote an ability or preference.
    	// - Min would indicate the minimal size the node or layout is able to render itself, 
    	// - Pref the preferred size a node would like to have, 
    	// - and Max the maximum size a node is ABLE to render itself.
    	//
    	// If a node were given more space to render itself, without any further instructions from the user (through layout constraints), 
    	// it should still stick to its preferred size, because that after all is its preferred size.
    	//
    	// However, in JavaFX Max does not denote an ability, but is seen as an intent / preference.
    	// If a node indicates it has a max of, say, Double.MAX_VALUE (as CircularPane should do, because it is able to render itself that size),
    	// the Pane implementations in JavaFX will actually make CircularPane always grow into any additional space, instead of keeping its preferred size.
    	// In my opinion this is wrong, but unfortunately this is the way things are in JavaFX.
    	// Therefore the Max size is the preferred size. 
    	ClipInfo lClipInfo = computePossiblyClippedWidth(Size.PREF);
		return lClipInfo.clippedSize;
    }

    @Override 
    protected double computeMaxHeight(double width) {
    	// For an explanation, see computerMaxWidth
    	ClipInfo lClipInfo = computePossiblyClippedHeight(Size.PREF);
		return lClipInfo.clippedSize;
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
	    	
	    	// determine the basic size we layout with
	    	// TODO: when to use SIZE.MIN?
	    	double lBeadDiameter = determineBeadDiameter(Size.PREF);
	    	//System.out.println(getId() + ": layout lBeadDiameter=" + lBeadDiameter);	    	

	    	// So we get a certain width & height assigned, and in this space we must render the nodes
	    	// But in order to do the calculations, we need to reverse engineer the available possibly clipped width & height to the full size
	    	double lLayoutWidth = getWidth(); // System.out.println(getId() + ": layout layoutWidth=" + lLayoutWidth);	    
			double lLayoutHeight = getHeight(); // System.out.println(getId() + ": layout lLayoutHeight=" + lLayoutHeight);
	    	double lUnclippedWidth = lLayoutWidth * prefWidthClipInfo.clippedToFullSizeFactor; // System.out.println(getId() + ": layout unclippedWidth=" + lUnclippedWidth);
			double lUnclippedHeight = lLayoutHeight * prefHeightClipInfo.clippedToFullSizeFactor; // System.out.println(getId() + ": layout lUnclippedHeight=" + lUnclippedHeight);
			double lClipLeft = prefWidthClipInfo.clipBefore * (lUnclippedWidth / prefWidthClipInfo.fullSize); // System.out.println(getId() + ": layout clipLeft=" + lClipLeft);		
			double lClipTop = prefHeightClipInfo.clipBefore * (lUnclippedHeight / prefHeightClipInfo.fullSize); // System.out.println(getId() + ": layout lClipTop=" + lClipTop);

			// prepare the layout loop
			// chain goes through the center of the beads, so on both sides 1/2 a bead must be subtracted
	    	double lChainDiameter = lUnclippedWidth - lBeadDiameter;  
	    	double lChainHeight = lUnclippedHeight - lBeadDiameter;
	    	double lAngleStep = getArc() / numberOfNodes;
	    	double lAngle = getStartAngle360();
	    	// System.out.println(getId() + ": layout startAngle=" + lAngle);	    	
	    	// int cnt = 0;
	    	for (final Node lNode : nodes) {
	    		
	    		// calculate the X,Y position on the chain where the bead should be placed
	    		// System.out.println((cnt++) + " layout startAngle=" + lAngle + " " + lNode);
	    		double lBeadCenterX = calculateX(lChainDiameter, lAngle);
	    		double lBeadCenterY = calculateY(lChainHeight, lAngle);
	    		double lBeadCenterXClipped = lBeadCenterX - lClipLeft;
	    		double lBeadCenterYClipped = lBeadCenterY - lClipTop;
	    		// System.out.println(getId() + ": " + (cnt++) + " layout beadCenter=" + lAngle + " (" + lBeadCenterX + "," + lBeadCenterY + ")" + " -> (" + lBeadCenterXClipped + "," + lBeadCenterYClipped + ")");
	    		
	    		// place a bead to show where this node should be
	    		if (getShowDebug() != null) {
	    			
		    		Bead lBead = new Bead();
		    		lBead.setRadius(lBeadDiameter / 2);
		    		nodeToBeadMap.put(lNode, lBead);
		    		getChildren().add(lBead);
		    		
		    		lBead.setLayoutX(lBeadCenterXClipped); 
		    		lBead.setLayoutY(lBeadCenterYClipped);
		    		// because a JavaFX circle has its origin in the top-left corner, we need to offset half a bead
		    		lBead.setTranslateX(lBeadDiameter / 2);  
		    		lBead.setTranslateY(lBeadDiameter / 2);
	    		}	    		

	    		// now place the node 
	    		// TODO: when / how to use MIN?
	    		double lW = calculateNodeWidth(lNode, Size.PREF);
	    		double lH = calculateNodeHeight(lNode, Size.PREF);
	    		lNode.resize(lW, lH);
	    		
	    		// place on the right spot
	    		Bounds lNodeBounds = lNode.layoutBoundsProperty().get();
	    		double lX = lBeadCenterX
	    			 	  + ((lBeadDiameter - lW) / 2) // add the difference between the bead's size and the node's, so it ends up in the center
	    			 	  - (lNode instanceof Parent ? 0 : lNodeBounds.getMinX()) // correct primitives with their bounds (primitives have their centre at 0,0, so minX is < 0)
	    				  ; 
	    		double lY = lBeadCenterY 
	    				  + ((lBeadDiameter - lH) / 2)  // add the difference between the bead's size and the node's, so it ends up in the center
	    				  - (lNode instanceof Parent ? 0 : lNodeBounds.getMinY()) // correct primitives with their bounds (primitives have their centre at 0,0, so minX is < 0) 
	    				  ; 
	    		double lXClipped = lX - lClipLeft;
	    		double lYClipped = lY - lClipTop;
	    		lNode.setLayoutX(lXClipped); 
	    		lNode.setLayoutY(lYClipped); 
	    		// System.out.println(getId() + ": " + (cnt++) + " layout startAngle=" + lAngle + " (" + lX + "," + lY + ") " + " -> (" + lXClipped + "," + lYClipped + ") " + lW + "x" + lH + " " + lNode);
	    		

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

    private ClipInfo computePossiblyClippedWidth(Size size) {
    	ClipInfo lClipInfo = new ClipInfo();
		lClipInfo.fullSize = Math.ceil(computeChainDiameter(size));
		lClipInfo.clipBefore = Math.floor(clipLeft(lClipInfo.fullSize, size));
		lClipInfo.clipAfter = Math.floor(clipRight(lClipInfo.fullSize, size));
		lClipInfo.clippedSize = Math.ceil(lClipInfo.fullSize - lClipInfo.clipBefore - lClipInfo.clipAfter);
		lClipInfo.clippedToFullSizeFactor = lClipInfo.fullSize / lClipInfo.clippedSize;
		return lClipInfo;
    }

    private ClipInfo computePossiblyClippedHeight(Size size) {
    	ClipInfo lClipInfo = new ClipInfo();
		lClipInfo.fullSize = Math.ceil(computeChainDiameter(size));
		lClipInfo.clipBefore = Math.floor(clipTop(lClipInfo.fullSize, size));
		lClipInfo.clipAfter = Math.floor(clipBottom(lClipInfo.fullSize, size));
		lClipInfo.clippedSize = Math.ceil(lClipInfo.fullSize - lClipInfo.clipBefore - lClipInfo.clipAfter);
		lClipInfo.clippedToFullSizeFactor = lClipInfo.fullSize / lClipInfo.clippedSize;
		return lClipInfo;
    }
    
    private class ClipInfo {
    	double fullSize;
    	double clipBefore;
    	double clipAfter;
    	double clippedSize;
    	double clippedToFullSizeFactor;
    }

	private List<Node> getManagedChildrenWithoutBeads() {
    	List<Node> nodes = new ArrayList<>(getManagedChildren());
    	nodes.removeAll(nodeToBeadMap.values());
    	return nodes;
	}
	
	private double calculateX(double chainDiameter, double angle) {
		angle = angle % 360;
		double lX = (chainDiameter / 2) // from the center
	              + ( (chainDiameter / 2) * -1 * Math.sin(degreesToRadials(angle + 180))) // determine the position on the chain (-1 = clockwise, 180 = start north)
	              ;
		//System.out.println(getId() + ": calculateX chainDiameter=" + chainWidth + " angle=" + angle + " -> " + lX);
		return lX;
	}
	
	private double calculateY(double chainDiameter, double angle) {
		angle = angle % 360;
		double lY = (chainDiameter / 2) // from the center 
	              + ( (chainDiameter / 2) * Math.cos(degreesToRadials(angle + 180))) // determine the position on the chain (180 = start north)
	              ;
		//System.out.println(getId() + ": calculateY chainDiameter=" + chainDiameter + " angle=" + angle + " -> " + lY);
		return lY;
	}
	
    private class Bead extends Circle {
    	public Bead() {
    		super();
    		setFill(null);
    		setStroke(getShowDebug());
    	}
    }
    
    private double computeChainDiameter(Size size) {
    	
    	// prepare
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	double lBeadDiameter = determineBeadDiameter(size);    	
    	
    	// special situations
    	if (numberOfNodes == 0) {
    		return 0;
    	}
    	if (numberOfNodes == 1) {
    		return lBeadDiameter;
    	}
    	if (numberOfNodes == 2) {
    		return 2 * lBeadDiameter;
    	}
    	
    	// determine the size of the circle where the center of the bead would be placed on (Daan's formula) 
    	double lDiameter = lBeadDiameter / Math.sin(degreesToRadials(360 / (numberOfNodes * (360 / getArc()) ) / 2));
    	lDiameter += lBeadDiameter; // but of course we need the outer circle
    	return lDiameter;
    }

	private double determineBeadDiameter(Size size) {
		double lBeadDiameter = 0.0;
		if (getChildrenAreCircular()) {
			lBeadDiameter = determineBeadDiameterUsingWidthOrHeight(Size.PREF);
		}
		else {
			lBeadDiameter = determineBeadDiameterUsingTheDiagonal(Size.PREF);
		}
		return lBeadDiameter;
	}

	private double determineBeadDiameterUsingWidthOrHeight(Size size) {
		List<Node> nodes = getManagedChildrenWithoutBeads();
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
    
	private double determineBeadDiameterUsingTheDiagonal(Size size) {
		List<Node> nodes = getManagedChildrenWithoutBeads();
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
    
    private double calculateNodeWidth(Node n, Size size) {
    	if (size == Size.MIN) {
    		return n.minWidth(-1);
    	}
    	if (size == Size.MAX) {
    		return n.maxWidth(-1);
    	}
    	return n.prefWidth(-1);
    }
    
    private double calculateNodeHeight(Node n, Size size) {
    	if (size == Size.MIN) {
    		return n.minHeight(-1);
    	}
    	if (size == Size.MAX) {
    		return n.maxHeight(-1);
    	}
    	return n.prefHeight(-1);
    }
    
    private double clipLeft(double width, Size size) {
    	// prepare
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = width - lBeadDiameter;

        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / getManagedChildrenWithoutBeads().size()); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipLeft startAngle=" + startAngle + " endAngle=" + endAngle);

        // if left most point of circle is part of the arc, then the clip is 0
        if (startAngle <= 270 &&  endAngle >= 270) {
            return 0;
        }
        
        // determine X of both angles
        double lStartX = calculateX(lChainDiameter, startAngle); // System.out.println(getId() + ": clipLeft startAngle=" + startAngle + " startX=" + lStartX);
        double lEndX = calculateX(lChainDiameter, endAngle); // System.out.println(getId() + ": clipLeft endAngle=" + endAngle + " endX=" + lEndX);
        
        // what is the lowest value
        double lX = Math.min(lStartX, lEndX);
        return lX;
    }

    private double clipRight(double width, Size size) {
    	// prepare
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = width - lBeadDiameter;

        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / getManagedChildrenWithoutBeads().size()); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipRight startAngle=" + startAngle + " endAngle=" + endAngle);

        // if left most point of circle is part of the arc, then the clip is 0
        if (startAngle <= 90 && endAngle >= 90) {
            return 0;
        }
        
        // determine X of both angles
        double lStartX = calculateX(lChainDiameter, startAngle); // System.out.println(getId() + ": clipRight startAngle=" + startAngle + " startX=" + lStartX);
        double lEndX = calculateX(lChainDiameter, endAngle); // System.out.println(getId() + ": clipRight endAngle=" + endAngle + " endX=" + lEndX);
        
        // what is the highest value
        double lX = Math.max(lStartX, lEndX);
        
        // determine offset from the right
        lX = lChainDiameter - lX;
        return lX;
    }
    
    private double clipTop(double height, Size size) {
    	// prepare
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = height - lBeadDiameter;

        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / getManagedChildrenWithoutBeads().size()); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipTop startAngle=" + startAngle + " endAngle=" + endAngle);

        // if top most point of circle is part of the arc, then the clip is 0
        // the top angle is 0 degrees, the start angle must be before that, so running up to 359.99999
        // the end angle must be after that, so 0 or higher
        // 
        if (startAngle == 0.0 || (startAngle <= 360 && endAngle >= 360)) {
            return 0;
        }
        
        // determine Y of both angles
        double lStartY = calculateY(lChainDiameter, startAngle); // System.out.println(getId() + ": clipTop startAngle=" + startAngle + " startY=" + lStartY);
        double lEndY = calculateY(lChainDiameter, endAngle); // System.out.println(getId() + ": clipTop endAngle=" + endAngle + " endY=" + lEndY);
        
        // what is the lowest value
        double lY = Math.min(lStartY, lEndY);
        return lY;
    }
    
    private double clipBottom(double height, Size size) {
    	// prepare
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = height - lBeadDiameter;

        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / getManagedChildrenWithoutBeads().size()); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipTop startAngle=" + startAngle + " endAngle=" + endAngle);

        // if top most point of circle is part of the arc, then the clip is 0
        // the top angle is 0 degrees, the start angle must be before that, so running up to 359.99999
        // the end angle must be after that, so 0 or higher
        // 
        if (startAngle <= 180 && endAngle >= 180) {
            return 0;
        }
        
        // determine Y of both angles
        double lStartY = calculateY(lChainDiameter, startAngle); // System.out.println(getId() + ": clipTop startAngle=" + startAngle + " startY=" + lStartY);
        double lEndY = calculateY(lChainDiameter, endAngle); // System.out.println(getId() + ": clipTop endAngle=" + endAngle + " endY=" + lEndY);
        
        // what is the lowest value
        double lY = Math.max(lStartY, lEndY);
        
        // determine offset from the bottom
        lY = lChainDiameter - lY;
        return lY;
    }
    
    private double degreesToRadials(double d) {
    	double r = (d % 360) / 360 * 2 * Math.PI;
    	return r;
    }
}
