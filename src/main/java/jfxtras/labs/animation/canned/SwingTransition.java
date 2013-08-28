package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a swing effect on the given node
 * 
 * Port of Swing from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes swing {
 * 	20% { transform: rotate(15deg); }	
 * 	40% { transform: rotate(-10deg); }
 * 	60% { transform: rotate(5deg); }	
 * 	80% { transform: rotate(-5deg); }	
 * 	100% { transform: rotate(0deg); }
 * }
 *
 * @author Jasper Potts
 */
public class SwingTransition extends CachedTimelineTransition {
    /**
     * Create new SwingTransition
     * 
     * @param node The node to affect
     */
    public SwingTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0), new KeyValue(node.rotateProperty(), 0, WEB_EASE)),
                    new KeyFrame(Duration.millis(200), new KeyValue(node.rotateProperty(), 15, WEB_EASE)),
                    new KeyFrame(Duration.millis(400), new KeyValue(node.rotateProperty(), -10, WEB_EASE)),
                    new KeyFrame(Duration.millis(600), new KeyValue(node.rotateProperty(), 5, WEB_EASE)),
                    new KeyFrame(Duration.millis(800), new KeyValue(node.rotateProperty(), -5, WEB_EASE)),
                    new KeyFrame(Duration.millis(1000), new KeyValue(node.rotateProperty(), 0, WEB_EASE))
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
