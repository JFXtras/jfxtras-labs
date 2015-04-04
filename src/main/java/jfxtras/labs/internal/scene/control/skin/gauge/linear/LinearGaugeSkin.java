package jfxtras.labs.internal.scene.control.skin.gauge.linear;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import jfxtras.css.CssMetaDataForSkinProperty;
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
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
//				needlePane.setValueText(); 
//				needlePane.scaleValueText();
//				needlePane.positionValueText();
			});
		}
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
        private static final CssMetaData<LinearGauge, Animated> ANIMATED_CSSMETADATA = new CssMetaDataForSkinProperty<LinearGauge, LinearGaugeSkin, Animated>("-fxx-animated", new EnumConverter<Animated>(Animated.class), Animated.YES ) {
        	@Override 
        	protected ObjectProperty<Animated> getProperty(LinearGaugeSkin s) {
            	return s.animatedProperty;
            }
        };
        
        private static final CssMetaData<LinearGauge, String> VALUE_FORMAT_CSSMETADATA = new CssMetaDataForSkinProperty<LinearGauge	, LinearGaugeSkin, String>("-fxx-value-format", StringConverter.getInstance(), "0" ) {
        	@Override 
        	protected SimpleStyleableStringProperty getProperty(LinearGaugeSkin s) {
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
	
	protected String validateSegments() {
 		for (Segment segment : getSkinnable().segments()) {
 			String message = validateSegment(segment);
			if (message != null) {
				return message;
			}
 		}
 		return null;
	}
	
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
	
	protected String validateMarkers() {
 		for (Marker marker : getSkinnable().markers()) {
 			String message = validateMarker(marker);
			if (message != null) {
				return message;
			}
 		}
 		return null;
	}

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
}
