package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bounce in effect on a node
 * 
 * Port of BounceIn from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes bounceIn {
 * 	0% {
 * 		opacity: 0;
 * 		-webkit-transform: scale(.3);
 * 	}
 * 	50% {
 * 		opacity: 1;
 * 		-webkit-transform: scale(1.05);
 * 	}
 * 	70% {
 * 		-webkit-transform: scale(.9);
 * 	}
 * 	100% {
 * 		-webkit-transform: scale(1);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class BounceInTransition extends CachedTimelineTransition {
    /**
     * Create new BounceInTransition
     * 
     * @param node The node to affect
     */
    public BounceInTransition(final Node node) {
        super(
            node,
                new Timeline(
                        new KeyFrame(Duration.millis(0),
                                new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                                new KeyValue(node.scaleXProperty(), 0.3, WEB_EASE),
                                new KeyValue(node.scaleYProperty(), 0.3, WEB_EASE)
                        ),
                        new KeyFrame(Duration.millis(500),
                                new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                                new KeyValue(node.scaleXProperty(), 1.05, WEB_EASE),
                                new KeyValue(node.scaleYProperty(), 1.05, WEB_EASE)
                        ),
                        new KeyFrame(Duration.millis(700),
                                new KeyValue(node.scaleXProperty(), 0.9, WEB_EASE),
                                new KeyValue(node.scaleYProperty(), 0.9, WEB_EASE)
                        ),
                        new KeyFrame(Duration.millis(1000),
                                new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                                new KeyValue(node.scaleYProperty(), 1, WEB_EASE)
                        )),
                false
        );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
