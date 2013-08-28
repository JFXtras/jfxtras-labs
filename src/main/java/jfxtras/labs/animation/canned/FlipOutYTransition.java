package jfxtras.labs.animation.canned;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Animate a flip out y effect on the given node
 * 
 * Port of FlipOutY from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes flipOutY {
 *     0% {
 *         transform: perspective(400px) rotateY(0deg);
 *         opacity: 1;
 *     }
 * 	100% {
 *         transform: perspective(400px) rotateY(90deg);
 *         opacity: 0;
 *     }
 * }
 *
 * @author Jasper Potts
 */
public class FlipOutYTransition extends CachedTimelineTransition {
    private Camera oldCamera;
    
    /**
     * Create new FlipOutYTransition
     * 
     * @param node The node to affect
     */
    public FlipOutYTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.rotateProperty(), -90, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE)
                    )
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        node.setRotationAxis(Rotate.Y_AXIS);
        oldCamera = node.getScene().getCamera();
        node.getScene().setCamera(new PerspectiveCamera());
    }

    @Override protected void stopping() {
        super.stopping();
        node.setRotate(0);
        node.setRotationAxis(Rotate.Z_AXIS);
        node.getScene().setCamera(oldCamera);
    }
}
