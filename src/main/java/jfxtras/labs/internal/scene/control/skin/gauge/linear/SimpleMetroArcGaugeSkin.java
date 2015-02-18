package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.Styleable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
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
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.labs.scene.control.gauge.linear.CompleteSegment;
import jfxtras.labs.scene.control.gauge.linear.Segment;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.StringConverter;

/**
 * 
 */
public class SimpleMetroArcGaugeSkin extends SkinBase<SimpleMetroArcGauge> {

	private static final double NEEDLE_ARC_RADIUS_FACTOR = 0.5;
	private static final double TIP_RADIUS_FACTOR = 0.9;
	static final private double FULL_ARC_IN_DEGREES = 270.0;

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public SimpleMetroArcGaugeSkin(SimpleMetroArcGauge control) {
		super(control);
		construct();
	}

	/*
	 * 
	 */
	private void construct() {
		createNodes();
	}
	
	
	// ==================================================================================================================
	// StyleableProperties
	
    /**
     * animated
     */
    public final ObjectProperty<Animated> animatedProperty() { return animatedProperty; }
    private ObjectProperty<Animated> animatedProperty = new SimpleStyleableObjectProperty<Animated>(StyleableProperties.ANIMATED_CSSMETADATA, StyleableProperties.ANIMATED_CSSMETADATA.getInitialValue(null));
    public final void setAnimated(Animated value) { animatedProperty().set(value); }
    public final Animated getAnimated() { return animatedProperty.get(); }
    public final SimpleMetroArcGaugeSkin withAnimated(Animated value) { setAnimated(value); return this; }
    public enum Animated {YES, NO}

    /**
     * valueFormat
     */
    public final SimpleStyleableStringProperty valueFormatProperty() { return valueFormatProperty; }
    private SimpleStyleableStringProperty valueFormatProperty = new SimpleStyleableStringProperty(StyleableProperties.VALUE_FORMAT_CSSMETADATA, StyleableProperties.VALUE_FORMAT_CSSMETADATA.getInitialValue(null));
    public final void setValueFormat(String value) { valueFormatProperty.set(value); }
    public final String getValueFormat() { return valueFormatProperty.get(); }
    public final SimpleMetroArcGaugeSkin withValueFormat(String value) { setValueFormat(value); return this; }
    private String valueFormat(double value) {
    	// TBEERNOT do not create a decimal format every time
    	return new DecimalFormat(getValueFormat()).format(value);
    }
    
    // -------------------------
        
    private static class StyleableProperties 
    {
        private static final CssMetaData<SimpleMetroArcGauge, Animated> ANIMATED_CSSMETADATA = new CssMetaDataForSkinProperty<SimpleMetroArcGauge, SimpleMetroArcGaugeSkin, Animated>("-fxx-animated", new EnumConverter<Animated>(Animated.class), Animated.YES ) {
        	@Override 
        	protected ObjectProperty<Animated> getProperty(SimpleMetroArcGaugeSkin s) {
            	return s.animatedProperty;
            }
        };
        
        private static final CssMetaData<SimpleMetroArcGauge, String> VALUE_FORMAT_CSSMETADATA = new CssMetaDataForSkinProperty<SimpleMetroArcGauge, SimpleMetroArcGaugeSkin, String>("-fxx-value-format", StringConverter.getInstance(), "0" ) {
        	@Override 
        	protected SimpleStyleableStringProperty getProperty(SimpleMetroArcGaugeSkin s) {
            	return s.valueFormatProperty;
            }
        };
        
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static  {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(ANIMATED_CSSMETADATA);
            styleables.add(VALUE_FORMAT_CSSMETADATA);
            STYLEABLES = Collections.unmodifiableList(styleables);                
        }
    }
    
