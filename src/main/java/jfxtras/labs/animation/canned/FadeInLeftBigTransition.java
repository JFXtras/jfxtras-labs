package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade in left big effect on a node
 * 
 * Port of FadeInLeftBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeInLeftBig {
 * 	0% {
 * 		opacity: 0;
 * 		transform: translateX(-2000px);
 * 	}
 * 	100% {
 * 		opacity: 1;
 * 		transform: translateX(0);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeInLeftBigTransition extends CachedTimelineTransition {
    /**
     * Create new FadeInLeftBigTransition
     * 
     * @param node The node to affect
     */
    public FadeInLeftBigTransition(final Node node) {
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
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                    )
                )
                ;
        super.starting();
    }
}
