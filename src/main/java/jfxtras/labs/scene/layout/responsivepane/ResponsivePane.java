package jfxtras.labs.scene.layout.responsivepane;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Window;

/*
 * TODO: controller in FXML
 * TODO: alias widths (desktop, tablets, etc)
 * 
*/

/**
 * 
 *
 */
public class ResponsivePane extends StackPane {
	
	
	// ==========================================================================================================================================================================================================================================
	// CONSTRUCTORS
	
	/**
	 * 
	 */
	public ResponsivePane() {
		
		// default device sizes
		deviceToSizeMap.put(Device.PHONE.toString(), Diagonal.inches(3.5));
		deviceToSizeMap.put(Device.TABLET.toString(), Diagonal.inches(7.0));
		deviceToSizeMap.put(Device.DESKTOP.toString(), Diagonal.inches(10.5));
		
		// react to changes in the scene
		sceneProperty().addListener((ChangeListener<Scene>) (observable, oldValue, newValue) -> {
			
			// by listening to it width
			if (oldValue != null) {
				oldValue.widthProperty().removeListener(sizeInvalidationListener);
				oldValue.heightProperty().removeListener(sizeInvalidationListener);
			}
			if (newValue != null) {
				newValue.widthProperty().addListener(sizeInvalidationListener);
				newValue.heightProperty().addListener(sizeInvalidationListener);
			}
		});
	}
	
	// react to changes in the size of the scene by (optionally) changing the layout 
	final private InvalidationListener sizeInvalidationListener = new InvalidationListener() {
		
		@Override
		public void invalidated(Observable observable) {
			
			// nothing to do yet
			if (getScene() == null || getScene().getWindow() == null) {
				return;
			}
			
			// see if something needs to be changed
			setupLayout();
		}
	};
	
	
	// ==========================================================================================================================================================================================================================================
	// PROPERTIES

	/** Id */
	public ResponsivePane withId(String v) { 
		setId(v); 
		return this; 
	}

	/** Debug: show rendering hints (for Ref) and prints out changes to the layout on the console */
	public ObjectProperty<Boolean> debugProperty() { return debugProperty; }
	final private SimpleObjectProperty<Boolean> debugProperty = new SimpleObjectProperty<Boolean>(this, "debug", Boolean.FALSE);
	public Boolean getDebug() { return debugProperty.getValue(); }
	public void setDebug(Boolean value) { debugProperty.setValue(value); }
	public ResponsivePane withDebug(Boolean value) { setDebug(value); return this; } 

	/** Trace: like debug, plus show calculations determining if changes are needed on the console */
	public ObjectProperty<Boolean> traceProperty() { return traceProperty; }
	final private SimpleObjectProperty<Boolean> traceProperty = new SimpleObjectProperty<Boolean>(this, "trace", Boolean.FALSE);
	public Boolean getTrace() { return traceProperty.getValue(); }
	public void setTrace(Boolean value) { traceProperty.setValue(value); }
	public ResponsivePane withTrace(Boolean value) { setTrace(value); return this; } 

	// -----------------------------------------------------------------------------------------------
	// REFS
	
	/** refs */
	public ObservableList<Node> getRefs() {
		return refs;
	}
	private ObservableList<Node> refs = FXCollections.observableArrayList();
	
	/**
	 * 
	 * @param id
	 * @param node
	 * @return
	 */
	public Node addRef(String id, Node node) {
		node.setId(id);
		getRefs().add(node);
		return node;
	}
	
	/**
	 * 
	 * @param refId
	 * @return
	 */
	Node findReferredNode(String refId) {
		for (Node lNode : refs) {
			if (refId.equals(lNode.getId())) {
				return lNode;
			}			
		}
		System.err.println("Could not find reference " + refId);
		return null;
	}
	
	// -----------------------------------------------------------------------------------------------
	// LAYOUTS

	/** layouts */
	public ObservableList<Layout> getLayouts() {
		return layouts;
	}
	final private ObservableList<Layout> layouts = FXCollections.observableArrayList();
	
