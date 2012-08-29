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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Builder;

import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 29.08.12
 * Time: 07:42
 * To change this template use File | Settings | File Templates.
 */
public class SectionBuilder implements Builder<Section> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected SectionBuilder() {};


    // ******************** Methods *******************************************
    public static final SectionBuilder create() {
        return new SectionBuilder();
    }

    public final SectionBuilder start(final double START) {
        properties.put("start", new SimpleDoubleProperty(START));
        return this;
    }

    public final SectionBuilder stop(final double STOP) {
        properties.put("stop", new SimpleDoubleProperty(STOP));
        return this;
    }

    public final SectionBuilder color(final Color COLOR) {
        properties.put("color", new SimpleObjectProperty<Color>(COLOR));
        return this;
    }

    public final SectionBuilder highlightColor(final Color HIGHLIGHT_COLOR) {
        properties.put("highlightColor", new SimpleObjectProperty<Color>(HIGHLIGHT_COLOR));
        return this;
    }

    public final SectionBuilder sectionArea(final Shape SECTION_AREA) {
        properties.put("sectionArea", new SimpleObjectProperty<Shape>(SECTION_AREA));
        return this;
    }

    public final SectionBuilder filledArea(final Shape FILLED_AREA) {
        properties.put("filledArea", new SimpleObjectProperty<Shape>(FILLED_AREA));
        return this;
    }

    public final SectionBuilder text(final String TEXT) {
        properties.put("text", new SimpleStringProperty(TEXT));
        return this;
    }

    @Override public final Section build() {
        final Section CONTROL = new Section();
        for (String key : properties.keySet()) {
            if ("start".equals(key)) {
                CONTROL.setStart(((DoubleProperty) properties.get(key)).get());
            } else if ("stop".equals(key)) {
                CONTROL.setStop(((DoubleProperty) properties.get(key)).get());
            } else if ("color".equals(key)) {
                CONTROL.setColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("highlightColor".equals(key)) {
                CONTROL.setHighlightColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("sectionArea".equals(key)) {
                CONTROL.setSectionArea(((ObjectProperty<Shape>) properties.get(key)).get());
            } else if ("filledArea".equals(key)) {
                CONTROL.setFilledArea(((ObjectProperty<Shape>) properties.get(key)).get());
            } else if ("text".equals(key)) {
                CONTROL.setText(((StringProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
