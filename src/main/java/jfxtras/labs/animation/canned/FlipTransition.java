/**
 * FlipTransition.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
