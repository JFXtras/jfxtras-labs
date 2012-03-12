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
 * Date: 12.03.12
 * Time: 19:14
 */
public class ClockBuilder {
    private Clock clock;

    public final ClockBuilder create() {
        clock = new Clock();
        return this;
    }

    public final ClockBuilder timeZone(final String TIME_ZONE) {
        clock.setTimeZone(TIME_ZONE);
        return this;
    }

    public final ClockBuilder daylightSavingTime(final boolean DAYLIGHT_SAVING_TIME) {
        clock.setDaylightSavingTime(DAYLIGHT_SAVING_TIME);
        return this;
    }

    public final ClockBuilder secondPointerVisible(final boolean SECOND_POINTER_VISIBLE) {
        clock.setSecondPointerVisible(SECOND_POINTER_VISIBLE);
        return this;
    }

    public final ClockBuilder autoDimEnabled(final boolean AUTO_DIM_ENABLED) {
        clock.setAutoDimEnabled(AUTO_DIM_ENABLED);
        return this;
    }

    public final ClockBuilder running(final boolean RUNNING) {
        clock.setRunning(RUNNING);
        return this;
    }

    public final Clock build() {
        return clock;
    }
}
