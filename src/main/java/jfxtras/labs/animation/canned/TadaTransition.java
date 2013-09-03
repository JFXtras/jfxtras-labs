/**
 * TadaTransition.java
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

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a wobble effect on the given node
 * 
 * Port of Shake from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes tada {
 * 	0% {transform: scale(1);}	
 * 	10%, 20% {transform: scale(0.9) rotate(-3deg);}
 * 	30%, 50%, 70%, 90% {transform: scale(1.1) rotate(3deg);}
 * 	40%, 60%, 80% {transform: scale(1.1) rotate(-3deg);}
 * 	100% {transform: scale(1) rotate(0);}
 * }
 * 
 * @author Jasper Potts
 */
public class TadaTransition extends CachedTimelineTransition {
    /**
     * Create new TadaTransition
     * 
     * @param node The node to affect
     */
    public TadaTransition(final Node node) {
        super(
            node,
            new Timeline(

                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(100),    
                        new KeyValue(node.scaleXProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(200),    
                        new KeyValue(node.scaleXProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 0.9, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(300),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(400),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(500),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(600),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(700),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(800),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), -3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(900),    
                        new KeyValue(node.scaleXProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1.1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 3, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.scaleXProperty(), 1, WEB_EASE),
                        new KeyValue(node.scaleYProperty(), 1, WEB_EASE),
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                    )
                )

            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
