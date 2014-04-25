package jfxtras.labs.scene.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
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

	private enum MinPrefMax { MIN, PREF, MAX }

	
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

	/** gap: space between nodes */
	public ObjectProperty<Double> gapProperty() { return gapObjectProperty; }
	final private ObjectProperty<Double> gapObjectProperty = new SimpleObjectProperty<Double>(this, "gap", 0.0) {
		public void set(Double v) {
			if (v == null) {
				throw new IllegalArgumentException(getName() + " cannot be null");
			}
			super.set(v);
		}
	};
	public Double getGap() { return gapObjectProperty.getValue(); }
	public void setGap(Double value) { gapObjectProperty.setValue(value); }
	public CircularPane withGap(Double value) { setGap(value); return this; }

	/** childrenAreCircular: if all children are circular, then we can use a different size */
	public ObjectProperty<Boolean> childrenAreCircularProperty() { return childrenAreCircularObjectProperty; }
	final private ObjectProperty<Boolean> childrenAreCircularObjectProperty = new SimpleObjectProperty<Boolean>(this, "childrenAreCircular", false);
	public Boolean getChildrenAreCircular() { return childrenAreCircularObjectProperty.getValue(); }
	public void setChildrenAreCircular(Boolean value) { childrenAreCircularObjectProperty.setValue(value); }
	public CircularPane withChildrenAreCircular(Boolean value) { setChildrenAreCircular(value); return this; } 

	/** clipAwayExcessWhitespace: cut away excess whitespace on the outside */
	public ObjectProperty<Boolean> clipAwayExcessWhitespaceProperty() { return clipAwayExcessWhitespaceObjectProperty; }
	final private ObjectProperty<Boolean> clipAwayExcessWhitespaceObjectProperty = new SimpleObjectProperty<Boolean>(this, "clipAwayExcessWhitespace", true);
	public Boolean getClipAwayExcessWhitespace() { return clipAwayExcessWhitespaceObjectProperty.getValue(); }
	public void setClipAwayExcessWhitespace(Boolean value) { clipAwayExcessWhitespaceObjectProperty.setValue(value); }
	public CircularPane withClipAwayExcessWhitespace(Boolean value) { setClipAwayExcessWhitespace(value); return this; } 

	/** animationDuration */
	public ObjectProperty<Duration> animationDurationProperty() { return animationDurationObjectProperty; }
	final private ObjectProperty<Duration> animationDurationObjectProperty = new SimpleObjectProperty<Duration>(this, "animationDuration", Duration.millis(500));
	public Duration getAnimationDuration() { return animationDurationObjectProperty.getValue(); }
	public void setAnimationDuration(Duration value) { animationDurationObjectProperty.setValue(value); }
	public CircularPane withAnimationDuration(Duration value) { setAnimationDuration(value); return this; } 

	/** animationInterpolation: calculate the position of a node during the animation (default: move from origin), use node.relocate to position node (or manually apply layoutBounds.minX/Y) */
	public ObjectProperty<AnimationInterpolation> animationInterpolationProperty() { return animationInterpolationObjectProperty; }
	final private ObjectProperty<AnimationInterpolation> animationInterpolationObjectProperty = new SimpleObjectProperty<AnimationInterpolation>(this, "animationInterpolation", null);
	public AnimationInterpolation getAnimationInterpolation() { return animationInterpolationObjectProperty.getValue(); }
	public void setAnimationInterpolation(AnimationInterpolation value) { animationInterpolationObjectProperty.setValue(value); }
	public CircularPane withAnimationInterpolation(AnimationInterpolation value) { setAnimationInterpolation(value); return this; } 

	/** debug: show debug hints */
	public ObjectProperty<Paint> showDebugProperty() { return showDebugObjectProperty; }
	final private ObjectProperty<Paint> showDebugObjectProperty = new SimpleObjectProperty<Paint>(this, "showDebug", null);
	public Paint getShowDebug() { return showDebugObjectProperty.getValue(); }
	public void setShowDebug(Paint value) { showDebugObjectProperty.setValue(value); }
	public CircularPane withShowDebug(Paint value) { setShowDebug(value); return this; } 

	// TODO: are margins & padding taken into account?
	
	// ==========================================================================================================================================================================================================================================
	// PANE
	
    @Override 
    protected double computeMinWidth(double height) {
    	return calculateLayout(MinPrefMax.MIN).clippedWidth;
    }

    @Override 
    protected double computeMinHeight(double width) {
    	return calculateLayout(MinPrefMax.MIN).clippedHeight;
    }

    @Override 
    protected double computePrefWidth(double height) {
    	return calculateLayout(MinPrefMax.PREF).clippedWidth;
    }

    @Override 
    protected double computePrefHeight(double width) {
    	return calculateLayout(MinPrefMax.PREF).clippedHeight;
    }

    @Override 
    protected double computeMaxWidth(double height) {
    	// In 'normal' layout logic these computed sizes would denote an ability.
    	// - Min would indicate the minimal size the node or layout is able to render itself, 
    	// - Pref the preferred size a node would like to have, 
    	// - and Max the maximum size a node is ABLE to render itself.
    	//
    	// If a node were given more space to render itself, without any further instructions from the user (through layout constraints), 
    	// it should still stick to its preferred size, because that after all is its preferred size.
    	//
    	// However, in JavaFX Max does not denote an ability, but is seen as an intent.
    	// If a node indicates it has a max of, say, Double.MAX_VALUE (as CircularPane should do, because it is able to render itself that size),
    	// the Pane implementations in JavaFX will actually make CircularPane always grow into any additional space, instead of keeping its preferred size.
    	// In my opinion this is wrong, but unfortunately this is the way things are in JavaFX.
    	// Therefore the Max size is the preferred size.
    	return computePrefWidth(height);
    }

    @Override 
    protected double computeMaxHeight(double width) {
    	// For an explanation, see computerMaxWidth
    	return computePrefHeight(width);
    }

    @Override 
    protected void layoutChildren() {
    	
    	if (layingoutChildren.get() > 0) {
    		// TODO: remember and request a relayout once we're done?
    		return;
    	}    	
    	layingoutChildren.addAndGet(1);
    	try {
    		//System.out.println("=============== layoutChildren ");

			// remove all beads
			getChildren().removeAll(nodeToBeadMap.values());
			nodeToBeadMap.clear();
			
			// calculate the layout
			LayoutInfo lLayoutInfo = calculateLayout(null); // null: use the available size instead of a calculated one

	    	// position the nodes
	    	List<Node> nodes = getManagedChildren();
	    	for (Node lNode : nodes) {
	    		
	    		/// get layout
	    		NodeLayoutInfo lNodeLayoutInfo = lLayoutInfo.layoutInfoMap.get(lNode);

	    		// position node
	    		lNode.resizeRelocate(lNodeLayoutInfo.x - lLayoutInfo.clipLeft, lNodeLayoutInfo.y - lLayoutInfo.clipTop, lNodeLayoutInfo.w, lNodeLayoutInfo.h);
	    		
	    		// add a bead to show where this node boundary is
	    		if (getShowDebug() != null) {
	    			
		    		Bead lBead = new Bead(lLayoutInfo.beadDiameter);
		    		nodeToBeadMap.put(lNode, lBead);
		    		getChildren().add(lBead);
		    		
		    		lBead.setLayoutX(lNodeLayoutInfo.beadX - lLayoutInfo.clipLeft); 
		    		lBead.setLayoutY(lNodeLayoutInfo.beadY - lLayoutInfo.clipTop);
		    		// because a JavaFX circle has its origin in the top-left corner, we need to offset half a bead
		    		lBead.setTranslateX(lLayoutInfo.beadDiameter / 2);  
		    		lBead.setTranslateY(lLayoutInfo.beadDiameter / 2);
	    		}
	    		
	    		// animated?
	    		if (initial && getAnimationInterpolation() != null) {
	
	    			// create the administration for the animation
	    			AnimationLayoutInfo lAnimated = new AnimationLayoutInfo();
	        		lAnimated.layoutInfo = lLayoutInfo;
	    			lAnimated.node = lNode;
	    			lAnimated.nodeLayoutInfo = lNodeLayoutInfo;
	    			lAnimated.originX = (lLayoutInfo.chainDiameter / 2)
  			 	          - lLayoutInfo.clipLeft
  			 	          - lNode.getLayoutBounds().getMinX() // for some reason this must be added, while when setting X&Y without animation it is not needed... Alas 
  			 	          ;	    					
	    			lAnimated.originY = (lLayoutInfo.chainDiameter / 2)
  				          - lLayoutInfo.clipTop
  				          - lNode.getLayoutBounds().getMinY() // for some reason this must be added, while when setting X&Y without animation it is not needed... Alas
  				          ;
	    			animationLayoutInfos.add(lAnimated);
	    			
	    			// initial position
					getAnimationInterpolation().interpolate(0.0, lAnimated);
	    		}
	    	}    	
	    	
	    	// no longer the initial layout
			if (initial) {
	    		initial = false;
			}
			
			// is there anything to animated?
			if (animationLayoutInfos.size() > 0 ) {

				// while the animation is running, don't touch the children
	        	layingoutChildren.addAndGet(1);
	    		new Transition() {
	    			// anonymous constructor
	    			{
	    				setCycleDuration(getAnimationDuration());
	    				setAutoReverse(false);
	    				setCycleCount(1);
	    				setOnFinished( (event) -> {
							animationLayoutInfos.clear();
	    			    	layingoutChildren.addAndGet(-1);
	    				});
	    			}
					
					@Override
					protected void interpolate(double progress) {
						for (AnimationLayoutInfo lAnimationLayoutInfo : animationLayoutInfos) {
							getAnimationInterpolation().interpolate(progress, lAnimationLayoutInfo);
						}
					}
				}.playFromStart();
			}
		}
		finally {
	    	layingoutChildren.addAndGet(-1);
		}
    }
    private AtomicInteger layingoutChildren = new AtomicInteger(0);
    private boolean initial = true;

    
	// ==========================================================================================================================================================================================================================================
	// layout

    /**
     * 
     */
    protected LayoutInfo calculateLayout(MinPrefMax size) {

    	// TODO: cache calculations
    	
		// layout info
		LayoutInfo lLayoutInfo = new LayoutInfo();
		
    	// get the nodes we need to render
    	List<Node> nodes = getManagedChildren();
    	nodes.removeAll(nodeToBeadMap.values());
    	if (nodes.size() == 0) {
    		// nothing to do
    		return lLayoutInfo;
    	}
    	int numberOfNodes = nodes.size();
    	
    	// First we're going to render like we have a full chain (circle) available
    	// Then we're going to clip away excess white space

		// determine the bead and chain size we layout with
    	// If we have a size, then this is a forced calculation
    	// If we do not have a size, we project our (assigned) size to the calculated preferred size to determine how if scaling is needed 
    	double lPrefToMinScaleFactor = 1.0;
    	if (size != null) {
    		// calculation
    		lLayoutInfo.beadDiameter = determineBeadDiameter(size);
        	lLayoutInfo.chainDiameter = computeChainDiameter(lLayoutInfo.beadDiameter);
    	}
    	else {
    		// project preferred to the assigned size
    		LayoutInfo lMinLayoutInfo = calculateLayout(MinPrefMax.MIN);
    		LayoutInfo lPrefLayoutInfo = calculateLayout(MinPrefMax.PREF);
    		double lWidth = Math.max( getWidth(), lMinLayoutInfo.clippedWidth);
    		double lHeight = Math.max( getHeight(), lMinLayoutInfo.clippedHeight);
    		lPrefToMinScaleFactor = Math.min( lWidth / lPrefLayoutInfo.clippedWidth,lHeight/ lPrefLayoutInfo.clippedHeight);
        	//System.out.println(getId() + ": layout lPrefScaleFactor=" + lPrefToMinScaleFactor);	    	
        	if (lPrefToMinScaleFactor > 1.0) {
        		lPrefToMinScaleFactor = 1.0;
        	}
    		lLayoutInfo.beadDiameter = lPrefLayoutInfo.beadDiameter * lPrefToMinScaleFactor;
        	lLayoutInfo.chainDiameter = lPrefLayoutInfo.chainDiameter * lPrefToMinScaleFactor;
        	// TODO: how do we handle scaling up?
    	}
		//System.out.println("----------------- " + size);
    	//System.out.println(getId() + ": layout lPrefToMinScaleFactor=" + lPrefToMinScaleFactor);	    	
    	//System.out.println(getId() + ": layout beadDiameter=" + lLayoutInfo.beadDiameter);	    	
    	//System.out.println(getId() + ": layout chainDiameter=" + lLayoutInfo.chainDiameter);	    	

		// prepare the layout loop
    	// the chain diameter runs through the center of the beads
    	lLayoutInfo.minX = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter;
    	lLayoutInfo.minY = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter;
    	lLayoutInfo.maxX = 0;
    	lLayoutInfo.maxY = 0;
    	double lAngleStep = getArc() / numberOfNodes;
    	double lAngle = getStartAngle360();
    	lLayoutInfo.startAngle = lAngle;
    	//System.out.println(getId() + ": layout startAngle=" + lAngle);	    	
    	//int cnt = 0;
    	for (Node lNode : nodes) {
    		
    		// bead layout
    		NodeLayoutInfo lNodeLayoutInfo = new NodeLayoutInfo();
    		lNodeLayoutInfo.angle = lAngle;
    		lLayoutInfo.layoutInfoMap.put(lNode, lNodeLayoutInfo);
    		
    		// calculate the X,Y position on the chain where the bead should be placed
    		//System.out.println(cnt + " layout startAngle=" + lAngle + " " + lNode);
    		lNodeLayoutInfo.beadX = calculateX(lLayoutInfo.chainDiameter, lAngle);
    		lNodeLayoutInfo.beadY = calculateY(lLayoutInfo.chainDiameter, lAngle);
    		//System.out.println(getId() + ": " + cnt + " layout beadCenter=" + lAngle + " (" + lNodeLayoutInfo.beadX + "," + lNodeLayoutInfo.beadY + ")");
    		
    		// size the node 
    		// if we are rendered smaller than the preferred, scale down to min gracefully
    		lNodeLayoutInfo.w = calculateNodeWidth(lNode, MinPrefMax.PREF) * lPrefToMinScaleFactor;
    		lNodeLayoutInfo.h = calculateNodeHeight(lNode, MinPrefMax.PREF) * lPrefToMinScaleFactor;
    		
    		// place on the right spot
    		lNodeLayoutInfo.x = lNodeLayoutInfo.beadX
    			 	          + ((lLayoutInfo.beadDiameter - lNodeLayoutInfo.w) / 2) // add the difference between the bead's size and the node's, so it ends up in the center
    				          ; 
    		lNodeLayoutInfo.y = lNodeLayoutInfo.beadY 
    				          + ((lLayoutInfo.beadDiameter - lNodeLayoutInfo.h) / 2)  // add the difference between the bead's size and the node's, so it ends up in the center
    				          ; 
    		//System.out.println(getId() + ": " + cnt + " layout startAngle=" + lAngle + " (" + lNodeLayoutInfo.x + "," + lNodeLayoutInfo.y + ") " + lNodeLayoutInfo.w + "x" + lNodeLayoutInfo.h + " " + lNode);	    		

    		// remember the min and max XY
    		lLayoutInfo.minX = Math.min(lLayoutInfo.minX, lNodeLayoutInfo.beadX);
    		lLayoutInfo.minY = Math.min(lLayoutInfo.minY, lNodeLayoutInfo.beadY);
    		lLayoutInfo.maxX = Math.max(lLayoutInfo.maxX, lNodeLayoutInfo.beadX);
    		lLayoutInfo.maxY = Math.max(lLayoutInfo.maxY, lNodeLayoutInfo.beadY);
    		
			// next
        	lAngle += lAngleStep;
        	//cnt++;
    	}
    	
    	// calculate the clip sizes
    	lLayoutInfo.clipTop = lLayoutInfo.minY;
    	lLayoutInfo.clipRight = lLayoutInfo.chainDiameter - lLayoutInfo.maxX;
    	lLayoutInfo.clipBottom = lLayoutInfo.chainDiameter - lLayoutInfo.maxY;
    	lLayoutInfo.clipLeft = lLayoutInfo.minX;
    	
    	// calculate size
    	lLayoutInfo.clippedWidth = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter // the chain runs through the center of the beads, so we need to add 2x 1/2 a bead to get to the encompassing diameter
       		                     - lLayoutInfo.clipLeft - lLayoutInfo.clipRight;
    	lLayoutInfo.clippedHeight = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter // the chain runs through the center of the beads, so we need to add 2x 1/2 a bead to get to the encompassing diameter
                                  - lLayoutInfo.clipTop - lLayoutInfo.clipBottom;
    	
    	// done
    	return lLayoutInfo;
    }
	private final Map<Node, Bead> nodeToBeadMap = new WeakHashMap<>();
	
    public class LayoutInfo {
    	public double startAngle;
    	public double chainDiameter = 0;
    	public double beadDiameter = 0;
    	public double minX = 0;
    	public double minY = 0;
    	public double maxX = 0;
    	public double maxY = 0;
    	public double clipTop = 0;
    	public double clipRight = 0;
    	public double clipBottom = 0;
    	public double clipLeft = 0;
    	public double clippedWidth = 0;
    	public double clippedHeight = 0;
    	final public Map<Node, NodeLayoutInfo> layoutInfoMap = new WeakHashMap<>();
    }
    
    public class NodeLayoutInfo {
    	public double angle;
    	public double beadX;
    	public double beadY;
    	public double x;
    	public double y;
    	public double w;
    	public double h;
    }
    
    public class AnimationLayoutInfo {
    	public LayoutInfo layoutInfo;
    	public Node node;
    	public NodeLayoutInfo nodeLayoutInfo;
    	public double originX;
    	public double originY;
		
    	public double calculateX(double angle) {
			return CircularPane.calculateX(layoutInfo.chainDiameter, angle);
		}
		
    	public double calculateY(double angle) {
			return CircularPane.calculateY(layoutInfo.chainDiameter, angle);
		}
    }
    final List<AnimationLayoutInfo> animationLayoutInfos = new ArrayList<>();
    
    @FunctionalInterface
    public interface AnimationInterpolation {
		public void interpolate(double progress, AnimationLayoutInfo animationLayoutInfo);    	
    }

    static public void animateFromTheOrigin(double progress, AnimationLayoutInfo animationLayoutInfo) {
		double lX = animationLayoutInfo.originX + (progress * -animationLayoutInfo.originX) + ((animationLayoutInfo.nodeLayoutInfo.x - animationLayoutInfo.layoutInfo.clipLeft) * progress); 
		double lY = animationLayoutInfo.originY + (progress * -animationLayoutInfo.originY) + ((animationLayoutInfo.nodeLayoutInfo.y - animationLayoutInfo.layoutInfo.clipTop) * progress);
		animationLayoutInfo.node.relocate(lX, lY);    	
    }
    
    static public void animateOverTheArc(double progress, AnimationLayoutInfo animated) {
		double lAngle = animated.layoutInfo.startAngle + (progress * animated.nodeLayoutInfo.angle);
		double lX = animated.calculateX(lAngle)
                  + ((animated.layoutInfo.beadDiameter - animated.nodeLayoutInfo.w) / 2) // add the difference between the bead's size and the node's, so it ends up in the center
                  - animated.layoutInfo.clipLeft
                  ; 
		double lY = animated.calculateY(lAngle)
                + ((animated.layoutInfo.beadDiameter - animated.nodeLayoutInfo.h) / 2) // add the difference between the bead's size and the node's, so it ends up in the center
                - animated.layoutInfo.clipTop
                ; 
		animated.node.relocate(lX, lY);    	
    }
	
	// ==========================================================================================================================================================================================================================================
	// SUPPORT

	private List<Node> getManagedChildrenWithoutBeads() {
    	List<Node> nodes = new ArrayList<>(getManagedChildren());
    	nodes.removeAll(nodeToBeadMap.values());
    	return nodes;
	}
	
	static private double calculateX(double chainDiameter, double angle) {
		angle = angle % 360;
		double lX = (chainDiameter / 2) // from the center
	              + ( (chainDiameter / 2) * -1 * Math.sin(degreesToRadials(angle + 180))) // determine the position on the chain (-1 = clockwise, 180 = start north)
	              ;
		//System.out.println(getId() + ": calculateX chainDiameter=" + chainDiameter + " angle=" + angle + " -> " + lX);
		return lX;
	}
	
	static private double calculateY(double chainDiameter, double angle) {
		angle = angle % 360;
		double lY = (chainDiameter / 2) // from the center 
	              + ( (chainDiameter / 2) * Math.cos(degreesToRadials(angle + 180))) // determine the position on the chain (180 = start north)
	              ;
		//System.out.println(getId() + ": calculateY chainDiameter=" + chainDiameter + " angle=" + angle + " -> " + lY);
		return lY;
	}
	
    private class Bead extends Circle {
    	public Bead(double diameter) {
    		super();
    		setRadius( (diameter - getStrokeWidth()) / 2); // make the line fall within the allotted room
    		setFill(null);
    		setStroke(getShowDebug());
    	}
    }
    
    /**
     * The chain is the circle that runs through the bead's centers
     * @param size
     * @param width
     * @return
     */
    private double computeChainDiameter(double beadDiameter) {

    	// prepare
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	int numberOfNodesOnFullChain = (int)Math.ceil(numberOfNodes * (360 / getArc())); // these are the number of bead as would be placed on the chain if it were used 360
    	//System.out.println(getId() + ": computeChainDiameter numberOfNodes=" + numberOfNodes + ", for calculation=" + numberOfNodesOnFullChain);
    	
    	// special situations
    	if (numberOfNodes == 0) {
    		return 0;
    	}
    	if (numberOfNodes == 1) {
    		return 0; // the chain runs through the center of the beads, with only one bead, there is no chain
    	}
    	if (numberOfNodesOnFullChain == 2) {
    		// the chain runs through the center of the beads, with only two beads the chain runs through 2x 1/2 a bead
    		return beadDiameter + getGap(); 
    	}
    	
    	// determine the size of the circle where the center of the bead would be placed on (Daan's formula) 
    	double lDiameter = (beadDiameter + getGap()) / Math.sin(degreesToRadials(360 / ((double)numberOfNodesOnFullChain) / 2));
    	return lDiameter;
    }

	private double determineBeadDiameter(MinPrefMax size) {
		double lBeadDiameter = 0.0;
		if (getChildrenAreCircular()) {
			lBeadDiameter = determineBeadDiameterUsingWidthOrHeight(size);
		}
		else {
			lBeadDiameter = determineBeadDiameterUsingTheDiagonal(size);
		}
		return lBeadDiameter;
	}

	private double determineBeadDiameterUsingWidthOrHeight(MinPrefMax size) {
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
    
	private double determineBeadDiameterUsingTheDiagonal(MinPrefMax size) {
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
    
    private double calculateNodeWidth(Node n, MinPrefMax size) {
    	if (size == MinPrefMax.MIN) {
    		return n.minWidth(-1);
    	}
    	if (size == MinPrefMax.MAX) {
    		return n.maxWidth(-1);
    	}
    	return n.prefWidth(-1);
    }
    
    private double calculateNodeHeight(Node n, MinPrefMax size) {
    	if (size == MinPrefMax.MIN) {
    		return n.minHeight(-1);
    	}
    	if (size == MinPrefMax.MAX) {
    		return n.maxHeight(-1);
    	}
    	return n.prefHeight(-1);
    }
    
    static private double degreesToRadials(double d) {
    	double r = (d % 360) / 360 * 2 * Math.PI;
    	return r;
    }
}
