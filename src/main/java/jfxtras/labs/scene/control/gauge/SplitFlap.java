/**
 * SplitFlap.java
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
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by
 * User: hansolo
 * Date: 23.02.12
 * Time: 09:11
 */
public class SplitFlap extends Control {
    public static final String[] TIME_0_TO_5  = {"1", "2", "3", "4", "5", "0"};
    public static final String[] TIME_0_TO_9  = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    public static final String[] NUMERIC      = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    public static final String[] ALPHANUMERIC = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                                                 "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                                                 "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                                                 "W", "X", "Y", "Z"};
    public static final String[] EXTENDED     = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                                                 "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                                                 "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                                                 "W", "X", "Y", "Z", "-", "/", ":", ",", ".", ";", "@",
                                                 "#", "+", "?", "!", "%", "$", "=", "<", ">"};

    public enum Sound {
        SOUND1,
        SOUND2,
        SOUND3
    }
    private static final String         DEFAULT_STYLE_CLASS = "split-flap";
    private ObjectProperty<Color>       color;
    private ObjectProperty<Color>       upperFlapTopColor;
    private ObjectProperty<Color>       upperFlapBottomColor;
    private ObjectProperty<Color>       lowerFlapTopColor;
    private ObjectProperty<Color>       lowerFlapBottomColor;
    private ObjectProperty<Color>       textColor;
    private ObjectProperty<Color>       textUpperFlapColor;
    private ObjectProperty<Color>       textLowerFlapColor;
    private DoubleProperty              flapCornerRadius;
    private BooleanProperty             upperFlapHighlightEnabled;
    private BooleanProperty             lowerFlapeHighlightEnabled;
    private BooleanProperty             darkFixtureEnabled;
    private ObjectProperty<String[]>    selection;
    private ArrayList<String>           selectedSet;
    private BooleanProperty             imageMode;
    private int                         currentSelectionIndex;
    private int                         nextSelectionIndex;
    private int                         previousSelectionIndex;
    private BooleanProperty             interactive;
    private StringProperty              text;
    private LongProperty                flipTimeInMs;
    private BooleanProperty             countdownMode;
    private BooleanProperty             soundOn;
    private ObjectProperty<Sound>       sound;
    private BooleanProperty             frameVisible;
    private ObjectProperty<Color>       frameTopColor;
    private ObjectProperty<Color>       frameBottomColor;
    private BooleanProperty             backgroundVisible;
    private boolean                     keepAspect;


    // ******************** Constructors **************************************
    public SplitFlap() {
        this(EXTENDED, " ");
    }

    public SplitFlap(final String[] CUSTOM_SELECTION) {
        this(CUSTOM_SELECTION, CUSTOM_SELECTION[0]);
    }

    public SplitFlap(final String[] CUSTOM_SELECTION, final String TEXT) {
        color                      = new SimpleObjectProperty<Color>(Color.rgb(56, 56, 56));
        upperFlapTopColor          = new SimpleObjectProperty<Color>(Color.rgb(43, 44, 39));
        upperFlapBottomColor       = new SimpleObjectProperty<Color>(Color.rgb(59, 58, 53));
        lowerFlapTopColor          = new SimpleObjectProperty<Color>(Color.rgb(59, 58, 53));
        lowerFlapBottomColor       = new SimpleObjectProperty<Color>(Color.rgb(40, 41, 35));
        textColor                  = new SimpleObjectProperty<Color>(Color.WHITE);
        textUpperFlapColor         = new SimpleObjectProperty<Color>(Color.rgb(255, 255, 255));
        textLowerFlapColor         = new SimpleObjectProperty<Color>(Color.rgb(248, 248, 248));
        flapCornerRadius           = new SimpleDoubleProperty(6);
        upperFlapHighlightEnabled  = new SimpleBooleanProperty(false);
        lowerFlapeHighlightEnabled = new SimpleBooleanProperty(false);
        darkFixtureEnabled         = new SimpleBooleanProperty(true);
        selection                  = new SimpleObjectProperty<String[]>(CUSTOM_SELECTION.length == 0 ? EXTENDED : CUSTOM_SELECTION);
        selectedSet                = new ArrayList<String>(64);
        imageMode                  = new SimpleBooleanProperty(false);
        currentSelectionIndex      = 0;
        nextSelectionIndex         = 1;
        previousSelectionIndex     = selection.get().length - 1;
        interactive                = new SimpleBooleanProperty(false);
        text                       = new SimpleStringProperty(TEXT);
        flipTimeInMs               = new SimpleLongProperty(200l);
        countdownMode              = new SimpleBooleanProperty(false);
        soundOn                    = new SimpleBooleanProperty(false);
        sound                      = new SimpleObjectProperty<Sound>(Sound.SOUND2);
        frameVisible               = new SimpleBooleanProperty(true);
        frameTopColor              = new SimpleObjectProperty<Color>(Color.rgb(52, 53, 43));
        frameBottomColor           = new SimpleObjectProperty<Color>(Color.rgb(61, 61, 55));
        backgroundVisible          = new SimpleBooleanProperty(true);
        keepAspect                 = false;

        getStyleClass().add(DEFAULT_STYLE_CLASS);
        selectedSet.addAll(Arrays.asList(EXTENDED));
    }


    // ******************** Methods *******************************************
    public final Color getColor() {
        return color.get();
    }

    public final void setColor(final Color COLOR) {
        lowerFlapTopColor.set(COLOR.brighter());
        lowerFlapBottomColor.set(COLOR);
        upperFlapTopColor.set(COLOR.darker());
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

    public final Color getTextColor() {
        return textColor.get();
    }

    public final void setTextColor(final Color COLOR) {
        textUpperFlapColor.set(COLOR.darker());
        textLowerFlapColor.set(COLOR.brighter());
        textColor.set(COLOR);
    }

    public final ObjectProperty<Color> textColorProperty() {
        return textColor;
    }

    public final Color getTextUpperFlapColor() {
        return textUpperFlapColor.get();
    }

    public final void setTextUpperFlapColor(final Color TEXT_UPPER_FLAP_COLOR) {
        textUpperFlapColor.set(TEXT_UPPER_FLAP_COLOR);
    }

    public final ObjectProperty<Color> textUpperFlapColorProperty() {
        return textUpperFlapColor;
    }

    public final Color getTextLowerFlapColor() {
        return textLowerFlapColor.get();
    }

    public final void setTextLowerFlapColor(final Color TEXT_LOWER_FLAP_COLOR) {
        textLowerFlapColor.set(TEXT_LOWER_FLAP_COLOR);
    }

    public final ObjectProperty<Color> textLowerFlapColorProperty() {
        return textLowerFlapColor;
    }

    public final double getFlapCornerRadius() {
        return flapCornerRadius.get();
    }

    public final void setFlapCornerRadius(final double FLAP_CORNER_RADIUS) {
        double radius = FLAP_CORNER_RADIUS < 0 ? 0 : (FLAP_CORNER_RADIUS > 20 ? 20 : FLAP_CORNER_RADIUS);
        flapCornerRadius.set(radius);
    }

    public final DoubleProperty flapCornerRadiusProperty() {
        return flapCornerRadius;
    }

    public final boolean isUpperFlapHighlightEnabled() {
        return upperFlapHighlightEnabled.get();
    }

    public final void setUpperFlapHighlightEnabled(final boolean UPPER_FLAP_HIGHLIGHT_ENABLED) {
        upperFlapHighlightEnabled.set(UPPER_FLAP_HIGHLIGHT_ENABLED);
    }

    public final BooleanProperty upperFlapHighlightEnabledProperty() {
        return upperFlapHighlightEnabled;
    }

    public final boolean isLowerFlapHighlightEnabled() {
        return lowerFlapeHighlightEnabled.get();
    }

    public final void setLowerFlapHighlightEnabled(final boolean LOWER_FLAP_HIGHLIGHT_ENABLED) {
        lowerFlapeHighlightEnabled.set(LOWER_FLAP_HIGHLIGHT_ENABLED);
    }

    public final BooleanProperty lowerFlapHighlightEnabledProperty() {
        return lowerFlapeHighlightEnabled;
    }

    public final boolean isDarkFixtureEnabled() {
        return darkFixtureEnabled.get();
    }

    public final void setDarkFixtureEnabled(final boolean DARK_FIXTURE_ENABLED) {
        darkFixtureEnabled.set(DARK_FIXTURE_ENABLED);
    }

    public final BooleanProperty darkFixtureEnabledProperty() {
        return darkFixtureEnabled;
    }

    public final String[] getSelection() {
        return selection.get();
    }

    public final void setSelection(final String[] SELECTION) {
        selectedSet.clear();
        if (SELECTION.length == 0) {
            selectedSet.addAll(Arrays.asList(EXTENDED));
        } else {
            selectedSet.addAll(Arrays.asList(SELECTION));
        }
        selection.set(SELECTION);
    }

    public final ObjectProperty<String[]> selectionProperty() {
        return selection;
    }

    public final ArrayList<String> getSelectedSet() {
        return selectedSet;
    }

    public final boolean isImageMode() {
        return imageMode.get();
    }

    public final void setImageMode(final boolean IMAGE_MODE) {
        imageMode.set(IMAGE_MODE);
    }

    public final BooleanProperty imageModeProperty() {
        return imageMode;
    }

    public final boolean isInteractive() {
        return interactive.get();
    }

    public final void setInteractive(final boolean INTERACTIVE) {
        interactive.set(INTERACTIVE);
    }

    public final BooleanProperty interactiveProperty() {
        return interactive;
    }

    public final String getText() {
        return text.get();
    }

    public final void setText(final String TEXT) {
        if(!TEXT.isEmpty() || selectedSet.contains(TEXT)) {
            text.set(TEXT);
        } else {
            text.set(selectedSet.get(0));
        }
    }

    public final StringProperty textProperty() {
        return text;
    }

    public final String getNextText() {
        return selectedSet.get(nextSelectionIndex);
    }

    public final String getPreviousText() {
        return selectedSet.get(previousSelectionIndex);
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

    public final Sound getSound() {
        return sound.get();
    }

    public final void setSound(final Sound SOUND) {
        sound.set(SOUND);
    }

    public final ObjectProperty<Sound> soundProperty() {
        return sound;
    }

    public final boolean isBackgroundVisible() {
        return backgroundVisible.get();
    }

    public final void setBackgroundVisible(final boolean BACKGROUND_VISIBLE) {
        backgroundVisible.set(BACKGROUND_VISIBLE);
    }

    public final BooleanProperty backgroundVisibleProperty() {
        return backgroundVisible;
    }

    public final boolean isFrameVisible() {
        return frameVisible.get();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        frameVisible.set(FRAME_VISIBLE);
    }

    public final BooleanProperty frameVisibleProperty() {
        return frameVisible;
    }

    public final Color getFrameTopColor() {
        return frameTopColor.get();
    }

    public final void setFrameTopColor(final Color FRAME_TOP_COLOR) {
        frameTopColor.set(FRAME_TOP_COLOR);
    }

    public final ObjectProperty<Color> frameTopColorProperty() {
        return frameTopColor;
    }

    public final Color getFrameBottomColor() {
        return frameBottomColor.get();
    }

    public final void setFrameBottomColor(final Color FRAME_BOTTOM_COLOR) {
        frameBottomColor.set(FRAME_BOTTOM_COLOR);
    }

    public final ObjectProperty<Color> frameBottomColorProperty() {
        return frameBottomColor;
    }

    public final void flipForward() {
        setCountdownMode(false);
        previousSelectionIndex = currentSelectionIndex;
        currentSelectionIndex++;
        if (currentSelectionIndex >= selectedSet.size()) {
            currentSelectionIndex = 0;
        }
        nextSelectionIndex = currentSelectionIndex + 1;
        if (nextSelectionIndex >= selectedSet.size()) {
            nextSelectionIndex = 0;
        }
        setText(selectedSet.get(currentSelectionIndex));
    }

    public final void flipBackward() {
        setCountdownMode(true);
        previousSelectionIndex = currentSelectionIndex;
        currentSelectionIndex--;
        if (currentSelectionIndex < 0) {
            currentSelectionIndex = selectedSet.size() - 1;
        }
        nextSelectionIndex = currentSelectionIndex - 1;
        if (nextSelectionIndex < 0) {
            nextSelectionIndex = selectedSet.size() - 1;
        }
        setText(selectedSet.get(currentSelectionIndex));
    }

    public final boolean isKeepAspect() {
        return keepAspect;
    }

    public final void setKeepAspect(final boolean KEEP_ASPECT) {
        keepAspect = KEEP_ASPECT;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double prefHeight = WIDTH < (HEIGHT * 0.5814977974) ? (WIDTH * 1.7196969697) : HEIGHT;
        double prefWidth = prefHeight * 0.5814977974;

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
