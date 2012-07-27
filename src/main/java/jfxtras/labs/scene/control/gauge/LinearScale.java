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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Created by
 * User: hansolo
 * Date: 27.07.12
 * Time: 16:15
 */
public class LinearScale extends Scale {
    private BooleanProperty         niceScale;
    private DoubleProperty          niceMinValue;
    private DoubleProperty          niceMaxValue;
    private DoubleProperty          majorTickSpacing;
    private DoubleProperty          minorTickSpacing;
    private IntegerProperty         maxNoOfMajorTicks;
    private IntegerProperty         maxNoOfMinorTicks;
    private DoubleProperty          niceRange;
    private ChangeListener<Number>  listener;


    // ******************** Constructors **************************************
    public LinearScale() {
        super(0, 100);
        init();
    }

    public LinearScale(final double MIN_VALUE, final double MAX_VALUE) {
        super(MIN_VALUE, MAX_VALUE);
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        niceScale                       = new SimpleBooleanProperty(true);
        niceMinValue                    = new SimpleDoubleProperty(getMinValue());
        niceMaxValue                    = new SimpleDoubleProperty(getMaxValue());
        niceRange                       = new SimpleDoubleProperty(getRange());
        maxNoOfMajorTicks               = new SimpleIntegerProperty(10);
        maxNoOfMinorTicks               = new SimpleIntegerProperty(10);
        majorTickSpacing                = new SimpleDoubleProperty(10);
        minorTickSpacing                = new SimpleDoubleProperty(1);
        listener = new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (niceScale.get()) {
                    calculate();
                }
            }
        };
        super.minValueProperty().addListener(listener);
        super.maxValueProperty().addListener(listener);
    }


    // ******************** Methods *******************************************
    public final boolean isNiceScale() {
        return niceScale.get();
    }

    public final void setNiceScale(final boolean NICE_SCALE) {
        niceScale.set(NICE_SCALE);
    }

    public final BooleanProperty niceScaleProperty() {
        return niceScale;
    }

    public final double getNiceMinValue() {
        return niceMinValue.get();
    }

    public final ReadOnlyDoubleProperty niceMinValueProperty() {
        return niceMinValue;
    }

    public final double getNiceMaxValue() {
        return niceMaxValue.get();
    }

    public final ReadOnlyDoubleProperty niceMaxValueProperty() {
        return niceMaxValue;
    }

    public final int getMaxNoOfMajorTicks() {
        return maxNoOfMajorTicks.get();
    }

    public final void setMaxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        maxNoOfMajorTicks.set(MAX_NO_OF_MAJOR_TICKS);
    }

    public final IntegerProperty maxNoOfMajorTicksProperty() {
        return maxNoOfMajorTicks;
    }

    public final int getMaxNoOfMinorTicks() {
        return maxNoOfMinorTicks.get();
    }

    public final void setMaxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        maxNoOfMinorTicks.set(MAX_NO_OF_MINOR_TICKS);
    }

    public final IntegerProperty maxNoOfMinorTicksProperty() {
        return maxNoOfMinorTicks;
    }

    public final double getMajorTickSpacing() {
        return majorTickSpacing.get();
    }

    public final ReadOnlyDoubleProperty majorTickSpacingProperty() {
        return majorTickSpacing;
    }

    public final double getMinorTickSpacing() {
        return minorTickSpacing.get();
    }

    public final ReadOnlyDoubleProperty minorTickSpacingProperty() {
        return minorTickSpacing;
    }

    /**
     * Calculate and update values for major and minor tick spacing and niceScale
     * minimum and maximum values on the axis.
     */
    protected void calculate() {
        if (isNiceScale()) {
            niceRange.set(calcNiceNumber(getRange(), false));
            majorTickSpacing.set((int) (calcNiceNumber(niceRange.get() / (getMaxNoOfMajorTicks() - 1), true)));
            niceMinValue.set(Math.floor(getMinValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            niceMaxValue.set(Math.ceil(getMaxValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            minorTickSpacing.set((int) calcNiceNumber(getMajorTickSpacing() / ((double) getMaxNoOfMajorTicks() - 1), true));
            setMinValue(niceMinValue.get());
            setMaxValue(niceMaxValue.get());
        } else {
            niceRange.set(getMaxValue() - getMinValue());
            niceMinValue.set(getMinValue());
            niceMaxValue.set(getMaxValue());
        }
    }

    /**
     * Returns a "niceScale" number approximately equal to the range.
     * Rounds the number if ROUND == true.
     * Takes the ceiling if ROUND = false.
     *
     * @param RANGE the value range (maxValue - minValue)
     * @param ROUND whether to round the result or ceil
     * @return a "niceScale" number to be used for the value range
     */
    private static double calcNiceNumber(final double RANGE, final boolean ROUND) {
        final double EXPONENT = Math.floor(Math.log10(RANGE));   // exponent of range
        final double FRACTION = RANGE / Math.pow(10, EXPONENT);  // fractional part of range

        // niceScale, rounded fraction
        final double NICE_FRACTION;
        if (ROUND) {
            if (FRACTION < 1.5) {
                NICE_FRACTION = 1;
            } else if (FRACTION < 3) {
                NICE_FRACTION = 2;
            } else if (FRACTION < 7) {
                NICE_FRACTION = 5;
            } else {
                NICE_FRACTION = 10;
            }
        } else {
            if (Double.compare(FRACTION, 1) <= 0) {
                NICE_FRACTION = 1;
            } else if (Double.compare(FRACTION, 2) <= 0) {
                NICE_FRACTION = 2;
            } else if (Double.compare(FRACTION, 5) <= 0) {
                NICE_FRACTION = 5;
            } else {
                NICE_FRACTION = 10;
            }
        }
        return NICE_FRACTION * Math.pow(10, EXPONENT);
    }
}
