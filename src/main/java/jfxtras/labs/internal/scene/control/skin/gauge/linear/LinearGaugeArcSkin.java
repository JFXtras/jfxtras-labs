package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.scene.control.gauge.linear.CompleteSegment;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.labs.scene.control.gauge.linear.Segment;

/**
 * Based on Gerrit Grunwald's Enzo SimpleGauge (https://bitbucket.org/hansolo/enzo/src)
 */
public class LinearGaugeArcSkin extends SkinBase<LinearGauge> {

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LinearGaugeArcSkin(LinearGauge control) {
		super(control);//, new ListSpinnerBehavior<T>(control));
		construct();
	}

	/*
	 * 
	 */
	private void construct() {
		createNodes();
	}
	
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// dial
		dialPane = new Pane();
		dialPane.widthProperty().addListener( (observable) -> {
			drawDialPane();
		});
		dialPane.heightProperty().addListener( (observable) -> {
			drawDialPane();
		});
		
		// needle
		needlePane = new Pane();
		needlePane.widthProperty().addListener( (observable) -> {
			drawNeedlePane();
		});
		needlePane.heightProperty().addListener( (observable) -> {
			drawNeedlePane();
		});
		
		// overlay
		overlayPane = new Pane();
		
		// we use a stack pane to control the layers
		StackPane lStackPane = new StackPane();
		lStackPane.getChildren().add(dialPane);
		lStackPane.getChildren().add(needlePane);
		lStackPane.getChildren().add(overlayPane);
		getChildren().add(lStackPane);
		
		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
	}
	private Pane dialPane;
	private Pane needlePane;
	private Pane overlayPane;

	/**
	 * 
	 */
	private void drawDialPane() {
		// TBEERNOT: can we optimize the drawing (e.g. when width & height have not changed, skip)

		// we always draw from scratch
		dialPane.getChildren().clear();
		
		// stuff we need
 		double width = dialPane.getWidth();
 		double height = dialPane.getHeight();
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();
 		double controlValueRange = controlMaxValue - controlMinValue;
 		
 		// determine what segments to draw
 		List<Segment> segments = new ArrayList<Segment>(getSkinnable().segments());
 		if (segments.size() == 0) {
 			segments.add(completeSegment);
 		}
 		
 		// draw the segments
		Point2D center = new Point2D(width / 2.0, height * 0.6);
 		double outerRadius = Math.min(center.getX(), center.getY());
		double innerRadius = outerRadius * 0.5;
 		int cnt = 0;
 		for (Segment segment : segments) {
 			
 			// create a path for this segment
 	 		double segmentMinValue = segment.getMinValue();
 	 		double segmentMaxValue = segment.getMaxValue();
 			double startAngle = (segmentMinValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
 			double endAngle = (segmentMaxValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
			Path segmentPath = createSegmentPath(center, outerRadius, innerRadius, startAngle, endAngle);
			dialPane.getChildren().add(segmentPath);
			
			// setup CSS on the path
	        segmentPath.getStyleClass().addAll("segment", "segment" + cnt);
	        if (segment.getId() != null) {
	        	segmentPath.setId(segment.getId());
	        }
	        
 			cnt++;
 		}
	}
	static final private double FULL_ARC_IN_DEGREES = 270.0;
	final private CompleteSegment completeSegment = new CompleteSegment(getSkinnable());

	private void drawNeedlePane() {
		// TBEERNOT: can we optimize the drawing (e.g. when width & height have not changed, skip)

 		// we always draw from scratch
		needlePane.getChildren().clear();
		
		double width = needlePane.getWidth();
 		double height = needlePane.getHeight();
		Point2D center = new Point2D(width / 2.0, height * 0.6);
 		double outerRadius = Math.min(center.getX(), center.getY());
		double size = Math.min(width, height);
		
		Path needle = new Path();
        needle.setFillRule(FillRule.EVEN_ODD);        
		needle.getStyleClass().add("needle");
//        needle.getElements().clear();
        needle.getElements().add(new MoveTo(0.275 * size, 0.5 * size));
        needle.getElements().add(new CubicCurveTo(0.275 * size, 0.62426575 * size,
                                                  0.37573425 * size, 0.725 * size,
                                                  0.5 * size, 0.725 * size));
        needle.getElements().add(new CubicCurveTo(0.62426575 * size, 0.725 * size,
                                                  0.725 * size, 0.62426575 * size,
                                                  0.725 * size, 0.5 * size));
        needle.getElements().add(new CubicCurveTo(0.725 * size, 0.3891265 * size,
                                                  0.6448105 * size, 0.296985 * size,
                                                  0.5392625 * size, 0.2784125 * size));
        needle.getElements().add(new LineTo(0.5 * size, 0.0225 * size));
        needle.getElements().add(new LineTo(0.4607375 * size, 0.2784125 * size));
        needle.getElements().add(new CubicCurveTo(0.3551895 * size, 0.296985 * size,
                                                  0.275 * size, 0.3891265 * size,
                                                  0.275 * size, 0.5 * size));
        needle.getElements().add(new ClosePath());
        needle.setStrokeWidth(size * 0.025);
        needlePane.getChildren().add(needle);
        needle.relocate(50.0, 10.0);

	}
	// ==================================================================================================================
	// SUPPORT
	
	/**
	 * 
	 * @param center
	 * @param outerRadius
	 * @param innerRadius
	 * @param startAngleInDegrees
	 * @param endAngleInDegrees
	 * @param cssClass
	 * @return
	 */
	private Path createSegmentPath(Point2D center, double outerRadius, double innerRadius, double startAngleInDegrees, double endAngleInDegrees) {
		
		// some additional info
		double angleInDegrees = endAngleInDegrees - startAngleInDegrees;
		
		// Java's math uses radians
		// 0 degrees is on the right side of the circle, the gauge starts in the bottom left, so add 90 + 45 degrees to offset to that. 
		double startAngleInRadians = Math.toRadians(startAngleInDegrees + 135.0); 
		double endAngleInRadians = Math.toRadians(endAngleInDegrees + 135.0);

		// calculate the four points of the segment
		Point2D startOuter = calculatePointOnCircle(center, outerRadius, startAngleInRadians);
		Point2D endOuter = calculatePointOnCircle(center, outerRadius, endAngleInRadians);
		Point2D startInner = calculatePointOnCircle(center, innerRadius, startAngleInRadians);
		Point2D endInner = calculatePointOnCircle(center, innerRadius, endAngleInRadians);
		
		// create a path to draw the segment with
        Path path = new Path();
        path.setFillRule(FillRule.EVEN_ODD);

        // arcs are drawn counter clockwise
        // begin of inner arc
        path.getElements().add( new MoveTo(startInner.getX(), startInner.getY()) );
        
        // inner arc to the end point
        {
	        ArcTo arcTo = new ArcTo();
	        arcTo.setX(endInner.getX());
	        arcTo.setY(endInner.getY());
	        arcTo.setRadiusX(innerRadius);
	        arcTo.setRadiusY(innerRadius);
	        arcTo.setLargeArcFlag(angleInDegrees > 180.0);
	        arcTo.setSweepFlag(true);
	        path.getElements().add(arcTo);
        }
        
        // restart at the begin of inner arc
        path.getElements().add( new MoveTo(startInner.getX(), startInner.getY()) );
        
        // leg to begin of outer arc
        path.getElements().add( new LineTo(startOuter.getX(), startOuter.getY()) );
        
        // outer arc (must be darn in the same direction as the inner arc)
        {
	        ArcTo arcTo = new ArcTo();
	        arcTo.setX(endOuter.getX());
	        arcTo.setY(endOuter.getY());
	        arcTo.setRadiusX(outerRadius);
	        arcTo.setRadiusY(outerRadius);
	        arcTo.setLargeArcFlag(angleInDegrees > 180.0);
	        arcTo.setSweepFlag(true);
	        path.getElements().add(arcTo);
        }

        // leg from end of outer arc to end of inner arc
        path.getElements().add( new LineTo(endInner.getX(), endInner.getY()) );
        
        // done
        return path;
    }
    
	/**
	 * http://www.mathopenref.com/coordparamcircle.html
	 * @param center
	 * @param radius
	 * @param angle
	 * @return
	 */
	static private Point2D calculatePointOnCircle(Point2D center, double radius, double angle) {
		double x = center.getX() + (radius * Math.cos(angle));
		double y = center.getY() + (radius * Math.sin(angle));
		return new Point2D(x, y);
	}
}
