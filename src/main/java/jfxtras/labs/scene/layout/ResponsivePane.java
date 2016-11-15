package jfxtras.labs.scene.layout;

import java.net.URL;

import javafx.beans.DefaultProperty;
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
 * TODO: FXML loading
 * TODO: alias widths (desktop, tablets, etc)
 * TODO: orientation
 * 
<ResponsivePane>
	<refs>
		<CalendarPicker id="yadda"/>
	</refs>
	
	<layouts>
	 	<Layout width="1024">
	 		<MigPane>
	 			<Ref id="yadda" cc="...."/>
	 		</MigPane>
	 	</Layout>
	 	
	 	<Layout width="600">
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
	 
	 <cssFiles>
	 	<CSSFile width="800" url="table.css"/>
	 </cssFiles>
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
	Node findRef(String refId) {
		for (Node lNode : refs) {
			if (refId.equals(lNode.getId())) {
				return lNode;
			}			
		}
		System.err.println("Could not fill reference " + refId);
		return null;
	}
	
	// -----------------------------------------------------------------------------------------------
	// LAYOUTS

	/** layouts */
	public ObservableList<Layout> getLayouts() {
		return layouts;
	}
	final private ObservableList<Layout> layouts = FXCollections.observableArrayList();
	
	public void addLayout(Double width, Node root) {
		layouts.add(new Layout(width, root));
	}

	/**
	 *
	 */
	@DefaultProperty(value="root")
	static public class Layout {
		
		public Layout() {
			
		}
		public Layout(Double width, Node root) {
			setWidth(width);
			setRoot(root);
		}
		
		/** Root */
		public ObjectProperty<Node> rootProperty() { return rootProperty; }
		final private SimpleObjectProperty<Node> rootProperty = new SimpleObjectProperty<>(this, "root", null);
		public Node getRoot() { return rootProperty.getValue(); }
		public void setRoot(Node value) { rootProperty.setValue(value); }
		public Layout withRoot(Node value) { setRoot(value); return this; } 

		/** Width */
		public ObjectProperty<Double> widthProperty() { return widthProperty; }
		final private SimpleObjectProperty<Double> widthProperty = new SimpleObjectProperty<>(this, "width", 0.0);
		public Double getWidth() { return widthProperty.getValue(); }
		public void setWidth(Double value) { widthProperty.setValue(value); }
		public Layout withWidth(Double value) { setWidth(value); return this; } 
	}
	
	/** ActiveLayout */
	public ObjectProperty<Layout> activeLayoutProperty() { return activeLayoutProperty; }
	final private SimpleObjectProperty<Layout> activeLayoutProperty = new SimpleObjectProperty<Layout>(this, "activeLayout", null);
	public Layout getActiveLayout() { return activeLayoutProperty.getValue(); }
	public void setActiveLayout(Layout value) { activeLayoutProperty.setValue(value); }
	public ResponsivePane withActiveLayout(Layout value) { setActiveLayout(value); return this; } 

	/**
	 * This class represents a placeholder for nodes in the refs list
	 */
	static public class Ref extends StackPane {
		public Ref (String id) { 
			setId(id);
			
			// TODO: check if scene property is a good indicator when to pull the ref
			sceneProperty().addListener( (observable) -> {
				Scene scene = sceneProperty().get();
//				System.out.println(id + ": Scene property changed: " + scene);
				getChildren().clear();
				if (scene != null) {
					pullRef();
				}
			});
		}

		void pullRef() {
			// find my containing ResponsiveLayout
			Node parent = this.getParent();
			while (parent != null && !(parent instanceof ResponsivePane)) {
				parent = parent.getParent();
			}
			ResponsivePane lResponsivePane = (ResponsivePane)parent;
			
			// find the reffered node
			String lRefId = getId();
			Node lReffedNode = lResponsivePane.findRef(lRefId);

			getChildren().clear();
			if (lReffedNode != null) {
//				System.out.println(lRefId + ": setting content");
				getChildren().add(lReffedNode);
			}
		}
	}
	
	// -----------------------------------------------------------------------------------------------
	// CSS files

	/** cssFiles */
	public ObservableList<CSSFile> getCSSFiles() {
		return cssFiles;
	}
	final private ObservableList<CSSFile> cssFiles = FXCollections.observableArrayList();

	public void addCSSFile(double width, URL url) {
		cssFiles.add(new CSSFile(width, url));
	}


	/**
	 *
	 */
	@DefaultProperty(value="url")
	static public class CSSFile {
		
		public CSSFile() {
			
		}
		
		public CSSFile(double width, URL url) {
			setWidth(width);
			setURL(url);
		}		
		
		/** URL */
		public ObjectProperty<URL> urlProperty() { return urlProperty; }
		final private SimpleObjectProperty<URL> urlProperty = new SimpleObjectProperty<>(this, "url", null);
		public URL getURL() { return urlProperty.getValue(); }
		public void setURL(URL value) { urlProperty.setValue(value); }
		public CSSFile withURL(URL value) { setURL(value); return this; } 

		/** Width */
		public ObjectProperty<Double> widthProperty() { return widthProperty; }
		final private SimpleObjectProperty<Double> widthProperty = new SimpleObjectProperty<>(this, "width", 0.0);
		public Double getWidth() { return widthProperty.getValue(); }
		public void setWidth(Double value) { widthProperty.setValue(value); }
		public CSSFile withWidth(Double value) { setWidth(value); return this; } 
	}
	
	/** ActiveCSSFile */
	public ObjectProperty<CSSFile> activeCSSFileProperty() { return activeCSSFileProperty; }
	final private SimpleObjectProperty<CSSFile> activeCSSFileProperty = new SimpleObjectProperty<CSSFile>(this, "activeCSSFile", null);
	public CSSFile getActiveCSSFile() { return activeCSSFileProperty.getValue(); }
	public void setActiveCSSFile(CSSFile value) { activeCSSFileProperty.setValue(value); }
	public ResponsivePane withActiveCSSFile(CSSFile value) { setActiveCSSFile(value); return this; } 

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
    		
    		System.out.println("Activating layout " + lLayout.getWidth());
        	setActiveLayout(lLayout);

    		// switch to active layout
        	ResponsivePane.this.getChildren().clear();
        	ResponsivePane.this.getChildren().add(lLayout.getRoot());
    	}
    	
    	// determine css
    	CSSFile lCSSFile = determineBestFittingCSSFile(lActualWidth);
    	if (!lCSSFile.equals(getActiveCSSFile())) {
    		
    		System.out.println("Activating CSS " + lCSSFile.getWidth());
    		setActiveCSSFile(lCSSFile);

			// switch to active CSS file
			load(lCSSFile);
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
//			System.out.println("screens of scene: " + screensForRectangle); 
			Screen lScreen = screensForRectangle.get(0);
			dpiFactor = lScreen.getDpi() / 96.0;
		}

		double width = lScene.getWidth() / dpiFactor;
