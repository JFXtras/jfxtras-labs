package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bounce out left big effect on a node
 * 
 * Port of BounceOutLeftBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes bounceOutLeft {
 * 	0% {
 * 		transform: translateX(0);
 * 	}
 * 
 * 	20% {
 * 		opacity: 1;
 * 		transform: translateX(20px);
 * 	}
 * 
 * 	100% {
 * 		opacity: 0;
 * 		transform: translateX(-2000px);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class BounceOutLeftTransition extends CachedTimelineTransition {
    /**
     * Create new BounceOutLeftTransition
     * 
     * @param node The node to affect
     */
    public BounceOutLeftTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double endX = -node.localToScene(0, 0).getX() - node.getBoundsInParent().getWidth();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 20, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), endX, WEB_EASE)
                )
        );
        super.starting();
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateX(0); // restore default
    }
}
