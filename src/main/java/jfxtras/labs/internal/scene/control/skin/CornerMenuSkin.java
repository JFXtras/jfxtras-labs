/**
 * ListSpinnerSkin.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.skin;

import java.util.ArrayList;

import javafx.collections.ListChangeListener;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.CornerMenu;
import jfxtras.labs.scene.layout.CircularPane;

/**
 * 
 * @author Tom Eugelink
 */
public class CornerMenuSkin extends SkinBase<CornerMenu>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CornerMenuSkin(CornerMenu control)
	{
		super(control);//, new ListSpinnerBehavior<T>(control));
		construct();
	}

	/*
	 * 
	 */
	private void construct()
	{
        // setup component
        createNodes();
        
        // orientation changes
        getSkinnable().orientationProperty().addListener( (observable) -> {
        	setupCircularPane();
        });
        
        // show or hide
        getSkinnable().shownProperty().addListener( (observable) -> {
        	if (getSkinnable().isShown()) {
				getSkinnable().setVisible(true);
        		circularPane.animateIn();
        	}
        	else {
        		circularPane.animateOut();
        		// if no animation, call the event directly
        		if (circularPane.getAnimationInterpolation() == null) {
        			circularPane.getOnAnimateOutFinished().handle(null);
        		}
        	}
        });
        
        // listen to items and modify circular pane's children accordingly
		getSkinnable().getItems().addListener( (ListChangeListener.Change<? extends MenuItem> change) -> {
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
	// DRAW
	
	/**
	 * Construct the nodes. 
	 * Spinner uses a GridPane where the arrows and the node for the value are laid out according to the arrows direction and location.
	 * A place holder in inserted into the GridPane to hold the value node, so the spinner can alternate between editable or readonly mode, without having to recreate the GridPane.  
	 */
	private void createNodes()
	{
		// setup circular pane
		circularPane = new CircularPane();
		setupCircularPane();
		circularPane.setOnAnimateOutFinished( (actionEvent) -> {
			getSkinnable().setVisible(false);
		});
		
		// add the menu items
		for (MenuItem lMenuItem : getSkinnable().getItems()) {
			circularPane.add( new CornerMenuNode(lMenuItem) );
		}
		
		// add to self
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(circularPane);
	}
	private CircularPane circularPane;
	
	private void setupCircularPane() {
		if (CornerMenu.Orientation.TOP_LEFT.equals(getSkinnable().getOrientation())) {
			circularPane.setStartAngle(90.0);
		}
		else if (CornerMenu.Orientation.TOP_RIGHT.equals(getSkinnable().getOrientation())) {
			circularPane.setStartAngle(180.0);
		}
		else if (CornerMenu.Orientation.BOTTOM_RIGHT.equals(getSkinnable().getOrientation())) {
			circularPane.setStartAngle(270.0);
		}
		else if (CornerMenu.Orientation.BOTTOM_LEFT.equals(getSkinnable().getOrientation())) {
			circularPane.setStartAngle(0.0);
		}		
		circularPane.setArc(90.0);
//		circularPane.setAnimationInterpolation(CircularPane::animateOverTheArc); // there is a bug here in arc mode
		circularPane.setAnimationInterpolation(CircularPane::animateFromTheOrigin);
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
}
