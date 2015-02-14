package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.scene.control.gauge.linear.CompleteSegment;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.labs.scene.control.gauge.linear.Segment;

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
			drawDial();
		});
		dialPane.heightProperty().addListener( (observable) -> {
			drawDial();
		});
		needlePane = new Pane();
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

	private void drawDial() {
		dialPane.getChildren().clear();
 		double width = dialPane.getWidth();
 		double height = dialPane.getHeight();
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();
 		double controlValueRange = controlMaxValue - controlMinValue;
 		
 		// draw the segments
		Point2D center = new Point2D(width / 2.0, height * 0.6);
 		double radius = Math.min(center.getX(), center.getY());
 		List<Segment> segments = new ArrayList<Segment>(getSkinnable().segments());
 		if (segments.size() == 0) {
 			segments.add(completeSegment);
 		}
 		int cnt = 0;
 		for (Segment segment : segments) {
 	 		double segmentMinValue = segment.getMinValue();
 	 		double segmentMaxValue = segment.getMaxValue();
 			double startAngle = (segmentMinValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
 			double endAngle = (segmentMaxValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
 			dialPane.getChildren().add(drawSegment(center, radius, radius * 0.5, startAngle, endAngle, "segment" + cnt));
 			cnt++;
 		}
		//dialPane.getChildren().add(drawSegment(center, radius, radius * 0.5, 0.0, 270.0, "segment1"));
// 		int n = 10;
// 		for (int i = 0; i < n; i++) {
// 			dialPane.getChildren().add(drawSegment(center, radius, radius * 0.5, i * 270.0 / ((double)n), (i + 1) * 270.0 / ((double)n), "segment" + i));
// 		}
	}
	static final private double FULL_ARC_IN_DEGREES = 270.0;
	final private CompleteSegment completeSegment = new CompleteSegment("default", getSkinnable());

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
	private Path drawSegment(Point2D center, double outerRadius, double innerRadius, double startAngleInDegrees, double endAngleInDegrees, String cssClass) {
		// some additional info
		double angleInDegrees = endAngleInDegrees - startAngleInDegrees;
		
		// math uses radians
		// The angles works counter clockwise, the gauge works clockwise, so * -1
		// 0 degrees is on the right side of the circle, the gauge starts in the bottom left, so add 90 + 45 degrees to offset that 
		double startAngleInRadians = Math.toRadians( (startAngleInDegrees) + 135.0); 
		double endAngleInRadians = Math.toRadians( (endAngleInDegrees) + 135.0);

		// calculate the four points of the segment
		Point2D startOuter = calculatePointOnCircle(center, outerRadius, startAngleInRadians);
		Point2D endOuter = calculatePointOnCircle(center, outerRadius, endAngleInRadians);
		Point2D startInner = calculatePointOnCircle(center, innerRadius, startAngleInRadians);
		Point2D endInner = calculatePointOnCircle(center, innerRadius, endAngleInRadians);
		
		// create a path to draw the segment with
        Path path = new Path();
        path.setFillRule(FillRule.EVEN_ODD);
        path.getStyleClass().addAll("segment", cssClass);

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
