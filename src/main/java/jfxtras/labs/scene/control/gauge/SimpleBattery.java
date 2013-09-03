/**
 * SimpleBattery.java
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;


/**
 * Created by
 * User: hansolo
 * Date: 30.03.12
 * Time: 09:19
 */
public class SimpleBattery extends Control {
    public enum ChargeCondition {
            EMPTY,
            PARTLY_CHARGED,
            CHARGED
        }
    public enum ChargeIndicator {
        PLUG,
        FLASH
    }
    private static final String             DEFAULT_STYLE_CLASS = "simple-battery";
    private DoubleProperty                  chargingLevel;
    private BooleanProperty                 charging;
    private ObjectProperty<ChargeCondition> chargeCondition;
    private ObjectProperty<ChargeIndicator> chargeIndicator;
    private ObjectProperty<Stop[]>          levelColors;


    // ******************** Constructors **************************************
    public SimpleBattery() {
        chargingLevel   = new SimpleDoubleProperty(0.0);
        charging        = new SimpleBooleanProperty(false);
        chargeCondition = new SimpleObjectProperty<ChargeCondition>(ChargeCondition.EMPTY);
        chargeIndicator = new SimpleObjectProperty<ChargeIndicator>(ChargeIndicator.PLUG);
        levelColors     = new SimpleObjectProperty<Stop[]>(new Stop[]{new Stop(0.0, Color.RED), new Stop(0.55, Color.YELLOW), new Stop(1.0, Color.hsb(102, 1.0, 0.85))});

        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    public final double getChargingLevel() {
            return chargingLevel.get();
        }

    public final void setChargingLevel(final double CHARGING_LEVEL) {
        chargingLevel.set(CHARGING_LEVEL < 0 ? 0 : (CHARGING_LEVEL > 1 ? 1 : CHARGING_LEVEL));
    }

    public final DoubleProperty chargingLevelProperty() {
        return chargingLevel;
    }

    public final boolean isCharging() {
        return charging.get();
    }

    public final void setCharging(final boolean CHARGING) {
        charging.set(CHARGING);
    }

    public final ChargeCondition getChargeCondition() {
        return chargeCondition.get();
    }

    public final void setChargeCondition(final ChargeCondition CHARGE_CONDITION) {
        chargeCondition.set(CHARGE_CONDITION);
    }

    public final ObjectProperty<ChargeCondition> chargeConditionProperty() {
        return chargeCondition;
    }

    public final ChargeIndicator getChargeIndicator() {
        return chargeIndicator.get();
    }

    public final void setChargeIndicator(final ChargeIndicator CHARGE_INDICATOR) {
        chargeIndicator.set(CHARGE_INDICATOR);
    }

    public final ObjectProperty<ChargeIndicator> chargeIndicatorProperty() {
        return chargeIndicator;
    }

    public final BooleanProperty chargingProperty() {
        return charging;
    }

    public final Stop[] getLevelColors() {
        return levelColors.get();
    }

    public final void setLevelColors(final Stop[] LEVEL_COLORS) {
        if (LEVEL_COLORS.length == 0) {
            levelColors.set(new Stop[]{new Stop(0.0, Color.RED), new Stop(0.55, Color.YELLOW), new Stop(1.0, Color.hsb(102, 1.0, 0.85))});
        } else {
            levelColors.set(LEVEL_COLORS);
        }
    }

    public final ObjectProperty<Stop[]> levelColorsProperty() {
        return levelColors;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH <= HEIGHT ? WIDTH : HEIGHT;
        super.setPrefSize(SIZE, SIZE);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
