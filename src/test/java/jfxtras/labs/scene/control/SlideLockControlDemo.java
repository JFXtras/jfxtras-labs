
package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.SlideLock;

/**
 *
 * @author cdea
 */
public class SlideLockControlDemo extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        SlideLock slideLock = new SlideLock("slide to JavaFX");
        slideLock.lockedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    System.out.println("unlocked!!!!");
                }
                System.out.println(newValue);
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(slideLock);
        
        Scene scene = new Scene(root, 600, 250);
        
        primaryStage.setTitle("Slide Lock control");
        primaryStage.setScene(scene);
        primaryStage.show();
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
