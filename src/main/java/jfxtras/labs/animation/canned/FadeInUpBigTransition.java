package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade in up big effect on a node
 * 
 * Port of FadeInUpBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeInUpBig {
 * 	0% {
 * 		opacity: 0;
 * 		transform: translateY(2000px);
 * 	}
 * 	100% {
 * 		opacity: 1;
 * 		transform: translateY(0);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeInUpBigTransition extends CachedTimelineTransition {
    /**
     * Create new FadeInUpBigTransition
     * 
     * @param node The node to affect
     */
    public FadeInUpBigTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double startY = node.getScene().getHeight() - node.localToScene(0, 0).getY();
        timeline = new Timeline(

                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateYProperty(), startY, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateYProperty(), 0, WEB_EASE)
                    )
                )
                ;
        super.starting();
    }
}
