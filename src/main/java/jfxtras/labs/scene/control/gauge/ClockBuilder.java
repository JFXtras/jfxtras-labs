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
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 12.03.12
 * Time: 19:14
 */
public class ClockBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final ClockBuilder create() {
        return new ClockBuilder();
    }

    public final ClockBuilder timeZone(final String TIME_ZONE) {
        properties.put("timeZone", new SimpleStringProperty(TIME_ZONE));
        return this;
    }

    public final ClockBuilder daylightSavingTime(final boolean DAYLIGHT_SAVING_TIME) {
        properties.put("daylightSavingTime", new SimpleBooleanProperty(DAYLIGHT_SAVING_TIME));
        return this;
    }

    public final ClockBuilder secondPointerVisible(final boolean SECOND_POINTER_VISIBLE) {
        properties.put("secondPointerVisible", new SimpleBooleanProperty(SECOND_POINTER_VISIBLE));
        return this;
    }

    public final ClockBuilder autoDimEnabled(final boolean AUTO_DIM_ENABLED) {
        properties.put("autoDimEnabled", new SimpleBooleanProperty(AUTO_DIM_ENABLED));
        return this;
    }

    public final ClockBuilder running(final boolean RUNNING) {
        properties.put("running", new SimpleBooleanProperty(RUNNING));
        return this;
    }

    public final Clock build() {
        final Clock CLOCK = new Clock();
        for (String key : properties.keySet()) {
            if ("timeZone".equals(key)) {
                CLOCK.setTimeZone(((StringProperty) properties.get(key)).get());
            } else if ("daylightSavingTime".equals(key)) {
                CLOCK.setDaylightSavingTime(((BooleanProperty) properties.get(key)).get());
            } else if ("secondPointerVisible".equals(key)) {
                CLOCK.setSecondPointerVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("autoDimEnabled".equals(key)) {
                CLOCK.setAutoDimEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("running".equals(key)) {
                CLOCK.setRunning(((BooleanProperty) properties.get(key)).get());
            }
        }
        return CLOCK;
    }
}
