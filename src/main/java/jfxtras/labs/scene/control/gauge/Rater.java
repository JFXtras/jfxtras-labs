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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 07.03.12
 * Time: 10:15
 */
public class Rater extends Control {
    private static final String   DEFAULT_STYLE_CLASS = "rater";
    private ObjectProperty<Color> brightColor;
    private ObjectProperty<Color> darkColor;
    private IntegerProperty       noOfStars;
    private IntegerProperty       rating;


    // ******************** Constructors **************************************
    public Rater() {
        this(Color.rgb(255, 231, 81), Color.rgb(255, 126, 18));
    }

    public Rater(final Color BRIGHT_COLOR, final Color DARK_COLOR) {
        brightColor = new SimpleObjectProperty<Color>(BRIGHT_COLOR);
        darkColor   = new SimpleObjectProperty<Color>(DARK_COLOR);
        noOfStars   = new SimpleIntegerProperty(5);
        rating      = new SimpleIntegerProperty(0);

        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    public final Color getDarkColor() {
        return darkColor.get();
    }

    public final void setDarkColor(final Color DARK_COLOR) {
        darkColor.set(DARK_COLOR);
    }

    public final ObjectProperty<Color> darkColorProperty() {
        return darkColor;
    }

    public final Color getBrightColor() {
        return brightColor.get();
    }

    public final void setBrightColor(final Color BRIGHT_COLOR) {
        brightColor.set(BRIGHT_COLOR);
    }

    public final ObjectProperty<Color> brightColorProperty() {
        return brightColor;
    }

    public final int getNoOfStars() {
        return noOfStars.get();
    }

    public final void setNoOfStars(final int NO_OF_STARS) {
        noOfStars.set(NO_OF_STARS < 1 ? 1 : (NO_OF_STARS > 10 ? 10 : NO_OF_STARS));
    }

    public final IntegerProperty noOfStarsProperty() {
        return noOfStars;
    }

    public final int getRating() {
        return rating.get();
    }

    public final void setRating(final int RATING) {
        rating.set(RATING < 0 ? 0 : (RATING > noOfStars.get() ? noOfStars.get() : RATING));
    }

    public final IntegerProperty ratingProperty() {
        return rating;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH < (HEIGHT) ? (WIDTH) : HEIGHT;
        super.setPrefSize(noOfStars.get() * SIZE, SIZE);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
