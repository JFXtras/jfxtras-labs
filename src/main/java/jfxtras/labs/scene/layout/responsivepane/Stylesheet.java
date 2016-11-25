package jfxtras.labs.scene.layout.responsivepane;

import java.net.URL;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 */
@DefaultProperty("url")
public class Stylesheet {
	
	public Stylesheet() {
		
	}
	
	/**
	 * 
	 * @param widthInInchesAtLeast width in inches
	 * @param url
	 */
	public Stylesheet(double widthInInchesAtLeast, URL url) {
		setWidthInInchesAtLeast(widthInInchesAtLeast);
		setUrl(url);
	}		
	
	/** Url */
	public ObjectProperty<URL> urlProperty() { return urlProperty; }
	final private SimpleObjectProperty<URL> urlProperty = new SimpleObjectProperty<>(this, "url", null);
	public URL getUrl() { return urlProperty.getValue(); }
	public void setUrl(URL value) { urlProperty.setValue(value); }
	public Stylesheet withUrl(URL value) { setUrl(value); return this; } 

	/** WidthInInchesAtLeast (in inches) */
	public ObjectProperty<Double> widthInInchesAtLeastProperty() { return widthInInchesAtLeastProperty; }
	final private SimpleObjectProperty<Double> widthInInchesAtLeastProperty = new SimpleObjectProperty<>(this, "widthInInchesAtLeast", 0.0);
	public Double getWidthInInchesAtLeast() { return widthInInchesAtLeastProperty.getValue(); }
	public void setWidthInInchesAtLeast(Double value) { widthInInchesAtLeastProperty.setValue(value); }
	public Stylesheet withWidthInInchesAtLeast(Double value) { setWidthInInchesAtLeast(value); return this; } 
}