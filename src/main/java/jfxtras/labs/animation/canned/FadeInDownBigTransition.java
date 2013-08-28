package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade in down big effect on a node
 * 
 * Port of FadeInDownBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeInDownBig {
 * 	0% {
 * 		opacity: 0;
 * 		transform: translateY(-2000px);
 * 	}
 * 	100% {
 * 		opacity: 1;
 * 		transform: translateY(0);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeInDownBigTransition extends CachedTimelineTransition {
    /**
     * Create new FadeInDownBigTransition
     * 
     * @param node The node to affect
     */
    public FadeInDownBigTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double startY = -node.localToScene(0, 0).getY() - node.getBoundsInParent().getHeight();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateYProperty(), startY, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateYProperty(), 0, WEB_EASE)
                )
        );
        super.starting();
    }
}
