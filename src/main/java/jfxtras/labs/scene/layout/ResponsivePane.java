package jfxtras.labs.scene.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jfxtras.internal.scene.control.skin.CalendarTimePickerSkin;
import jfxtras.scene.control.CalendarTimePicker;

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
	
	@DefaultProperty(value="root")
	static public class Layout {
		/** Root */
		public ObjectProperty<Node> rootProperty() { return rootProperty; }
		final private SimpleObjectProperty<Node> rootProperty = new SimpleObjectProperty<>(this, "root", null);
		public Node getRoot() { return rootProperty.getValue(); }
		public void setRoot(Node value) { rootProperty.setValue(value); }
		public Layout withRoot(Node value) { setRoot(value); return this; } 

		/** Width */
		public ObjectProperty<Integer> widthProperty() { return widthProperty; }
		final private SimpleObjectProperty<Integer> widthProperty = new SimpleObjectProperty<>(this, "width", 0);
		public Integer getWidth() { return widthProperty.getValue(); }
		public void setWidth(Integer value) { widthProperty.setValue(value); }
		public Layout withWidth(Integer value) { setWidth(value); return this; } 
	}
	
	static public class Ref extends StackPane {
		public Ref (String id) { 
			setId(id);
		}

		public void pullRef(List<Node> refs) {
			getChildren().clear();
			for (Node n : refs) {
				System.out.println(this.getId() + " <-> " + n.getId());
				if (this.getId().equals(n.getId())) {
					getChildren().add(n);
				}
			}
		}
	}
	
	// ==========================================================================================================================================================================================================================================
	// LAYOUT

    @Override 
    protected void layoutChildren() {
    	this.getChildren().clear();
    	this.getChildren().add(layouts.get(0).getRoot());
    	((Ref)((Pane)this.getChildren().get(0)).getChildren().get(1)).pullRef(refs);
    	super.layoutChildren();
    }
}
