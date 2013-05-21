/*
 * Copyright (c) 2013, JFXtras
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of the <organization> nor the
 *         names of its contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.layout;

import java.util.ArrayList;
import java.util.Collection;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Transform;


/**
 * An optimizable content pane can be used to reduce the number of visible nodes
 * in the scene graph. The default optimization rule uses the number of pixels
 * that are occupied by the pane and its children to determine whether to 
 * visualize or attach the nodes to the scene graph. Nodes that are barely 
 * visible can still cause serious performance issues. In combination with 
 * {@link ScalableContentPane} a user interface can use multiple levels of
 * detail (LOD).
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class OptimizableContentPane extends StackPane {

    private OptimizationRule optimizationRule;
    private Transform transform;
    private boolean optimizing = false;
    private boolean visibility = true;
    private boolean attached = true;
    private Collection<Node> detatched = new ArrayList<>();

    /**
     * Constructor.
     */
    public OptimizableContentPane() {
        this.optimizationRule = new DefaultOptimizationRuleImpl();

        localToSceneTransformProperty().addListener(new ChangeListener<Transform>() {
            @Override
            public void changed(ObservableValue<? extends Transform> ov, Transform oldVal, Transform newVal) {
                transform = newVal;
                updateOptimizationRule();
            }
        });

        boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds t, Bounds t1) {
                updateOptimizationRule();
            }
        });

        visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if (!optimizing) {
                    visibility = newValue;
                }
            }
        });

    }

    private void updateOptimizationRule() {

        if (!visibility) {
            return;
        }
        
        // TODO seems like its called multiple times at once!
        if (optimizing) {
            return;
        }

        optimizing = true;

        if (transform == null) {
            transform = OptimizableContentPane.this.localToSceneTransformProperty().get();
        }

        boolean visible = optimizationRule.visible(this, transform);

        if (isVisible() != visible) {
            setVisible(visible);
        }

        boolean attachedReq = optimizationRule.attached(this, transform);

        if (attached != attachedReq) {
            if (attachedReq) {
                
                getChildren().addAll(detatched);
                detatched.clear();
            } else {
                detatched.addAll(getChildren());
                getChildren().removeAll(detatched);
            }
            attached = attachedReq;
        }

        optimizing = false;
    }

    /**
     * Returns the optimization rule of this pane.
     * @return the optimization rule
     */
    public OptimizationRule getOptimizationRule() {
        return optimizationRule;
    }

    /**
     * Defines the optimization rule.
     * @param optimizationRule the optimization rule to set
     */
    public void setOptimizationRule(OptimizationRule optimizationRule) {
        this.optimizationRule = optimizationRule;
    }
}