	/**
	 * 
	 */
	public void addLayout(Diagonal sizeAtLeast, Node root) {
		layouts.add(new Layout(sizeAtLeast, root));
	}
	
	/**
	 * 
	 */
	public void addLayout(Diagonal sizeAtLeast, Orientation orientation, Node root) {
		layouts.add(new Layout(sizeAtLeast, orientation, root));
	}
	
	/** ActiveLayout */
	public ObjectProperty<Layout> activeLayoutProperty() { return activeLayoutProperty; }
	final private SimpleObjectProperty<Layout> activeLayoutProperty = new SimpleObjectProperty<Layout>(this, "activeLayout", null);
	public Layout getActiveLayout() { return activeLayoutProperty.getValue(); }
	public void setActiveLayout(Layout value) { activeLayoutProperty.setValue(value); }
	public ResponsivePane withActiveLayout(Layout value) { setActiveLayout(value); return this; } 

		
	// -----------------------------------------------------------------------------------------------
	// SceneStylesheets

	/** sceneStylesheets */
	public ObservableList<Stylesheet> getSceneStylesheets() {
		return sceneStylesheets;
	}
	final private ObservableList<Stylesheet> sceneStylesheets = FXCollections.observableArrayList();

	/**
	 * 
	 */
	public void addSceneStylesheet(Diagonal sizeAtLeast, URL url) {
		sceneStylesheets.add(new Stylesheet(sizeAtLeast, url));
	}
	
	/** ActiveSceneStylesheet */
	public ObjectProperty<Stylesheet> activeSceneStylesheetProperty() { return activeSceneStylesheetProperty; }
	final private SimpleObjectProperty<Stylesheet> activeSceneStylesheetProperty = new SimpleObjectProperty<Stylesheet>(this, "activeSceneStylesheet", null);
	public Stylesheet getActiveSceneStylesheet() { return activeSceneStylesheetProperty.getValue(); }
	public void setActiveSceneStylesheet(Stylesheet value) { activeSceneStylesheetProperty.setValue(value); }
	public ResponsivePane withActiveSceneStylesheet(Stylesheet value) { setActiveSceneStylesheet(value); return this; } 

	// -----------------------------------------------------------------------------------------------
	// MyStylesheets

	/** myStylesheets */
	public ObservableList<Stylesheet> getMyStylesheets() {
		return myStylesheets;
	}
	final private ObservableList<Stylesheet> myStylesheets = FXCollections.observableArrayList();

	/**
	 * 
	 * @param sizeAtLeast size in inches
	 * @param url
	 */
	public void addMyStylesheet(Diagonal sizeAtLeast, URL url) {
		myStylesheets.add(new Stylesheet(sizeAtLeast, url));
	}
	
	/** ActiveMyStylesheet */
	public ObjectProperty<Stylesheet> activeMyStylesheetProperty() { return activeMyStylesheetProperty; }
	final private SimpleObjectProperty<Stylesheet> activeMyStylesheetProperty = new SimpleObjectProperty<Stylesheet>(this, "activeMyStylesheet", null);
	public Stylesheet getActiveMyStylesheet() { return activeMyStylesheetProperty.getValue(); }
	public void setActiveMyStylesheet(Stylesheet value) { activeMyStylesheetProperty.setValue(value); }
	public ResponsivePane withActiveMyStylesheet(Stylesheet value) { setActiveMyStylesheet(value); return this; } 
	
	// ==========================================================================================================================================================================================================================================
	// DEVICE
	
	/**
	 * 
	 * @param device
	 * @param size
	 */
	public void setDeviceSize(Device device, Diagonal size) {
		setDeviceSize(device.toString(), size);
	}
	public Diagonal getDeviceSize(Device device) {
		return getDeviceSize(device.toString());
	}
	
	/**
	 * 
	 * @param device
	 * @param size
	 */
	public void setDeviceSize(String device, Diagonal size) {
		deviceToSizeMap.put(device, size);
	}
	public Diagonal getDeviceSize(String device) {
		if (!deviceToSizeMap.containsKey(device)) {
			throw new IllegalArgumentException("Unknown device '" +  device + "', not found in " + deviceToSizeMap.keySet());
		}
		return deviceToSizeMap.get(device);
	}
	final Map<String, Diagonal> deviceToSizeMap = new HashMap<>();


