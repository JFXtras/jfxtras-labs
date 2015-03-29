package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import jfxtras.labs.scene.control.gauge.linear.CompleteSegment;
import jfxtras.labs.scene.control.gauge.linear.Marker;
import jfxtras.labs.scene.control.gauge.linear.Segment;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;

/**
 * 
 */
public class SimpleMetroArcGaugeSkin extends LinearGaugeSkin<SimpleMetroArcGaugeSkin, SimpleMetroArcGauge> {

	private static final double FULL_ARC_RADIUS_FACTOR = 0.95;
	private static final double NEEDLE_ARC_RADIUS_FACTOR = 0.5;
	private static final double TIP_RADIUS_FACTOR = 0.87;
	static final private double FULL_ARC_IN_DEGREES = 270.0;

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public SimpleMetroArcGaugeSkin(SimpleMetroArcGauge control) {
		super(control);
		constructNodes();
	}
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void constructNodes()
	{
		// use a stack pane to control the layers
		stackPane.getChildren().addAll(segmentPane, markerPane, indicatorPane, needlePane);
		getChildren().add(stackPane);
		stackPane.setPrefSize(200, 200);

		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because with multiple skins CSS should relate to the skin not the control		
	}
	final private StackPane stackPane = new StackPane();
	final private SegmentPane segmentPane = new SegmentPane();
	final private MarkerPane markerPane = new MarkerPane();
	final private IndicatorPane indicatorPane = new IndicatorPane();
	final private NeedlePane needlePane = new NeedlePane();

	// ==================================================================================================================
	// Segments
	
	private class SegmentPane extends Pane {

		final private List<Segment> segments = new ArrayList<>();
		final private Map<Segment, Arc> segmentToArc = new HashMap<>();
		
		/**
		 * 
		 */
		private SegmentPane() {

			// react to changes in the segments
			getSkinnable().segments().addListener( (ListChangeListener.Change<? extends Segment> change) -> {
				getChildren().clear();
				createAndAddSegments();
			});
			createAndAddSegments();
		}
		
