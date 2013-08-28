package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade in right effect on a node
 * 
 * Port of FadeInRight from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeInRight {
 * 	0% {
 * 		opacity: 0;
 * 		transform: translateX(20px);
 * 	}
 * 	100% {
 * 		opacity: 1;
 * 		transform: translateX(0);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeInRightTransition extends CachedTimelineTransition {
    /**
     * Create new FadeInUpTransition
     * 
     * @param node The node to affect
     */
    public FadeInRightTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 20, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                    )
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
