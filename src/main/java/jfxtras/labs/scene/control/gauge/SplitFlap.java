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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 23.02.12
 * Time: 09:11
 */
public class SplitFlap extends Control {
    public enum Type {
        NUMERIC(48, 57),
        ALPHANUMERIC(32, 94),
        TIME(48, 53);

        public final int LOWER_BOUND;
        public final int UPPER_BOUND;

        private Type(final int LOWER_BOUND, final int UPPER_BOUND) {
            this.LOWER_BOUND = LOWER_BOUND;
            this.UPPER_BOUND = UPPER_BOUND;
        }
    }
    private static final String   DEFAULT_STYLE_CLASS = "splitflap";
    private ObjectProperty<Color> color;
    private ObjectProperty<Color> upperFlapTopColor;
    private ObjectProperty<Color> upperFlapBottomColor;
    private ObjectProperty<Color> lowerFlapTopColor;
    private ObjectProperty<Color> lowerFlapBottomColor;
    private ObjectProperty<Color> characterColor;
    private ObjectProperty<Color> characterUpperFlapColor;
    private ObjectProperty<Color> characterLowerFlapColor;
    private ObjectProperty<Type>  type;
    private IntegerProperty       character;
    private LongProperty          flipTimeInMs;
    private BooleanProperty       countdownMode;
    private BooleanProperty       soundOn;
    private boolean               keepAspect;


    // ******************** Constructors **************************************
    public SplitFlap() {
        this("0");
    }

    public SplitFlap(final String CHARACTER) {
        color                   = new SimpleObjectProperty<Color>(Color.rgb(56, 56, 56));
        upperFlapTopColor       = new SimpleObjectProperty<Color>(Color.rgb(45, 46, 43));
        upperFlapBottomColor    = new SimpleObjectProperty<Color>(Color.rgb(52, 53, 43));
        lowerFlapTopColor       = new SimpleObjectProperty<Color>(Color.rgb(61, 61, 55));
        lowerFlapBottomColor    = new SimpleObjectProperty<Color>(Color.rgb(43, 40, 34));
        characterColor          = new SimpleObjectProperty<Color>(Color.WHITE);
        characterUpperFlapColor = new SimpleObjectProperty<Color>(Color.rgb(255, 255, 255));
        characterLowerFlapColor = new SimpleObjectProperty<Color>(Color.rgb(244, 242, 232));
        type                    = new SimpleObjectProperty<Type>(Type.NUMERIC);
        character               = new SimpleIntegerProperty(CHARACTER.charAt(0));
        flipTimeInMs            = new SimpleLongProperty(200l);
        countdownMode           = new SimpleBooleanProperty(false);
        soundOn                 = new SimpleBooleanProperty(false);
        keepAspect              = true;

        init();
    }

