package jfxtras.labs.internal.scene.control.skin;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import jfxtras.labs.scene.control.Harmonica;
import jfxtras.labs.scene.control.Harmonica.Tab;
import jfxtras.scene.layout.VBox;

public class HarmonicaSkin extends SkinBase<Harmonica> {

	/**
	 * 
	 */
	public HarmonicaSkin(Harmonica control) {
		super(control);
		construct();
	}

	/*
	 * 
	 */
	private void construct() {
		// setup component
		createNodes();
		
		// listen to changes
		monitorTabs();
	}

	// construct property
	private void monitorTabs()
	{
		getSkinnable().tabs().addListener(new ListChangeListener<Harmonica.Tab>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Harmonica.Tab> change)
			{
				try {
					while (change.next()) {
						for (Harmonica.Tab lTab : change.getAddedSubList()) {
							addTab(lTab);
						}
						for (Harmonica.Tab lTab : change.getRemoved()) {
							addRemove(lTab);
						}
					}
				}
				finally {
				}
			}
		});
		
		for (Harmonica.Tab lTab : getSkinnable().tabs()) {
			addTab(lTab);
		}
	}

	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes() {
		
		// add to self
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(vbox);
	}
	final private VBox vbox = new VBox();
	
	private void addTab(Harmonica.Tab tab) {
		Button lButton = new Button(tab.getName());
		vbox.add(lButton, new VBox.C().maxWidth(Double.MAX_VALUE));
		
		BorderPane lBorderPane = new BorderPane(tab.getNode());
		//lBorderPane.setStyle("-fx-border-color:red; -fx-border-width:1px;"); 
		vbox.add(lBorderPane, new VBox.C().maxWidth(Double.MAX_VALUE));

		lButton.onActionProperty().set(event -> {
			setVGrow(lBorderPane);
		});
		setVGrow(lBorderPane); // latest node gets grow
	}
	
	private void setVGrow(Node node) {
		for (Node lNode : vbox.getChildren()) {
			if (lNode instanceof BorderPane) {
				lNode.setVisible(node == lNode);
				lNode.setManaged(lNode.isVisible());
				VBox.setVgrow(lNode, (lNode.isVisible() ? Priority.ALWAYS : Priority.NEVER) );
			}
		}
	}

	private void addRemove(Tab lTab) {
		// TODO Auto-generated method stub
		
	}
}
