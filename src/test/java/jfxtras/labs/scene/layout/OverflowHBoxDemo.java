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

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;

public class OverflowHBoxDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(OverflowHBoxDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField lTextField = new TextField();
		String s = "sdfas dfasdf asdf sad fasrf awer ftar";
		
//		OverflowHBox lOverflowHBox = new OverflowHBox(10.0, 10.0);
		OverflowHBox lOverflowHBox = new OverflowHBox();
		
		for (int i = 1; i < 10; i++) {
			Button node = new Button("much longer label " + i + " " + s.substring(0, new Random().nextInt(s.length())));			
			lOverflowHBox.add(node);
			node.setOnAction(event -> {
				lTextField.requestFocus();
			});
		}
		
		// show
		primaryStage.setScene(new Scene(new VBox(lOverflowHBox
//				, lTextField, new CalendarPicker()
				), 800, 300));
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}
