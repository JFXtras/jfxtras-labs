package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a wobble effect on the given node
 * 
 * Port of Shake from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes tada {
 * 	0% {transform: scale(1);}	
 * 	10%, 20% {transform: scale(0.9) rotate(-3deg);}
 * 	30%, 50%, 70%, 90% {transform: scale(1.1) rotate(3deg);}
 * 	40%, 60%, 80% {transform: scale(1.1) rotate(-3deg);}
 * 	100% {transform: scale(1) rotate(0);}
 * }
 * 
 * @author Jasper Potts
 */
public class TadaTransition extends CachedTimelineTransition {
    /**
     * Create new TadaTransition
     * 
     * @param node The node to affect
     */
    public TadaTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(100),    
                        new KeyValue(node.scaleXProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(200),    
                        new KeyValue(node.scaleXProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(300),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(400),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(500),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(600),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(700),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(800),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(900),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    )
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