    /** 
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */    
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * This method should delegate to {@link Node#getClassCssMetaData()} so that
     * a Node's CssMetaData can be accessed without the need for reflection.
     * @return The CssMetaData associated with this node, which may include the
     * CssMetaData of its super classes.
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
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
		needleRotate = new Rotate(0.0);
		rotateNeedle(false);
		
		// value
		valueText = new Text("");
		valueScale = new Scale(1.0, 1.0);
		valueText.getStyleClass().add("value");
		valueText.getTransforms().setAll(valueScale);
		minmaxValueText = new Text("");
		minmaxValueText.getStyleClass().add("value");
		getSkinnable().valueProperty().addListener( (observable) -> {
			rotateNeedle(true);
			setValueText();
			positionValueText();
		});
		getSkinnable().minValueProperty().addListener( (observable) -> {
			scaleValueText();
			positionValueText();
		});
		getSkinnable().maxValueProperty().addListener( (observable) -> {
			scaleValueText();
			positionValueText();
		});
		
		// we use a stack pane to control the layers
		StackPane lStackPane = new StackPane();
		lStackPane.getChildren().add(dialPane);
		lStackPane.getChildren().add(needlePane);
		getChildren().add(lStackPane);

		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
	}
	private Pane dialPane;
	private Pane needlePane;
	private Rotate needleRotate;
	private Text valueText;
	private Scale valueScale;
	private Text minmaxValueText;

	/**
	 * There is no way to detect if some node's CSS has been applied.
	 * So we cannot determine if valueText may have changed and needs repositioning.
	 * As a brute force approach it is repositioned in every layout pass.
	 */
	@Override
	protected void layoutChildren(double arg0, double arg1, double arg2, double arg3) {
		super.layoutChildren(arg0, arg1, arg2, arg3);
		setValueText();
		scaleValueText();
		positionValueText(); 
	}

