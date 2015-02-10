package jfxtras.labs.scene.control.gauge.linear;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class LinearGauge extends Control {

	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public LinearGauge() {
		construct();
	}

	/*
	 * 
	 */
	private void construct() {
		
	}


	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet() {
		return ListSpinner.class.getResource("/jfxtras/internal/scene/control/gauge/linear/" + LinearGauge.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new jfxtras.labs.internal.scene.control.skin.gauge.linear.LinearGaugeRainbowSkin(this); 
	}
	
	
	// ==================================================================================================================
	// PROPERTIES

	/** value: the currently rendered value */
	// TODO: validate that min <= value <= max
	public DoubleProperty valueProperty() { return valueProperty; }
	final private DoubleProperty valueProperty = new SimpleDoubleProperty(this, "value", 50.0);
	public double getValue() { return valueProperty.getValue(); }
	public void setValue(double value) { valueProperty.setValue(value); }
	public LinearGauge withValue(double value) { setValue(value); return this; } 

	/** minValue: the lowest value of the gauge */
	// TODO: validate that min <= value <= max
	public DoubleProperty minValueProperty() { return minValueProperty; }
	final private DoubleProperty minValueProperty = new SimpleDoubleProperty(this, "minValue", 0.0);
	public double getMinValue() { return minValueProperty.getValue(); }
	public void setMinValue(double value) { minValueProperty.setValue(value); }
	public LinearGauge withMinValue(double value) { setMinValue(value); return this; } 

	/** maxValue: the highest value of the gauge */
	// TODO: validate that min <= value <= max
	public DoubleProperty maxValueProperty() { return maxValueProperty; }
	final private DoubleProperty maxValueProperty = new SimpleDoubleProperty(this, "maxValue", 100.0);
	public double getMaxValue() { return maxValueProperty.getValue(); }
	public void setMaxValue(double value) { maxValueProperty.setValue(value); }
	public LinearGauge withMaxValue(double value) { setMaxValue(value); return this; } 

	/** segments: */
	public ObservableList<Segment> segments() { return segments; }
	final private ObservableList<Segment> segments =  javafx.collections.FXCollections.observableArrayList();
}
