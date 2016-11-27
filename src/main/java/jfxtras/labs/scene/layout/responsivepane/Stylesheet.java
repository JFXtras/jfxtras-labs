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
	public Stylesheet(Width widthAtLeast, URL url) {
		setWidthAtLeast(widthAtLeast);
		setUrl(url);
	}		
	
	/** Url */
	public ObjectProperty<URL> urlProperty() { return urlProperty; }
	final private SimpleObjectProperty<URL> urlProperty = new SimpleObjectProperty<>(this, "url", null);
	public URL getUrl() { return urlProperty.getValue(); }
	public void setUrl(URL value) { urlProperty.setValue(value); }
	public Stylesheet withUrl(URL value) { setUrl(value); return this; } 

	/** WidthAtLeast (in inches) */
	public ObjectProperty<Width> widthAtLeastProperty() { return widthAtLeastProperty; }
	final private SimpleObjectProperty<Width> widthAtLeastProperty = new SimpleObjectProperty<>(this, "widthAtLeast", Width.inches(0.0));
	public Width getWidthAtLeast() { return widthAtLeastProperty.getValue(); }
	public void setWidthAtLeast(Width value) { widthAtLeastProperty.setValue(value); }
	public Stylesheet withWidthAtLeast(Width value) { setWidthAtLeast(value); return this; } 
}