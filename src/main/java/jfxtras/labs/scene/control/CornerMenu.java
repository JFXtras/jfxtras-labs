package jfxtras.labs.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import jfxtras.labs.internal.scene.control.skin.CornerMenuSkin;

/**
 * CornerMenu uses CircularPane to render a menu that can be placed in the corner of a screen.
 * The coder is responsible for placing the menu at the proper location.
 *  
 * @author Tom Eugelink
 *
 */
public class CornerMenu extends Control {
	// TODO: show and hide (with animation)
	// TODO: allow the animationInterpolation to be set
	
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
    

	// ==================================================================================================================
	// SUPPORT
    
    /**
     * Install this CornerMenu in a new the top pane
     */
    public void install(StackPane stackPane) {
    	// make sure we have a pane
    	if (pane == null) {
        	pane = new Pane();
    	}
    	pane.getChildren().clear();
    	
    	// if we were installed on a different StackPane, remove us there first
    	if (this.stackPane != null) {
    		System.out.println("removing pane");
    		this.stackPane.getChildren().remove(pane);
    		return;
    	}
    	this.stackPane = stackPane;
    	
    	// clear?
    	if (this.stackPane == null) {
    		this.pane = null;
    		return;
    	}
    	
    	// add the pane to the stack pane and us to the pane
    	stackPane.getChildren().add(pane);
    	pane.getChildren().add(this);
    	
    	// setup the layout 
		layoutXProperty().unbind();
    	layoutYProperty().unbind();
		if (CornerMenu.Orientation.TOP_LEFT.equals(getOrientation())) {
			setLayoutX(0);
			setLayoutY(0);
		}
		else if (CornerMenu.Orientation.TOP_RIGHT.equals(getOrientation())) {
			layoutXProperty().bind( pane.widthProperty().subtract(widthProperty()));
			setLayoutY(0);
		}
		else if (CornerMenu.Orientation.BOTTOM_RIGHT.equals(getOrientation())) {
			layoutXProperty().bind( pane.widthProperty().subtract(widthProperty()));
	    	layoutYProperty().bind( pane.heightProperty().subtract(heightProperty()));
		}
		else if (CornerMenu.Orientation.BOTTOM_LEFT.equals(getOrientation())) {
			setLayoutX(0);
	    	layoutYProperty().bind( pane.heightProperty().subtract(heightProperty()));
		}
    }
    StackPane stackPane = null;
    Pane pane = null;

}
