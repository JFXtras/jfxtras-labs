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
	 * @param sizeInInchesAtLeast size in inches
	 * @param url
	 */
	public Stylesheet(Diagonal sizeAtLeast, URL url) {
		setSizeAtLeast(sizeAtLeast);
		setUrl(url);
	}		
	
	/** Url */
	public ObjectProperty<URL> urlProperty() { return urlProperty; }
	final private SimpleObjectProperty<URL> urlProperty = new SimpleObjectProperty<>(this, "url", null);
	public URL getUrl() { return urlProperty.getValue(); }
	public void setUrl(URL value) { urlProperty.setValue(value); }
	public Stylesheet withUrl(URL value) { setUrl(value); return this; } 

	/** SizeAtLeast (in inches) */
	public ObjectProperty<Diagonal> sizeAtLeastProperty() { return sizeAtLeastProperty; }
	final private SimpleObjectProperty<Diagonal> sizeAtLeastProperty = new SimpleObjectProperty<>(this, "sizeAtLeast", Diagonal.inches(0.0));
	public Diagonal getSizeAtLeast() { return sizeAtLeastProperty.getValue(); }
	public void setSizeAtLeast(Diagonal value) { sizeAtLeastProperty.setValue(value); }
	public Stylesheet withSizeAtLeast(Diagonal value) { setSizeAtLeast(value); return this; } 
}