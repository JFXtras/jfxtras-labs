package jfxtras.labs.scene.layout;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;
import jfxtras.util.NodeUtil;

//TODO: can we just use Pane and just place the hbox and button?
public class OverflowHBox extends StackPane { 
	
	public OverflowHBox() {
		this(0.0, 0.0);
	}
	
	public OverflowHBox(double hboxSpacing, double vboxSpacing) {
		super();
		this.hboxSpacing = hboxSpacing;
		this.vboxSpacing = vboxSpacing;
		
		// create nodes
		createNodes();

		// modify the style classes depending on in which collection they are
		hbox.getChildren().addListener((ListChangeListener<Node>) change -> {
			while(change.next()) {
				for (Node n : change.getAddedSubList()) {
					n.getStyleClass().add(OUTSIDE_CLASS);
					n.getStyleClass().remove(POPUP_CLASS);
				}
				for (Node n : change.getRemoved()) {
					n.getStyleClass().remove(OUTSIDE_CLASS);
					n.getStyleClass().add(POPUP_CLASS);
				}
			}
		});
	}
	final private double hboxSpacing;
	final private double vboxSpacing;
	private final String OUTSIDE_CLASS = OverflowHBox.class.getSimpleName() + "_outside";
	private final String POPUP_CLASS = OverflowHBox.class.getSimpleName() + "_popup";
	

	// ==========================================================================================================================================================================================================================================
	// PROPERTIES
	
	public void add(Node node) {
		hbox.getChildren().add(node);
	}
	
	public void add(Node node, HBox.C hboxC, VBox.C vboxC) {
		hbox.getChildren().add(node);
		hbox.setConstraint(node, hboxC);
		vbox.setConstraint(node, vboxC);
	}
	
	// ==========================================================================================================================================================================================================================================
	// NODE
	
	private void createNodes() {
		hbox = new HBox(hboxSpacing);
		vbox = new VBox(vboxSpacing);
		dropDown = new ToggleButton("V");
		borderPane = new MyBorderPane();
		popup = new Popup();

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
        popup.focusedProperty().addListener((observable) -> {
        	System.out.println("focus " + popup.isFocused());
        });
	}
	private HBox hbox;
	private ToggleButton dropDown;
	private MyBorderPane borderPane;
	private VBox vbox;
	private Popup popup;
	
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
    	
    	// TODO: what happens with spacing?
    	// TODO: what happens with insets?
    	
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
			Node candidateToBeAddedNode = children.get(0);
			double candidatePrefWidth = Math.ceil(candidateToBeAddedNode.prefWidth(-1) + vbox.getSpacing()); // if a node is removed from the hbox, the hbox resizes with its ceiled integer. This may cause the node to be added again here immediately.
			double lActualWidth = getWidth();
			double lPreferredWidth = hbox.prefWidth(-1) + dropDown.prefWidth(-1) + candidatePrefWidth;
	    	System.out.println("add " + lActualWidth + " > " + hbox.prefWidth(-1) + " + " + dropDown.prefWidth(-1) + " + " + candidatePrefWidth + " (" + lPreferredWidth + ")");
			if (lActualWidth > lPreferredWidth) {
				System.out.println("adding");
				children.remove(candidateToBeAddedNode);
				hbox.getChildren().add(candidateToBeAddedNode);
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
