package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import com.sun.javafx.animation.TickCalculation;

import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import jfxtras.labs.scene.control.gauge.linear.BasicArcGauge;

/**
 * 
 */
public class BasicArcGaugeSkin extends LinearGaugeSkin<BasicArcGaugeSkin, BasicArcGauge> {

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
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void constructNodes()
	{
		// use a stack pane to control the layers
		stackPane.getChildren().addAll(backPlatePane, glassPlatePane);
		getChildren().add(stackPane);
		stackPane.setPrefSize(200, 200);

		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because with multiple skins CSS should relate to the skin not the control		
	}
	final private StackPane stackPane = new StackPane();
	final private BackPlatePane backPlatePane = new BackPlatePane();
	final private GlassPlatePane glassPlatePane = new GlassPlatePane();

	// ==================================================================================================================
	// BackPlate
	
	private class BackPlatePane extends Pane {

		/**
		 * 
		 */
		private BackPlatePane() {
			// backpane
			backpaneCircle.getStyleClass().addAll("backplate");
			
			// ticks
            ticksCanvas.setCache(true);
            ticksCanvas.setCacheHint(CacheHint.QUALITY);
            ticksCanvas.getStyleClass().addAll("tick");
            
            // add them
			getChildren().addAll(backpaneCircle, ticksCanvas);
		}
		final private Circle backpaneCircle = new Circle();
		final private Canvas ticksCanvas = new Canvas();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			Point2D center = determineCenter();
			double radius = calculateRadius();

			// position and size the circle
			double plateRadius = radius * BACKPLATE_RADIUS_FACTOR;
			backpaneCircle.setCenterX(center.getX());
			backpaneCircle.setCenterY(center.getY());
			backpaneCircle.setRadius(plateRadius);
			
			// paint the ticks
			ticksCanvas.setLayoutX(0.0);
			ticksCanvas.setLayoutY(0.0);
			double size = radius * 2.0;
			ticksCanvas.setWidth(stackPane.getWidth());
			ticksCanvas.setHeight(stackPane.getHeight());
			GraphicsContext graphicsContext = ticksCanvas.getGraphicsContext2D();
			graphicsContext.clearRect(0, 0, size, size);
			graphicsContext.setStroke(Color.BLACK); // TBEERNOT: styleable property -fxx-tick-color
			double tickInnerRadius = radius * TICK_INNER_RADIUS_FACTOR;
			double tickOuterRadius = radius * TICK_OUTER_RADIUS_FACTOR;
			double tickMajorRadius = radius * TICK_MAJOR_RADIUS_FACTOR;
			double tickMinorRadius = radius * TICK_MINOR_RADIUS_FACTOR;
            for (int i = 0; i <= 100; i++) { 
            	double angle = FULL_ARC_IN_DEGREES / 100.0 * (double)i; 
            	Point2D outerPoint2D = calculatePointOnCircle(center, tickOuterRadius, angle);
            	Point2D innerPoint2D = null;
            	
            	if (i % 10 == 0) {
                	innerPoint2D = calculatePointOnCircle(center, tickMajorRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.0055);
            	}
            	else if (i % 5 == 0) {
                	innerPoint2D = calculatePointOnCircle(center, tickMinorRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.0035);
            	}
            	else {
                	innerPoint2D = calculatePointOnCircle(center, tickInnerRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.00225);
            	}
            	graphicsContext.strokeLine(innerPoint2D.getX(), innerPoint2D.getY(), outerPoint2D.getX(), outerPoint2D.getY());
            }
		}
	}
	
	// ==================================================================================================================
	// GlassPlate
	
	private class GlassPlatePane extends Pane {

		/**
		 * 
		 */
		private GlassPlatePane() {
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			Point2D center = determineCenter();
			double radius = calculateRadius();
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
		Point2D center = new Point2D(stackPane.getWidth() / 2.0, stackPane.getHeight() / 2.0);
		return center;
	}
}
