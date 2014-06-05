package jfxtras.labs.scene.menu;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.labs.scene.layout.CircularPane.AnimationLayoutInfo;

public class CornerMenu {
	// TODO: allow the animationInterpolation to be set
	// TODO: should we always require a StackPane to install upon (in the constructor)? This will make the shown property have a sensible reason to exist
	
	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public CornerMenu(Orientation orientation, StackPane stackPane)
	{
		orientationObjectProperty.set(orientation);
		construct();
		addToStackPane(stackPane);
	}

	/*
	 * 
	 */
	private void construct()
	{
        // listen to items and modify circular pane's children accordingly
		getItems().addListener( (ListChangeListener.Change<? extends MenuItem> change) -> {
			while (change.next())
			{
				for (MenuItem lMenuItem : change.getRemoved())
				{
					for (javafx.scene.Node lNode : new ArrayList<javafx.scene.Node>(circularPane.getChildren())) {
						if (lNode instanceof CornerMenuNode) {
							CornerMenuNode lCornerMenuNode = (CornerMenuNode)lNode;
							if (lCornerMenuNode.menuItem == lMenuItem) {
								circularPane.remove(lCornerMenuNode);
							}
						}
					}
				}
				for (MenuItem lMenuItem : change.getAddedSubList()) 
				{
					circularPane.add( new CornerMenuNode(lMenuItem) );
				}
			}
		});	
	}
	

	// ==================================================================================================================
	// PROPERTIES
	
	/** Orientation: TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT */	
    public ReadOnlyObjectProperty<Orientation> orientationProperty() { 
    	return new ReadOnlyObjectWrapper<Orientation>(this, "orientation").getReadOnlyProperty();
    }
	final private SimpleObjectProperty<Orientation> orientationObjectProperty = new SimpleObjectProperty<Orientation>(this, "orientation", Orientation.TOP_LEFT);
	public static enum Orientation {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT}
	public Orientation getOrientation() { return orientationObjectProperty.getValue(); }
	
	// Items
    private final ObservableList<MenuItem> items = FXCollections.observableArrayList(); 
    public final ObservableList<MenuItem> getItems() {
        return items;
    }
    
	// ==================================================================================================================
	// ACTION
	
    public void show() {
		circularPane.setVisible(true);
		circularPane.animateIn();
	}
    
    public void hide() {
		circularPane.animateOut();
		// if no animation, call the event directly
		if (circularPane.getAnimationInterpolation() == null) {
			circularPane.getOnAnimateOutFinished().handle(null);
		}
    }

	// ==================================================================================================================
	// SUPPORT
	
    final private Pane pane = new Pane();
    final private CircularPane circularPane = new CircularPane();
    
    /**
     * Install this CornerMenu in a new the top pane
     */
    private void addToStackPane(StackPane stackPane) {
    	// make sure we have a pane
      	pane.getChildren().add(circularPane);
    	
    	// add the pane to the stack pane
    	this.stackPane = stackPane;
    	stackPane.getChildren().add(pane);
    	
    	// positon
    	setupCircularPane();
    }
    private StackPane stackPane = null;
    
    private void setupCircularPane() {
//		circularPane.setShowDebug(Color.GREEN);
		if (CornerMenu.Orientation.TOP_LEFT.equals(getOrientation())) {
			circularPane.setStartAngle(90.0);
		}
		else if (CornerMenu.Orientation.TOP_RIGHT.equals(getOrientation())) {
			circularPane.setStartAngle(180.0);
		}
		else if (CornerMenu.Orientation.BOTTOM_RIGHT.equals(getOrientation())) {
			circularPane.setStartAngle(270.0);
		}
		else if (CornerMenu.Orientation.BOTTOM_LEFT.equals(getOrientation())) {
			circularPane.setStartAngle(0.0);
		}		
		circularPane.setArc(90.0);
//		circularPane.setAnimationInterpolation(CornerMenu::animateOverTheArc);
		circularPane.setAnimationInterpolation(CornerMenu::animateFromTheOrigin);

		circularPane.setOnAnimateOutFinished( (actionEvent) -> {
			circularPane.setVisible(false);
		});

		// setup the layout 
    	circularPane.layoutXProperty().unbind();
    	circularPane.layoutYProperty().unbind();
		if (CornerMenu.Orientation.TOP_LEFT.equals(getOrientation())) {
			circularPane.setLayoutX(0);
			circularPane.setLayoutY(0);
		}
		else if (CornerMenu.Orientation.TOP_RIGHT.equals(getOrientation())) {
			circularPane.layoutXProperty().bind( pane.widthProperty().subtract(circularPane.widthProperty()));
			circularPane.setLayoutY(0);
		}
		else if (CornerMenu.Orientation.BOTTOM_RIGHT.equals(getOrientation())) {
			circularPane.layoutXProperty().bind( pane.widthProperty().subtract(circularPane.widthProperty()));
			circularPane.layoutYProperty().bind( pane.heightProperty().subtract(circularPane.heightProperty()));
		}
		else if (CornerMenu.Orientation.BOTTOM_LEFT.equals(getOrientation())) {
			circularPane.setLayoutX(0);
			circularPane.layoutYProperty().bind( pane.heightProperty().subtract(circularPane.heightProperty()));
		}
    }


	/* 
	 * This class renders a MenuItem in CircularPane
	 */
	private class CornerMenuNode extends Pane {
		CornerMenuNode (MenuItem menuItem) {
			this.menuItem = menuItem;
			
			getChildren().add(menuItem.getGraphic());

			if (menuItem.getText() != null && menuItem.getText().length() > 0) {
				Tooltip t = new Tooltip(menuItem.getText());
				Tooltip.install(this, t);
			}
			
			setOnMouseClicked( (eventHandler) -> {
				menuItem.getOnAction().handle(null);
			});
		}
		final MenuItem menuItem;
	}
	
	public void removeFromStackPane() {
		stackPane.getChildren().remove(pane);
	}
	
    static public void animateFromTheOrigin(double progress, AnimationLayoutInfo animationLayoutInfo) {
    	// do the calculation
    	CircularPane.animateFromTheOrigin(progress, animationLayoutInfo);
    	
    	// add a fade
    	animationLayoutInfo.node.setOpacity(progress);
    	animationLayoutInfo.node.setRotate(2 * 360 * progress);
    }
	
    static public void animateOverTheArc(double progress, AnimationLayoutInfo animationLayoutInfo) {
    	// do the calculation
    	CircularPane.animateOverTheArc(progress, animationLayoutInfo);
    	
    	// add a fade
    	animationLayoutInfo.node.setOpacity(progress);
    }
}
