
package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

/**
 *
 * @author cdea
 */
public class SlideLockControlDemo { //extends Application {
//    
//    @Override
//    public void start(Stage primaryStage) {
//        SlideLock slideLock1 = new SlideLock("slide to JavaFX");
//
//        SlideLock slideLock2 = new SlideLock("slide to unlock");
//        slideLock2.setBackgroundVisible(true);
//
//        SlideLock slideLock3 = new SlideLock("slide to power off");
//        slideLock3.setBackgroundVisible(true);
//        slideLock3.setButtonGlareVisible(false);
//        slideLock3.setButtonArrowBackgroundColor(Color.WHITE);
//        slideLock3.setButtonColor(LinearGradientBuilder.create()
//                .proportional(true)
//                .startX(0)
//                .startY(1)
//                .endX(0)
//                .endY(0)
//                .stops(new Stop(0, Color.rgb(250, 111, 108)),
//                        new Stop(.25, Color.rgb(180,30, 31)),
//                        new Stop(.50, Color.rgb(180,30, 31)),
//                        new Stop(1, Color.rgb(250, 111, 108))
//                ).build() // red button);
//        );
//        slideLock3.lockedProperty().addListener(new ChangeListener<Boolean>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!newValue) {
//                    System.out.println("unlocked!!!!");
//                }
//                System.out.println(newValue);
//            }
//        });
//
//        // demo of the builder
//        SlideLock slideLock4 = SlideLockBuilder.create()
//                                    .text("slide to JavaOne")
//                                    .backgroundVisible(true)
//                                    .buttonGlareVisible(true)
//                                    .buttonArrowBackgroundColor(Color.WHITE)
//                                    .buttonColor(LinearGradientBuilder.create()
//                                                        .proportional(true)
//                                                        .startX(0)
//                                                        .startY(1)
//                                                        .endX(0)
//                                                        .endY(0)
//                                                        .stops(new Stop(0, Color.rgb(111,246,3)),
//                                                                new Stop(1, Color.rgb(54,128,5)))
//                                                        .build() // green button);
//                                ).build();
//
//        Group root = new Group();
//        VBox vBox = new VBox();
//        vBox.getChildren().addAll(slideLock1, slideLock2, slideLock3, slideLock4);
//        root.getChildren().add(vBox);
//        Scene scene = new Scene(root, 525, 750);
//        
//        primaryStage.setTitle("Slide Lock control");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    /**
//     * The main() method is ignored in correctly deployed JavaFX application.
//     * main() serves only as fallback in case the application can not be
//     * launched through deployment artifacts, e.g., in IDEs with limited FX
//     * support. NetBeans ignores main().
//     *
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        launch(args);
//    }
}
