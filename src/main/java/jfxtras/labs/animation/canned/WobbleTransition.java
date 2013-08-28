package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a wobble effect on the given node
 * 
 * Port of Wobble from Animate.css http://daneden.me/animate by Dan Eden
 * which was originally authored by Nick Pettit - https://github.com/nickpettit/glide 
 * 
 * {@literal @}keyframes wobble {
 *   0% { transform: translateX(0%); }
 *   15% { transform: translateX(-25%) rotate(-5deg); }
 *   30% { transform: translateX(20%) rotate(3deg); }
 *   45% { transform: translateX(-15%) rotate(-3deg); }
 *   60% { transform: translateX(10%) rotate(2deg); }
 *   75% { transform: translateX(-5%) rotate(-1deg); }
 *   100% { transform: translateX(0%); }
 * }
 * 
 * @author Jasper Potts
 */
public class WobbleTransition  extends CachedTimelineTransition {
    /**
     * Create new WobbleTransition
     * 
     * @param node The node to affect
     */
    public WobbleTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(150),    
                        new KeyValue(node.translateXProperty(), -0.25*node.getBoundsInParent().getWidth(), WEB_EASE),
                        new KeyValue(node.rotateProperty(), -5, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(300),    
                        new KeyValue(node.translateXProperty(), 0.2*node.getBoundsInParent().getWidth(), WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(450),    
                        new KeyValue(node.translateXProperty(), -0.15*node.getBoundsInParent().getWidth(), WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(600),    
                        new KeyValue(node.translateXProperty(), 0.1*node.getBoundsInParent().getWidth(), WEB_EASE),
                        new KeyValue(node.rotateProperty(), 2, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(750),    
                        new KeyValue(node.translateXProperty(), -0.05*node.getBoundsInParent().getWidth(), WEB_EASE),
                        new KeyValue(node.rotateProperty(), -1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    )
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
