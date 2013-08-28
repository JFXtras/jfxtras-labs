package jfxtras.labs.animation.canned;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Animate a hinge effect on a node
 * 
 * Port of Hinge from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes hinge {
 * 	0% { 
 *             transform: rotate(0); 
 *             transform-origin: top left; 
 *             animation-timing-function: ease-in-out; 
 *         }	
 * 	20%, 60% { 
 *             transform: rotate(80deg); 
 *             transform-origin: top left; 
 *             animation-timing-function: ease-in-out; 
 *         }	
 * 	40% { 
 *             transform: rotate(60deg); 
 *             transform-origin: top left; 
 *             animation-timing-function: ease-in-out; 
 *         }	
 * 	80% { 
 *             transform: rotate(60deg) 
 *             translateY(0); 
 *             opacity: 1; 
 *             transform-origin: top left; 
 *             animation-timing-function: ease-in-out; 
 *         }	
 * 	100% { 
 *             transform: translateY(700px);
 *             opacity: 0; 
 *         }
 * }
 * 
 * @author Jasper Potts
 */
public class HingeTransition extends CachedTimelineTransition {
    private Rotate rotate;
    /**
     * Create new HingeTransition
     * 
     * @param node The node to affect
     */
    public HingeTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(2));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        double endY = node.getScene().getHeight() - node.localToScene(0, 0).getY();
        rotate = new Rotate(0,0,0);
        timeline = new Timeline(

                new KeyFrame(Duration.millis(0),    
                    new KeyValue(rotate.angleProperty(), 0, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(200),    
                    new KeyValue(rotate.angleProperty(), 80, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(400),    
                    new KeyValue(rotate.angleProperty(), 60, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(600),    
                    new KeyValue(rotate.angleProperty(), 80, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(800),    
                    new KeyValue(node.opacityProperty(), 1, Interpolator.EASE_BOTH),
                    new KeyValue(node.translateYProperty(), 0, Interpolator.EASE_BOTH),
                    new KeyValue(rotate.angleProperty(), 60, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(1000),    
                    new KeyValue(node.opacityProperty(), 0, Interpolator.EASE_BOTH),
                    new KeyValue(node.translateYProperty(), endY, Interpolator.EASE_BOTH),
                    new KeyValue(rotate.angleProperty(), 60, Interpolator.EASE_BOTH)
                )
            )
            ;
        node.getTransforms().add(rotate);
    }

    @Override protected void stopping() {
        super.stopping();
        node.getTransforms().remove(rotate);
        node.setTranslateY(0);
    }
}
