package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.labs.scene.control.gauge.linear.BasicArcGauge;

import com.sun.javafx.css.converters.PaintConverter;

/**
 * 
 */
public class BasicArcGaugeSkin extends LinearGaugeSkin<BasicArcGaugeSkin, BasicArcGauge> {

	private static final double RING_OUTER_RADIUS_FACTOR = 0.97;
	private static final double RING_INNER_RADIUS_FACTOR = 0.94;
	private static final double RING_WIDTH_FACTOR = 0.04;
	private static final double BACKPLATE_RADIUS_FACTOR = 0.95;
	private static final double TICK_OUTER_RADIUS_FACTOR = 0.90;
	private static final double TICK_INNER_RADIUS_FACTOR = 0.80;
	private static final double TICK_MINOR_RADIUS_FACTOR = 0.77;
	private static final double TICK_MAJOR_RADIUS_FACTOR = 0.75;
	static final private double FULL_ARC_IN_DEGREES = 270.0;

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public BasicArcGaugeSkin(BasicArcGauge control) {
		super(control);
		constructNodes();
	}
	
	// ==================================================================================================================
	// StyleableProperties
	
    /**
     * animated
     */
    public final ObjectProperty<Paint> tickColorProperty() { return tickColorProperty; }
    private ObjectProperty<Paint> tickColorProperty = new SimpleStyleableObjectProperty<Paint>(StyleableProperties.TICKCOLOR_CSSMETADATA, StyleableProperties.TICKCOLOR_CSSMETADATA.getInitialValue(null));
    public final void setTickColor(Paint value) { tickColorProperty().set(value); }
    public final Paint getTickColor() { return tickColorProperty.get(); }
    public final BasicArcGaugeSkin withTickColor(Paint value) { setTickColor(value); return this; }


    // -------------------------
        
    private static class StyleableProperties 
    {
        private static final CssMetaData<BasicArcGauge, Paint> TICKCOLOR_CSSMETADATA = new CssMetaDataForSkinProperty<BasicArcGauge, BasicArcGaugeSkin, Paint>("-fxx-tick-color", PaintConverter.getInstance(), Color.BLACK ) {
        	@Override 
        	protected ObjectProperty<Paint> getProperty(BasicArcGaugeSkin s) {
            	return s.tickColorProperty;
            }
        };
        
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static  {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(TICKCOLOR_CSSMETADATA);
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
	private void constructNodes()
	{
		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because with multiple skins CSS should relate to the skin not the control		

		// determine center
		centerX.bind(stackPane.widthProperty().multiply(0.5));
		centerY.bind(stackPane.heightProperty().multiply(0.5));

		// use a stack pane to control the layers
		stackPane.getChildren().addAll(backPlatePane, needlePane, glassPlatePane);
		getChildren().add(stackPane);
		stackPane.setPrefSize(200, 200);
	}
	final private SimpleDoubleProperty centerX = new SimpleDoubleProperty();
	final private SimpleDoubleProperty centerY = new SimpleDoubleProperty();
	final private StackPane stackPane = new StackPane();
	final private BackPlatePane backPlatePane = new BackPlatePane();
	final private NeedlePane needlePane = new NeedlePane();
	final private GlassPlatePane glassPlatePane = new GlassPlatePane();
	

	// ==================================================================================================================
	// BackPlate
	
	private class BackPlatePane extends Pane {

		/**
		 * 
		 */
		private BackPlatePane() {
			this.getStyleClass().add("BackPlatePane");
			
			// backpane
			backpaneCircle.getStyleClass().addAll("backplate");
			backpaneCircle.centerXProperty().bind(centerX);
			backpaneCircle.centerYProperty().bind(centerY);
			
			// ticks
            ticksCanvas.setCache(true);
            ticksCanvas.setCacheHint(CacheHint.QUALITY);
            ticksCanvas.getStyleClass().addAll("tick");
			ticksCanvas.layoutXProperty().set(0.0);
			ticksCanvas.layoutYProperty().set(0.0);
			ticksCanvas.widthProperty().bind(stackPane.widthProperty());
			ticksCanvas.heightProperty().bind(stackPane.heightProperty());
            
            // add them
			getChildren().addAll(backpaneCircle, ticksCanvas);
		}
		final private Circle backpaneCircle = new Circle();
		final private Canvas ticksCanvas = new Canvas();
		final private Text tickText = new Text();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			double radius = calculateRadius(); // radius must be calculated and cannot use bind

			// size the circle
			double plateRadius = radius * BACKPLATE_RADIUS_FACTOR;
			backpaneCircle.setRadius(plateRadius);
			
			// paint the ticks
			double size = radius * 2.0;
			GraphicsContext graphicsContext = ticksCanvas.getGraphicsContext2D();
			graphicsContext.clearRect(0, 0, size, size);
			graphicsContext.setStroke(getTickColor());
			double tickInnerRadius = radius * TICK_INNER_RADIUS_FACTOR;
			double tickOuterRadius = radius * TICK_OUTER_RADIUS_FACTOR;
			double tickMajorRadius = radius * TICK_MAJOR_RADIUS_FACTOR;
			double tickMinorRadius = radius * TICK_MINOR_RADIUS_FACTOR;
            for (int i = 0; i <= 100; i++) { 
            	double angle = FULL_ARC_IN_DEGREES / 100.0 * (double)i; 
            	Point2D outerPoint2D = calculatePointOnCircle(tickOuterRadius, angle);
            	Point2D innerPoint2D = null;
            	
            	// major
            	if (i % 10 == 0) {
                	innerPoint2D = calculatePointOnCircle(tickMajorRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.0055);
            	}
            	// medium
            	else if (i % 5 == 0) {
                	innerPoint2D = calculatePointOnCircle(tickMinorRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.0035);
            	}
            	// minor
            	else {
                	innerPoint2D = calculatePointOnCircle(tickInnerRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.00225);
            	}
            	graphicsContext.strokeLine(innerPoint2D.getX(), innerPoint2D.getY(), outerPoint2D.getX(), outerPoint2D.getY());
            	
            	// major
            	if (i % 10 == 0) {
//            		double tickValue = getSkinnable().getMinValue() + ((getSkinnable().getMaxValue() - getSkinnable().getMinValue()) * ((double)i) / 100.0);
        			tickText.setFont(Font.font("Verdana", FontWeight.NORMAL, 0.045 * size));
            		 
	                // Draw text
                	Point2D textPoint2D = calculatePointOnCircle(tickMajorRadius * 0.75, angle);
	                graphicsContext.save();
	                graphicsContext.translate(textPoint2D.getX(), textPoint2D.getY());
	                graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL, 0.045 * size));
	                graphicsContext.setTextAlign(TextAlignment.CENTER);
	                graphicsContext.setTextBaseline(VPos.CENTER);
	                graphicsContext.setFill(getTickColor());
	                graphicsContext.fillText(i + "%", 0, 0); // TBEERNOT print value and format
	                graphicsContext.restore();
            	}
            }
		}
	}
	
	// ==================================================================================================================
	// Needle
	
	private class NeedlePane extends Pane {

		/**
		 * 
		 */
		private NeedlePane() {
			this.getStyleClass().add("NeedlePane");
			
			// knob
			needleRegion.setPickOnBounds(false);
			needleRegion.getStyleClass().setAll("needle-standard");
			needleRegion.setPrefSize(6.0, 75.0);
			needleRegion.layoutXProperty().bind(centerX.add( needleRegion.widthProperty().multiply(-0.5) ));
			needleRegion.layoutYProperty().bind(centerY); //.add( needleRegion.heightProperty().multiply(-1.0) ));
			needleRotate.pivotXProperty().bind(needleRegion.widthProperty().multiply(0.5));
			needleRegion.getTransforms().add(needleRotate);
			needleRegion.getTransforms().add(needleScale);
			needleScale.yProperty().bind(needleScale.xProperty());

			// knob
			knobRegion.setPickOnBounds(false);
			knobRegion.getStyleClass().setAll("knob");
			knobRegion.layoutXProperty().bind(centerX);
			knobRegion.layoutYProperty().bind(centerY);
			knobRegion.getTransforms().add(new Scale(1.0, 1.0));

            // add them
			getChildren().addAll(needleRegion, knobRegion);
			
			getSkinnable().valueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
//				setValueText();
			});
			
	        // min and max value text need to be added to the scene in order to have the CSS applied
			getSkinnable().minValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true); 
