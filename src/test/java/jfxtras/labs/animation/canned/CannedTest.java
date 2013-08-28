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
