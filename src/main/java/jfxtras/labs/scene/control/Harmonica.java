package jfxtras.labs.scene.control;

import java.util.Calendar;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import jfxtras.scene.control.CalendarPicker;

public class Harmonica extends Control {
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public Harmonica()
	{
		construct();
	}
	
	/**
	 * 
	 */
	public Harmonica(Tab... tabs)
	{
		construct();
		
		// add tabs
		for (Tab lTab : tabs) {
			this.tabs.add(lTab);
		}
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(Harmonica.class.getSimpleName());
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet()
	{
		return Harmonica.class.getResource("/jfxtras/labs/internal/scene/control/" + Harmonica.class.getSimpleName() + ".css").toExternalForm();
	}
	
	@Override public Skin<?> createDefaultSkin() {
		return new jfxtras.labs.internal.scene.control.skin.HarmonicaSkin(this); 
	}

	// ==================================================================================================================
	// PROPERTIES

	/** Id: for a fluent API */
	public Harmonica withId(String value) { setId(value); return this; }

	/** tabs */
	public ObservableList<Tab> tabs() { return tabs; }
	final private ObservableList<Tab> tabs =  javafx.collections.FXCollections.observableArrayList();

	// ==================================================================================================================
	// TAB

	public static class Tab {

		public Tab(String name, Node node) {
			this.name = name;
			this.node = node;
		}
		final private String name;
		final private Node node;
		
		public String getName() {
			return name;
		}
		
		public Node getNode() {
			return node;
		}
	}
}
