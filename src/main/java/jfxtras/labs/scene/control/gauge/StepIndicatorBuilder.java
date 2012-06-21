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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 22.03.12
 * Time: 11:28
 */
public class StepIndicatorBuilder<B extends StepIndicatorBuilder<B>> extends ControlBuilder<B> implements Builder<StepIndicator> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected StepIndicatorBuilder() {};


    // ******************** Methods *******************************************
    public static final StepIndicatorBuilder create() {
        return new StepIndicatorBuilder();
    }

    public final StepIndicatorBuilder noOfSteps(final int NO_OF_STEPS) {
        properties.put("noOfSteps", new SimpleIntegerProperty(NO_OF_STEPS));
        return this;
    }

    public final StepIndicatorBuilder color(final Color COLOR) {
        properties.put("color", new SimpleObjectProperty<Color>(COLOR));
        return this;
    }

    public final StepIndicatorBuilder currentStep(final int CURRENT_STEP) {
        properties.put("currentStep", new SimpleIntegerProperty(CURRENT_STEP));
        return this;
    }

    @Override public final B prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return (B)this;
    }

    @Override public final B prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return (B)this;
    }

    @Override public final StepIndicator build() {
        final StepIndicator CONTROL = new StepIndicator();
        for (String key : properties.keySet()) {
            if ("noOfSteps".equals(key)) {
                CONTROL.setNoOfSteps(((IntegerProperty) properties.get(key)).get());
            } else if ("color".equals(key)) {
                CONTROL.setColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("currentStep".equals(key)) {
                CONTROL.setCurrentStep(((IntegerProperty) properties.get(key)).get());
            }  else if ("prefWidth".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("prefHeight".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