		/**
		 * 
		 */
		private void createAndAddSegments() {
	 		// determine what segments to draw
			segments.clear();
			segments.addAll(getSkinnable().segments());
	 		if (segments.size() == 0) {
	 			segments.add(new CompleteSegment(getSkinnable()));
	 		}

	 		// create the nodes representing each segment
	 		segmentToArc.clear();
	 		int segmentCnt = 0;
	 		for (Segment segment : segments) {
	 			
	 			// create an arc for this segment
	 			Arc arc = new Arc();
				getChildren().add(arc);
				segmentToArc.put(segment, arc);
				
				// setup CSS on the path
		        arc.getStyleClass().addAll("segment", "segment" + segmentCnt);
		        if (segment.getId() != null) {
		        	arc.setId(segment.getId());
		        }
	 			segmentCnt++;
	 		}
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();
			
			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
			Point2D center = determineCenter();
	 		
	 		// validate the segments
	 		List<Segment> lSegments = segments;
			String segmentValidationMessage = validateSegments();
			if (segmentValidationMessage != null) {
				System.err.println(segmentValidationMessage);
				lSegments = new ArrayList<Segment>();
	 			lSegments.add(new CompleteSegment(getSkinnable()));
			};

			// layout the segments
	 		double segmentRadius = calculateRadius() * FULL_ARC_RADIUS_FACTOR;
	 		for (Segment segment : lSegments) {
	 			
	 			// layout the arc for this segment
	 	 		double segmentMinValue = segment.getMinValue();
	 	 		double segmentMaxValue = segment.getMaxValue();
	 			double startAngle = (segmentMinValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
	 			double endAngle = (segmentMaxValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
	 			Arc arc = segmentToArc.get(segment);
	 			arc.setCenterX(center.getX());
	 			arc.setCenterY(center.getY());
	 			arc.setRadiusX(segmentRadius);
	 			arc.setRadiusY(segmentRadius);
	 			// 0 degrees is on the right side of the circle (3 o'clock), the gauge starts in the bottom left (about 7 o'clock), so add 90 + 45 degrees to offset to that.
	 			// The arc draws counter clockwise, so we need to negate to make it clock wise.
	 			arc.setStartAngle(-1 * (startAngle + 135.0));
	 			arc.setLength(-1 * (endAngle - startAngle));
	 			arc.setType(ArcType.ROUND);
	 		}
		}
		
		/**
		 * Make segments active
		 */
		void activateSegments() {
	 		// make those segments active that fall under the needle
			double lValue = getSkinnable().getValue();
	 		int cnt = 0;
	 		for (Segment segment : segments) {
	 			
	 			// layout the arc for this segment
	 	 		double segmentMinValue = segment.getMinValue();
	 	 		double segmentMaxValue = segment.getMaxValue();
	 	 		String lSegmentActiveId = "segment" + cnt + "-active";
	 	 		String lSegmentIdActiveId = "segment-" + segment.getId() + "-active";
	 	 		segmentPane.getStyleClass().remove(lSegmentActiveId);
	 	 		segmentToArc.get(segment).getStyleClass().remove("segment-active");
	 	 		if (segment.getId() != null) {
		 	 		segmentPane.getStyleClass().remove(lSegmentIdActiveId);
	 	 		}
	 	 		if (segmentMinValue <= lValue && lValue <= segmentMaxValue) {
		 	 		segmentPane.getStyleClass().add(lSegmentActiveId);
		 	 		segmentToArc.get(segment).getStyleClass().add("segment-active");
		 	 		if (segment.getId() != null) {
			 	 		segmentPane.getStyleClass().add(lSegmentIdActiveId);
		 	 		}
	 	 		}
	 			cnt++;
	 		}
		}
	}
	
	
	// ==================================================================================================================
	// Marker
	
	private class MarkerPane extends Pane {

		final private Map<Marker, SVGPath> markerToSVGPath = new HashMap<>();
		
		/**
		 * 
		 */
		private MarkerPane() {

			// react to changes in the markers
			getSkinnable().markers().addListener( (ListChangeListener.Change<? extends Marker> change) -> {
				getChildren().clear();
				createAndAddMarkers();
			});
			createAndAddMarkers();
		}
		
		/**
		 * 
		 */
		private void createAndAddMarkers() {
	 		// create the nodes representing each marker
	 		markerToSVGPath.clear();
	 		int markerCnt = 0;
	 		for (Marker marker : getSkinnable().markers()) {
	 			
	 			// create an svg path for this marker
	 			SVGPath svgPath = new SVGPath();
	 			svgPath.setContent("M 0 0 L -3 -6 L 3 -6 Z"); // TBEERNOT: why wont -fx-shape work?
				getChildren().add(svgPath);
				markerToSVGPath.put(marker, svgPath);
				
				// setup rotation
				Rotate rotate = new Rotate(0.0);
				rotate.setPivotX(0.0);
				rotate.setPivotY(0.0);
				svgPath.getTransforms().add(rotate);
				
				// setup scaling
				Scale scale = new Scale();
				svgPath.getTransforms().add(scale);
				
				// setup CSS on the path
				svgPath.getStyleClass().addAll("marker", "marker" + markerCnt);
		        if (marker.getId() != null) {
		        	svgPath.setId(marker.getId());
		        }
	 			markerCnt++;
	 		}
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();
			
			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
			Point2D center = determineCenter();
	 		double segmentRadius = calculateRadius() * FULL_ARC_RADIUS_FACTOR;
	 		
	 		// validate the markers
	 		List<Marker> lMarkers = getSkinnable().markers();
			String markerValidationMessage = validateMarkers();
			if (markerValidationMessage != null) {
				System.err.println(markerValidationMessage);
				lMarkers = new ArrayList<Marker>();
			};

			// layout the markers
	 		for (Marker marker : lMarkers) {
	 			
	 			// layout the arc for this segment
	 	 		double markerValue = marker.getValue();
	 			double angle = (markerValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES;
	 			SVGPath svgPath = markerToSVGPath.get(marker);
	 			Point2D markerPoint2D = calculatePointOnCircle(center, segmentRadius, angle);
	 			svgPath.setLayoutX(markerPoint2D.getX());
	 			svgPath.setLayoutY(markerPoint2D.getY());
	 			Rotate rotate = (Rotate)svgPath.getTransforms().get(0);
				rotate.setAngle(angle - 135.0); // the angle also determines the rotation	 			
	 			Scale scale = (Scale)svgPath.getTransforms().get(1);
	 			scale.setX(getWidth() / 300.0); // SVG was created against a sample with 300.0 pixels  
	 			scale.setY(scale.getX()); 
	 		}
		}
	}
	
	
	// ==================================================================================================================
	// Indicators
	
	private class IndicatorPane extends Pane {

		/**
		 * 
		 */
		private IndicatorPane() {

	 		// add the indicators
			getChildren().add(warningIndicator);
			getChildren().add(errorIndicator);
			warningIndicator.getStyleClass().add("warning-indicator");
			errorIndicator.getStyleClass().add("error-indicator");
		}
		private Circle warningIndicator = new Circle(20.0);
		private Circle errorIndicator = new Circle(20.0);

		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prepare
			Point2D center = determineCenter();
	 		double segmentRadius = calculateRadius() * FULL_ARC_RADIUS_FACTOR;
	 		
	 		// size & position the indicators
	 		double indicatorRadius = segmentRadius * 0.15;
	 		double indicatorDiameter = 2 * indicatorRadius;
	 		warningIndicator.setRadius(indicatorRadius);
			warningIndicator.layoutXProperty().set(center.getX() - indicatorDiameter);
			warningIndicator.layoutYProperty().set(center.getY() + segmentRadius - indicatorDiameter);
			errorIndicator.setRadius(indicatorRadius);
			errorIndicator.layoutXProperty().set(center.getX() + indicatorDiameter);
			errorIndicator.layoutYProperty().set(center.getY() + segmentRadius - indicatorDiameter);
		}
	}
	
	
	// ==================================================================================================================
	// NEEDLE
	// OPTION: Move needle path out into CSS and allow for other needles, like https://www.youtube.com/watch?v=oAdwQTy4jms
	
	private class NeedlePane extends Pane {
		
		final private Path needlePath = new Path();
		final private Rotate needleRotate = new Rotate(0.0);
		final private Text valueText = new Text("");
		final private Scale valueScale = new Scale(1.0, 1.0);
		final private Text minmaxValueText = new Text("");

		/**
		 * 
		 */
		private NeedlePane() {
	        
	        // add the needle
	        getChildren().add(needlePath);
	        needlePath.getStyleClass().add("needle");
			rotateNeedle(false);
	        
	        // value text
	        getChildren().add(valueText);
			valueText.getStyleClass().add("value");
			valueText.getTransforms().setAll(valueScale);
			minmaxValueText.getStyleClass().add("value");
			getSkinnable().valueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
				setValueText();
				positionValueText();
			});
			
	        // min and max value text need to be added to the scene in order to have the CSS applied
	        getChildren().add(minmaxValueText);
	        minmaxValueText.setVisible(false);
			getSkinnable().minValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true); 
				setValueText(); 
				scaleValueText();
				positionValueText();
			});
			getSkinnable().maxValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
				setValueText();
				scaleValueText();
				positionValueText();
			});
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// we only need to layout if the size changes
			if (previousWidth != getWidth() || previousHeight != getHeight()) {
				
				// preparation
				Point2D center = determineCenter();
		 		double radius = calculateRadius();
				double tipRadius = radius * TIP_RADIUS_FACTOR;
				double arcRadius = radius * NEEDLE_ARC_RADIUS_FACTOR;
				
				// calculate the important points of the needle
				Point2D arcStartPoint2D = calculatePointOnCircle(center, arcRadius, 0.0 - 15.0);
				Point2D arcEndPoint2D = calculatePointOnCircle(center, arcRadius, 0.0 + 15.0);
				Point2D tipPoint2D = calculatePointOnCircle(center, tipRadius, 0.0);
				
				// we use a path to draw the needle
				needlePath.getElements().clear();
		        needlePath.setFillRule(FillRule.EVEN_ODD);        
				needlePath.getStyleClass().add("needle");
				needlePath.setStrokeLineJoin(StrokeLineJoin.ROUND);
				
		        // begin of arc
		        needlePath.getElements().add( new MoveTo(arcStartPoint2D.getX(), arcStartPoint2D.getY()) );
		        
		        // arc to the end point
		        ArcTo arcTo = new ArcTo();
		        arcTo.setX(arcEndPoint2D.getX());
		        arcTo.setY(arcEndPoint2D.getY());
		        arcTo.setRadiusX(arcRadius);
		        arcTo.setRadiusY(arcRadius);
		        arcTo.setLargeArcFlag(true);
		        arcTo.setSweepFlag(false);
		        needlePath.getElements().add(arcTo);
		        
		        // two lines to the tip
		        needlePath.getElements().add(new LineTo(tipPoint2D.getX(), tipPoint2D.getY()));
		        needlePath.getElements().add(new LineTo(arcStartPoint2D.getX(), arcStartPoint2D.getY()));
		        
		        // set the line around the needle; this is relative to the size of the gauge, so it is not set in CSS
		        needlePath.setStrokeWidth(arcRadius * 0.10);
		        
		        // set to rotate around the center of the gauge
		        needleRotate.setPivotX(center.getX());
		        needleRotate.setPivotY(center.getY());
		        needlePath.getTransforms().setAll(needleRotate);
		        
		        // layout value
				setValueText();
				scaleValueText();
				positionValueText();

				// remember
				previousWidth = getWidth();
				previousHeight = getHeight();
			}
		}
		private double previousWidth = -1.0;
		private double previousHeight = -1.0;

		/**
		 * @param allowAnimation AllowAnimation is needed only in the first pass during skin construction: the Animated property has not been set at that time, so we do not need if animation is wanted. So the initial rotation is always done unanimated.  
		 */
		private void rotateNeedle(boolean allowAnimation) {
			
			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
	 		double value = getSkinnable().getValue();
	 		double angle = (value - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES;
	 		
	 		// We cannot use node.setRotate(angle), because this rotates always around the center of the node and the needle's rotation center is not the same as the node's center.
	 		// So we need to use the Rotate transformation, which allows to specify the center of rotation.
	 		// This however also means that we cannot use RotateTransition, because that manipulates the rotate property of a node (and -as explain above- we couldn't use that).
	 		// The only way to animate a Rotate transformation is to use a timeline and keyframes.
	 		if (allowAnimation == false || Animated.NO.equals(getAnimated())) {
	 	 		needleRotate.setAngle(angle);
	 		}
	 		else {
	 			timeline.stop();
		        final KeyValue KEY_VALUE = new KeyValue(needleRotate.angleProperty(), angle, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
		        final KeyFrame KEY_FRAME = new KeyFrame(Duration.millis(1000), KEY_VALUE);
		        timeline.getKeyFrames().setAll(KEY_FRAME);
		        timeline.play();
	 		}
	 		
	 		// make certain segments active because the needle moved
	 		segmentPane.activateSegments();
		}
		final private Timeline timeline = new Timeline();
		
		/**
		 * 
		 */
		private void setValueText() {
			if (!validateValueAndHandleInvalid()) {
				return;
			}
			valueText.setText(valueFormat(getSkinnable().getValue()));
		}

		/**
		 * The value should automatically fill the needle as much as possible.
		 * But it should not constantly switch font size, so it cannot be based on the current content of value's Text node.
		 * So to determine how much the Text node must be scaled, the calculation is based on value's extremes: min and max value.
		 * The smallest scale factor is the one to use (using the larger would make the other extreme go out of the circle).   
		 */
		private void scaleValueText() {
			
			// preparation
	 		double radius = calculateRadius();
			double arcRadius = radius * NEEDLE_ARC_RADIUS_FACTOR;
			
			// use the two extreme's to determine the scaling factor
			double minScale = calculateScaleFactor(arcRadius, getSkinnable().getMinValue());
			double maxScale = calculateScaleFactor(arcRadius, getSkinnable().getMaxValue());
			double scale = Math.min(minScale, maxScale);
			valueScale.setX(scale);
			valueScale.setY(scale);
		}
		
		/**
		 * Determine how much to scale the Text node containing the value to fill up the needle's circle
		 * @param radius The radius of the needle
		 * @param value The value to be rendered
		 * @return
		 */
		private double calculateScaleFactor(double radius, double value) {
			minmaxValueText.setText(valueFormat(value));
			double width = minmaxValueText.getBoundsInParent().getWidth();
			double height = minmaxValueText.getBoundsInParent().getHeight();
			double diameter = radius * 2.0;
			// Width and height construct a right angled triangle, where the hypotenuse should be equal to the diameter of the needle's circle.
			// So apply some Pythagoras...
			double scaleFactor = diameter / Math.sqrt((width*width) + (height*height));
			return scaleFactor;
		}
		
		/**
		 * After having set the value in the Text and determining the scaling, position the Text node in the center of the needle.
		 */
		private void positionValueText() {
			// position in center of needle
			Point2D center = determineCenter();
			double width = valueText.getBoundsInParent().getWidth();
			double height = valueText.getBoundsInParent().getHeight();
			valueText.setLayoutX(center.getX() - (width  / 2.0)); 
			valueText.setLayoutY(center.getY() + (height  / 4.0));
		}
	}

	private boolean validateValueAndHandleInvalid() {
		String validationMessage = validateValue();
		if (validationMessage != null) {
			new Throwable(validationMessage).printStackTrace();
			needlePane.valueText.setText("");
			needlePane.needleRotate.setAngle(-45.0);
			return false;
		};
		return true;
	}
	
	// ==================================================================================================================
	// SUPPORT
	
	/**
	 * http://www.mathopenref.com/coordparamcircle.html
	 * @param center
	 * @param radius
	 * @param angleInDegrees
	 * @return
	 */
	static private Point2D calculatePointOnCircle(Point2D center, double radius, double angleInDegrees) {
		// Java's math uses radians
		// 0 degrees is on the right side of the circle (3 o'clock), the gauge starts in the bottom left (about 7 o'clock), so add 90 + 45 degrees to offset to that. 
		double angleInRadians = Math.toRadians(angleInDegrees + 135.0);
		
		// calculate point on circle
		double x = center.getX() + (radius * Math.cos(angleInRadians));
		double y = center.getY() + (radius * Math.sin(angleInRadians));
		return new Point2D(x, y);
	}
	
	/**
	 * 
	 * @return
	 */
	private double calculateRadius() {
		Point2D center = determineCenter();
		return Math.min(center.getX(), center.getY());
	}

	/**
	 * 
	 * @return
	 */
	private Point2D determineCenter() {
		Point2D center = new Point2D(stackPane.getWidth() / 2.0, stackPane.getHeight() * 0.55);
		return center;
	}
}