	/**
	 * 
	 */
	private void drawDialPane() {
		// TBEERNOT: can we optimize the drawing (e.g. when width & height have not changed, skip)
		// TBEERNOT: handle that not min <= value <= max

		// we always draw from scratch
		dialPane.getChildren().clear();
		
		// preparation
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();
 		double controlValueRange = controlMaxValue - controlMinValue;
 		
 		// determine what segments to draw
 		List<Segment> segments = new ArrayList<Segment>(getSkinnable().segments());
 		if (segments.size() == 0) {
 			segments.add(completeSegment);
 		}
 		
 		// draw the segments
		Point2D center = determineCenter();
 		double radius = Math.min(center.getX(), center.getY());
 		int cnt = 0;
 		for (Segment segment : segments) {
 			
 			// create an arc for this segment
 	 		double segmentMinValue = segment.getMinValue();
 	 		double segmentMaxValue = segment.getMaxValue();
 			double startAngle = (segmentMinValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
 			double endAngle = (segmentMaxValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
 			Arc arc = new Arc();
 			arc.setCenterX(center.getX());
 			arc.setCenterY(center.getY());
 			arc.setRadiusX(radius);
 			arc.setRadiusY(radius);
 			// 0 degrees is on the right side of the circle (3 o'clock), the gauge starts in the bottom left (about 7 o'clock), so add 90 + 45 degrees to offset to that.
 			// The arc draws counter clockwise, so we need to negate to make it clock wise.
 			arc.setStartAngle(-1 * (startAngle + 135.0));
 			arc.setLength(-1 * (endAngle - startAngle));
 			arc.setType(ArcType.ROUND);
			dialPane.getChildren().add(arc);
			
			// setup CSS on the path
	        arc.getStyleClass().addAll("segment", "segment" + cnt);
	        if (segment.getId() != null) {
	        	arc.setId(segment.getId());
	        }
	        
 			cnt++;
 		}
	}
	final private CompleteSegment completeSegment = new CompleteSegment(getSkinnable());

	/**
	 * 
	 */
	private void drawNeedlePane() {
		// TBEERNOT: can we optimize the drawing (e.g. when width & height have not changed, skip)
		// TBEERNOT: handle that not min <= value <= max

 		// we always draw from scratch
		needlePane.getChildren().clear();
		
		// preparation
		Point2D center = determineCenter();
 		double radius = Math.min(center.getX(), center.getY());
		double tipRadius = radius * TIP_RADIUS_FACTOR;
		double arcRadius = radius * NEEDLE_ARC_RADIUS_FACTOR;
		
		// calculate the important points of the needle
		Point2D arcStartPoint2D = calculatePointOnCircle(center, arcRadius, 0.0 - 15.0);
		Point2D arcEndPoint2D = calculatePointOnCircle(center, arcRadius, 0.0 + 15.0);
		Point2D tipPoint2D = calculatePointOnCircle(center, tipRadius, 0.0);
		
		// we use a path to draw the needle
		Path needle = new Path();
        needle.setFillRule(FillRule.EVEN_ODD);        
		needle.getStyleClass().add("needle");
		needle.setStrokeLineJoin(StrokeLineJoin.ROUND);
		
        // begin of arc
        needle.getElements().add( new MoveTo(arcStartPoint2D.getX(), arcStartPoint2D.getY()) );
        
        // arc to the end point
        {
	        ArcTo arcTo = new ArcTo();
	        arcTo.setX(arcEndPoint2D.getX());
	        arcTo.setY(arcEndPoint2D.getY());
	        arcTo.setRadiusX(arcRadius);
	        arcTo.setRadiusY(arcRadius);
	        arcTo.setLargeArcFlag(true);
	        arcTo.setSweepFlag(false);
	        needle.getElements().add(arcTo);
        }
        
        // two lines to the tip
        needle.getElements().add(new LineTo(tipPoint2D.getX(), tipPoint2D.getY()));
        needle.getElements().add(new LineTo(arcStartPoint2D.getX(), arcStartPoint2D.getY()));
        
        // set the line around the needle; this is relative to the size of the gauge, so it is not set in CSS
        needle.setStrokeWidth(arcRadius * 0.10);
        
        // set to rotate around the center of the gauge
        needleRotate.setPivotX(center.getX());
        needleRotate.setPivotY(center.getY());
        needle.getTransforms().setAll(needleRotate); 
        
        // add the needle
        needlePane.getChildren().add(needle);
        
        // add the text
        needlePane.getChildren().add(valueText);
        // min and max value text need to be added to the scene in order to have the CSS applied
        needlePane.getChildren().add(minmaxValueText);
        minmaxValueText.setVisible(false);
	}
	

	/**
	 * 
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

	}
	final private Timeline timeline = new Timeline();

	/**
	 * 
	 */
	private void setValueText() {
		valueText.setText(valueFormat(getSkinnable().getValue()));
	}
	
	/**
	 * 
	 */
	private void scaleValueText() {
		
		// preparation
		Point2D center = determineCenter();
 		double radius = Math.min(center.getX(), center.getY());
		double arcRadius = radius * NEEDLE_ARC_RADIUS_FACTOR;
		
		// use the two extreme's to determine the scaling factor
		double minScale = calculateScaleFactor(arcRadius, getSkinnable().getMinValue());
		double maxScale = calculateScaleFactor(arcRadius, getSkinnable().getMaxValue());
		double scale = Math.min(minScale, maxScale);
		valueScale.setX(scale);
		valueScale.setY(scale);
	}
	
	private double calculateScaleFactor(double radius, double value) {
		double diameter = radius * 2.0;
		minmaxValueText.setText(valueFormat(value));
		double width = minmaxValueText.getBoundsInParent().getWidth();
		double height = minmaxValueText.getBoundsInParent().getHeight();
		double scaleFactor = diameter / Math.sqrt((width*width) + (height*height));
		return scaleFactor;
	}
	
	private void positionValueText() {
		// position in center of needle
		Point2D center = determineCenter();
		double width = valueText.getBoundsInParent().getWidth();
		double height = valueText.getBoundsInParent().getHeight();
		valueText.setLayoutX(center.getX() - (width  / 2.0)); 
		valueText.setLayoutY(center.getY() + (height  / 4.0));
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
	private Point2D determineCenter() {
		Point2D center = new Point2D(dialPane.getWidth() / 2.0, dialPane.getHeight() * 0.6);
		return center;
	}
}
