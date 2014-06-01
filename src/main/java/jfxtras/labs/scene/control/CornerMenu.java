package jfxtras.labs.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.CornerMenuSkin;

/**
 * CornerMenu uses CircularPane to render a menu that can be placed in the corner of a screen.
 * The coder is responsible for placing the menu at the proper location.
 *  
 * @author Tom Eugelink
 *
 */
public class CornerMenu extends Control {
	
	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public CornerMenu()
	{
		construct();
	}

	/*
	 * 
	 */
	private void construct()
	{
	}
	

	@Override public Skin<CornerMenu> createDefaultSkin() {
		return new CornerMenuSkin(this); 
	}
	
//	/**
//	 * Return the path to the CSS file so things are setup right
//	 */
//	@Override protected String getUserAgentStylesheet()
//	{
//		return this.getClass().getResource("/jfxtras/internal/scene/control/" + this.getClass().getSimpleName() + ".css").toExternalForm();
//	}

	// ==================================================================================================================
	// PROPERTIES

	/** Orientation: TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT */
	public ObjectProperty<Orientation> orientationProperty() { return orientationObjectProperty; }
	final private SimpleObjectProperty<Orientation> orientationObjectProperty = new SimpleObjectProperty<Orientation>(this, "orientation", Orientation.TOP_LEFT);
	public static enum Orientation {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT}
	public Orientation getOrientation() { return orientationObjectProperty.getValue(); }
	public void setOrientation(Orientation value) { orientationObjectProperty.setValue(value); }
	public CornerMenu withOrientation(Orientation value) { setOrientation(value); return this; } 
	
	// Items
    private final ObservableList<MenuItem> items = FXCollections.observableArrayList(); 
    public final ObservableList<MenuItem> getItems() {
        return items;
    }
}
