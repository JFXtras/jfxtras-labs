/**
 * CachedTimelineTransition.java
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
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A Transition that uses a Timeline internally and turns SPEED caching on for 
 * the animated node during the animation.
 * 
 * @author Jasper Potts
 */
public class CachedTimelineTransition extends Transition {
    protected static final Interpolator WEB_EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
    protected final Node node;
    protected Timeline timeline;
    private boolean oldCache = false;
    private CacheHint oldCacheHint = CacheHint.DEFAULT;
    private final boolean useCache;

    /**
     * Create new CachedTimelineTransition
     * 
     * @param node The node that is being animated by the timeline
     * @param timeline The timeline for the animation, it should be from 0 to 1 seconds
     */
    public CachedTimelineTransition(final Node node, final Timeline timeline) {
        this(node, timeline, true);
    }
    
    /**
     * Create new CachedTimelineTransition
     * 
     * @param node The node that is being animated by the timeline
     * @param timeline The timeline for the animation, it should be from 0 to 1 seconds
     * @param useCache When true the node is cached as image during the animation
     */
    public CachedTimelineTransition(final Node node, final Timeline timeline, final boolean useCache) {
        this.node = node;
        this.timeline = timeline;
        this.useCache = useCache;
        statusProperty().addListener(new ChangeListener<Status>() {
            @Override public void changed(ObservableValue<? extends Status> ov, Status t, Status newStatus) {
                switch(newStatus) {
                    case RUNNING:
                        starting();
                        break;
                    default: 
                        stopping();
                        break;
                }
            }
        });
    }
    
    /**
     * Called when the animation is starting
     */
    protected void starting() {
        if (useCache) {
            oldCache = node.isCache();
            oldCacheHint = node.getCacheHint();
            node.setCache(true);
            node.setCacheHint(CacheHint.SPEED);
        }
    }
    
    /**
     * Called when the animation is stopping
     */
    protected void stopping() {
        if (useCache) {
            node.setCache(oldCache);
            node.setCacheHint(oldCacheHint);
        }
    }

    @Override protected void interpolate(double d) {
        timeline.playFrom(Duration.seconds(d));
        timeline.stop();
    }

}