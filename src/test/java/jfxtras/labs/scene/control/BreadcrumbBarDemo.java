/**
 * BreadcrumbBarDemo.java
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

package jfxtras.labs.scene.control;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import jfxtras.labs.util.BreadcrumbBarEventHandler;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Thierry Wasylczenko
 */
public class BreadcrumbBarDemo extends Application {
    
    BreadcrumbBar bar;
    BorderPane bp;
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(demo2(), 800, 100);

        primaryStage.setTitle("BreadcrumbBarFX Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane demo2() {
        bar = new BreadcrumbBar();
//        bar.setPrefHeight(50);
//        bar.setStyle("-fx-background-color: yellow");
        
        bp = new BorderPane();
        
        bar.setOnItemAction(new BreadcrumbBarEventHandler() {

            @Override
            public void handle(Event event) {
                bp.setCenter(((BreadcrumbItem) event.getSource()).getContent());
            }
        });

        Button btn = new Button("Show content 1");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button btn2 = new Button("Show content 2");
                btn2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        AnchorPane content2 = new AnchorPane();
                        content2.setStyle("-fx-background-color: green");
                        content2.setPrefSize(300, 300);

                        SVGPath icon = new SVGPath();
                        icon.setContent("M0,0 L10,0 L10,10 L0,10 Z");
                        icon.setStyle("-fx-fill: green;");
                        bar.addItem("Item 2", icon, content2);
                        bp.setCenter(content2);
                    }
                });

                AnchorPane content1 = new AnchorPane();
                content1.setStyle("-fx-background-color: blue");
                content1.setPrefSize(300, 300);
                content1.getChildren().add(btn2);

                SVGPath icon = new SVGPath();
                icon.setContent("M0,0 L10,0 L10,10 L0,10 Z");
                icon.setStyle("-fx-fill: blue;");
                bar.addItem("Item 1", icon, content1);                
                bp.setCenter(content1);
            }
        });

        AnchorPane homeContent = new AnchorPane();
        homeContent.setStyle("-fx-background-color: red");
        homeContent.setPrefSize(300, 300);
        homeContent.getChildren().add(btn);

        bar.addHome(homeContent);
        bp.setTop(bar);
        bp.setCenter(homeContent);

        return bp;
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
