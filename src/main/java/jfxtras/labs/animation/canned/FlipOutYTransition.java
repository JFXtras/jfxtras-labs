/**
 * FlipOutYTransition.java
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
