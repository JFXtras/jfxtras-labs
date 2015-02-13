package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;

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
			drawSegments();
		});
		dialPane.heightProperty().addListener( (observable) -> {
			drawSegments();
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

	private void drawSegments() {
		dialPane.getChildren().clear();
 		double width = dialPane.getWidth();
 		double height = dialPane.getHeight();
 		double radius = Math.min(width / 2, height);
		dialPane.getChildren().add(drawSegment(width / 2.0, height, radius, 50, 0.0, 180.0));
	}
	
	private Path drawSegment(double centerX, double centerY, double outerRadius, double innerRadius, double startAngleDegrees, double endAngleDegrees) {
		// TBEE: why do I need to swap start and end angle here?
		double startAngle = degreesToRadials(endAngleDegrees - 180.0); 
		double endAngle = degreesToRadials(startAngleDegrees - 180.0);
		
		double startOuterX = centerX + (outerRadius * Math.cos(startAngle));
		double startOuterY = centerY + (outerRadius * Math.sin(startAngle));
		double endOuterX = centerX + (outerRadius * Math.cos(endAngle));
		double endOuterY = centerY + (outerRadius * Math.sin(endAngle));
		double startInnerX = centerX + (innerRadius * Math.cos(startAngle));
		double startInnerY = centerY + (innerRadius * Math.sin(startAngle));
		double endInnerX = centerX + (innerRadius * Math.cos(endAngle));
		double endInnerY = centerY + (innerRadius * Math.sin(endAngle));
//		System.out.println("-----");
//		System.out.println("startOuterX = " + startOuterX);
//		System.out.println("startOuterY = " + startOuterY);
//		System.out.println("endOuterX = " + endOuterX);
//		System.out.println("endOuterY = " + endOuterY);
//		System.out.println("startInnerX = " + startInnerX);
//		System.out.println("startInnerY = " + startInnerY);
//		System.out.println("endInnerX = " + endInnerX);
//		System.out.println("endInnerY = " + endInnerY);
		
        Path path = new Path();
        path.setFillRule(FillRule.EVEN_ODD);
        path.getStyleClass().add("segment");

        // begin of inner arc
        {
	        MoveTo moveTo = new MoveTo();
	        moveTo.setX(startInnerX);
	        moveTo.setY(startInnerY);
	        path.getElements().add(moveTo);
        }
        
        // inner arc
        {
	        ArcTo arcTo = new ArcTo();
	        arcTo.setX(endInnerX);
	        arcTo.setY(endInnerY);
	        arcTo.setRadiusX(innerRadius);
	        arcTo.setRadiusY(innerRadius);
	        path.getElements().add(arcTo);
        }
        
        // begin of inner arc
        {
	        MoveTo moveTo = new MoveTo();
	        moveTo.setX(startInnerX);
	        moveTo.setY(startInnerY);
	        path.getElements().add(moveTo);
        }
        
        // leg to begin of outer arc
        { 
	        LineTo lineTo = new LineTo();
	        lineTo.setX(startOuterX);
	        lineTo.setY(startOuterY);
	        path.getElements().add(lineTo);
        }
        
        // outer arc (must be darn in the same direction as the inner arc)
        {
	        ArcTo arcTo = new ArcTo();
	        arcTo.setX(endOuterX);
	        arcTo.setY(endOuterY);
	        arcTo.setRadiusX(outerRadius);
	        arcTo.setRadiusY(outerRadius);
	        path.getElements().add(arcTo);
        }

        // leg from end of outer arc to end of inner arc
        {
	        LineTo lineTo = new LineTo();
	        lineTo.setX(endInnerX);
	        lineTo.setY(endInnerY);
	        path.getElements().add(lineTo);
        }
        
        return path;
    }
    
    static private double degreesToRadials(double d) {
    	double r = d / 360.0 * 2.0 * Math.PI;
    	return r;
    }
}
