/**
 * LinearScale.java
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * Created by
 * User: hansolo
 * Date: 27.07.12
 * Time: 16:15
 */
public class LinearScale extends Scale {
    private BooleanProperty tightScale;
    private BooleanProperty niceScaling;
    private DoubleProperty  niceMinValue;
    private DoubleProperty  niceMaxValue;
    private DoubleProperty  majorTickSpacing;
    private DoubleProperty  minorTickSpacing;
    private IntegerProperty maxNoOfMajorTicks;
    private IntegerProperty maxNoOfMinorTicks;
    private DoubleProperty  niceRange;
    private BooleanProperty largeNumberScale;
    private double          tightScaleOffset;


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
        tightScale        = new SimpleBooleanProperty(false);
        niceScaling       = new SimpleBooleanProperty(true);
        niceMinValue      = new SimpleDoubleProperty(getMinValue());
        niceMaxValue      = new SimpleDoubleProperty(getMaxValue());
        niceRange         = new SimpleDoubleProperty(getRange());
        largeNumberScale  = new SimpleBooleanProperty(false);
        maxNoOfMajorTicks = new SimpleIntegerProperty(10);
        maxNoOfMinorTicks = new SimpleIntegerProperty(10);
        majorTickSpacing  = new SimpleDoubleProperty(10);
        minorTickSpacing  = new SimpleDoubleProperty(1);
        tightScaleOffset  = 0;
    }


    // ******************** Methods *******************************************
    public final boolean isTightScale() {
        return tightScale.get();
    }

    public final void setTightScale(final boolean TIGHT_SCALE) {
        tightScale.set(TIGHT_SCALE);
    }

    public final BooleanProperty tightScaleProperty() {
        return tightScale;
    }

    public final boolean isNiceScaling() {
        return niceScaling.get();
    }

    public final void setNiceScaling(final boolean NICE_SCALING) {
        niceScaling.set(NICE_SCALING);
    }

    public final BooleanProperty niceScalingProperty() {
        return niceScaling;
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

    public final double getNiceRange() {
        return niceRange.get();
    }

    public final ReadOnlyDoubleProperty niceRangeProperty() {
        return niceRange;
    }

    public final boolean isLargeNumberScale() {
        return largeNumberScale.get();
    }

    public final void setLargeNumberScale(final boolean LARGE_NUMBER_SCALE) {
        largeNumberScale.set(LARGE_NUMBER_SCALE);
    }

    public final BooleanProperty largeNumberScaleProperty() {
        return largeNumberScale;
    }

    public final double getMajorTickSpacing() {
        return majorTickSpacing.get();
    }

    public final void setMajorTickSpacing(final double MAJOR_TICKSPACING) {
        majorTickSpacing.set(MAJOR_TICKSPACING);
    }

    public final DoubleProperty majorTickSpacingProperty() {
        return majorTickSpacing;
    }

    public final double getMinorTickSpacing() {
        return minorTickSpacing.get();
    }

    public final void setMinorTickSpacing(final double MINOR_TICK_SPACING) {
        minorTickSpacing.set(MINOR_TICK_SPACING);
    }

    public final DoubleProperty minorTickSpacingProperty() {
        return minorTickSpacing;
    }

    public final double getTightScaleOffset() {
        return tightScaleOffset;
    }

    /**
     * Calculate and update values for major and minor tick spacing and niceScaling
     * minimum and maximum values on the axis.
     */
    protected void calculateLoose() {
        if (isNiceScaling()) {
            niceRange.set(calcNiceNumber(getRange(), false));
            majorTickSpacing.set(calcNiceNumber(niceRange.doubleValue() / (maxNoOfMajorTicks.doubleValue() - 1), true));
            niceMinValue.set(Math.floor(getMinValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            niceMaxValue.set(Math.ceil(getUncorrectedMaxValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            minorTickSpacing.set(calcNiceNumber(majorTickSpacing.doubleValue() / (maxNoOfMinorTicks.intValue() - 1), true));
            niceRange.set(niceMaxValue.doubleValue() - niceMinValue.doubleValue());
            setMinValue(niceMinValue.get());
            setMaxValue(niceMaxValue.get());
        } else {
            niceRange.set(getRange());
            niceMinValue.set(getMinValue());
            niceMaxValue.set(getMaxValue());
        }
    }

    protected void calculateTight() {
        if (isNiceScaling()) {
            niceRange.set(getUncorrectedMaxValue() - getUncorrectedMinValue());
            majorTickSpacing.set(calcNiceNumber(niceRange.doubleValue() / (maxNoOfMajorTicks.doubleValue() - 1), true));
            niceMinValue.set(Math.floor(getUncorrectedMinValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            niceMaxValue.set(Math.ceil(getUncorrectedMaxValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            minorTickSpacing.set(calcNiceNumber(majorTickSpacing.doubleValue() / (maxNoOfMinorTicks.doubleValue() - 1), true));
            tightScaleOffset = (int) (((getUncorrectedMinValue() - niceMinValue.doubleValue()) + 1) / minorTickSpacing.doubleValue());
            niceMinValue.set(getUncorrectedMinValue());
            niceMaxValue.set(getUncorrectedMaxValue());
        } else {
            niceRange.set(getRange());
            niceMinValue.set(getMinValue());
            niceMaxValue.set(getMaxValue());
        }
    }

    /**
     * Returns a "niceScaling" number approximately equal to the range.
     * Rounds the number if ROUND == true.
     * Takes the ceiling if ROUND = false.
     *
     * @param RANGE the value range (maxValue - minValue)
     * @param ROUND whether to round the result or ceil
     * @return a "niceScaling" number to be used for the value range
     */
    private double calcNiceNumber(final double RANGE, final boolean ROUND) {
        final double EXPONENT = Math.floor(Math.log10(RANGE));   // exponent of range
        final double FRACTION = RANGE / Math.pow(10, EXPONENT);  // fractional part of range
        final double MOD      = FRACTION % 0.5;
        double niceFraction;

        // niceScaling
        if (isLargeNumberScale()) {
            if (MOD != 0) {
                niceFraction = FRACTION - MOD;
                niceFraction += 0.5;
            } else {
                niceFraction = FRACTION;
            }
        } else {
            if (ROUND) {
                if (FRACTION < 1.5) {
                    niceFraction = 1;
                } else if (FRACTION < 3) {
                    niceFraction = 2;
                } else if (FRACTION < 7) {
                    niceFraction = 5;
                } else {
                    niceFraction = 10;
                }
            } else {
                if (Double.compare(FRACTION, 1) <= 0) {
                    niceFraction = 1;
                } else if (Double.compare(FRACTION, 2) <= 0) {
                    niceFraction = 2;
                } else if (Double.compare(FRACTION, 5) <= 0) {
                    niceFraction = 5;
                } else {
                    niceFraction = 10;
                }
            }
        }
        return niceFraction * Math.pow(10, EXPONENT);
    }
}
