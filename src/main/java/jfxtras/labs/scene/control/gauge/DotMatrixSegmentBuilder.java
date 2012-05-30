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
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by
 * User: hansolo
 * Date: 16.03.12
 * Time: 09:15
 */
public class DotMatrixSegmentBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final DotMatrixSegmentBuilder create() {
        return new DotMatrixSegmentBuilder();
    }

    public final DotMatrixSegmentBuilder character(final String CHARACTER) {
        properties.put("character", new SimpleStringProperty(CHARACTER));
        return this;
    }

    public final DotMatrixSegmentBuilder character(final Character CHARACTER) {
        properties.put("charCharacter", new SimpleObjectProperty<Character>(CHARACTER));
        return this;
    }

    public final DotMatrixSegmentBuilder dotOn(final boolean DOT_ON) {
        properties.put("dotOn", new SimpleBooleanProperty(DOT_ON));
        return this;
    }

    public final DotMatrixSegmentBuilder customSegmentMapping(final Map<Integer, List<DotMatrixSegment.Dot>> CUSTOM_SEGMENT_MAPPING) {
        properties.put("customSegmentMapping", new SimpleObjectProperty<Map<Integer, List<DotMatrixSegment.Dot>>>(CUSTOM_SEGMENT_MAPPING));
        return this;
    }

    public final DotMatrixSegmentBuilder color(final Color COLOR) {
        properties.put("color", new SimpleObjectProperty<Color>(COLOR));
        return this;
    }

    public final DotMatrixSegmentBuilder plainColor(final boolean PLAIN_COLOR) {
        properties.put("plainColor", new SimpleBooleanProperty(PLAIN_COLOR));
        return this;
    }

    public final DotMatrixSegment build() {
        final DotMatrixSegment SEGMENT = new DotMatrixSegment();
        for (String key : properties.keySet()) {
            if ("character".equals(key)) {
                SEGMENT.setCharacter(((StringProperty) properties.get(key)).get());
            } else if("charCharacter".equals(key)) {
                SEGMENT.setCharacter(((ObjectProperty<Character>) properties.get(key)).get());
            } else if ("dotOn".equals(key)) {
                SEGMENT.setDotOn(((BooleanProperty) properties.get(key)).get());
            } else if ("customSegmentMapping".equals(key)) {
                SEGMENT.setCustomDotMapping(((ObjectProperty<Map<Integer, List<DotMatrixSegment.Dot>>>) properties.get(key)).get());
            } else if ("color".equals(key)) {
                SEGMENT.setColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("plainColor".equals(key)) {
                SEGMENT.setPlainColor(((BooleanProperty) properties.get(key)).get());
            }
        }
        return SEGMENT;
    }
}
