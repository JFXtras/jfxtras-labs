/**
 * NixieTube.java
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

package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 24.02.12
 * Time: 20:13
 */
public class NixieTube extends Control {
    private static final String   DEFAULT_STYLE_CLASS = "nixie-tube";
    private ObjectProperty<Color> glowColor;
    private IntegerProperty       number;
    private boolean               keepAspect;


    // ******************** Constructors **************************************
    public NixieTube() {
        glowColor  = new SimpleObjectProperty<Color>(Color.color(1.0, 0.4, 0.0, 1.0));
        number     = new SimpleIntegerProperty(-1);
        keepAspect = true;

        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Getters and Setters *******************************
    public final Color getGlowColor() {
        return glowColor.get();
    }

    public final void setGlowColor(final Color GLOW_COLOR) {
        glowColor.set(GLOW_COLOR);
    }

    public final ObjectProperty<Color> glowColorProperty() {
        return glowColor;
    }

    public final int getNumber() {
        return number.get();
    }

    public final void setNumber(final int NUMBER) {
        if (NUMBER > 9 || NUMBER < 0) {
            number.set(0);
        } else {
            number.set(NUMBER);
        }
    }

    public final void setNumber(final String NUMBER_STRING) {
        final int NUMBER = Integer.parseInt(NUMBER_STRING);
        setNumber(NUMBER);
    }

    public final IntegerProperty numberProperty() {
        return number;
    }


    // ******************** Style related *************************************
    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

