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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * Created by
 * User: hansolo
 * Date: 27.07.12
 * Time: 15:44
 */
public class Scale {
    private DoubleProperty minValue;
    private DoubleProperty maxValue;
    private DoubleProperty range;


    // ******************** Constructors **************************************
    public Scale() {
        this(0, 100);
    }

    public Scale(final double MIN_VALUE, final double MAX_VALUE) {
        minValue = new SimpleDoubleProperty(MIN_VALUE);
        maxValue = new SimpleDoubleProperty(MAX_VALUE);
        range    = new SimpleDoubleProperty();
        range.bind(maxValueProperty().subtract(minValueProperty()));
    }


    // ******************** Methods *******************************************
    public final double getMinValue() {
        return minValue.get();
    }

    public final void setMinValue(final double MIN_VALUE) {
        if (Double.compare(MIN_VALUE, getMaxValue()) >= 0) {
            throw new IllegalArgumentException("minValue must be smaller than maxValue");
        }
        minValue.set(MIN_VALUE);
    }

    public final ReadOnlyDoubleProperty minValueProperty() {
        return minValue;
    }

    public final double getMaxValue() {
        return maxValue.get();
    }

    public final void setMaxValue(final double MAX_VALUE) {
        if (Double.compare(MAX_VALUE,getMinValue()) <= 0) {
            throw new IllegalArgumentException("maxValue must be bigger than minValue");
        }
        maxValue.set(MAX_VALUE);
    }

    public final ReadOnlyDoubleProperty maxValueProperty() {
        return maxValue;
    }

    public final double getRange() {
        return range.get();
    }

    public final ReadOnlyDoubleProperty rangeProperty() {
        return range;
    }

    protected double clamp(final double MIN_VALUE, final double MAX_VALUE, final double VALUE) {
        return VALUE < MIN_VALUE ? MIN_VALUE : (VALUE > MAX_VALUE ? MAX_VALUE : VALUE);
    }
}
