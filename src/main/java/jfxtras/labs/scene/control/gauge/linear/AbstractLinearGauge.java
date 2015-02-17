package jfxtras.labs.scene.control.gauge.linear;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * This class contains commonalities for all linear gauges
 */
abstract public class AbstractLinearGauge<T> extends Control {

	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public AbstractLinearGauge() {
		construct();
	}

	/*
	 * 
	 */
	private void construct() {
		// pref size 
		// TBEERNOT the skin should determine this
		setPrefSize(200, 200);
		
		// setup the CSS
		this.getStyleClass().add(this.getClass().getSimpleName());
	}


	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override abstract public String getUserAgentStylesheet();

	@Override abstract public Skin<?> createDefaultSkin();
	
	
	// ==================================================================================================================
	// PROPERTIES

	/** value: the currently rendered value */
	public DoubleProperty valueProperty() { return valueProperty; }
	final private DoubleProperty valueProperty = new SimpleDoubleProperty(this, "value", 0.0);
	public double getValue() { return valueProperty.getValue(); }
	public void setValue(double value) { valueProperty.setValue(value); }
	public T withValue(double value) { setValue(value); return (T)this; } 

	/** minValue: the lowest value of the gauge */
	public DoubleProperty minValueProperty() { return minValueProperty; }
	final private DoubleProperty minValueProperty = new SimpleDoubleProperty(this, "minValue", 0.0);
	public double getMinValue() { return minValueProperty.getValue(); }
	public void setMinValue(double value) { minValueProperty.setValue(value); }
	public T withMinValue(double value) { setMinValue(value); return (T)this; } 

	/** maxValue: the highest value of the gauge */
	public DoubleProperty maxValueProperty() { return maxValueProperty; }
	final private DoubleProperty maxValueProperty = new SimpleDoubleProperty(this, "maxValue", 100.0);
	public double getMaxValue() { return maxValueProperty.getValue(); }
	public void setMaxValue(double value) { maxValueProperty.setValue(value); }
	public T withMaxValue(double value) { setMaxValue(value); return (T)this; } 

	/** segments:
	 * If you do not provide any segments, a default segment will be drawn.
	 * But if you provide at least one segment, you are responsible for making sure the segments cover the range of the gauge. 
	 */
	public ObservableList<Segment> segments() { return segments; }
	final private ObservableList<Segment> segments =  javafx.collections.FXCollections.observableArrayList();
}
