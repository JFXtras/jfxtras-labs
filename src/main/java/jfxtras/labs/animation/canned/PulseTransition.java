package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a pulse effect on the given node
 * 
 * Port of Pulse from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}@keyframes pulse {
 *     0% { transform: scale(1); }	
 * 	50% { transform: scale(1.1); }
 *     100% { transform: scale(1); }
 * }
 *
 * @author Jasper Potts
 */
public class PulseTransition extends CachedTimelineTransition {
    /**
     * Create new PulseTransition
     * 
     * @param node The node to affect
     */
    public PulseTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(500), 
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE)
                    )
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