//				scaleValueText();
			});
			getSkinnable().maxValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
//				scaleValueText();
			});
			rotateNeedle(false);
		}
		final private Region needleRegion = new Region();
		final private Rotate needleRotate = new Rotate();
		final private Scale needleScale = new Scale();
		final private Region knobRegion = new Region();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			double radius = calculateRadius();
			
			// needle
			{
				needleScale.setX(radius / 100.0);
			}
			
			// knob
			{
				Scale scale = (Scale)knobRegion.getTransforms().get(0);
				scale.setX(radius / 200.0 * 0.3);
				scale.setY(scale.getX());
			}
		}
		
		/**
		 * @param allowAnimation AllowAnimation is needed only in the first pass during skin construction: the Animated property has not been set at that time, so we do not need if animation is wanted. So the initial rotation is always done unanimated.  
		 */
		private void rotateNeedle(boolean allowAnimation) {
			if (!validateValueAndHandleInvalid()) {
				return;
			}

			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
	 		double value = getSkinnable().getValue();
	 		double angle = (value - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES;
	 		angle += 45;
	 		
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
//	 		segmentPane.activateSegments();
		}
		final private Timeline timeline = new Timeline();
	}
	
	private boolean validateValueAndHandleInvalid() {
		String validationMessage = validateValue();
		if (validationMessage != null) {
			new Throwable(validationMessage).printStackTrace();
			if (needlePane != null) {
//				needlePane.valueText.setText("");
				needlePane.needleRotate.setAngle(90.0);
			}
			return false;
		};
		return true;
	}


	// ==================================================================================================================
	// GlassPlate
	
	private class GlassPlatePane extends Pane {

		/**
		 * 
		 */
		private GlassPlatePane() {
			this.getStyleClass().add("GlassPlatePane");
			
			// backpane
			outerringCircle.getStyleClass().addAll("outerring");
			outerringCircle.centerXProperty().bind(centerX);
			outerringCircle.centerYProperty().bind(centerY);
			
			// backpane
			innerringCircle.getStyleClass().addAll("innerring");
			innerringCircle.centerXProperty().bind(centerX);
			innerringCircle.centerYProperty().bind(centerY);
			
            // add them
			getChildren().addAll(outerringCircle, innerringCircle);
		}
		final private Circle outerringCircle = new Circle();
		final private Circle innerringCircle = new Circle();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			double radius = calculateRadius();
			
			// size the circle
			outerringCircle.setRadius(radius * RING_OUTER_RADIUS_FACTOR);
			outerringCircle.setStyle("-fx-stroke-width: " + (radius * RING_WIDTH_FACTOR) + ";");
			innerringCircle.setRadius(radius * RING_INNER_RADIUS_FACTOR);
			innerringCircle.setStyle("-fx-stroke-width: " + (radius * RING_WIDTH_FACTOR) + ";");
			
		}
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
