package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bounce in down big effect on a node
 *
 * Port of BounceInDownBig from Animate.css http://daneden.me/animate by Dan Eden
 *
 * {@literal @}keyframes  bounceInDown {
 * 	0% {
 * 		opacity: 0;
 * 		-webkit-transform: translateY(-2000px);
 * 	}
 * 	60% {
 * 		opacity: 1;
 * 		-webkit-transform: translateY(30px);
 * 	}
 * 	80% {
 * 		-webkit-transform: translateY(-10px);
 * 	}
 * 	100% {
 * 		-webkit-transform: translateY(0);
 * 	}
 * }
 *
 * @author Jasper Potts
 */
public class BounceInDownTransition extends CachedTimelineTransition {
    /**
     * Create new BounceInDownBigTransition
     *
     * @param node The node to affect
     */
    public BounceInDownTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double startY = -node.localToScene(0, 0).getY() -node.getBoundsInParent().getHeight();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateYProperty(), startY, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateYProperty(), 30, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(node.translateYProperty(), -10, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.translateYProperty(), 0, WEB_EASE)
                )
        );
        super.starting();
    }
}
