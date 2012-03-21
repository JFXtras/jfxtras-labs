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
 * Date: 21.03.12
 * Time: 11:52
 */
public class StepIndicator extends Control {
    private static final String   DEFAULT_STYLE_CLASS = "step-indicator";
    private ObjectProperty<Color> color;
    private IntegerProperty       noOfCircles;
    private IntegerProperty       score;


    // ******************** Constructors **************************************
    public StepIndicator() {
        this(Color.rgb(138, 205, 250));
    }

    public StepIndicator(final Color COLOR) {
        color       = new SimpleObjectProperty<Color>(COLOR);
        noOfCircles = new SimpleIntegerProperty(5);
        score       = new SimpleIntegerProperty(0);

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

    public final int getNoOfCircles() {
        return noOfCircles.get();
    }

    public final void setNoOfCircles(final int NO_OF_CIRCLES) {
        noOfCircles.set(NO_OF_CIRCLES < 3 ? 3 : (NO_OF_CIRCLES > 10 ? 10 : NO_OF_CIRCLES));
    }

    public final IntegerProperty noOfCirclesProperty() {
        return noOfCircles;
    }

    public final int getScore() {
        return score.get();
    }

    public final void setScore(final int SCORE) {
        score.set(SCORE < 0 ? 0 : (SCORE > noOfCircles.get() ? noOfCircles.get() : SCORE));
    }

    public final IntegerProperty scoreProperty() {
        return score;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH < (HEIGHT) ? (WIDTH) : HEIGHT;
        super.setPrefSize(noOfCircles.get() * SIZE, SIZE);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}
