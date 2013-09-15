package jfxtras.labs.animation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BindableTransitionTrial 
{ /* fails during build
extends Application {

	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Button button = new Button("BindableTransition");
		DropShadow shadow = DropShadowBuilder.create().build();
		button.setEffect(shadow);
		button.setStyle("-fx-font-size: 32px;");
		final Duration duration = Duration.millis(1200);
		BindableTransition transition = new BindableTransition(duration);
		transition.setCycleCount(1000);
		transition.setAutoReverse(true);
		shadow.offsetXProperty().bind(transition.fractionProperty().multiply(32));
		shadow.offsetYProperty().bind(transition.fractionProperty().multiply(32));
		button.translateXProperty().bind(transition.fractionProperty().multiply(-32));
		transition.play();
		
		StackPane pane = new StackPane();
		pane.getChildren().add(button);
		
		Scene myScene = new Scene(pane, 800, 600);
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
*/
}
