package jfxtras.labs.scene.layout;

import java.net.URL;
import java.util.List;

import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Window;

/*
 * TODO: FXML loading
 * TODO: alias widths (desktop, tablets, etc)
 * TODO: orientation
 * 
<ResponsivePane>
	<refs>
		<CalendarPicker id="yadda"/>
	</refs>
	
	<layouts>
	 	<Layout widthAtLeast="1024">
	 		<MigPane>
	 			<Ref id="yadda" cc="...."/>
	 		</MigPane>
	 	</Layout>
	 	
	 	<Layout widthAtLeast="600">
	 		<TabPane>
	 			<tabs>
	 				<Tab>
	 					<content>
	 						<Ref id="yadda" cc="...."/>
	 					</content>
	 				</Tab>
	 			</tabs>
	 		</TabPane>
	 	</Layout>
	 </layouts>
	 
	 <sceneStylesheets>
	 	<Stylesheet widthAtLeast="800" url="table.css"/>
	 </sceneStylesheets>
</ResponsivePane>
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
		
		// react to changes in the scene
		sceneProperty().addListener((ChangeListener<Scene>) (observable, oldValue, newValue) -> {
			
			// by listening to it width
			if (oldValue != null) {
				oldValue.widthProperty().removeListener(sizeInvalidationListener);
			}
			if (newValue != null) {
				newValue.widthProperty().addListener(sizeInvalidationListener);
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
	
	public void addLayout(Double widthAtLeast, Node root) {
		layouts.add(new Layout(widthAtLeast, root));
	}
	
	/** ActiveLayout */
	public ObjectProperty<Layout> activeLayoutProperty() { return activeLayoutProperty; }
	final private SimpleObjectProperty<Layout> activeLayoutProperty = new SimpleObjectProperty<Layout>(this, "activeLayout", null);
	public Layout getActiveLayout() { return activeLayoutProperty.getValue(); }
	public void setActiveLayout(Layout value) { activeLayoutProperty.setValue(value); }
	public ResponsivePane withActiveLayout(Layout value) { setActiveLayout(value); return this; } 

	/**
	 *
	 */
	@DefaultProperty("root")
	static public class Layout {
		
		public Layout() {
			
		}
		public Layout(Double widthAtLeast, Node root) {
			setWidthAtLeast(widthAtLeast);
			setRoot(root);
		}
		
		/** Root */
		public ObjectProperty<Node> rootProperty() { return rootProperty; }
		final private SimpleObjectProperty<Node> rootProperty = new SimpleObjectProperty<>(this, "root", null);
		public Node getRoot() { return rootProperty.getValue(); }
		public void setRoot(Node value) { rootProperty.setValue(value); }
		public Layout withRoot(Node value) { setRoot(value); return this; } 

		/** WidthAtLeast */
		public ObjectProperty<Double> widthAtLeastProperty() { return widthAtLeastProperty; }
		final private SimpleObjectProperty<Double> widthAtLeastProperty = new SimpleObjectProperty<>(this, "widthAtLeast", 0.0);
		public Double getWidthAtLeast() { return widthAtLeastProperty.getValue(); }
		public void setWidthAtLeast(Double value) { widthAtLeastProperty.setValue(value); }
		public Layout withWidthAtLeast(Double value) { setWidthAtLeast(value); return this; } 
	}

	/**
	 * This class represents a placeholder for nodes in the refs list
	 */
	static public class Ref extends StackPane {
		
		public Ref() {
			construct();
		}
		
		public Ref (String to) { 
			setTo(to);
			construct();
		}
		
		private void construct() {
			// when the scene changes to not null, pull the referred node  
			sceneProperty().addListener( (observable) -> {
				getChildren().clear();
				if (sceneProperty().get() != null) {
					pullRef();
				}
			});
		}

		/** To */
		public ObjectProperty<String> toProperty() { return toProperty; }
		final private SimpleObjectProperty<String> toProperty = new SimpleObjectProperty<>(this, "to", null);
		public String getTo() { return toProperty.getValue(); }
		public void setTo(String value) { toProperty.setValue(value); }
		public Ref withTo(String value) { setTo(value); return this; } 

		
		void pullRef() {
			
			// find my containing ResponsiveLayout
			if (lResponsivePane == null) {
				Node parent = this.getParent();
				while (parent != null && !(parent instanceof ResponsivePane)) {
					parent = parent.getParent();
				}
				lResponsivePane = (ResponsivePane)parent;
			}
			
			// find the referred to node
			String lRefTo = getTo();
			Node lReferredNode = lResponsivePane.findReferredNode(lRefTo);
			if (lResponsivePane.getTrace()) System.out.println("Ref " + getId() + " referring to " + lRefTo + " becomes " + lReferredNode);
			
			// pull the referred node into this ref
			getChildren().clear();
			if (lReferredNode != null) {
				getChildren().add(lReferredNode);
			}
			
			// show debug information
			if (!lResponsivePane.getDebug() && !lResponsivePane.getTrace()) {
				setStyle(null);
			}
			else {
				// draw a border around the reference
				this.setStyle("-fx-border-color: blue; -fx-border-insets: 3; -fx-border-width: 2; -fx-border-style: dashed;");
				
				// and an ID in the top left
				Label label = new Label((getId() == null ? "" : getId() + "->") + getTo());
				label.setWrapText(true);
				label.setStyle("-fx-text-fill: blue; -fx-background-color: white;");
				getChildren().add(label);
				StackPane.setAlignment(label, Pos.TOP_LEFT);
			}
		}
		private ResponsivePane lResponsivePane = null;
	}
	
	// -----------------------------------------------------------------------------------------------
	// SceneStylesheets

	/** sceneStylesheets */
	public ObservableList<Stylesheet> getSceneStylesheets() {
		return sceneStylesheets;
	}
	final private ObservableList<Stylesheet> sceneStylesheets = FXCollections.observableArrayList();

	public void addSceneStylesheet(double widthAtLeast, URL url) {
		sceneStylesheets.add(new Stylesheet(widthAtLeast, url));
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

	public void addMyStylesheet(double widthAtLeast, URL url) {
		myStylesheets.add(new Stylesheet(widthAtLeast, url));
	}
	
	/** ActiveMyStylesheet */
	public ObjectProperty<Stylesheet> activeMyStylesheetProperty() { return activeMyStylesheetProperty; }
	final private SimpleObjectProperty<Stylesheet> activeMyStylesheetProperty = new SimpleObjectProperty<Stylesheet>(this, "activeMyStylesheet", null);
	public Stylesheet getActiveMyStylesheet() { return activeMyStylesheetProperty.getValue(); }
	public void setActiveMyStylesheet(Stylesheet value) { activeMyStylesheetProperty.setValue(value); }
	public ResponsivePane withActiveMyStylesheet(Stylesheet value) { setActiveMyStylesheet(value); return this; } 

	// -----------------------------------------------------------------------------------------------
	// Stylesheet

	/**
	 *
	 */
	@DefaultProperty("url")
	static public class Stylesheet {
		
		public Stylesheet() {
			
		}
		
		public Stylesheet(double widthAtLeast, URL url) {
			setWidthAtLeast(widthAtLeast);
			setUrl(url);
		}		
		
		/** Url */
		public ObjectProperty<URL> urlProperty() { return urlProperty; }
		final private SimpleObjectProperty<URL> urlProperty = new SimpleObjectProperty<>(this, "url", null);
		public URL getUrl() { return urlProperty.getValue(); }
		public void setUrl(URL value) { urlProperty.setValue(value); }
		public Stylesheet withUrl(URL value) { setUrl(value); return this; } 

		/** WidthAtLeast */
		public ObjectProperty<Double> widthAtLeastProperty() { return widthAtLeastProperty; }
		final private SimpleObjectProperty<Double> widthAtLeastProperty = new SimpleObjectProperty<>(this, "widthAtLeast", 0.0);
		public Double getWidthAtLeast() { return widthAtLeastProperty.getValue(); }
		public void setWidthAtLeast(Double value) { widthAtLeastProperty.setValue(value); }
		public Stylesheet withWidthAtLeast(Double value) { setWidthAtLeast(value); return this; } 
	}

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
		double lActualWidth = determineActualWidth();

    	// determine layout
    	Layout lLayout = determineBestFittingLayout(lActualWidth);
    	if (!lLayout.equals(getActiveLayout())) {
    		
    		if (getDebug() || getTrace()) System.out.println("Activating layout " + lLayout.getWidthAtLeast());
        	setActiveLayout(lLayout);

    		// switch to active layout
        	ResponsivePane.this.getChildren().clear();
        	ResponsivePane.this.getChildren().add(lLayout.getRoot());
    	}
    	
    	// determine scene stylesheet
    	{
	    	Stylesheet lStylesheet = determineBestFittingStylesheet(lActualWidth, sceneStylesheets);
	    	if (!lStylesheet.equals(getActiveSceneStylesheet())) {
	    		
	    		setActiveSceneStylesheet(lStylesheet);
	
				// switch to active CSS file
				load(lStylesheet, sceneStylesheets, getScene().getStylesheets());
	    	}
    	}
    	
    	// determine my stylesheet
    	{
	    	Stylesheet lStylesheet = determineBestFittingStylesheet(lActualWidth, myStylesheets);
	    	if (!lStylesheet.equals(getActiveMyStylesheet())) {
	    		
	    		setActiveMyStylesheet(lStylesheet);
	
				// switch to active CSS file
				load(lStylesheet, myStylesheets, getStylesheets());
	    	}
    	}
	}


	/**
	 * 
	 * @return
	 */
	double determineActualWidth() {
		Scene lScene = getScene();
		
		// determine the DPI factor, so the thresholds become larger on screens with a higher DPI
		double dpiFactor = 1.0; // based on 96 dpi
		Window window = lScene.getWindow();
		ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(window.getX(), window.getY(), window.getWidth(), window.getHeight());
		if (screensForRectangle.size() > 0) {
			// System.out.println("screens of scene: " + screensForRectangle); 
			Screen lScreen = screensForRectangle.get(0);
			dpiFactor = lScreen.getDpi() / 96.0;
		}

		double width = lScene.getWidth() / dpiFactor;
		if (getTrace()) System.out.println("Actual width=" + lScene.getWidth() + ", converted for widthAtLeast=" + width);
		return width;
	}
	
	/**
	 * 
	 * @return
	 */
	Layout determineBestFittingLayout(double lActualWidth) {
		
		Layout lBestFittingLayout = null;
		for (Layout lLayout : layouts) {
			if (lActualWidth >= lLayout.getWidthAtLeast()) {
				if (lBestFittingLayout == null || lBestFittingLayout.getWidthAtLeast() < lLayout.getWidthAtLeast()) {
					lBestFittingLayout = lLayout;
				}
			}
		}
		
		// default layout
		if (lBestFittingLayout == null) {
			lBestFittingLayout = SINGULARITY_LAYOUT;
		}
		
		if (getTrace()) System.out.println("determineBestFittingLayout=" + lBestFittingLayout.getWidthAtLeast());
		return lBestFittingLayout;
	}
	private final Layout SINGULARITY_LAYOUT = new Layout(0.0, new Label("?") );
	
	/**
	 * 
	 * @return
	 */
	Stylesheet determineBestFittingStylesheet(double lActualWidth, List<Stylesheet> availableStylesheets) {
		
		Stylesheet lBestFittingStylesheet = null;
		for (Stylesheet lStylesheet : availableStylesheets) {
			if (lActualWidth >= lStylesheet.getWidthAtLeast()) {
				if (lBestFittingStylesheet == null || lBestFittingStylesheet.getWidthAtLeast() < lStylesheet.getWidthAtLeast()) {
					lBestFittingStylesheet = lStylesheet;
				}
			}
		}
		
		// default Stylesheet
		if (lBestFittingStylesheet == null) {
			lBestFittingStylesheet = SINGULAR_Stylesheet;
		}
		
		if (getTrace()) System.out.println("determineBestFittingStylesheet=" + lBestFittingStylesheet.getWidthAtLeast() + " -> " + lBestFittingStylesheet.getUrl());
		return lBestFittingStylesheet;
	}
	final private Stylesheet SINGULAR_Stylesheet = new Stylesheet(0.0, null);
	
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
				if (getDebug() || getTrace()) System.out.println("Removed " + lActiveStylesheetUrl);
			}			
		}
		
		// load new
		if (lStylesheetUrl != null) {
			if (getDebug() || getTrace()) System.out.println("Loading " + lStylesheetUrl);
			activeStylesheetUrls.add(lStylesheetUrl);
		}
	}

}
