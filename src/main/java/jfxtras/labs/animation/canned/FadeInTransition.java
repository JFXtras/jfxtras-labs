package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade in effect on a node
 * 
 * Port of FadeIn from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeIn {
 * 	0% {opacity: 0;}	
 * 	100% {opacity: 1;}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeInTransition extends CachedTimelineTransition {
    /**
     * Create new FadeInTransition
     * 
     * @param node The node to affect
     */
    public FadeInTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0),    new KeyValue(node.opacityProperty(), 0, WEB_EASE)),
//                    new KeyFrame(Duration.millis(1000),  new KeyValue(node.opacityProperty(), 1, WEB_EASE))
                    new KeyFrame(Duration.millis(500),  new KeyValue(node.opacityProperty(), 1, WEB_EASE))
                )

            );
        setCycleDuration(Duration.seconds(1));
//        setCycleDuration(Duration.seconds(500));
//        setDelay(Duration.seconds(0.2));
    }
}
