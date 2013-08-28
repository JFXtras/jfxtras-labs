package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Animate a rotate out down right effect on a node
 * 
 * Port of RotateOutDownRight from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes rotateOutDownRight {
 * 	0% {
 * 		transform-origin: right bottom;
 * 		transform: rotate(0);
 * 		opacity: 1;
 * 	}
 * 	100% {
 * 		transform-origin: right bottom;
 * 		transform: rotate(-90deg);
 * 		opacity: 0;
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class RotateOutDownRightTransition extends CachedTimelineTransition {
    private Rotate rotate;
    /**
     * Create new RotateOutDownRightTransition
     * 
     * @param node The node to affect
     */
    public RotateOutDownRightTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        rotate = new Rotate(0,
                node.getBoundsInLocal().getWidth(),
                node.getBoundsInLocal().getHeight());
        timeline = new Timeline(

                new KeyFrame(Duration.millis(0),    
                    new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                    new KeyValue(rotate.angleProperty(), 0, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),    
                    new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                    new KeyValue(rotate.angleProperty(), -90, WEB_EASE)
                )
            )
            ;
        node.getTransforms().add(rotate);
    }

    @Override protected void stopping() {
        super.stopping();
        node.getTransforms().remove(rotate);
    }
}
