package jfxtras.labs.internal.scene.control.gauge.linear.skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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

	private static final double SEGMENT_RADIUS_FACTOR = 0.95;
	private static final double NEEDLE_RADIUS_FACTOR = 0.5;
	private static final double NEEDLE_TIP_RADIUS_FACTOR = 0.87;
	private static final double MARKER_RADIUS_FACTOR = SEGMENT_RADIUS_FACTOR * 0.15;
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
		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because with multiple skins the CSS should relate to the skin not the control		

		// determine center
		centerX.bind(stackPane.widthProperty().multiply(0.5));
		centerY.bind(stackPane.heightProperty().multiply(0.55));

		// use a stack pane to control the layers
		stackPane.getChildren().addAll(segmentPane, markerPane, indicatorPane, needlePane);
		getChildren().add(stackPane);
	}
	final private SimpleDoubleProperty centerX = new SimpleDoubleProperty();
	final private SimpleDoubleProperty centerY = new SimpleDoubleProperty();
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
				createAndAddSegments();
			});
			createAndAddSegments();
		}
		
		/**
		 * 
		 */
		private void createAndAddSegments() {
	 		// determine what segments to draw
			getChildren().clear();
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
	 		
			// layout the segments
	 		double segmentRadius = calculateRadius() * SEGMENT_RADIUS_FACTOR;
	 		for (Segment segment : segments) {
	 			String message = validateSegment(segment);
	 			if (message != null) {
	 				new Throwable(message).printStackTrace();
	 				continue;
	 			}
	 			
	 			// layout the arc for this segment
	 	 		double segmentMinValue = segment.getMinValue();
	 	 		double segmentMaxValue = segment.getMaxValue();
	 			double startAngle = (segmentMinValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
	 			double endAngle = (segmentMaxValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
	 			Arc arc = segmentToArc.get(segment);
	 			if (arc != null) {
		 			arc.setCenterX(centerX.get());
		 			arc.setCenterY(centerY.get());
		 			arc.setRadiusX(segmentRadius);
		 			arc.setRadiusY(segmentRadius);
		 			// 0 degrees is on the right side of the circle (3 o'clock), the gauge starts in the bottom left (about 7 o'clock), so add 90 + 45 degrees to offset to that.
		 			// The arc draws counter clockwise, so we need to negate to make it clock wise.
		 			arc.setStartAngle(-1 * (startAngle + 135.0));
		 			arc.setLength(-1 * (endAngle - startAngle));
		 			arc.setType(ArcType.ROUND);
	 			}
	 		}
		}
	}
	
	
	// ==================================================================================================================
	// Marker
	
	private class MarkerPane extends Pane {

		final private Map<Marker, Region> markerToRegion = new HashMap<>();
		
		/**
		 * 
		 */
		private MarkerPane() {

			// react to changes in the markers
			getSkinnable().markers().addListener( (ListChangeListener.Change<? extends Marker> change) -> {
				createAndAddMarkers();
			});
			createAndAddMarkers();
		}
		
		/**
		 * 
		 */
		private void createAndAddMarkers() {
	 		// create the nodes representing each marker
			getChildren().clear();
	 		markerToRegion.clear();
	 		int markerCnt = 0;
	 		for (Marker marker : getSkinnable().markers()) {

	 			// create an svg path for this marker
	 			Region region = new Region();
				getChildren().add(region);
				markerToRegion.put(marker, region);
				
				// setup rotation
				Rotate rotate = new Rotate(0.0);
				rotate.setPivotX(0.0);
				rotate.setPivotY(0.0);
				region.getTransforms().add(rotate);
				
				// setup scaling
				Scale scale = new Scale();
				region.getTransforms().add(scale);
				
				// setup CSS on the path
				region.getStyleClass().addAll("marker", "marker" + markerCnt);
		        if (marker.getId() != null) {
		        	region.setId(marker.getId());
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
	 		double radius = calculateRadius();
			double segmentRadius = radius * SEGMENT_RADIUS_FACTOR;
	 		
			// layout the markers
	 		for (Marker marker : getSkinnable().markers()) {
	 			String message = validateMarker(marker);
	 			if (message != null) {
	 				new Throwable(message).printStackTrace();
	 				continue;
	 			}

	 			// layout the svg shape 
	 	 		double markerValue = marker.getValue();
	 			double angle = (markerValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES;
	 			Region region = markerToRegion.get(marker);
	 			Point2D markerPoint2D = calculatePointOnCircle(segmentRadius, angle);
	 			region.setLayoutX(markerPoint2D.getX());
	 			region.setLayoutY(markerPoint2D.getY());
	 			Rotate rotate = (Rotate)region.getTransforms().get(0);
				rotate.setAngle(angle - 135.0); // the angle also determines the rotation	 			
	 			Scale scale = (Scale)region.getTransforms().get(1);
	 			scale.setX(2 * radius / 300.0); // SVG shape was created against a sample gauge with 300x300 pixels  
	 			scale.setY(scale.getX()); 
	 		}
		}
	}
	
	
	// ==================================================================================================================
	// Indicators
	
	protected class IndicatorPane extends AbstractIndicatorPane {
		
		@Override
		protected double calculateRadius() {
			return SimpleMetroArcGaugeSkin.this.calculateRadius();
		}
		
		@Override
		protected Point2D calculateLocation(int idx) {

			// prepare
	 		double radius = calculateRadius();
			double segmentRadius = radius * SEGMENT_RADIUS_FACTOR;
			double indicatorRadius = radius * MARKER_RADIUS_FACTOR;
	 		double indicatorDiameter = 2 * indicatorRadius;
	 			
			// TBEERNOT: can we do something better than cascades ifs?
			if (idx == 0 ) {
		 		return new Point2D(centerX.get() - indicatorDiameter, centerY.get() + segmentRadius - indicatorDiameter);
 			}
 			if (idx == 1) {
 				return new Point2D(centerX.get() + indicatorDiameter, centerY.get() + segmentRadius - indicatorDiameter);
 			}
			System.err.println("The " + getSkinnable().getClass().getSimpleName() + " gauge supports indicators [0,1], not " + idx);
			return null;
		}
	}
	
	
	// ==================================================================================================================
	// NEEDLE
	// OPTION: Move needle path out into CSS and allow for other needles, like https://www.youtube.com/watch?v=oAdwQTy4jms
	
	private class NeedlePane extends Pane {
		
		final private Path needlePath = new Path();
		final private Rotate needleRotate = new Rotate(0.0);
		final private Text valueText = new Text("");
		final private Pane valueTextPane = new StackPane(valueText);
		final private Scale valueScale = new Scale(1.0, 1.0);

		/**
		 * 
		 */
		private NeedlePane() {
	        
	        // add the needle
	        getChildren().add(needlePath);
	        needlePath.getStyleClass().add("needle");
	        needleRotate.pivotXProperty().bind(centerX);
	        needleRotate.pivotYProperty().bind(centerY);
	        
	        // value text
	        getChildren().add(valueTextPane);
			valueText.getStyleClass().add("value");
			valueTextPane.getTransforms().setAll(valueScale);
			// for debugging valueTextPane.setStyle("-fx-border-color: #000000;");
			getSkinnable().valueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
				setValueText();
			});
			
	        // min and max value text need to be added to the scene in order to have the CSS applied
			getSkinnable().minValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true); 
				scaleValueText();
			});
			getSkinnable().maxValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
				scaleValueText();
			});
			
			// a hidden text to determine how large the min and max value would be
			hiddenText.getStyleClass().add("value");
	        hiddenText.setVisible(false);
	        getChildren().add(hiddenText);

			// position valueTextPane
			valueTextPane.layoutXProperty().bind(centerX.subtract( valueTextPane.widthProperty().multiply(0.5).multiply(valueScale.xProperty()) )); 
			valueTextPane.layoutYProperty().bind(centerY.subtract( valueTextPane.heightProperty().multiply(0.5).multiply(valueScale.yProperty()) ));

			// init
			rotateNeedle(false);
			setValueText();
			scaleValueText();
		}
		final private Text hiddenText = new Text("");
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// we only need to layout if the size changes
			if (previousWidth != getWidth() || previousHeight != getHeight()) {
				
				// preparation
		 		double radius = calculateRadius();
				double tipRadius = radius * NEEDLE_TIP_RADIUS_FACTOR;
				double arcRadius = radius * NEEDLE_RADIUS_FACTOR;
				
				// calculate the important points of the needle
				Point2D arcStartPoint2D = calculatePointOnCircle(arcRadius, 0.0 - 15.0);
				Point2D arcEndPoint2D = calculatePointOnCircle(arcRadius, 0.0 + 15.0);
				Point2D tipPoint2D = calculatePointOnCircle(tipRadius, 0.0);
				
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
		        needlePath.getTransforms().setAll(needleRotate);
		        
		        // layout value
				setValueText();
				scaleValueText();

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
	 		activateSegments(segmentPane.segmentToArc);
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
			double arcRadius = radius * NEEDLE_RADIUS_FACTOR;
			
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
			hiddenText.setText(valueFormat(value));
			double width = hiddenText.getBoundsInParent().getWidth();
			double height = hiddenText.getBoundsInParent().getHeight();
			double diameter = radius * 2.0;
			// Width and height construct a right angled triangle, where the hypotenuse should be equal to the diameter of the needle's circle.
			// So apply some Pythagoras...
			double scaleFactor = diameter / Math.sqrt((width*width) + (height*height));
			return scaleFactor;
		}
	}

	private boolean validateValueAndHandleInvalid() {
		String validationMessage = validateValue();
		if (validationMessage != null) {
			new Throwable(validationMessage).printStackTrace();
			if (needlePane != null) {
				needlePane.valueText.setText("");
				needlePane.needleRotate.setAngle(-45.0);
			}
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
	private Point2D calculatePointOnCircle(double radius, double angleInDegrees) {
		// Java's math uses radians
		// 0 degrees is on the right side of the circle (3 o'clock), the gauge starts in the bottom left (about 7 o'clock), so add 90 + 45 degrees to offset to that. 
		double angleInRadians = Math.toRadians(angleInDegrees + 135.0);
		
		// calculate point on circle
		double x = centerX.get() + (radius * Math.cos(angleInRadians));
		double y = centerY.get() + (radius * Math.sin(angleInRadians));
		return new Point2D(x, y);
	}
	
	/**
	 * 
	 * @return
	 */
	private double calculateRadius() {
		return Math.min(centerX.get(), centerY.get());
	}
}
