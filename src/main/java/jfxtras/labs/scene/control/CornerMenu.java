package jfxtras.labs.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.CornerMenuSkin;

import com.sun.javafx.collections.TrackableObservableList;

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
    private final ObservableList<MenuItem> items = new TrackableObservableList<MenuItem>() {
        @Override protected void onChanged(Change<MenuItem> c) {
//            while (c.next()) {
//                // remove the parent menu from all menu items that have been removed
//                for (MenuItem item : c.getRemoved()) {
//                    item.setParentMenu(null);
//                    item.setParentPopup(null);
//                }
//
//                // set the parent menu to be this menu for all added menu items
//                for (MenuItem item : c.getAddedSubList()) {
//                    if (item.getParentMenu() != null) {
//                        Logging.getControlsLogger().warning("Adding MenuItem " +
//                                item.getText() + " that has already been added to "
//                                + item.getParentMenu().getText());
//                        item.getParentMenu().getItems().remove(item);
//                    }
//
//                    item.setParentMenu(Menu.this);
//                    item.setParentPopup(getParentPopup());
//                }
//            }
//            if (getItems().size() == 0 && isShowing()) {
//                showingPropertyImpl().set(false);
//            }
        }
    };

    /**
     * The items to show within this menu. If this ObservableList is modified at
     * runtime, the Menu will update as expected.
     */
    public final ObservableList<MenuItem> getItems() {
        return items;
    }
}
