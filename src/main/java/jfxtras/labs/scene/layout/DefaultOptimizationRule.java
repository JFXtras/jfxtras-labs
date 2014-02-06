/**
 * DefaultOptimizationRule.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.labs.scene.layout;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.transform.Transform;

/**
 * Default optimization rule. It uses the scene area occupied by the pane and
 * <code>min(width,height)</code> to optimize,e.g., to decide whether to
 * visualize the children of the pane (see {@link OptimizableContentPane}).
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class DefaultOptimizationRule implements OptimizationRule {

    private final DoubleProperty minSceneArea = new SimpleDoubleProperty(2000);
    private final DoubleProperty minSceneDimension = new SimpleDoubleProperty(50);
    private final BooleanProperty attachNodesProperty = new SimpleBooleanProperty();

    @Override
    public boolean visible(OptimizableContentPane p, Transform t) {

        Bounds bounds = p.getBoundsInLocal();
        bounds = p.localToScene(bounds);

        boolean visible = getMinSceneArea() <= bounds.getWidth() * bounds.getHeight();

        if (visible) {
            visible = Math.min(bounds.getWidth(), bounds.getHeight()) > getMinSceneDimension();
        }

        return visible;
    }

    @Override
    public boolean attached(OptimizableContentPane p, Transform t) {
        if (isAttachNodes()) {
            return visible(p, t);
        } else {
            return true;
        }
    }

    /**
     *
     * @return minimum scene area property
     */
    public DoubleProperty minSceneAreaProperty() {
        return minSceneArea;
    }

    /**
     * Defines the minimum scene area. If the area occupied by the pane is less
     * than the specified value the children of the pane will be invisible.
     *
     * @param s
     */
    public void setMinSceneArea(double s) {
        minSceneArea.set(s);
    }

    /**
     * Returns the minimum scene area.
     *
     * @return the minimum scene area
     */
    public double getMinSceneArea() {
        return minSceneArea.get();
    }

    /**
     *
     * @return minimum scene dimension property
     */
    public DoubleProperty minSceneDimensionProperty() {
        return minSceneDimension;
    }

    /**
     * Defines the minimum scene dimension. If
     * <code>min(width,height)</code> is less than the specified vaule the
     * children of the pane will be invisible.
     *
     * @param d minimum scene dimension
     */
    public void setMinSceneDimension(double d) {
        minSceneDimension.set(d);
    }

    /**
     * Returns the minimum scene dimension.
     *
     * @return the minimum scene dimension
     */
    public double getMinSceneDimension() {
        return minSceneDimension.get();
    }

    /**
     *
     * @return the attach property
     */
    public BooleanProperty attachNodesProperty() {
        return attachNodesProperty;
    }

    /**
     * Defines whether this optimization shall attach/detach nodes from the
     * scene graph.
     *
     * @param attach the state to set
     */
    public void setAttachNodes(boolean attach) {
        attachNodesProperty().set(attach);
    }

    /**
     * Indicates whether this optimization shall attach/detach nodes from the
     * scene graph.
     *
     * @return <code>true</code> if nodes shall be attached/detached;
     * <code>false</code> otherwise
     */
    public boolean isAttachNodes() {
        return attachNodesProperty().get();
    }
}