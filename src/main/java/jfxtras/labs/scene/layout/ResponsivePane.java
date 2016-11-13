package jfxtras.labs.scene.layout;

import java.util.List;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Window;

/*
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
	 
	 <csses>
	 	<Css width="800" file="table.css"/>
	 </csses>
</ResponsivePane>
*/

public class ResponsivePane extends StackPane {
	
	// ==========================================================================================================================================================================================================================================
	// PROPERTIES

	/** Id */
	public ResponsivePane withId(String v) { setId(v); return this; }

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
	
	// -----------------------------------------------------------------------------------------------
	// LAYOUTS

	/** layouts */
	public ObservableList<Layout> getLayouts() {
		return layouts;
	}
	private ObservableList<Layout> layouts = FXCollections.observableArrayList();

	/**
	 *
	 */
	@DefaultProperty(value="root")
	static public class Layout {
		
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
	 * 
	 */
	static public class Ref extends Pane {
		public Ref (String id) { 
			setId(id);
		}

		public void pullRef(List<Node> refs) {
			String lRefId = getId();
			Node lReffedNode = findRef(refs, lRefId);

			getChildren().clear();
			if (lReffedNode != null) {
				getChildren().add(lReffedNode);
			}
		}
	}
	
	// ==========================================================================================================================================================================================================================================
	// LAYOUT
	
    @Override 
    protected void layoutChildren() {
    	this.getChildren().clear();
    	
    	// get active layout
//    	Layout lLayout = getActiveLayout();
    	Layout lLayout = determineBestFittingLayout();
    	setActiveLayout(lLayout);
    	if (lLayout != null) {

    		// switch to active layout
    		// TODO: only when there something has changed
    		fillRefs(lLayout);
    		this.getChildren().add(lLayout.getRoot());
    	}
    	
    	// continue layout
    	super.layoutChildren();
    }
    
    void fillRefs(Layout layout) {
   
    	// no layout, nothing to do
    	if (layout == null) {
    		return;
    	}
   
    	// scan through all the nodes
    	// TODO: cache the found Refs per layout
    	fillRefs(layout.getRoot());
    }
    
	/**
	 * 
	 */
	void fillRefs(Node n) {
		if (n instanceof Ref) {
			Ref lRef = (Ref)n;
			lRef.pullRef(refs);
		}
		
		// scan children
		if (n instanceof Control) {
			Control lControl = (Control)n;
			Skin lSkin = lControl.getSkin();
			if (lSkin instanceof SkinBase) {
				SkinBase lSkinBase = (SkinBase)lSkin;
				for (Object lChild : lSkinBase.getChildren()) {
					fillRefs((Node)lChild);
				}
			}
		}
		else if (n instanceof Pane) {
			Pane lPane = (Pane)n;
			for (Node lChild : lPane.getChildren()) {
				fillRefs(lChild);
			}
		}
	}

	/**
	 * 
	 * @param refs2 
	 * @param refId
	 * @return
	 */
	static Node findRef(List<Node> refs, String refId) {
		for (Node lNode : refs) {
			if (refId.equals(lNode.getId())) {
				return lNode;
			}			
		}
		return null;
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
		return width;
	}
	
	Layout determineBestFittingLayout() {
		double lActualWidth = determineActualWidth();
		
		Layout lBestFittingLayout = null;
		for (Layout lLayout : layouts) {
			if (lActualWidth >= lLayout.getWidth()) {
				if (lBestFittingLayout == null || lBestFittingLayout.getWidth() < lLayout.getWidth()) {
					lBestFittingLayout = lLayout;
				}
			}
		}
		
		return lBestFittingLayout;
	}
}
