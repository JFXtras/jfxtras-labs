/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;


/**
 * Created by
 * User: hansolo
 * Date: 20.02.12
 * Time: 20:52
 */
public class TrafficLight extends Control {
    private static final String DEFAULT_STYLE_CLASS = "trafficlight";
    private BooleanProperty greenBlinking;
    private BooleanProperty greenOn;
    private BooleanProperty redOn;
    private BooleanProperty redBlinking;
    private BooleanProperty yellowOn;
    private BooleanProperty yellowBlinking;
    private BooleanProperty darkBackground;


    // ******************** Constructors **************************************
    public TrafficLight() {
        greenBlinking  = new SimpleBooleanProperty(false);
        greenOn        = new SimpleBooleanProperty(false);
        redOn          = new SimpleBooleanProperty(false);
        redBlinking    = new SimpleBooleanProperty(false);
        yellowOn       = new SimpleBooleanProperty(false);
        yellowBlinking = new SimpleBooleanProperty(false);
        darkBackground = new SimpleBooleanProperty(false);

        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Methods *******************************************
    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double prefHeight = WIDTH < (HEIGHT * 0.4) ? (WIDTH * 2.5) : HEIGHT;
        double prefWidth = prefHeight * 0.4;
        super.setPrefSize(prefWidth, prefHeight);
    }

    @Override public void setMinSize(final double WIDTH, final double HEIGHT) {
        double minHeight = WIDTH < (HEIGHT * 0.4) ? (WIDTH * 2.5) : HEIGHT;
        double minWidth = minHeight * 0.4;
        super.setMinSize(minWidth, minHeight);
    }

    @Override public void setMaxSize(final double WIDTH, final double HEIGHT) {
        double maxHeight = WIDTH < (HEIGHT * 0.4) ? (WIDTH * 2.5) : HEIGHT;
        double maxWidth = maxHeight * 0.4;
        super.setMaxSize(maxWidth, maxHeight);
    }

    public final boolean isRedOn() {
        return redOn.get();
    }

    public final void setRedOn(final boolean REDON) {
        redOn.set(REDON);
    }

    public final BooleanProperty redOnProperty() {
        return redOn;
    }

    public final boolean isRedBlinking() {
        return redBlinking.get();
    }

    public final void setRedBlinking(final boolean REDBLINKING) {
        redBlinking.set(REDBLINKING);
    }

    public final BooleanProperty redBlinkingProperty() {
        return redBlinking;
    }

    public final boolean isYellowOn() {
        return yellowOn.get();
    }

    public final void setYellowOn(final boolean YELLOWON) {
        yellowOn.set(YELLOWON);
    }

    public final BooleanProperty yellowOnProperty() {
        return yellowOn;
    }

    public final boolean isYellowBlinking() {
        return yellowBlinking.get();
    }

    public final void setYellowBlinking(final boolean YELLOWBLINKING) {
        yellowBlinking.set(YELLOWBLINKING);
    }

    public final BooleanProperty yellowBlinkingProperty() {
        return yellowBlinking;
    }

    public final boolean isGreenOn() {
        return greenOn.get();
    }

    public final void setGreenOn(final boolean GREENON) {
        greenOn.set(GREENON);
    }

    public final BooleanProperty greenOnProperty() {
        return greenOn;
    }

    public final boolean isGreenBlinking() {
        return greenBlinking.get();
    }

    public final void setGreenBlinking(final boolean GREENBLINKING) {
        greenBlinking.set(GREENBLINKING);
    }

    public final BooleanProperty greenBlinkingProperty() {
        return greenBlinking;
    }

    public final boolean isDarkBackground() {
        return darkBackground.get();
    }

    public final void setDarkBackground(final boolean DARK_BACKGROUND) {
        darkBackground.set(DARK_BACKGROUND);
    }

    public final BooleanProperty darkBackgroundProperty() {
        return darkBackground;
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

