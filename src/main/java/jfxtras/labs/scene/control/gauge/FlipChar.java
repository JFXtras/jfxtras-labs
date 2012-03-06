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
public class FlipChar extends Control {
    public enum Type {
        NUMERIC(48, 57),
        ALPHANUMERIC(45, 94),
        TIME(48, 53);

        public final int LOWER_BOUND;
        public final int UPPER_BOUND;

        private Type(final int LOWER_BOUND, final int UPPER_BOUND) {
            this.LOWER_BOUND = LOWER_BOUND;
            this.UPPER_BOUND = UPPER_BOUND;
        }
    }
    private static final String   DEFAULT_STYLE_CLASS = "flipchar";
    private ObjectProperty<Color> color;
    private ObjectProperty<Color> characterColor;
    private ObjectProperty<Type>  type;
    private IntegerProperty       character;
    private LongProperty          flipTime;
    private BooleanProperty       countdownMode;
    private boolean               keepAspect;


    // ******************** Constructors **************************************
    public FlipChar() {
        this("0");
    }

    public FlipChar(final String CHARACTER) {
        color            = new SimpleObjectProperty<Color>(Color.rgb(80, 80, 80));
        characterColor   = new SimpleObjectProperty<Color>(Color.WHITE);
        type             = new SimpleObjectProperty<Type>(Type.NUMERIC);
        character        = new SimpleIntegerProperty(CHARACTER.charAt(0));
        flipTime         = new SimpleLongProperty(200000000l);
        countdownMode    = new SimpleBooleanProperty(false);
        keepAspect       = true;

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
        color.set(COLOR);
    }

    public final ObjectProperty<Color> colorProperty() {
        return color;
    }

    public final Color getCharacterColor() {
        return characterColor.get();
    }

    public final void setCharacterColor(final Color COLOR) {
        characterColor.set(COLOR);
    }

    public final ObjectProperty<Color> characterColorProperty() {
        return characterColor;
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
        if (CHARACTER.charAt(0) == 32 || (CHARACTER.charAt(0) >= type.get().LOWER_BOUND && CHARACTER.charAt(0) <= type.get().UPPER_BOUND)) {
            character.set(CHARACTER.charAt(0));
        } else {
            character.set(32);
        }
    }

    public final IntegerProperty characterProperty() {
        return character;
    }

    public final long getFlipTime() {
        return flipTime.get();
    }

    public final void setFlipTime(final long FLIP_TIME) {
        flipTime.set(FLIP_TIME < 16666666l ? 16666666l : (FLIP_TIME > 3000000000l ? 3000000000l : FLIP_TIME));
    }

    public final LongProperty flipTimeProperty() {
        return flipTime;
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
