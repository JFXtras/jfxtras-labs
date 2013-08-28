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
 * Animate a flip in x effect on the given node
 * 
 * Port of FlipInX from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes flipInX {
 *     0% {
 *         transform: perspective(400px) rotateX(90deg);
 *         opacity: 0;
 *     }
 *     40% {
 *         transform: perspective(400px) rotateX(-10deg);
 *     }
 *     70% {
 *         transform: perspective(400px) rotateX(10deg);
 *     }
 *     100% {
 *         transform: perspective(400px) rotateX(0deg);
 *         opacity: 1;
 *     }
 * }
 *
 * @author Jasper Potts
 */
public class FlipInXTransition extends CachedTimelineTransition {
    private Camera oldCamera;
    
    /**
     * Create new FlipInXTransition
     * 
     * @param node The node to affect
     */
    public FlipInXTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.rotateProperty(), -90, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(400), 
                        new KeyValue(node.rotateProperty(), 10, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(700), 
                        new KeyValue(node.rotateProperty(), -10, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE)
                    )
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        node.setRotationAxis(Rotate.X_AXIS);
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
