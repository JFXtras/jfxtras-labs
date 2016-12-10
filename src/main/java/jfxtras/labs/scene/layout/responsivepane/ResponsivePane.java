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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * = ResponsivePane
 * 
 * This layout chooses the best fitting layout and stylesheet for a given stage size.
 * 
 * ResponsivePane is loosely based on responsive design as advocated by website designs, and implemented in for example the Twitter Bootstrap project.
 * But there is a twist in the logic; responsive design assumes a given width and unlimited vertical space with the use of a scrollbar.
 * For websites this is ok, but applications do not have unlimited vertical space. 
 * Take for example a webbrowser; it contains a webview with a scrollbar, but it does not run inside a scrollpane itself.
 * So for applications there is no such thing as unlimited vertical space!
 * It has to use scalable controls, like lists, tables ort webview, to dynamically fill its horizontal and vertical space, just like we are used to in Swing and JavaFX for ages.
 * But given large screen size differences, from 4 inch phones to 32 inch desktops, it is ridiculous to think that a single layout can adapt to all sizes: on a phone you may choose to use a TabbedPane with less controls visible, and on desktop you go all out with a MigPane and a number of gimmicks.   
 * ResponsiveLayout allows you to define reusable nodes and have specific screen size related layouts, using the reusable nodes.
 * It will then use what layout fits best.  
 * 
 * A typical use of ResponsivePane would look like this:
 * [source,java]
 * --
    @Override
    public void start(Stage primaryStage) throws Exception {
    
        // create pane with reusable nodes
        ResponsivePane lResponsivePane = new ResponsivePane();
        lResponsivePane.addReusableNode("CalendarPicker", new CalendarPicker());
        lResponsivePane.addReusableNode("TreeView", new TreeView());
        lResponsivePane.addReusableNode("TableView", new TableView());
        lResponsivePane.addReusableNode("save", new Button("save"));
        lResponsivePane.addReusableNode("saveAndTomorrow", new Button("saveAndTomorrow"));
        lResponsivePane.addReusableNode("-", new Button("-"));
        lResponsivePane.addReusableNode("+", new Button("+"));
        lResponsivePane.addReusableNode("Logo", new Button("Logo"));
        lResponsivePane.addReusableNode("version", new Label("v1.0"));
        
        // define layouts
        lResponsivePane.addLayout(Diagonal.inches(3.5), createPhoneLayout());
        lResponsivePane.addLayout(Diagonal.inches(12.0), createDesktopLayout());
                
        // define css
        lResponsivePane.addSceneStylesheet(Diagonal.inches(4.0), getClass().getResource("phone.css"));
        lResponsivePane.addSceneStylesheet(Diagonal.inches(6.0), getClass().getResource("tablet.css"));
        lResponsivePane.addSceneStylesheet(Diagonal.inches(12.0), getClass().getResource("desktop.css"));
    }
    
    private Node createDesktopLayout() {
        MigPane migPane = new MigPane();
        migPane.add(new Ref("Logo"), new CC());
        migPane.add(new Ref("version"), new CC().spanX(2).alignX("right").alignY("top").wrap());
        migPane.add(new Ref("CalendarPicker"), new CC().alignY("top"));
        migPane.add(new Ref("TreeView"), new CC().grow().pushX().spanY(3).wrap());
        ...
        
        return migPane;
    }
    
    private Node createPhoneLayout() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createTab("Date", "Calendar", new Ref("CalendarPicker")));
        tabPane.getTabs().add(createTab("Tree", "Projectboom", new Ref("TreeView")));
        tabPane.getTabs().add(createTab("Calc", "Totalen", new Ref("TreeView")));
        ... 
           
        return tabPane;
    }

 * --
 * 
 * The exact implementation of createPhoneLayout() and createDesktopLayout() is not relevant, except that they use "Ref" nodes to include the reusable nodes.
 *  
 * == Reusable nodes:
 * 
 * Reusable nodes are identified by their id, this is done for ease of use in FXML. 
 * They can then be used in the different layouts via a "new Ref(<id>)". 
 * Be aware that you cannot reuse Refs, they are place holders and each layout needs a Ref instance on its own. 
 * 
 * 
 * == Stylesheets:
 * Like layouts it is also possible to associated a stylesheet based on the size of the stage. 
 * A stylesheet can be assigned to the scene (applicable for everything on the scene) or to the layout (applicable for the underlying node).
 * Responsive design assigned classes to the nodes, based on the size. 
 * Test have shown that this approach works ok when running the application on a desktop computer, with enough CPU power (GUI Garage's implementation does this), but on mobile these changes have a sever performance impact.
 * So ResponsivePane does not assign classes, but instead it replaces whole CSS files; for the CSS engine this is a single change and it needs to rerender only once.
 * In those CSS files you can assign behavior to the CSS classes, or not.  
 *
 * == Size:
 * Determining the size is an interesting topic. 
 * Responsive design only looks at the width in pixel, but it is already clear that ResponsivePane does not assume unlimited vertical space, so the height must also be taking into account.
 * So size is expressed as a diagonal.
 * But also the pixels that responsive design uses to denote width is debatable; after all a 1000 pixels on a 100 ppi screen is something completely different from a 350 ppi screen; the first is 10 inches in real life, the second not even 3 inch!
 * ResponsivePane therefore per default uses the diagonal size in inches, as we are used to do when talking about devices; a 4 inch phone, a 27 inch monitor.
 * 
 * But because some situations may prefer using width based logic. 
 * So it also is possible to specify size using Width; it will be at runtime converted to diagonal using the actual height of the layout. 
 *  
 * TODO: device
 */
public class ResponsivePane extends StackPane {
	
	
	// ==========================================================================================================================================================================================================================================
	// CONSTRUCTORS
	
	/**
	 * 
	 */
	public ResponsivePane() {
		
		// default device sizes
		deviceToSizeMap.put(DeviceType.PHONE.toString(), Diagonal.inches(3.5));
		deviceToSizeMap.put(DeviceType.TABLET.toString(), Diagonal.inches(7.0));
		deviceToSizeMap.put(DeviceType.DESKTOP.toString(), Diagonal.inches(10.5));
		
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
		
		// react to changes in the available layouts and stylesheets
		layouts.addListener(new ListChangeListener<Layout>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Layout> c) {
				setupLayout();
			}
		});
		sceneStylesheets.addListener(new ListChangeListener<Stylesheet>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Stylesheet> c) {
				setupLayout();
			}
		});
		myStylesheets.addListener(new ListChangeListener<Stylesheet>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Stylesheet> c) {
				setupLayout();
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
	// Reusable nodes
	
	/** refs */
	public ObservableList<Node> getReusableNodes() {
		return reusableNodes;
	}
	final private ObservableList<Node> reusableNodes = FXCollections.observableArrayList();
	
	/**
	 * 
	 * @param id
	 * @param node
	 * @return
	 */
	public Node addReusableNode(String id, Node node) {
		node.setId(id);
		getReusableNodes().add(node);
		return node;
	}
	
	/**
	 * 
	 * @param id
	 * @param node
	 * @return
	 */
	public Node addReusableNode(Node node) {
		if (node.getId() == null || node.getId().trim().length() == 0) {
			throw new IllegalArgumentException("A reusable node must have an id");
		}
		getReusableNodes().add(node);
		return node;
	}
	
	/**
	 * 
	 * @param refId
	 * @return
	 */
	Node findResuableNode(String refId) {
		for (Node lNode : reusableNodes) {
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
	public void addLayout(Size sizeAtLeast, Node root) {
		layouts.add(new Layout(sizeAtLeast, root));
	}
	
	/**
	 * 
	 */
	public void addLayout(Size sizeAtLeast, Orientation orientation, Node root) {
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
	public void addSceneStylesheet(Size sizeAtLeast, URL url) {
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
	public void addMyStylesheet(Size sizeAtLeast, URL url) {
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
	public void setDeviceSize(DeviceType device, Size size) {
		setDeviceSize(device.toString(), size);
	}
	public Size getDeviceSize(DeviceType device) {
		return getDeviceSize(device.toString());
	}
	
	/**
	 * 
	 * @param device
	 * @param size
	 */
	public void setDeviceSize(String device, Size size) {
		deviceToSizeMap.put(device, size);
	}
	public Size getDeviceSize(String device) {
		if (!deviceToSizeMap.containsKey(device)) {
			throw new IllegalArgumentException("Unknown device '" +  device + "', not found in " + deviceToSizeMap.keySet());
		}
		return deviceToSizeMap.get(device);
	}
	final Map<String, Size> deviceToSizeMap = new HashMap<>();


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
	 * Determine the pixels-per-inch of the screen we are on
	 * @return
	 */
	double determinePPI() {
		// determine the DPI factor, so the thresholds become larger on screens with a higher DPI
		double lPPI = 100.0; // average dpi
		Window window = getScene().getWindow();
		ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(window.getX(), window.getY(), window.getWidth(), window.getHeight());
		if (screensForRectangle.size() > 0) {
			// System.out.println("screens of scene: " + screensForRectangle); 
			Screen lScreen = screensForRectangle.get(0); // we just use the first screen
			lPPI = lScreen.getDpi();
		}
		return lPPI;		
	}
	
	/**
	 * 
	 * @return
	 */
	double determineActualSizeInInches() {
		Scene lScene = getScene();
		
		// determine the DPI factor, so the thresholds become larger on screens with a higher DPI
		double lPPI = determinePPI();
		double lWidthInInches = lScene.getWidth() / lPPI;
		double lHeightInInches = lScene.getHeight() / lPPI;
		double lSizeInInches = Math.sqrt( (lWidthInInches * lWidthInInches) + (lHeightInInches * lHeightInInches) );
		if (getTrace()) System.out.println("Actual size=" + lScene.getWidth() + "x" + lScene.getHeight() + "px, size in inches=" + lSizeInInches + " (ppi=" + lPPI + ")");
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
	private final Layout SINGULARITY_LAYOUT = new Layout(Size.ZERO, new Label("?") );
	
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
	final private Stylesheet SINGULAR_STYLESHEET = new Stylesheet(Size.ZERO, null);
	
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
