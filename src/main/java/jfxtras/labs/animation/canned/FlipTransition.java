package jfxtras.labs.animation.canned;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Animate a flip effect on the given node
 * 
 * Port of Flip from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes flip {
 * 	0% {
 * 		transform: perspective(400px) rotateY(0);
 * 		animation-timing-function: ease-out;
 * 	}
 * 	40% {
 * 		transform: perspective(400px) translateZ(150px) rotateY(170deg);
 * 		animation-timing-function: ease-out;
 * 	}
 * 	50% {
 * 		transform: perspective(400px) translateZ(150px) rotateY(190deg) scale(1);
 * 		animation-timing-function: ease-in;
 * 	}
 * 	80% {
 * 		transform: perspective(400px) rotateY(360deg) scale(.95);
 * 		animation-timing-function: ease-in;
 * 	}
 * 	100% {
 * 		transform: perspective(400px) scale(1);
 * 		animation-timing-function: ease-in;
 * 	}
 * }
 *
 * @author Jasper Potts
 */
public class FlipTransition extends CachedTimelineTransition {
    private final Node node;
    private boolean first = true;
    private Camera oldCamera;
    
    /**
     * Create new FlipTransition
     * 
     * @param node The node to affect
     */
    public FlipTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.rotateProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(node.translateZProperty(), 0, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.millis(400), 
                        new KeyValue(node.translateZProperty(), -150, Interpolator.EASE_OUT),
                        new KeyValue(node.rotateProperty(), -170, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.millis(500), 
                        new KeyValue(node.translateZProperty(), -150, Interpolator.EASE_IN),
                        new KeyValue(node.rotateProperty(), -190, Interpolator.EASE_IN),
                        new KeyValue(node.scaleXProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(node.scaleYProperty(), 1, Interpolator.EASE_IN)
                    ),
                    new KeyFrame(Duration.millis(800), 
                        new KeyValue(node.translateZProperty(), 0, Interpolator.EASE_IN),
                        new KeyValue(node.rotateProperty(), -360, Interpolator.EASE_IN),
                        new KeyValue(node.scaleXProperty(), 0.95, Interpolator.EASE_IN),
                        new KeyValue(node.scaleYProperty(), 0.95, Interpolator.EASE_IN)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.scaleXProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(node.scaleYProperty(), 1, Interpolator.EASE_IN)
                    )
                )

            );
        this.node = node;
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void interpolate(double d) {
        if (first) { // setup
            node.setRotationAxis(Rotate.Y_AXIS);
            oldCamera = node.getScene().getCamera();
            node.getScene().setCamera(new PerspectiveCamera());
            first = false;
        }
        super.interpolate(d);
        if (d == 1) { // restore
            first = true;
            node.setRotate(0);
            node.setRotationAxis(Rotate.Z_AXIS);
            node.getScene().setCamera(oldCamera);
        }
    }
}