	// ==========================================================================================================================================================================================================================================
	// LAYOUT
	
    @Override 
    protected void layoutChildren() {
    	
    	// this is only for the first initialization
    	if (getActiveLayout() == null) {
    		setupLayout();
    	}
    	
    	// continue layout
    	super.layoutChildren();
    }
    
    /**
     * 
     */
	void setupLayout() {

    	// layout
    	Layout lLayout = determineBestFittingLayout();
    	if (!lLayout.equals(getActiveLayout())) {
    		
    		if (getDebug() || getTrace()) System.out.println("Activating layout " + lLayout);
        	setActiveLayout(lLayout);

    		// switch to active layout
        	ResponsivePane.this.getChildren().clear();
        	ResponsivePane.this.getChildren().add(lLayout.getRoot());
    	}
    	
    	// scene stylesheet
    	{
	    	Stylesheet lStylesheet = determineBestFittingStylesheet(sceneStylesheets);
	    	if (!lStylesheet.equals(getActiveSceneStylesheet())) {
	    		
	    		setActiveSceneStylesheet(lStylesheet);
	
				// switch to active CSS file
				load(lStylesheet, sceneStylesheets, getScene().getStylesheets());
	    	}
    	}
    	
    	// my stylesheet
    	{
	    	Stylesheet lStylesheet = determineBestFittingStylesheet(myStylesheets);
	    	if (!lStylesheet.equals(getActiveMyStylesheet())) {
	    		
	    		setActiveMyStylesheet(lStylesheet);
	
				// switch to active CSS file
				load(lStylesheet, myStylesheets, getStylesheets());
	    	}
    	}
	}


	// ==========================================================================================================================================================================================================================================
	// SUPPORT
	
	/**
	 * 
	 * @return
	 */
	double determineActualSizeInInches() {
		Scene lScene = getScene();
		
		// determine the DPI factor, so the thresholds become larger on screens with a higher DPI
		double lPPI = 100.0; // average dpi
		Window window = lScene.getWindow();
		ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(window.getX(), window.getY(), window.getWidth(), window.getHeight());
		if (screensForRectangle.size() > 0) {
			// System.out.println("screens of scene: " + screensForRectangle); 
			Screen lScreen = screensForRectangle.get(0); // we just use the first screen
			lPPI = lScreen.getDpi();
		}

		double lWidthInInches = lScene.getWidth() / lPPI;
		double lHeightInInches = lScene.getHeight() / lPPI;
		double lSizeInInches = Math.sqrt( (lWidthInInches * lWidthInInches) + (lHeightInInches * lHeightInInches) );
		if (getTrace()) System.out.println("Actual size=" + lScene.getWidth() + "x" + lScene.getHeight() + "px, diagonal in inches=" + lSizeInInches + " (ppi=" + lPPI + ")");
		return lSizeInInches;
	}
	
