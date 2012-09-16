/**
 * Copyright (c) 2011, JFXtras
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
package jfxtras.labs.scene.control;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

/**
 * This class represents a slide to unlock control. This control's metaphor is to prevent a user from
 * accidental input via keyboard, touch, or mouse. The user would need to actively slide the button
 * to the far right to unlock, thus allowing the user to proceed. When using a mouse the control would
 * detect a mouse press, dragged, and release. When using a touch enabled devices the control can detect
 * a touch press, touch dragged, and touch release events.
 *
 * @author cdea
 */
public class SlideLock extends Control {
    private static final String DEFAULT_STYLE_CLASS = "slide-lock";
    public static final double  START_XCOORD = 33;
    public static final double  END_XCOORD = 375;
    public static final double  BUTTON_YCOORD = 49;
    public static final double  PREFERRED_WIDTH = 523.28571;
    public static final double  PREFERRED_HEIGHT = 188;
    private BooleanProperty     backgroundVisible;
    private BooleanProperty     buttonGlareVisible;
    private BooleanProperty     locked;
    private StringProperty      text;
    private DoubleProperty      startX;
    private DoubleProperty      endX;
    private DoubleProperty      textOpacity;
    private Timeline            snapButtonBackAnim;
    private Timeline            unlockAnimation;
    private ObjectProperty<Paint>  buttonArrowBackgroundColor;
    private ObjectProperty<Paint>  buttonColor;

    public SlideLock() {
        this("slide to unlock");
    }

    public SlideLock(String displayText) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        backgroundVisible   = new SimpleBooleanProperty(false);
        buttonGlareVisible  = new SimpleBooleanProperty(true);
        buttonGlareVisible  = new SimpleBooleanProperty(true);
        locked              = new SimpleBooleanProperty(true);
        text                = new SimpleStringProperty(displayText);
        startX              = new SimpleDoubleProperty(START_XCOORD);
        endX                = new SimpleDoubleProperty(START_XCOORD);
        textOpacity         = new SimpleDoubleProperty(1);
        snapButtonBackAnim  = new Timeline();
        unlockAnimation     = new Timeline();

        // simple gray as a default
        buttonArrowBackgroundColor = new SimpleObjectProperty<Paint>();
        // main button area surface
        buttonColor = new SimpleObjectProperty<Paint>();

        init();
    }

    private void init() {
        // snap button back
        final KeyValue BUTTON_X_START = new KeyValue(endX, START_XCOORD);
        final KeyFrame LOCK_KEY_FRAME = new KeyFrame(Duration.millis(150), BUTTON_X_START);

        // make text more opaque
        final KeyValue TEXT_OPACITY_LOCK_KV = new KeyValue(textOpacity, 1);
        final KeyFrame TEXT_OPACITY_LOCK_KF = new KeyFrame(Duration.millis(150), TEXT_OPACITY_LOCK_KV);

        snapButtonBackAnim.getKeyFrames().addAll(LOCK_KEY_FRAME, TEXT_OPACITY_LOCK_KF);

        // move button to the right
        final KeyValue BUTTON_X_END     = new KeyValue(endX, END_XCOORD);
        final KeyFrame UNLOCK_KEY_FRAME = new KeyFrame(Duration.millis(1000), BUTTON_X_END);

        final KeyValue TEXT_OPACITY_UNLOCK_KV = new KeyValue(textOpacity, 0);
        final KeyFrame TEXT_OPACITY_UNLOCK_KF = new KeyFrame(Duration.millis(1000), TEXT_OPACITY_UNLOCK_KV);

        unlockAnimation.getKeyFrames().addAll(UNLOCK_KEY_FRAME, TEXT_OPACITY_UNLOCK_KF);

        // default to a grey background for the button arrow region.
        buttonArrowBackgroundColor.set(LinearGradientBuilder.create()
                .proportional(true)
                .startX(0)
                .startY(1)
                .endX(0)
                .endY(0)
                .stops(new javafx.scene.paint.Stop(0, javafx.scene.paint.Color.web("747474")),
                        new javafx.scene.paint.Stop(1, javafx.scene.paint.Color.web("e8e8e8")))
                .build());
        // default main button color gradient light gray to dark gray
        buttonColor.set(LinearGradientBuilder.create()
                .proportional(true)
                .startX(0)
                .startY(1)
                .endX(0)
                .endY(0)
                .stops(new Stop(0, Color.web("c5c5c5")),
                       new Stop(1, Color.web("f0f0f0")))
                .build());
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/jfxtras/labs/scene/control/slidelock.css").toExternalForm();
    }

    public final boolean isBackgroundVisible() {
        return backgroundVisible.get();
    }

    public final void setBackgroundVisible(final boolean backgroundVisible) {
        this.backgroundVisible.set(backgroundVisible);
    }

    public final BooleanProperty backgroundVisibleProperty() {
        return backgroundVisible ;
    }

    public final boolean isButtonGlareVisible() {
        return buttonGlareVisible.get();
    }

    public final void setButtonGlareVisible(final boolean backgroundVisible) {
        this.buttonGlareVisible.set(backgroundVisible);
    }

    public final BooleanProperty buttonGlareVisibleProperty() {
        return buttonGlareVisible ;
    }

    public final boolean isLocked() {
        return locked.get();
    }

    public final void setLocked(final boolean locked) {
        this.locked.set(locked);
    }

    public final BooleanProperty lockedProperty() {
        return locked;
    }

    public final void autoUnlock() {
        unlockAnimation.play();
    }

    public final String getText() {
        return text.get();
    }

    public final void setText(final String text) {
        this.text.set(text);
    }

    public final StringProperty textProperty() {
        return text;
    }

    public final double getStartX() {
        return startX.get();
    }

    public final void setStartX(final double startX) {
        this.startX.set(startX);
    }

    public final DoubleProperty startXProperty() {
        return startX;
    }

    public final double getEndX() {
        return endX.get();
    }

    public final void setEndX(final double endX) {
        this.endX.set(endX);
    }

    public final DoubleProperty endXProperty() {
        return endX;
    }

    public final double getTextOpacity() {
        return textOpacity.get();
    }

    public final void setTextOpacity(final double opacity) {
        textOpacity.set(opacity);
    }

    public final DoubleProperty textOpacityProperty() {
        return textOpacity;
    }

    public final Paint getButtonArrowBackgroundColor() {
        return buttonArrowBackgroundColor.get();
    }

    public final void setButtonArrowBackgroundColor(final Paint color) {
        this.buttonArrowBackgroundColor.set(color);
    }

    public final ObjectProperty<Paint> buttonArrowBackgroundColorProperty() {
        return buttonArrowBackgroundColor;
    }

    public final Paint getButtonColor() {
        return buttonColor.get();
    }

    public final void setButtonColor(final Paint color) {
        this.buttonColor.set(color);
    }

    public final ObjectProperty<Paint> buttonColorProperty() {
        return buttonColor;
    }

    protected Timeline getSnapButtonBackAnim() {
        return snapButtonBackAnim;
    }

}
