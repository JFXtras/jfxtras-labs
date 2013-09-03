/**
 * CannedTest.java
 *
 * Copyright (c) 2011-2013, JFXtras
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

package jfxtras.labs.animation.canned;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CannedTest extends Application {

	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		final StackPane pane = new StackPane();
		
		ObservableList<String> classes = FXCollections.observableArrayList("BounceInDownTransition",
			"BounceInLeftTransition",
			"BounceInRightTransition",
			"BounceInTransition",
			"BounceInUpTransition",
			"BounceOutDownTransition",
			"BounceOutLeftTransition",
			"BounceOutRightTransition",
			"BounceOutTransition",
			"BounceOutUpTransition",
			"BounceTransition",
			"FadeInDownBigTransition",
			"FadeInDownTransition",
			"FadeInLeftBigTransition",
			"FadeInLeftTransition",
			"FadeInRightBigTransition",
			"FadeInRightTransition",
			"FadeInTransition",
			"FadeInUpBigTransition",
			"FadeInUpTransition",
			"FadeOutDownBigTransition",
			"FadeOutDownTransition",
			"FadeOutLeftBigTransition",
			"FadeOutLeftTransition",
			"FadeOutRightBigTransition",
			"FadeOutRightTransition",
			"FadeOutTransition",
			"FadeOutUpBigTransition",
			"FadeOutUpTransition",
			"FlashTransition",
			"FlipInXTransition",
			"FlipInYTransition",
			"FlipOutXTransition",
			"FlipOutYTransition",
			"FlipTransition",
			"HingeTransition",
			"PulseTransition",
			"RollInTransition",
			"RollOutTransition",
			"RotateInDownLeftTransition",
			"RotateInDownRightTransition",
			"RotateInTransition",
			"RotateInUpLeftTransition",
			"RotateInUpRightTransition",
			"RotateOutDownLeftTransition",
			"RotateOutDownRightTransition",
			"RotateOutTransition",
			"RotateOutUpLeftTransition",
			"RotateOutUpRightTransition",
			"ShakeTransition",
			"SwingTransition",
			"TadaTransition",
			"WobbleTransition");
		final ComboBox<String> comboBox = new ComboBox<>(classes);
		comboBox.onActionProperty().set(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				try
				{
					final Button lButton = new Button("TEST THE ANIMATION");
					
					String clazzName = comboBox.getValue();
					System.out.println(clazzName);
					Class<?> clazz = Class.forName("jfxtras.labs.animation.canned." + clazzName);
					CachedTimelineTransition lCachedTimelineTransition = (CachedTimelineTransition)clazz.getConstructor(Node.class).newInstance(lButton);
					
					comboBox.setVisible(false);
					pane.getChildren().add(lButton);		
					lCachedTimelineTransition.onFinishedProperty().set(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle(ActionEvent arg0)
						{
							pane.getChildren().remove(lButton);		
							comboBox.setVisible(true);
						}
					});
					//lCachedTimelineTransition.setDelay(Duration.seconds(1)); // wait a sec before starting
					lCachedTimelineTransition.play();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		pane.getChildren().add(comboBox);
		
		Scene myScene = new Scene(pane, 800, 600);
		primaryStage.setScene(myScene);
		primaryStage.show();
	}

}