	/**
	 * 
	 * @return
	 */
	Layout determineBestFittingLayout() {
		double actualSizeInInches = determineActualSizeInInches();
		Scene lScene = getScene();
		Orientation lSceneOrientation = (lScene.getWidth() > lScene.getHeight() ? Orientation.LANDSCAPE : Orientation.PORTRAIT);

		// find the best fitting layout
		Layout lBestFittingLayout = null;
		for (Layout lLayout : layouts) {
			if (getTrace()) System.out.println("determineBestFittingLayout, examining layout: " + lLayout);
			if (actualSizeInInches >= lLayout.getSizeAtLeast().toInches(this)) {
				if (getTrace()) System.out.println("determineBestFittingLayout, layout is candidate based on scene width: " + actualSizeInInches + "in");
				if (lBestFittingLayout == null || lBestFittingLayout.getSizeAtLeast().toInches(this) <= lLayout.getSizeAtLeast().toInches(this)) {
					if (getTrace()) System.out.println("determineBestFittingLayout, layout is a better candidate based on width vs best fitting so far: " + lBestFittingLayout);
					
					// Based on the width, this layout is a candidate. But is it also based on the orientation?
					Orientation lBestFittingLayoutOrientation = (lBestFittingLayout == null ? null : lBestFittingLayout.getOrientation()); 
					Orientation lLayoutOrientation = lLayout.getOrientation(); 
					if ( lBestFittingLayout == null || lLayoutOrientation == null || lSceneOrientation.equals(lLayoutOrientation)) {
						if (getTrace()) System.out.println("determineBestFittingLayout, layout is also candidate based on orientation");
						
						// Orientation also matches, do we prefer the one orientation of the other?
						if ( (lBestFittingLayoutOrientation == null && lLayoutOrientation == null) 
						  || (lBestFittingLayoutOrientation == null && lLayoutOrientation != null) // Prefer a layout with orientation above one without
						   ) {
							if (getTrace()) System.out.println("determineBestFittingLayout, layout is also a better candidate based on orientation vs best fitting so far: " + lBestFittingLayout);
							lBestFittingLayout = lLayout;
							if (getTrace()) System.out.println("determineBestFittingLayout, best fitting so far: " + lBestFittingLayout);
						}
					}
				}
			}
		}
		
		// if none found, use the default layout
		if (lBestFittingLayout == null) {
			lBestFittingLayout = SINGULARITY_LAYOUT;
		}
		
		// done
		if (getTrace()) System.out.println("determineBestFittingLayout=" + lBestFittingLayout);
		return lBestFittingLayout;
	}
	private final Layout SINGULARITY_LAYOUT = new Layout(Diagonal.ZERO, new Label("?") );
	
	/**
	 * 
	 * @return
	 */
	Stylesheet determineBestFittingStylesheet(List<Stylesheet> availableStylesheets) {
		double actualSizeInInches = determineActualSizeInInches();
		
		// find the best fitting stylesheet
		Stylesheet lBestFittingStylesheet = null;
		for (Stylesheet lStylesheet : availableStylesheets) {
			if (actualSizeInInches >= lStylesheet.getSizeAtLeast().toInches(this)) {
				if (lBestFittingStylesheet == null || lBestFittingStylesheet.getSizeAtLeast().toInches(ResponsivePane.this) < lStylesheet.getSizeAtLeast().toInches(ResponsivePane.this)) {
					lBestFittingStylesheet = lStylesheet;
				}
			}
		}
		
		// if none found, use the default Stylesheet
		if (lBestFittingStylesheet == null) {
			lBestFittingStylesheet = SINGULAR_STYLESHEET;
		}
		
		// done
		if (getTrace()) System.out.println("determineBestFittingStylesheet=" + lBestFittingStylesheet.getSizeAtLeast() + " -> " + lBestFittingStylesheet.getUrl());
		return lBestFittingStylesheet;
	}
	final private Stylesheet SINGULAR_STYLESHEET = new Stylesheet(Diagonal.ZERO, null);
	
	/**
	 * 
	 * @param stylesheet
	 */	
	void load(Stylesheet stylesheet, List<Stylesheet> availableStylesheets, List<String> activeStylesheetUrls) {
		String lStylesheetUrl = (stylesheet.getUrl() == null ? null : stylesheet.getUrl().toExternalForm());
		
		// remove all of the available stylesheets
		for (Stylesheet lStylesheet : availableStylesheets) {
			String lActiveStylesheetUrl = lStylesheet.getUrl().toExternalForm();
			if (activeStylesheetUrls.remove(lActiveStylesheetUrl)) {
				if (getDebug() || getTrace()) System.out.println("Removed stylesheet " + lStylesheet.getSizeAtLeast() + " -> " + lActiveStylesheetUrl);
			}			
		}
		
		// load new
		if (lStylesheetUrl != null) {
			if (getDebug() || getTrace()) System.out.println("Loading stylesheet " + stylesheet.getSizeAtLeast() + " -> " + lStylesheetUrl);
			activeStylesheetUrls.add(lStylesheetUrl);
		}
	}

}
