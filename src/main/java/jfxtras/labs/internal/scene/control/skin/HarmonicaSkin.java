package jfxtras.labs.internal.scene.control.skin;

import javafx.collections.ListChangeListener;
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
		
		// listen to changes in the visible tab 
		getSkinnable().visibleTabProperty().addListener( (observable) -> {
			if (getSkinnable().getVisibleTab() != null) {
				show(getSkinnable().getVisibleTab().getNode());
			}
		});
		
		// listen to changes in the tabs
		monitorTabs();
	}

	/**
	 * 
	 */
	private void monitorTabs()
	{
		// react to changes
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
							System.out.println("Removing " + lTab);
							removeTab(lTab);
						}
					}
				}
				finally {
				}
			}
		});
		
		// add any already existing tabs
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
	
	/**
	 * 
	 * @param tab
	 */
	private void addTab(Harmonica.Tab tab) {
		
		// check
		if (tab.getName() == null) {
			throw new NullPointerException("Harmonica tabs must have name set");
		}
		if (tab.getNode() == null) {
			throw new NullPointerException("Harmonica tabs must have node set");
		}
		
		// add the tab button
		TabButton lTabButton = new TabButton(tab.getName());
		vbox.add(lTabButton, new VBox.C().maxWidth(Double.MAX_VALUE));
		
		// add the tab pane
		TabPane lTabPane = new TabPane(tab.getNode());
		vbox.add(lTabPane, new VBox.C().maxWidth(Double.MAX_VALUE).vgrow(Priority.ALWAYS)); // since only one TabPane is visible at any given time, all TabPanes can be ALWAYS

		// on click of the button, make the pane visible (and hide any visible ones)
		lTabButton.onActionProperty().set(event -> {
			show(lTabPane.getContent());
		});
		
		// the first added node is shown per default
		if (getSkinnable().getVisibleTab() == null) {
			getSkinnable().setVisibleTab(tab);
		}
		
		// make sure the display is up to date
		refresh();
	}

	/**
	 * TODO: what if the visible tab is removed
	 */
	private void removeTab(Tab lTab) {
		
		// scan all TabPanes, last to first
		for (int i = vbox.getChildren().size() - 1; i >= 0; i-=2) {
			Node lNode = vbox.getChildren().get(i);
			TabPane lTabPane = (TabPane)lNode;
			
			// if the scanned tabpane has the same contents as in the tab, remove it
			if (lTabPane.getContent() == lTab.getNode()) {
				vbox.getChildren().remove(i); // remove tab
				vbox.getChildren().remove(i - 1); // remove button
			}
		}
		
		// if the visible tab is removed
		if (lTab == getSkinnable().getVisibleTab()) {
			// make the first tab visible
			getSkinnable().setVisibleTab( getSkinnable().tabs().size() == 0 ? null : getSkinnable().tabs().get(0) );
		}
		
		// make sure the display is up to date
		refresh();
	}

	/**
	 * 
	 * @param node
	 */
	private void show(Node node) {
		// scan all TabPanes, first to last
		for (int i = 1; i < vbox.getChildren().size(); i+=2) {
			TabPane lTabPane = (TabPane)vbox.getChildren().get(i);
			
			// if the tabpane holds the node that needs to be shown, make it visible, otherwise hide it
			lTabPane.setVisible(node == lTabPane.getContent());
			lTabPane.setManaged(lTabPane.isVisible());
			
			// make sure the focus moves from the button to the tab contents
			if (lTabPane.isVisible()) {
				lTabPane.getContent().requestFocus();
			}
			
			// update the visibleTab in the control
			for (Tab lTab : getSkinnable().tabs()) {
				if (lTab.getNode() == node) {
					getSkinnable().setVisibleTab(lTab);
				}
			}
		}
	}

	/**
	 * Show the correct tab
	 */
	private void refresh() {
		if (getSkinnable().getVisibleTab() != null) {
			show(getSkinnable().getVisibleTab().getNode());
		}
	}
	
	class TabButton extends Button {
		TabButton(String name) {
			super(name);
		}
	}
	
	class TabPane extends BorderPane {
		TabPane(Node node) {
			super(node);
			//for debugging setStyle("-fx-border-color:red; -fx-border-width:1px;"); 
		}
		
		Node getContent() {
			return getCenter();
		}
	}
}
