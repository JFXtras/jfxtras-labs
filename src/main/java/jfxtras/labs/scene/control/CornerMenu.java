package jfxtras.labs.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
	// TODO: allow the animationInterpolation to be set
	// TODO: should we always require a StackPane to install upon (in the constructor)? This will make the shown property have a sensible reason to exist
	
	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public CornerMenu(Orientation orientation)
	{
		orientationObjectProperty.set(orientation);
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
    public ReadOnlyObjectProperty<Orientation> orientationProperty() { 
    	return new ReadOnlyObjectWrapper<Orientation>(this, "orientation").getReadOnlyProperty();
    }
	final private SimpleObjectProperty<Orientation> orientationObjectProperty = new SimpleObjectProperty<Orientation>(this, "orientation", Orientation.TOP_LEFT);
	public static enum Orientation {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT}
	public Orientation getOrientation() { return orientationObjectProperty.getValue(); }
	
	// Items
    private final ObservableList<MenuItem> items = FXCollections.observableArrayList(); 
    public final ObservableList<MenuItem> getItems() {
        return items;
    }
    
	/** Shown: */
	public ObjectProperty<Boolean> shownProperty() { return this.shownObjectProperty; }
	final private ObjectProperty<Boolean> shownObjectProperty = new SimpleObjectProperty<Boolean>(this, "shown", true);
	public Boolean isShown() { return this.shownObjectProperty.getValue(); }
	public void setShown(Boolean value) { this.shownObjectProperty.setValue(value); }
	public CornerMenu withShown(Boolean value) { setShown(value); return this; }
    

	// ==================================================================================================================
	// SUPPORT
    
    /**
     * Install this CornerMenu in a new the top pane
     */
    public void install(StackPane stackPane) {
    	// make sure we have a pane
    	if (pane == null) {
        	pane = new Pane();
        	pane.getChildren().add(this);
    	}
    	
    	// if we were installed on a different StackPane, remove us there first
    	if (this.stackPane != null) {
    		this.stackPane.getChildren().remove(pane);
    	}
    	
    	// add the pane to the stack pane
    	this.stackPane = stackPane;
    	if (this.stackPane == null) {
    		this.pane = null;
    		return;
    	}
    	stackPane.getChildren().add(pane);
    	
    	// positon
    	positionInPane();
    }
    private StackPane stackPane = null;
    private Pane pane = null;
    
    private void positionInPane() {
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

}
