package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.Styleable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.labs.scene.control.gauge.linear.Indicator;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.labs.scene.control.gauge.linear.Marker;
import jfxtras.labs.scene.control.gauge.linear.Segment;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.StringConverter;

/**
 * 
 */
public class LinearGaugeSkin<T, C extends LinearGauge<?>> extends SkinBase<C> {

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LinearGaugeSkin(C control) {
		super(control);
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
    public final T withAnimated(Animated value) { setAnimated(value); return (T)this; }
    public enum Animated {YES, NO}

    /**
     * valueFormat
     */
    public final SimpleStyleableStringProperty valueFormatProperty() { return valueFormatProperty; }
    private SimpleStyleableStringProperty valueFormatProperty = new SimpleStyleableStringProperty(StyleableProperties.VALUE_FORMAT_CSSMETADATA, StyleableProperties.VALUE_FORMAT_CSSMETADATA.getInitialValue(null)) {
//		{ // anonymous constructor
//			addListener( (invalidationEvent) -> {
//				System.out.println("ValueFormat changed " + valueFormatProperty.get());
//				System.out.println("Style " + getSkinnable().getStyle());
//				new Throwable().printStackTrace();
//			});
//		}
	};
    public final void setValueFormat(String value) { valueFormatProperty.set(value); }
    public final String getValueFormat() { return valueFormatProperty.get(); }
    public final T withValueFormat(String value) { setValueFormat(value); return (T)this; }
    protected String valueFormat(double value) {
    	// TBEERNOT do not create a decimal format every time
    	return new DecimalFormat(getValueFormat()).format(value);
    }
    

    // -------------------------
        
    private static class StyleableProperties 
    {
        private static final CssMetaData<LinearGauge<?>, Animated> ANIMATED_CSSMETADATA = new CssMetaDataForSkinProperty<LinearGauge<?>, LinearGaugeSkin<?,?>, Animated>("-fxx-animated", new EnumConverter<Animated>(Animated.class), Animated.YES ) {
        	@Override 
        	protected ObjectProperty<Animated> getProperty(LinearGaugeSkin<?,?> s) {
            	return s.animatedProperty;
            }
        };
        
        private static final CssMetaData<LinearGauge<?>, String> VALUE_FORMAT_CSSMETADATA = new CssMetaDataForSkinProperty<LinearGauge<?>, LinearGaugeSkin<?,?>, String>("-fxx-value-format", StringConverter.getInstance(), "0" ) {
        	@Override 
        	protected SimpleStyleableStringProperty getProperty(LinearGaugeSkin<?,?> s) {
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
	// VALIDATE
	
    /**
     * 
     * @return
     */
	protected String validateValue() {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();
 		double controlValue = getSkinnable().getValue();
		if (controlMinValue > controlMaxValue) {
			return String.format("Min-value (%f) cannot be greater than max-value (%f)", controlMinValue, controlMaxValue);
		}
		if (controlMinValue > controlValue) {
			return String.format("Min-value (%f) cannot be greater than the value (%f)", controlMinValue, controlValue);
		}
		if (controlValue > controlMaxValue) {
			return String.format("Value (%f) cannot be greater than max-value (%f)", controlValue, controlMaxValue);
		}
		return null;
	}
	
	/**
	 * 
	 * @param segment
	 * @return
	 */
	protected String validateSegment(Segment segment) {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();

 		double segmentMinValue = segment.getMinValue();
 		double segmentMaxValue = segment.getMaxValue();
		if (segmentMinValue < controlMinValue) {
			return String.format("Segments min-value (%f) cannot be less than the controls min-value (%f)", segmentMinValue, controlMinValue);
		}
		if (segmentMaxValue > controlMaxValue) {
			return String.format("Segments max-value (%f) cannot be greater than the controls max-value (%f)", segmentMaxValue, controlMaxValue);
		}
 		return null;
	}

	/**
	 * 
	 * @param marker
	 * @return
	 */
	protected String validateMarker(Marker marker) {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();

 		double markerValue = marker.getValue();
		if (markerValue < controlMinValue) {
			return String.format("Marker value (%f) cannot be less than the controls min-value (%f)", markerValue, controlMinValue);
		}
		if (markerValue > controlMaxValue) {
			return String.format("Marker max-value (%f) cannot be greater than the controls max-value (%f)", markerValue, controlMaxValue);
		}
 		return null;
	}
	
	// ==================================================================================================================
	// Indicators
	
	abstract protected class AbstractIndicatorPane extends Pane {
		final private Map<Indicator, Region> indicatorToRegion = new HashMap<>();

		/**
		 * 
		 */
		protected AbstractIndicatorPane() {

			// react to changes in the markers
			getSkinnable().indicators().addListener( (ListChangeListener.Change<? extends Indicator> change) -> {
				createAndAddIndicators();
			});
			createAndAddIndicators();
		}
		
		/**
		 * 
		 */
		private void createAndAddIndicators() {
	 		// create the nodes representing each marker
			getChildren().clear();
			indicatorToRegion.clear();
	 		for (Indicator indicator : getSkinnable().indicators()) {
	 			
	 			// create an svg path for this marker
	 			Region region = new Region();
				getChildren().add(region);
				indicatorToRegion.put(indicator, region);
				
				// setup scaling
				Scale scale = new Scale();
				region.getTransforms().add(scale);
				
				// setup CSS on the path
				region.getStyleClass().addAll("indicator", indicator.getId() + "-indicator");
	        	region.setId(indicator.getId());
	 		}
		}

		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

	 		// size & position the indicators
	 		double radius = calculateRadius();
	 		for (Indicator indicator : getSkinnable().indicators()) {
	 			
	 			int idx = indicator.getIdx();
	 			Region region = indicatorToRegion.get(indicator);
	 			if (region != null) {
	 				Point2D point2D = calculateLocation(idx);
					if (point2D != null) {
				 		region.layoutXProperty().set(point2D.getX());
				 		region.layoutYProperty().set(point2D.getY());
		 			}
	 				Scale scale = (Scale)region.getTransforms().get(0);
		 			scale.setX(40.0/100.0 * radius/150.0); // SVG is setup on a virtual 100x100 canvas, it is scaled to fit the size of the gauge. For a width of 300 (radius 150) this is 40 pixels
		 			scale.setY(scale.getX()); 
	 			}
	 		}
		}
		
		abstract protected double calculateRadius();
		abstract protected Point2D calculateLocation(int idx);
	}
}
