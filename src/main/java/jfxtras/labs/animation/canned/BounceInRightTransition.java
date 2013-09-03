/**
 * BounceInRightTransition.java
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
 * Animate a bounce in right big effect on a node
 * 
 * Port of BounceInRightBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes bounceInRight {
 * 	0% {
 * 		opacity: 0;
 * 		-webkit-transform: translateX(2000px);
 * 	}
 * 	60% {
 * 		opacity: 1;
 * 		-webkit-transform: translateX(-30px);
 * 	}
 * 	80% {
 * 		-webkit-transform: translateX(10px);
 * 	}
 * 	100% {
 * 		-webkit-transform: translateX(0);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class BounceInRightTransition extends CachedTimelineTransition {
    /**
     * Create new BounceInRightBigTransition
     * 
     * @param node The node to affect
     */
    public BounceInRightTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double startX = node.getScene().getWidth() - node.localToScene(0, 0).getX();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), startX, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), -30, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(node.translateXProperty(), 10, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                )
        );
        super.starting();
    }
}
