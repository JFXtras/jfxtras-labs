/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 28.12.11
 * Time: 10:04
 */
public class Indicator {
    private DoubleProperty        indicatorValue;
    private ObjectProperty<Color> indicatorColor;
    private StringProperty        indicatorText;
    private BooleanProperty       visible;


    // ******************** Constructors **************************************
    public Indicator(final double VALUE, final Color COLOR) {
        this(VALUE, COLOR, Double.toString(VALUE));
    }

    public Indicator(final double VALUE, final Color COLOR, final String TEXT) {
        this(VALUE, COLOR, TEXT, true);
    }

    public Indicator(final double VALUE, final Color COLOR, final String TEXT, final boolean VISIBLE) {
        indicatorValue = new SimpleDoubleProperty(VALUE);
        indicatorColor = new SimpleObjectProperty<Color>(COLOR);
        indicatorText  = new SimpleStringProperty(TEXT);
        visible        = new SimpleBooleanProperty(VISIBLE);
    }


    // ******************** Methods *******************************************
    public final double getIndicatorValue() {
        return indicatorValue.get();
    }

    public final void setIndicatorValue(final double VALUE) {
        indicatorValue.set(VALUE);
    }

    public final DoubleProperty indicatorValueProperty() {
        return indicatorValue;
    }

    public final Color getIndicatorColor() {
        return indicatorColor.get();
    }

    public final void setIndicatorColor(final Color COLOR) {
        indicatorColor.set(COLOR);
    }

    public final ObjectProperty<Color> indicatorColorProperty() {
        return indicatorColor;
    }

    public final String getIndicatorText() {
        return indicatorText.get();
    }

    public final void setIndicatorText(final String TEXT) {
        indicatorText.set(TEXT);
    }

    public final StringProperty indicatorTextProperty() {
        return indicatorText;
    }

    public final boolean isVisible() {
        return visible.get();
    }

    public final void setVisible(final boolean VISIBLE) {
        visible.set(VISIBLE);
    }

    public final BooleanProperty visibleProperty() {
        return visible;
    }
}
