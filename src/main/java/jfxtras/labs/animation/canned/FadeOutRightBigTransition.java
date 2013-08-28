package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade out right big effect on a node
 * 
 * Port of FadeOutRightBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeOutRightBig {
 * 	0% {
 * 		opacity: 1;
 * 		transform: translateX(0);
 * 	}
 * 	100% {
 * 		opacity: 0;
 * 		transform: translateX(2000px);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeOutRightBigTransition extends CachedTimelineTransition {
    /**
     * Create new FadeOutRightBigTransition
     * 
     * @param node The node to affect
     */
    public FadeOutRightBigTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double endX = node.getScene().getWidth() - node.localToScene(0, 0).getX();
        timeline = new Timeline(

                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), endX, WEB_EASE)
                    )
                )
                ;
        super.starting();
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateX(0); // restore default
    }
    
    
}
