/**
 * Copyright (c) 2011, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
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

import com.javafx.experiments.scenicview.ScenicView;
import java.io.IOException;
import java.io.InputStream;
import jfxtras.labs.scene.control.ListSpinnerIntegerList;
import java.math.BigInteger;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.scene.control.ListSpinner.ArrowDirection;
import jfxtras.labs.scene.control.ListSpinner.ArrowPosition;
import jfxtras.labs.util.StringConverterFactory;



/**
 * 
 * @author Tom Eugelink
 *
 */
public class ListSpinnerDemoApp extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {		
		
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("background");
       
        Label header = new Label("List Spinner");
        header.getStyleClass().add("header");
        header.setLayoutX(44);
        header.setLayoutY(34);
        
        Label normalTitle = new Label("normal");
        normalTitle.getStyleClass().add("item-title");
        normalTitle.setLayoutX(95);
        normalTitle.setLayoutY(145);
        
        Label disabledTitle = new Label("disabled");
        disabledTitle.getStyleClass().add("item-title");
        disabledTitle.setLayoutX(310);
        disabledTitle.setLayoutY(145);
        
        ListSpinner<Integer> listSpinner = new ListSpinner<Integer>(0, 100);
        listSpinner.withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.SPLIT).withEditable(false);
        listSpinner.setLayoutX(87 - 3);
        listSpinner.setLayoutY(168);
        listSpinner.setPrefWidth(60);
        
        ListSpinner<Integer> disabledListSpinner = new ListSpinner<Integer>(0, 100);
        disabledListSpinner.setDisable(true);
        disabledListSpinner.withArrowDirection(ArrowDirection.HORIZONTAL).withArrowPosition(ArrowPosition.SPLIT).withEditable(false);
        disabledListSpinner.setLayoutX(307 - 5);
        disabledListSpinner.setLayoutY(168);
        disabledListSpinner.setPrefWidth(60);
        
        anchorPane.getChildren().addAll(header, normalTitle, disabledTitle, listSpinner, disabledListSpinner);


        Scene scene = new Scene(anchorPane, 444, 228 + 50);
        scene.getStylesheets().addAll(this.getClass().getResource("JMetroDarkTheme.css").toExternalForm());
		
        // create stage
        stage.setTitle("List Spinner Demo App");
        stage.setScene(scene);
        stage.show();
    }
}
