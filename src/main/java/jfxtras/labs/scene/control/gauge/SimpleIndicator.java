/*
 * Copyright (c) 2012, JFXtras
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

package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 06.03.12
 * Time: 13:48
 */
public class SimpleIndicator extends Control {
    private static final String   DEFAULT_STYLE_CLASS = "simple-indicator";
    private ObjectProperty<Color> innerColor;
    private ObjectProperty<Color> outerColor;
    private BooleanProperty       glowVisible;


    // ******************** Constructors **************************************
    public SimpleIndicator() {
        this(Color.rgb(153, 255, 255), Color.rgb(0, 29, 255), true);
    }

    public SimpleIndicator(final Color INNER_COLOR, final Color OUTER_COLOR, final boolean GLOW_VISIBLE) {
        innerColor = new SimpleObjectProperty<Color>(INNER_COLOR);
        outerColor  = new SimpleObjectProperty<Color>(OUTER_COLOR);
        glowVisible = new SimpleBooleanProperty(GLOW_VISIBLE);

        init();
    }

    private void init() {
        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double SIZE = WIDTH < (HEIGHT * 1.0) ? (WIDTH * 1.0) : HEIGHT;
        super.setPrefSize(SIZE, SIZE);
    }

    public final Color getInnerColor() {
        return innerColor.get();
    }

    public final void setInnerColor(final Color INNER_COLOR) {
        innerColor.set(INNER_COLOR);
    }

    public final ObjectProperty<Color> innerColorProperty() {
        return innerColor;
    }

    public final Color getOuterColor() {
        return outerColor.get();
    }

    public final void setOuterColor(final Color OUTER_COLOR) {
        outerColor.set(OUTER_COLOR);
    }

    public final ObjectProperty<Color> outerColorProperty() {
        return outerColor;
    }

    public final boolean isGlowVisible() {
        return glowVisible.get();
    }

    public final void setGlowVisible(final boolean GLOW_VISIBLE) {
        glowVisible.set(GLOW_VISIBLE);
    }

    public final BooleanProperty glowVisibleProperty() {
        return glowVisible;
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