    private void init() {
        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    public final Color getColor() {
        return color.get();
    }

    public final void setColor(final Color COLOR) {
        lowerFlapTopColor.set(COLOR.brighter().brighter());
        lowerFlapBottomColor.set(COLOR);
        upperFlapTopColor.set(COLOR.darker().darker());
        upperFlapBottomColor.set(COLOR);
        color.set(COLOR);
    }

    public final ObjectProperty<Color> colorProperty() {
        return color;
    }

    public final Color getUpperFlapTopColor() {
        return upperFlapTopColor.get();
    }

    public final void setUpperFlapTopColor(final Color UPPER_FLAP_TOP_COLOR) {
        upperFlapTopColor.set(UPPER_FLAP_TOP_COLOR);
    }

    public final ObjectProperty<Color> upperFlapTopColorProperty() {
        return upperFlapTopColor;
    }

    public final Color getUpperFlapBottomColor() {
        return upperFlapBottomColor.get();
    }

    public final void setUpperFlapBottomColor(final Color UPPER_FLAP_BOTTOM_COLOR) {
        upperFlapBottomColor.set(UPPER_FLAP_BOTTOM_COLOR);
    }

    public final ObjectProperty<Color> upperFlapBottomColorProperty() {
        return upperFlapBottomColor;
    }

    public final Color getLowerFlapTopColor() {
            return lowerFlapTopColor.get();
        }

    public final void setLowerFlapTopColor(final Color LOWER_FLAP_TOP_COLOR) {
        lowerFlapTopColor.set(LOWER_FLAP_TOP_COLOR);
    }

    public final ObjectProperty<Color> lowerFlapTopColorProperty() {
        return lowerFlapTopColor;
    }

    public final Color getLowerFlapBottomColor() {
        return lowerFlapBottomColor.get();
    }

    public final void setLowerFlapBottomColor(final Color LOWER_FLAP_BOTTOM_COLOR) {
        lowerFlapBottomColor.set(LOWER_FLAP_BOTTOM_COLOR);
    }

    public final ObjectProperty<Color> lowerFlapBottomColorProperty() {
        return lowerFlapBottomColor;
    }

    public final Color getCharacterColor() {
        return characterColor.get();
    }

    public final void setCharacterColor(final Color COLOR) {
        characterUpperFlapColor.set(COLOR.darker());
        characterLowerFlapColor.set(COLOR.brighter());
        characterColor.set(COLOR);
    }

    public final ObjectProperty<Color> characterColorProperty() {
        return characterColor;
    }

    public final Color getCharacterUpperFlapColor() {
        return characterUpperFlapColor.get();
    }

    public final void setCharacterUpperFlapColor(final Color CHARACTER_UPPER_FLAP_COLOR) {
        characterUpperFlapColor.set(CHARACTER_UPPER_FLAP_COLOR);
    }

    public final ObjectProperty<Color> characterUpperFlapColorProperty() {
        return characterUpperFlapColor;
    }

    public final Color getCharacterLowerFlapColor() {
        return characterLowerFlapColor.get();
    }

    public final void setCharacterLowerFlapColor(final Color CHARACTER_LOWER_FLAP_COLOR) {
        characterLowerFlapColor.set(CHARACTER_LOWER_FLAP_COLOR);
    }

    public final ObjectProperty<Color> characterLowerFlapColorProperty() {
        return characterLowerFlapColor;
    }

    public final Type getType() {
        return type.get();
    }

    public final void setType(final Type TYPE) {
        type.set(TYPE);
    }

    public final ObjectProperty<Type> typeProperty() {
        return type;
    }

    public final char getCharacter() {
        return (char) character.get();
    }

    public final void setCharacter(final String CHARACTER) {
        if (!CHARACTER.isEmpty() || (CHARACTER.charAt(0) >= type.get().LOWER_BOUND && CHARACTER.charAt(0) <= type.get().UPPER_BOUND)) {
            character.set(CHARACTER.charAt(0));
        } else {
            character.set(32);
        }
    }

    public final IntegerProperty characterProperty() {
        return character;
    }

    public final long getFlipTimeInMs() {
        return flipTimeInMs.get();
    }

    public final void setFlipTimeInMs(final long FLIP_TIME_IN_MS) {
        flipTimeInMs.set(FLIP_TIME_IN_MS < 16l ? 16l : (FLIP_TIME_IN_MS > 3000l ? 3000l : FLIP_TIME_IN_MS));
    }

    public final LongProperty flipTimeInMsProperty() {
        return flipTimeInMs;
    }

    public final boolean isCountdownMode() {
        return countdownMode.get();
    }

    public final void setCountdownMode(final boolean COUNTDOWN_MODE) {
        countdownMode.set(COUNTDOWN_MODE);
    }

    public final BooleanProperty countdownModeProperty() {
        return countdownMode;
    }

    public final boolean isSoundOn() {
        return soundOn.get();
    }

    public final void setSoundOn(final boolean SOUND_ON) {
        soundOn.set(SOUND_ON);
    }

    public final BooleanProperty soundOnProperty() {
        return soundOn;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double prefHeight = WIDTH < (HEIGHT * 0.5925925925925926) ? (WIDTH * 1.6875) : HEIGHT;
        double prefWidth = prefHeight * 0.5925925925925926;

        if (keepAspect) {
            super.setPrefSize(prefWidth, prefHeight);
        } else {
            super.setPrefSize(WIDTH, HEIGHT);
        }
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
