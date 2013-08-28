package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a rotate in effect on a node
 * 
 * Port of RotateIn from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes rotateIn {
 * 	0% {
 * 		transform-origin: center center;
 * 		transform: rotate(-200deg);
 * 		opacity: 0;
 * 	}=
 * 	100% {
 * 		transform-origin: center center;
 * 		transform: rotate(0);
 * 		opacity: 1;
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class RotateInTransition extends CachedTimelineTransition {
    /**
     * Create new RotateInTransition
     * 
     * @param node The node to affect
     */
    public RotateInTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -200, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    )
                )
                );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
