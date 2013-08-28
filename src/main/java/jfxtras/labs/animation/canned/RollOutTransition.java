package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a roll out right effect on a node
 * 
 * Port of RollOut from Animate.css http://daneden.me/animate by Dan Eden
 * Which was originally authored by Nick Pettit - https://github.com/nickpettit/glide 
 * 
 * {@literal @}keyframes rollOut {
 *     0% {
 * 		opacity: 1;
 * 		transform: translateX(0px) rotate(0deg);
 * 	}
 *     100% {
 * 		opacity: 0;
 * 		transform: translateX(100%) rotate(120deg);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class RollOutTransition extends CachedTimelineTransition {
    /**
     * Create new RollOutTransition
     * 
     * @param node The node to affect
     */
    public RollOutTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        timeline = new Timeline(

                new KeyFrame(Duration.millis(0),    
                    new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                    new KeyValue(node.translateXProperty(), 0, WEB_EASE),
                    new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),    
                    new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                    new KeyValue(node.translateXProperty(), node.getBoundsInLocal().getWidth(), WEB_EASE),
                    new KeyValue(node.rotateProperty(), 120, WEB_EASE)
                )
            )
            ;
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateX(0); // restore default
        node.setRotate(0); // restore default
    }
}
