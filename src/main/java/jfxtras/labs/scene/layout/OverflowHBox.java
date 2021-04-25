/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.layout;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
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

//		// modify the style classes depending on in which collection they are
//		hbox.getChildren().addListener((ListChangeListener<Node>) change -> {
//			while(change.next()) {
//				for (Node n : change.getAddedSubList()) {
//					n.getStyleClass().add(OUTSIDE_CLASS);
//					n.getStyleClass().remove(POPUP_CLASS);
//				}
//				for (Node n : change.getRemoved()) {
//					n.getStyleClass().remove(OUTSIDE_CLASS);
//					n.getStyleClass().add(POPUP_CLASS);
//				}
//			}
//		});
	}
	final private double hboxSpacing;
	final private double vboxSpacing;
	private final String OUTSIDE_CLASS = OverflowHBox.class.getSimpleName() + "_outside";
	private final String POPUP_CLASS = OverflowHBox.class.getSimpleName() + "_popup";
	

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet()
	{
		return NodeUtil.deriveCssFile(this);
	}
	
	// ==========================================================================================================================================================================================================================================
	// PROPERTIES
	
	public void add(Node node) {
//		children.add(node);
		super.getChildren().add(node);
	}
	
	// ==========================================================================================================================================================================================================================================
	// NODE
	
	private void createNodes() {
		dropDown = new ToggleButton("V");
		dropDown.onActionProperty().set(event -> {
			showPopup();
		});
		popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.getContent().add(lVbox);
        popup.onHiddenProperty().addListener((observable) -> dropDown.setSelected(false) ); // TODO: not working
        popup.focusedProperty().addListener((observable) -> {
        	System.out.println("focus " + popup.isFocused());
        });
		super.getChildren().add(dropDown);
	}
	private ToggleButton dropDown;
	private Popup popup;
	
	private void showPopup() {
		System.out.println("click");
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
    	double iconPrefWidth = dropDown.prefWidth(-1);
		double width = getWidth();
    	
//		lVbox.getChildren().clear();
		ObservableList<Node> children = super.getChildren();
		
		boolean lVertical = false;
    	double x = 0.0;
    	double y = 0.0;
    	int lMax = children.size();
    	int lCnt = 0;
		for (Node n : children) {
    		if (n == dropDown) {
    			continue;
    		}
    		
    		double prefWidth = n.prefWidth(-1);
    		double prefHeight = n.prefHeight(-1);
    		
			if (!lVertical && x + prefWidth > width - (lCnt < lMax - 1 ? iconPrefWidth : 0.0)) {
				System.out.println("place dropdown " + x + "," + y);
				lVertical = true;
				dropDown.relocate(x, y);
				dropDown.resize(dropDown.prefWidth(-1), dropDown.prefHeight(-1));
				y += dropDown.prefHeight(-1);
    		}
			
    		if (!lVertical) {
				n.resize(prefWidth, prefHeight);
	    		n.relocate(x, y);
	    		x += prefWidth;
				lCnt++;
    		}
    		if (lVertical) {
				n.resize(prefWidth, prefHeight);
	    		n.relocate(x + iconPrefWidth - prefWidth, y);
	    		y += prefHeight;
//	    		super.getChildren().remove(n);
//    			lVbox.getChildren().add(n);
    		}
    	}
    }
	final javafx.scene.layout.VBox lVbox = new javafx.scene.layout.VBox();


	// ==========================================================================================================================================================================================================================================
	// SUPPORT
}
