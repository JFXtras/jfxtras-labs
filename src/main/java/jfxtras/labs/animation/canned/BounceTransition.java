package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bounce effect on the given node
 * 
 * Port of Bounce from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes bounce {
 * 	0%, 20%, 50%, 80%, 100% {transform: translateY(0);}
 * 	40% {transform: translateY(-30px);}
 * 	60% {transform: translateY(-15px);}
 * }
 *
 * @author Jasper Potts
 */
public class BounceTransition extends CachedTimelineTransition {
    /**
     * Create new BounceTransition
     * 
     * @param node The node to affect
     */
    public BounceTransition(final Node node) {
        super(
                node,
                new Timeline(
                        new KeyFrame(Duration.millis(0), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(400), new KeyValue(node.translateYProperty(), -0.30 * node.getBoundsInParent().getHeight(), WEB_EASE)),
                        new KeyFrame(Duration.millis(500), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(600), new KeyValue(node.translateYProperty(), -0.15 * node.getBoundsInParent().getHeight(), WEB_EASE)),
                        new KeyFrame(Duration.millis(800), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(1000), new KeyValue(node.translateYProperty(), 0, WEB_EASE))
                )
        );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
