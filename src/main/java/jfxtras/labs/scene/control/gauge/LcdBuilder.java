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

/**
 * Created by
 * User: hansolo
 * Date: 14.03.12
 * Time: 15:34
 */
public class LcdBuilder {
    private Lcd lcd;

    public final LcdBuilder create() {
        lcd = new Lcd();
        return this;
    }

    public final LcdBuilder minMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        lcd.setLcdMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final LcdBuilder minMeasuredValueDecimals(final int MIN_MEASURED_VALUE_DECIMALS) {
        lcd.setLcdMinMeasuredValueDecimals(MIN_MEASURED_VALUE_DECIMALS);
        return this;
    }

    public final LcdBuilder maxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        lcd.setLcdMaxMeasuredValueVisible(MAX_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final LcdBuilder maxMeasuredValueDecimals(final int MAX_MEASURED_VALUE_DECIMALS) {
        lcd.setLcdMaxMeasuredValueDecimals(MAX_MEASURED_VALUE_DECIMALS);
        return this;
    }

    public final LcdBuilder formerValueVisible(final boolean FORMER_VALUE_VISIBLE) {
        lcd.setLcdFormerValueVisible(FORMER_VALUE_VISIBLE);
        return this;
    }

    public final LcdBuilder bargraphVisible(final boolean BARGRAPH_VISIBLE) {
        lcd.setBargraphVisible(BARGRAPH_VISIBLE);
        return this;
    }

    public final LcdBuilder title(final String TITLE) {
        lcd.setTitle(TITLE);
        return this;
    }

    public final LcdBuilder trendVisible(final boolean TREND_VISIBLE) {
        lcd.setTrendVisible(TREND_VISIBLE);
        return this;
    }

    public final LcdBuilder thresholdVisible(final boolean THRESHOLD_VISIBLE) {
        lcd.setLcdThresholdVisible(THRESHOLD_VISIBLE);
        return this;
    }

    public final LcdBuilder thresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        lcd.setLcdThresholdBehaviorInverted(THRESHOLD_BEHAVIOR_INVERTED);
        return this;
    }

    public final LcdBuilder numberSystemVisible(final boolean NUMBER_SYSTEM_VISIBLE) {
        lcd.setLcdNumberSystemVisible(NUMBER_SYSTEM_VISIBLE);
        return this;
    }

    public final Lcd build() {
        return lcd;
    }
}
