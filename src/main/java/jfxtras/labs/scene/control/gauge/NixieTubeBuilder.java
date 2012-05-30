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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 09.03.12
 * Time: 16:26
 */
public class NixieTubeBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final NixieTubeBuilder create() {
        return new NixieTubeBuilder();
    }

    public final NixieTubeBuilder glowColor(final Color GLOW_COLOR) {
        properties.put("glowColor", new SimpleObjectProperty<Color>(GLOW_COLOR));
        return this;
    }

    public final NixieTubeBuilder number(final String NUMBER) {
        properties.put("number", new SimpleStringProperty(NUMBER));
        return this;
    }

    public final NixieTubeBuilder number(final int NUMBER) {
        properties.put("intNumber", new SimpleIntegerProperty(NUMBER));
        return this;
    }

    public final NixieTube build() {
        final NixieTube TUBE = new NixieTube();
        for (String key : properties.keySet()) {
            if ("glowColor".equals(key)) {
                TUBE.setGlowColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("number".equals(key)) {
                TUBE.setNumber(((StringProperty) properties.get(key)).get());
            } else if ("intNumber".equals(key)) {
                TUBE.setNumber(((IntegerProperty) properties.get(key)).get());
            }
        }
        return TUBE;
    }
}