//		System.out.println("determineActualWidth=" + lScene.getWidth() + " -> " + width);
		return width;
	}
	
	/**
	 * 
	 * @return
	 */
	Layout determineBestFittingLayout(double lActualWidth) {
		
		Layout lBestFittingLayout = null;
		for (Layout lLayout : layouts) {
			if (lActualWidth >= lLayout.getWidth()) {
				if (lBestFittingLayout == null || lBestFittingLayout.getWidth() < lLayout.getWidth()) {
					lBestFittingLayout = lLayout;
				}
			}
		}
		
		// default layout
		if (lBestFittingLayout == null) {
			lBestFittingLayout = SINGULARITY_LAYOUT;
		}
		
//		System.out.println("determineBestFittingLayout=" + lBestFittingLayout.getWidth());
		return lBestFittingLayout;
	}
	private final Layout SINGULARITY_LAYOUT = new Layout(0.0, new Label("?") );
	
	/**
	 * 
	 * @return
	 */
	CSSFile determineBestFittingCSSFile(double lActualWidth) {
		
		CSSFile lBestFittingCSSFile = null;
		for (CSSFile lCSSFile : cssFiles) {
			if (lActualWidth >= lCSSFile.getWidth()) {
				if (lBestFittingCSSFile == null || lBestFittingCSSFile.getWidth() < lCSSFile.getWidth()) {
					lBestFittingCSSFile = lCSSFile;
				}
			}
		}
		
		// default CSSfile
		if (lBestFittingCSSFile == null) {
			lBestFittingCSSFile = SINGULAR_CSSFILE;
		}
		
//		System.out.println("determineBestFittingCSSFile=" + lBestFittingCSSFile.getWidth() + " -> " + lBestFittingCSSFile.getURL());
		return lBestFittingCSSFile;
	}
	final private CSSFile SINGULAR_CSSFILE = new CSSFile(0.0, null);
	
	/**
	 * 
	 * @param cssFile
	 */	
	void load(CSSFile cssFile) {
		Scene scene = getScene();
		String stylesheet = (cssFile.getURL() == null ? null : cssFile.getURL().toExternalForm());
		
		// remove old (there may be a delay, so we attempt to remove all)
		for (CSSFile lCSSFile : cssFiles) {
			String lStylesheet = lCSSFile.getURL().toExternalForm();
			if (scene.getStylesheets().remove(lStylesheet)) {
				System.out.println("Removed " + lStylesheet);
			}			
		}
		
		// load new
		if (stylesheet != null) {
			System.out.println("Loading " + stylesheet);
			scene.getStylesheets().add(stylesheet);
		}
	}

}
