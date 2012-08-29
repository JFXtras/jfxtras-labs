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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Stop;
import javafx.util.Builder;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 23.03.12
 * Time: 15:58
 */
public class BatteryBuilder<B extends BatteryBuilder<B>> extends ControlBuilder<B> implements Builder<Battery> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected BatteryBuilder() {};


    // ******************** Methods *******************************************
    public static final BatteryBuilder create() {
        return new BatteryBuilder();
    }

    public final BatteryBuilder charging(final boolean CHARGING) {
        properties.put("charging", new SimpleBooleanProperty(CHARGING));
        return this;
    }

    public final BatteryBuilder chargeIndicator(final Battery.ChargeIndicator CHARGE_INDICATOR) {
        properties.put("chargeIndicator", new SimpleObjectProperty<Battery.ChargeIndicator>(CHARGE_INDICATOR));
        return this;
    }

    public final BatteryBuilder chargingLevel(final double CHARGING_LEVEL) {
        properties.put("chargingLevel", new SimpleDoubleProperty(CHARGING_LEVEL));
        return this;
    }

    public final BatteryBuilder levelColors(final Stop[] LEVEL_COLORS) {
        properties.put("levelColors", new SimpleObjectProperty<Stop[]>(LEVEL_COLORS));
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

    @Override public final B layoutX(final double LAYOUT_X) {
            properties.put("layoutX", new SimpleDoubleProperty(LAYOUT_X));
            return (B)this;
        }

    @Override public final B layoutY(final double LAYOUT_Y) {
        properties.put("layoutY", new SimpleDoubleProperty(LAYOUT_Y));
        return (B)this;
    }

    @Override public final Battery build() {
        final Battery CONTROL = new Battery();
        for (String key : properties.keySet()) {
            if ("charging".equals(key)) {
                CONTROL.setCharging(((BooleanProperty) properties.get(key)).get());
            } else if ("chargeIndicator".equals(key)) {
                CONTROL.setChargeIndicator(((ObjectProperty<Battery.ChargeIndicator>) properties.get(key)).get());
            } else if ("chargingLevel".equals(key)) {
                CONTROL.setChargingLevel(((DoubleProperty) properties.get(key)).get());
            } else if ("levelColors".equals(key)) {
                CONTROL.setLevelColors(((ObjectProperty<Stop[]>) properties.get(key)).get());
            }  else if ("prefWidth".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("prefHeight".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutX".equals(key)) {
                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutY".equals(key)) {
                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
