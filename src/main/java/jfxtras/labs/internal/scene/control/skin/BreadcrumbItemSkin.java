/**
 * BreadcrumbItemSkin.java
 *
 * Copyright (c) 2011-2015, JFXtras
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
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import jfxtras.labs.scene.control.BreadcrumbItem;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

/**
 *
 * @author Thierry Wasylczenko
 */
public class BreadcrumbItemSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<BreadcrumbItem, BehaviorBase<BreadcrumbItem>> {

    private class BreadcrumbItemMouseHandler implements EventHandler<MouseEvent> {

        List<Node> nodes = null;

        public List<Node> getNodes() {
            if (this.nodes == null) {
                this.nodes = new ArrayList<Node>();
            }
            return nodes;
        }

        @Override
        public void handle(MouseEvent event) {
            if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
                for (Node node : getNodes()) {
                    node.getStyleClass().add("breadcrumbitem-ui-hover");
                    node.getStyleClass().add("breadcrumbitem-text-hover");
                }
            } else if (event.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                for (Node node : getNodes()) {
                    node.getStyleClass().remove("breadcrumbitem-ui-hover");
                    node.getStyleClass().remove("breadcrumbitem-text-hover");
                }
            }
        }
    }
    
    public BreadcrumbItemSkin(BreadcrumbItem c) {
        super(c, new BehaviorBase<BreadcrumbItem>(c,new ArrayList<KeyBinding>()));

        HBox box = new HBox();
        
        final SVGPath leftSide = new SVGPath();
        if(!c.isFirst()) {
            leftSide.setContent("M0 0 L15 0 l0 30 L0 30 l10 -15 Z");
        } else {
            leftSide.setContent("M0 0 L15 0 l0 30 L0 30 Z");
        }
        leftSide.getStyleClass().add("breadcrumbitem-ui");
        
        final SVGPath rightSide = new SVGPath();
        rightSide.setContent("M0,0 L5,0 15,15 5,30 0,30 Z");
        rightSide.getStyleClass().add("breadcrumbitem-ui");
        rightSide.setLayoutY(10);
        final StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add("breadcrumbitem-ui");
        stackPane.setAlignment(Pos.CENTER);
        
        final HBox stackContent = new HBox(10);
        stackContent.setAlignment(Pos.CENTER);
        
        stackPane.getChildren().add(stackContent);
        
        box.getChildren().add(leftSide);
        box.getChildren().add(stackPane);

        final Label textLabel = new Label();
        if (c != null) {
            if (c.getIcon() != null) {
                ImageView iv = new ImageView(c.getIcon());
                iv.setPreserveRatio(true);
                iv.setFitHeight(20);
                stackContent.getChildren().add(iv);
            }
            
            if(c.getSvgIcon() != null) {
                stackContent.getChildren().add(c.getSvgIcon());
            }
            
            if (c.getText() != null) {
                textLabel.setText(c.getText());
                textLabel.getStyleClass().add("breadcrumbitem-text");
                stackContent.getChildren().add(textLabel);
            }
        }
        box.getChildren().add(rightSide);
        
        leftSide.addEventHandler(MouseEvent.ANY, new BreadcrumbItemMouseHandler(){
            {
                getNodes().add(rightSide);
                getNodes().add(stackPane);
                if(!textLabel.getText().isEmpty()) {
                    getNodes().add(textLabel);
                }
            }
        });
        
        rightSide.addEventHandler(MouseEvent.ANY, new BreadcrumbItemMouseHandler(){
            {
                getNodes().add(leftSide);
                getNodes().add(stackPane);
                if(!textLabel.getText().isEmpty()) {
                    getNodes().add(textLabel);
                }
            }
        });
        
        stackPane.addEventHandler(MouseEvent.ANY, new BreadcrumbItemMouseHandler(){
            {
                getNodes().add(rightSide);
                getNodes().add(leftSide);
                if(!textLabel.getText().isEmpty()) {
                    getNodes().add(textLabel);
                }
            }
        });
        
        box.setPrefHeight(c.getBreadcrumbBar().getPrefHeight());
        getChildren().add(box);
    }
}
