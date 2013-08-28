package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bounce in left big effect on a node
 * 
 * Port of BounceInLeftBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes bounceInLeft {
 * 	0% {
 * 		opacity: 0;
 * 		-webkit-transform: translateX(-2000px);
 * 	}
 * 	60% {
 * 		opacity: 1;
 * 		-webkit-transform: translateX(30px);
 * 	}
 * 	80% {
 * 		-webkit-transform: translateX(-10px);
 * 	}
 * 	100% {
 * 		-webkit-transform: translateX(0);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class BounceInLeftTransition extends CachedTimelineTransition {
    /**
     * Create new BounceInLeftBigTransition
     * 
     * @param node The node to affect
     */
    public BounceInLeftTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double startX = -node.localToScene(0, 0).getX() -node.getBoundsInParent().getWidth();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), startX, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 30, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(node.translateXProperty(), -10, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                )
        );
        super.starting();
    }
}
