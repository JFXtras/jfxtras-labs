/**
 * HingeTransition.java
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
