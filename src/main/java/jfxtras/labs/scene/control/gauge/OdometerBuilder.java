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

import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 12.04.12
 * Time: 15:38
 */
public class OdometerBuilder {
    private Odometer odometer = new Odometer();

    public static final OdometerBuilder create() {
        return new OdometerBuilder();
    }

    public final OdometerBuilder color(final Color COLOR) {
        odometer.setColor(COLOR);
        return this;
    }

    public final OdometerBuilder decimalColor(final Color DECIMAL_COLOR) {
        odometer.setDecimalColor(DECIMAL_COLOR);
        return this;
    }

    public final OdometerBuilder numberColor(final Color NUMBER_COLOR) {
        odometer.setNumberColor(NUMBER_COLOR);
        return this;
    }

    public final OdometerBuilder noOfDecimals(final int NO_OF_DECIMALS) {
        odometer.setNoOfDecimals(NO_OF_DECIMALS);
        return this;
    }

    public final OdometerBuilder noOfDigits(final int NO_OF_DIGITS) {
        odometer.setNoOfDigits(NO_OF_DIGITS);
        return this;
    }

    public final OdometerBuilder interval(final long INTERVAL) {
        odometer.setInterval(INTERVAL);
        return this;
    }

    public final Odometer build() {
        return odometer != null ? odometer : new Odometer();
    }
}
