package jfxtras.labs.scene.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


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
	// PROPERTIES

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

	/** arc in degrees: the arc is used to determine the end position; default = 360 = north (top) */
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
	public ObjectProperty<Boolean> childrenAreCircularProperty() { return childrenAreCircularObjectProperty; }
	final private ObjectProperty<Boolean> childrenAreCircularObjectProperty = new SimpleObjectProperty<Boolean>(this, "childrenAreCircular", false);
	public Boolean getChildrenAreCircular() { return childrenAreCircularObjectProperty.getValue(); }
	public void setChildrenAreCircular(Boolean value) { childrenAreCircularObjectProperty.setValue(value); }
	public CircularPane withChildrenAreCircular(Boolean value) { setChildrenAreCircular(value); return this; } 

	/** animate: is the layout animating */
	public ObjectProperty<Boolean> animateProperty() { return animateObjectProperty; }
	final private ObjectProperty<Boolean> animateObjectProperty = new SimpleObjectProperty<Boolean>(this, "animate", false);
	public Boolean getAnimate() { return animateObjectProperty.getValue(); }
	public void setAnimate(Boolean value) { animateObjectProperty.setValue(value); }
	public CircularPane withAnimate(Boolean value) { setAnimate(value); return this; } 

	/** animateDuration */
	public ObjectProperty<Duration> animateDurationProperty() { return animateDurationObjectProperty; }
	final private ObjectProperty<Duration> animateDurationObjectProperty = new SimpleObjectProperty<Duration>(this, "animateDuration", Duration.millis(500));
	public Duration getAnimateDuration() { return animateDurationObjectProperty.getValue(); }
	public void setAnimateDuration(Duration value) { animateDurationObjectProperty.setValue(value); }
	public CircularPane withAnimateDuration(Duration value) { setAnimateDuration(value); return this; } 

	/** animateInterpolation: calculate the position of a node during the animation (default: move from origin) */
	public ObjectProperty<AnimationInterpolation> animateInterpolationProperty() { return animateInterpolationObjectProperty; }
	final private ObjectProperty<AnimationInterpolation> animateInterpolationObjectProperty = new SimpleObjectProperty<AnimationInterpolation>(this, "animateInterpolation", (progress, animated) -> {
		animated.node.setLayoutX( animated.originX + (progress * -animated.originX) + (animated.targetX * progress) );
		animated.node.setLayoutY( animated.originY + (progress * -animated.originY) + (animated.targetY * progress) );
	});
	public AnimationInterpolation getAnimateInterpolation() { return animateInterpolationObjectProperty.getValue(); }
	public void setAnimateInterpolation(AnimationInterpolation value) { animateInterpolationObjectProperty.setValue(value); }
	public CircularPane withAnimateInterpolation(AnimationInterpolation value) { setAnimateInterpolation(value); return this; } 

	/** debug: show debug hints */
	public ObjectProperty<Paint> showDebugProperty() { return showDebugObjectProperty; }
	final private ObjectProperty<Paint> showDebugObjectProperty = new SimpleObjectProperty<Paint>(this, "showDebug", null);
	public Paint getShowDebug() { return showDebugObjectProperty.getValue(); }
	public void setShowDebug(Paint value) { showDebugObjectProperty.setValue(value); }
	public CircularPane withShowDebug(Paint value) { setShowDebug(value); return this; } 

	
	// ==========================================================================================================================================================================================================================================
	// PANE
	
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
    		// TODO: remember and request a relayout once we're done?
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
	    	
	    	// So we get a certain width & height assigned, and in this space we must render the nodes
	    	// But in order to do the calculations, we need to reverse engineer the available (possibly clipped) width & height to the full (unclipped) size
	    	double lLayoutWidth = getWidth(); // System.out.println(getId() + ": layout layoutWidth=" + lLayoutWidth);	    
			double lLayoutHeight = getHeight(); // System.out.println(getId() + ": layout lLayoutHeight=" + lLayoutHeight);
	    	double lUnclippedWidth = lLayoutWidth * prefWidthClipInfo.clippedToFullSizeFactor; //System.out.println(getId() + ": layout unclippedWidth=" + lUnclippedWidth);
			double lUnclippedHeight = lLayoutHeight * prefHeightClipInfo.clippedToFullSizeFactor; //System.out.println(getId() + ": layout lUnclippedHeight=" + lUnclippedHeight);
			double lClipLeft = prefWidthClipInfo.clipBefore * (lUnclippedWidth / prefWidthClipInfo.fullSize); //System.out.println(getId() + ": layout lClipLeft=" + lClipLeft);		
			double lClipTop = prefHeightClipInfo.clipBefore * (lUnclippedHeight / prefHeightClipInfo.fullSize); //System.out.println(getId() + ": layout lClipTop=" + lClipTop);

    		// if we are rendered smaller than the preferred, we slowly scale down to the min instead of jumping
			double lPrefToMinWidthFactor = 1.0;
    		if (lLayoutWidth < prefWidthClipInfo.clippedSize) {
    			lPrefToMinWidthFactor = lLayoutWidth / prefWidthClipInfo.clippedSize;
    		}
			double lPrefToMinHeightFactor = 1.0;
    		if (lLayoutHeight < prefHeightClipInfo.clippedSize) {
    			lPrefToMinHeightFactor = lLayoutHeight / prefHeightClipInfo.clippedSize;
    		}
			double lPrefToMinFactor = Math.min(lPrefToMinWidthFactor, lPrefToMinHeightFactor);
	    	//System.out.println(getId() + ": layout lPrefToMinFactor=" + lPrefToMinFactor);	    	

    		// determine the bead size we layout with
	    	double lBeadDiameter = determineBeadDiameter(Size.PREF);
	    	if (lPrefToMinFactor < 1.0) {
	    		lBeadDiameter *= lPrefToMinFactor;
		    	double lBeadDiameterMin = determineBeadDiameter(Size.MIN);
		    	if (lBeadDiameter < lBeadDiameterMin) {
		    		lBeadDiameter = lBeadDiameterMin;
		    	}
	    	}
	    	//System.out.println(getId() + ": layout lBeadDiameter=" + lBeadDiameter);	    	

			// prepare the layout loop
			// chain goes through the center of the beads, so on both sides 1/2 a bead must be subtracted
	    	double lChainWidth = lUnclippedWidth - lBeadDiameter;
	    	double lChainHeight = lUnclippedHeight - lBeadDiameter;
	    	double lAngleStep = getArc() / numberOfNodes;
	    	double lAngle = getStartAngle360();
	    	//System.out.println(getId() + ": layout startAngle=" + lAngle);	    	
	    	//int cnt = 0;
	    	for (final Node lNode : nodes) {
	    		
	    		// calculate the X,Y position on the chain where the bead should be placed
	    		//System.out.println(cnt + " layout startAngle=" + lAngle + " " + lNode);
	    		double lBeadCenterX = calculateX(lChainWidth, lAngle);
	    		double lBeadCenterY = calculateY(lChainHeight, lAngle);
	    		double lBeadCenterXClipped = lBeadCenterX - lClipLeft;
	    		double lBeadCenterYClipped = lBeadCenterY - lClipTop;
	    		//System.out.println(getId() + ": " + cnt + " layout beadCenter=" + lAngle + " (" + lBeadCenterX + "," + lBeadCenterY + ")" + " -> (" + lBeadCenterXClipped + "," + lBeadCenterYClipped + ")");
	    		
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

	    		// size the node 
	    		double lW = calculateNodeWidth(lNode, Size.PREF);
	    		double lH = calculateNodeHeight(lNode, Size.PREF);
	    		// if we are rendered smaller than the preferred, scale down to min gracefully
	    		if (lPrefToMinFactor < 1.0) {
	    			// width
		    		lW *= lPrefToMinFactor;
		    		double lMinW = calculateNodeWidth(lNode, Size.MIN);
		    		lW = Math.max(lW, lMinW);
		    		// height
		    		lH *= lPrefToMinFactor;
		    		double lMinH = calculateNodeHeight(lNode, Size.MIN);
		    		lH = Math.max(lH, lMinH);
	    		}
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
	    		final double lXClipped = lX - lClipLeft;
	    		final double lYClipped = lY - lClipTop;
	    		lNode.setLayoutX(lXClipped); 
	    		lNode.setLayoutY(lYClipped); 
	    		// System.out.println(getId() + ": " + cnt + " layout startAngle=" + lAngle + " (" + lX + "," + lY + ") " + " -> (" + lXClipped + "," + lYClipped + ") " + lW + "x" + lH + " " + lNode);	    		

	    		// animated?
	    		if (initial && getAnimate()) {

	    			// create the administration for the animation
	    			AnimatingNode lAnimated = new AnimatingNode();
	    			lAnimated.node = lNode;
	    			lAnimated.width = lChainWidth;
	    			lAnimated.height = lChainHeight;
	    			lAnimated.originX = (lChainWidth / 2) + (lBeadDiameter / 2) - lClipLeft;
	    			lAnimated.originY = (lChainHeight / 2) + (lBeadDiameter / 2) - lClipTop;
	    			lAnimated.targetX = lXClipped;
	    			lAnimated.targetY = lYClipped;
	    			lAnimated.startAngle = getStartAngle();
	    			lAnimated.targetAngle = lAngle;
	    			animations.add(lAnimated);
	    			
	    			// initial position
					getAnimateInterpolation().interpolate(0.0, lAnimated);
	    		}
	    		
				// next
	        	lAngle += lAngleStep;
	        	//cnt++;
	    	}
	    	
	    	// no longer the initial layout
    		if (initial) {
	    		initial = false;
    		}
    		
    		// is there anything to animated?
    		if (animations.size() > 0 ) {
	    		// while the animation is running, don't touch the children
	        	layingoutChilderen.addAndGet(1);
	    		new Transition() {
	    			// anonymous constructor
	    			{
	    				setCycleDuration(getAnimateDuration());
	    				setAutoReverse(false);
	    				setCycleCount(1);
	    				setOnFinished( (event) -> {
							animations.clear();
	    			    	layingoutChilderen.addAndGet(-1);
	    				});
	    			}
					
					@Override
					protected void interpolate(double progress) {
						for (AnimatingNode lAnimated : animations) {
							getAnimateInterpolation().interpolate(progress, lAnimated);
						}
					}
				}.playFromStart();
    		}
    	}
    	finally {
        	layingoutChilderen.addAndGet(-1);
    	}
    }
    private AtomicInteger layingoutChilderen = new AtomicInteger(0);
	private final Map<Node, Bead> nodeToBeadMap = new WeakHashMap<>();
    private boolean initial = true;
    
    public class AnimatingNode {
		Node node;
    	public double width;
		public double height;
    	double originX;
    	double originY;
    	double targetX;
    	double targetY;
    	double startAngle;
    	double targetAngle;
    }
    final List<AnimatingNode> animations = new ArrayList<>();
    
    @FunctionalInterface
    interface AnimationInterpolation {
		public void interpolate(double progress, AnimatingNode animated);    	
    }

	
	// ==========================================================================================================================================================================================================================================
	// SUPPORT

    private ClipInfo computePossiblyClippedWidth(Size size) {
    	ClipInfo lClipInfo = new ClipInfo();
		lClipInfo.fullSize = Math.ceil(computeChainDiameter(size, true));
		lClipInfo.clipBefore = Math.floor(clipLeft(lClipInfo.fullSize, size));
		lClipInfo.clipAfter = Math.floor(clipRight(lClipInfo.fullSize, size));
		lClipInfo.clippedSize = Math.ceil(lClipInfo.fullSize - lClipInfo.clipBefore - lClipInfo.clipAfter);
		lClipInfo.clippedToFullSizeFactor = lClipInfo.fullSize / lClipInfo.clippedSize;
		return lClipInfo;
    }

    private ClipInfo computePossiblyClippedHeight(Size size) {
    	ClipInfo lClipInfo = new ClipInfo();
		lClipInfo.fullSize = Math.ceil(computeChainDiameter(size, false));
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
		//System.out.println(getId() + ": calculateX chainDiameter=" + chainDiameter + " angle=" + angle + " -> " + lX);
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
    
    private double computeChainDiameter(Size size, boolean width) {
    	
    	// prepare
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	int numberOfNodesForCalculation = (int)Math.ceil(numberOfNodes * (360 / getArc())); // these are the number of nodes as would be placed on the circle if it were 360
    	double lBeadDiameter = determineBeadDiameter(size);    	
    	// System.out.println(getId() + ": computeChainDiameter numberOfNodes=" + numberOfNodes + ", for calculation=" + numberOfNodesForCalculation);
    	
    	// special situations
    	if (numberOfNodes == 0) {
    		return 0;
    	}
    	if (numberOfNodes == 1) {
    		return lBeadDiameter;
    	}
    	if (numberOfNodesForCalculation == 2) {
    		// 2 beads varies between two extremes: either both beads are vertical or horizontal
    		//   O
    		//   O -> OO
    		//
    		// We solve this by always assigning maximum width and height, and then clipping away the excess 
    		return 2 * lBeadDiameter;
    	}
    	
    	// determine the size of the circle where the center of the bead would be placed on (Daan's formula) 
    	double lDiameter = lBeadDiameter / Math.sin(degreesToRadials(360 / numberOfNodesForCalculation / 2));
    	lDiameter += lBeadDiameter; // but of course we need the outer circle
    	return lDiameter;
    }

	private double determineBeadDiameter(Size size) {
		double lBeadDiameter = 0.0;
		if (getChildrenAreCircular()) {
			lBeadDiameter = determineBeadDiameterUsingWidthOrHeight(size);
		}
		else {
			lBeadDiameter = determineBeadDiameterUsingTheDiagonal(size);
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
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = width - lBeadDiameter;
        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / numberOfNodes); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipLeft startAngle=" + startAngle + " endAngle=" + endAngle);

        // special situations
        if (numberOfNodes <= 1) {
        	return 0;
        }
        else if (numberOfNodes == 2 && arc == 360.0) {
        	double lWhiteSpace = ((1.0 - Math.abs(Math.sin(degreesToRadials(startAngle)))) * lBeadDiameter);
        	return lWhiteSpace / 2;
        }
        else if (arc == 360.0) {
        	return 0;
        }
        
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
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = width - lBeadDiameter;
        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / numberOfNodes); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipLeft startAngle=" + startAngle + " endAngle=" + endAngle);

        // special situations
        if (numberOfNodes <= 1) {
        	return 0;
        }
        else if (numberOfNodes == 2 && arc == 360.0) {
        	double lWhiteSpace = ((1.0 - Math.abs(Math.sin(degreesToRadials(startAngle)))) * lBeadDiameter);
        	return lWhiteSpace / 2;
        }
        else if (arc == 360.0) {
        	return 0;
        }
        
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
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = height - lBeadDiameter;
        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / numberOfNodes); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipLeft startAngle=" + startAngle + " endAngle=" + endAngle);

        // special situations
        if (numberOfNodes <= 1) {
        	return 0;
        }
        else if (numberOfNodes == 2 && arc == 360.0) {
        	double lWhiteSpace = ((1.0 - Math.abs(Math.cos(degreesToRadials(startAngle)))) * lBeadDiameter);
        	return lWhiteSpace / 2;
        }
        else if (arc == 360.0) {
        	return 0;
        }
        
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
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	double lBeadDiameter = determineBeadDiameter(size);    	
        double lChainDiameter = height - lBeadDiameter;
        double startAngle = getStartAngle360();
        double arc = getArc();
        double endAngle = startAngle + arc - (arc / numberOfNodes); // if we take both extremes we basically render one node too many; it's all about the trees and spaces between the trees in a street
        //System.out.println(getId() + ": clipLeft startAngle=" + startAngle + " endAngle=" + endAngle);

        // special situations
        if (numberOfNodes <= 1) {
        	return 0;
        }
        else if (numberOfNodes == 2 && arc == 360.0) {
        	double lWhiteSpace = ((1.0 - Math.abs(Math.cos(degreesToRadials(startAngle)))) * lBeadDiameter);
        	return lWhiteSpace / 2;
        }
        else if (arc == 360.0) {
        	return 0;
        }
        
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
        
        // what is the highest value
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
