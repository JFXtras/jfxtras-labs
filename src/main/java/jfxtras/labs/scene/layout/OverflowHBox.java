package jfxtras.labs.scene.layout;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import jfxtras.util.NodeUtil;

//TODO: can we just use Pane and just place the hbox and button?
public class OverflowHBox extends StackPane { 
	
	public OverflowHBox() {
		super();
		createNodes();
		
		hbox.widthProperty().addListener( (observable) -> {
		});
	}
	
	// ==========================================================================================================================================================================================================================================
	// PROPERTIES
	
	public void add(Node node) {
		hbox.getChildren().add(node);
	}
	
	// ==========================================================================================================================================================================================================================================
	// NODE
	
	private void createNodes() {
//		borderPane.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-border-style: dashed;");
//		hbox.setStyle("-fx-border-color: green; -fx-border-width: 1; -fx-border-style: dashed;");
		getChildren().add(borderPane);
		dropDown.onActionProperty().set(event -> {
			showPopup();
		});
		popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.getContent().add(vbox);
        popup.onHiddenProperty().addListener((observable) -> dropDown.setSelected(false) ); // TODO: not working
	}
	private HBox hbox = new HBox();
	private ToggleButton dropDown = new ToggleButton("V");
	private MyBorderPane borderPane = new MyBorderPane();
	private VBox vbox = new VBox();
	private Popup popup = new Popup();
	
	class MyBorderPane extends BorderPane {
		public MyBorderPane() {
			super(hbox);
			setRight(dropDown);
		}

		@Override
		public void layoutChildren() {
			super.layoutChildren();
		}
	};
	
	private void showPopup() {
		if (dropDown.isSelected()) {
			popup.show(dropDown, NodeUtil.screenX(dropDown), NodeUtil.screenY(dropDown) + dropDown.getHeight());
		}
		else {
			popup.hide();
		}
	}

	
	// ==========================================================================================================================================================================================================================================
	// LAYOUT
	
    @Override 
    protected void layoutChildren() {
    	super.layoutChildren();
    	// TODO: what hebbens with insets?
    	
    	// should we remove a node?
		while (hbox.getChildren().size() > 0) {
			double lActualWidth = getWidth();
			double lPreferredWidth = hbox.prefWidth(-1) + dropDown.prefWidth(-1);
	    	System.out.println("remove " + lActualWidth + " <= " + hbox.prefWidth(-1) + " + " + dropDown.prefWidth(-1) + " (" + lPreferredWidth + ")");
			if (lActualWidth <= lPreferredWidth) {
				ObservableList<Node> children = hbox.getChildren();
				Node removedNode = children.remove(children.size() - 1);
				System.out.println("removing " + removedNode.prefWidth(-1));				
				vbox.getChildren().add(0, removedNode);
		    	super.layoutChildren();
			}
			else {
				break;
			}
		}
		
		// should we add a node?
		while (vbox.getChildren().size() > 0) {
			ObservableList<Node> children = vbox.getChildren();
			Node candidateToBeAdded = children.get(0);
			double candidatePrefWidth = Math.ceil(candidateToBeAdded.prefWidth(-1)); // if a node is removed from the hbox, the hbox resizes with its ceiled integer. This may cause the node to be added again here immediately.
			double lActualWidth = getWidth();
			double lPreferredWidth = hbox.prefWidth(-1) + dropDown.prefWidth(-1) + candidatePrefWidth;
	    	System.out.println("add " + lActualWidth + " > " + hbox.prefWidth(-1) + " + " + dropDown.prefWidth(-1) + " + " + candidatePrefWidth + " (" + lPreferredWidth + ")");
			if (lActualWidth > lPreferredWidth) {
				System.out.println("adding");
				children.remove(candidateToBeAdded);
				hbox.getChildren().add(candidateToBeAdded);
		    	super.layoutChildren();
			}
			else {
				break;
			}
		}
		
		dropDown.setDisable(vbox.getChildren().size() == 0);
    }


	// ==========================================================================================================================================================================================================================================
	// SUPPORT
}
