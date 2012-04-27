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

package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import jfxtras.labs.internal.scene.control.behavior.SplitFlapBehavior;
import jfxtras.labs.scene.control.gauge.SplitFlap;

import java.util.ArrayList;


/**
 * Created by
 * User: hansolo
 * Date: 23.02.12
 * Time: 09:12
 */
public class SplitFlapSkin extends SkinBase<SplitFlap, SplitFlapBehavior> {
    private SplitFlap         control;
    private static double     MIN_FLIP_TIME = 16666666.6666667; // 60 fps
    private final AudioClip   SOUND1 = new AudioClip(getClass().getResource("/jfxtras/labs/scene/control/gauge/flap.mp3").toExternalForm());
    private final AudioClip   SOUND2 = new AudioClip(getClass().getResource("/jfxtras/labs/scene/control/gauge/flap1.mp3").toExternalForm());
    private final AudioClip   SOUND3 = new AudioClip(getClass().getResource("/jfxtras/labs/scene/control/gauge/flap2.mp3").toExternalForm());
    private boolean           isDirty;
    private boolean           initialized;
    private Group             background;
    private Group             fixture;
    private Group             flip;
    private Group             frame;
    private Shape             upper;
    private Text              upperText;
    private Shape             upperNext;
    private Text              upperNextText;
    private Shape             lower;
    private Text              lowerText;
    private Text              lowerNextText;
    private ArrayList<String> selectedSet;
    private int               currentSelectionIndex;
    private int               nextSelectionIndex;
    private int               previousSelectionIndex;
    private Rotate            rotate;
    private Rotate            lowerFlipVert;
    private double            angleStep;
    private double            currentAngle;
    private boolean           flipping;
    private AnimationTimer    timer;


    // ******************** Constructors **************************************
    public SplitFlapSkin(final SplitFlap CONTROL) {
        super(CONTROL, new SplitFlapBehavior(CONTROL));
        control                = CONTROL;
        initialized            = false;
        isDirty                = false;
        background             = new Group();
        fixture                = new Group();
        flip                   = new Group();
        frame                  = new Group();
        upperText              = new Text(control.getText());
        lowerText              = new Text(control.getText());
        upperNextText          = new Text(control.getNextText());
        lowerNextText          = new Text(control.getNextText());
        selectedSet            = new ArrayList<String>(64);
        currentSelectionIndex  = 0;
        nextSelectionIndex     = 1;
        previousSelectionIndex = 52;
        rotate                 = new Rotate();
        angleStep              = 180.0 / ((control.getFlipTimeInMs() * 1000000) / (MIN_FLIP_TIME));
        currentAngle           = 0;
        flipping               = false;
        timer                  = new AnimationTimer() {
            @Override public void handle(long l) {
                if (initialized) {
                    if (!control.isCountdownMode()) {
                        flipForward(angleStep);}
                    else {
                        flipBackward(angleStep);
                    }
                }
            }
        };
        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(132, 227);
        }

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        rotate.setAxis(Rotate.X_AXIS);
        rotate.setPivotY(control.getPrefHeight() * 0.4625550661);

        lowerFlipVert = new Rotate();

        selectedSet.clear();
        selectedSet.addAll(control.getSelectedSet());

        // Register listeners
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.textColorProperty(), "CHARACTER_COLOR");
        registerChangeListener(control.textProperty(), "TEXT");
        registerChangeListener(control.flipTimeInMsProperty(), "FLIP_TIME");
        registerChangeListener(control.frameVisibleProperty(), "FRAME_VISIBILITY");
        registerChangeListener(control.backgroundVisibleProperty(), "BACKGROUND_VISIBILITY");
        registerChangeListener(control.countdownModeProperty(), "COUNTDOWN_MODE");

        control.selectionProperty().addListener(new ChangeListener<String[]>() {
            @Override
            public void changed(ObservableValue<? extends String[]> ov, String[] oldValue, String[] newValue) {
                selectedSet.clear();
                for (String text : newValue) {
                    selectedSet.add(text);
                }
            }
        });

        frame.setVisible(control.isFrameVisible());
        background.setVisible(control.isBackgroundVisible());

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawBackground();
        drawFixture();
        drawFlip();
        drawFrame();
        getChildren().addAll(background,
                             fixture,
                             flip,
                             frame);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "COLOR") {
            paint();
        } else if (PROPERTY == "CHARACTER_COLOR") {
            paint();
        } else if (PROPERTY == "TEXT") {
            if (control.getText() != selectedSet.get(currentSelectionIndex)) {
                timer.stop();
                flipping = true;
                timer.start();
            }
        } else if (PROPERTY == "FLIP_TIME") {
            angleStep = 180.0 / ((control.getFlipTimeInMs() * 1000000) / (MIN_FLIP_TIME));
        } else if (PROPERTY == "FRAME_VISIBILITY") {
            frame.setVisible(control.isFrameVisible());
        } else if (PROPERTY == "BACKGROUND_VISIBILITY") {
            background.setVisible(control.isBackgroundVisible());
        } else if (PROPERTY == "COUNDOWN_MODE") {
            currentAngle = 180;
        } else if (PROPERTY == "SELECTION") {
            selectedSet.clear();
            selectedSet.addAll(control.getSelectedSet());
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final SplitFlap getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 132;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 227;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private void flipForward(final double ANGLE) {
        currentAngle += ANGLE;
        if (Double.compare(currentAngle, 180) >= 0) {
            if (control.isSoundOn()) {
                switch(control.getSound()) {
                    case SOUND1:
                        SOUND1.play();
                        break;
                    case SOUND2:
                        SOUND2.play();
                        break;
                    case SOUND3:
                        SOUND3.play();
                }
            }
            currentAngle = 0;
            upper.getTransforms().clear();
            upperText.getTransforms().clear();
            lowerNextText.getTransforms().clear();
            lowerNextText.setVisible(false);
            lowerFlipVert.setAxis(Rotate.X_AXIS);
            lowerFlipVert.setPivotY(control.getPrefHeight() * 0.4625550661);
            lowerFlipVert.setAngle(180);
            lowerNextText.getTransforms().add(lowerFlipVert);
            upperText.setVisible(true);

            previousSelectionIndex = currentSelectionIndex;
            currentSelectionIndex++;
            if (currentSelectionIndex >= selectedSet.size()) {
                currentSelectionIndex = 0;
            }
            nextSelectionIndex = currentSelectionIndex + 1;
            if (nextSelectionIndex >= selectedSet.size()) {
                nextSelectionIndex = 0;
            }

            if (selectedSet.get(currentSelectionIndex).equals(control.getText())) {
                timer.stop();
                flipping = false;
            }
            upperText.setText(selectedSet.get(currentSelectionIndex));
            lowerText.setText(selectedSet.get(currentSelectionIndex));
            upperNextText.setText(selectedSet.get(nextSelectionIndex));
            lowerNextText.setText(selectedSet.get(nextSelectionIndex));

            if (selectedSet.get(currentSelectionIndex).length() > 1) {
                double textOffset = 0.105 * frame.getLayoutBounds().getHeight();
                upperText.setX(textOffset);
                lowerText.setX(textOffset);
            }
            if (selectedSet.get(nextSelectionIndex).length() > 1) {
                double textOffset = 0.105 * frame.getLayoutBounds().getHeight();
                upperNextText.setX(textOffset);
                lowerNextText.setX(textOffset);
            }

        }
        if (currentAngle > 90) {
            upperText.setVisible(false);
            lowerNextText.setVisible(true);
        }
        if (flipping) {
            rotate.setAngle(ANGLE);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
    }

    private void flipBackward(final double ANGLE) {
        currentAngle += ANGLE;
        if (Double.compare(currentAngle, 180) >= 0) {
            if (control.isSoundOn()) {
                switch(control.getSound()) {
                    case SOUND1:
                        SOUND1.play();
                        break;
                    case SOUND2:
                        SOUND2.play();
                        break;
                    case SOUND3:
                        SOUND3.play();
                }
            }
            currentAngle = 0;
            upper.getTransforms().clear();
            upperText.getTransforms().clear();
            lowerNextText.getTransforms().clear();
            lowerNextText.setVisible(false);
            lowerFlipVert.setAxis(Rotate.X_AXIS);
            lowerFlipVert.setPivotY(control.getPrefHeight() * 0.4625550661);
            lowerFlipVert.setAngle(180);
            lowerNextText.getTransforms().add(lowerFlipVert);
            upperText.setVisible(true);

            previousSelectionIndex = currentSelectionIndex;
            currentSelectionIndex--;
            if (currentSelectionIndex < 0) {
                currentSelectionIndex = selectedSet.size() - 1;
            }
            nextSelectionIndex = currentSelectionIndex - 1;
            if (nextSelectionIndex < 0) {
                nextSelectionIndex = selectedSet.size() - 1;
            }

            if (selectedSet.get(currentSelectionIndex).equals(control.getText())) {
                timer.stop();
                flipping = false;
            }
            upperText.setText(selectedSet.get(currentSelectionIndex));
            lowerText.setText(selectedSet.get(currentSelectionIndex));
            upperNextText.setText(selectedSet.get(nextSelectionIndex));
            lowerNextText.setText(selectedSet.get(nextSelectionIndex));
            if (selectedSet.get(currentSelectionIndex).length() > 1) {
                double textOffset = 0.1057268722 * getPrefHeight();
                upperText.setX(textOffset);
                lowerText.setX(textOffset);
                upperNextText.setX(textOffset);
                lowerNextText.setX(textOffset);
            }
        }
        if (currentAngle > 90) {
            upperText.setVisible(false);
            lowerNextText.setVisible(true);
        }
        if (flipping) {
            rotate.setAngle(ANGLE);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
    }


    // ******************** Mouse event handling ******************************
    private void addMouseEventListener(final Shape FLAP, final int FLAP_INDEX) {
        FLAP.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent EVENT) {
                switch(FLAP_INDEX) {
                    case 1:
                        control.flipForward();
                        break;
                    case -1:
                        control.flipBackward();
                        break;
                }
            }
        });
    }


    // ******************** Drawing related ***********************************
    public final void drawBackground() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        background.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        background.getChildren().add(IBOUNDS);

        final Rectangle INNER_BACKGROUND = new Rectangle(0.0352422907 * HEIGHT, 0.0352422907 * HEIGHT,
                                                         WIDTH - 0.0352422907 * HEIGHT, 0.9207048458 * HEIGHT);
        final Paint INNER_BACKGROUND_FILL = new LinearGradient(0, 0.0352422907 * HEIGHT,
                                                               0, 0.9559471366 * HEIGHT,
                                                               false, CycleMethod.NO_CYCLE,
                                                               new Stop(0.0, Color.BLACK),
                                                               new Stop(1.0, Color.rgb(20, 20, 20)));
        INNER_BACKGROUND.setFill(INNER_BACKGROUND_FILL);
        INNER_BACKGROUND.setStroke(null);

        double offset   = 0.04405286343612335 * HEIGHT;
        double posLeft  = offset;

        final Rectangle RECT3 = new Rectangle(posLeft, 0.8149779736 * HEIGHT,
                                              WIDTH - 2 * offset, 0.1321585903 * HEIGHT);
        RECT3.setArcWidth(0.05286343612334802 * HEIGHT);
        RECT3.setArcHeight(0.05286343612334802 * HEIGHT);
        final Paint RECT3_FILL = new LinearGradient(0.0, 0.8149779736 * HEIGHT,
                                                    0.0, 0.8149779736 * HEIGHT + 0.1321585903 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getLowerFlapTopColor()),
                                                    new Stop(1.0, control.getLowerFlapTopColor()));
        RECT3.setFill(RECT3_FILL);
        RECT3.setStroke(null);

        final Rectangle RECT2 = new Rectangle(posLeft, 0.7973568282 * HEIGHT,
                                              WIDTH - 2 * offset, 0.1321585903 * HEIGHT);
        RECT2.setArcWidth(0.05286343612334802 * HEIGHT);
        RECT2.setArcHeight(0.05286343612334802 * HEIGHT);
        final Paint RECT2_FILL = new LinearGradient(0.0, 0.7973568282 * HEIGHT,
                                                    0.0, 0.7973568282 * HEIGHT + 0.1321585903 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getLowerFlapTopColor()),
                                                    new Stop(1.0, control.getLowerFlapTopColor()));
        RECT2.setFill(RECT2_FILL);
        RECT2.setStroke(null);

        final Rectangle RECT1 = new Rectangle(posLeft, 0.7797356828 * HEIGHT,
                                              WIDTH - 2 * offset, 0.1321585903 * HEIGHT);
        RECT1.setArcWidth(0.05286343612334802 * HEIGHT);
        RECT1.setArcHeight(0.05286343612334802 * HEIGHT);
        final Paint RECT1_FILL = new LinearGradient(0.0, 0.7797356828 * HEIGHT,
                                                    0.0, 0.7797356828 * HEIGHT + 0.1321585903 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getLowerFlapTopColor()),
                                                    new Stop(1.0, control.getLowerFlapTopColor()));
        RECT1.setFill(RECT1_FILL);
        RECT1.setStroke(null);

        final Rectangle RECT0 = new Rectangle(posLeft, 0.7621145374 * HEIGHT,
                                              WIDTH - 2 * offset, 0.1321585903 * HEIGHT);
        RECT0.setArcWidth(0.05286343612334802 * HEIGHT);
        RECT0.setArcHeight(0.05286343612334802 * HEIGHT);
        final Paint RECT0_FILL = new LinearGradient(0.0, 0.7621145374 * HEIGHT,
                                                    0.0, 0.7621145374 * HEIGHT + 0.1321585903 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getLowerFlapTopColor()),
                                                    new Stop(1.0, control.getLowerFlapTopColor()));
        RECT0.setFill(RECT0_FILL);
        RECT0.setStroke(null);

        final InnerShadow LOWER_INNER_SHADOW = new InnerShadow();
        LOWER_INNER_SHADOW.setWidth(0.075 * RECT3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setHeight(0.075 * RECT3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setOffsetX(0.0);
        LOWER_INNER_SHADOW.setOffsetY(0.0);
        LOWER_INNER_SHADOW.setRadius(0.075 * RECT3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setColor(Color.BLACK);
        LOWER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow LOWER_LIGHT_EFFECT = new InnerShadow();
        LOWER_LIGHT_EFFECT.setWidth(0.05 * RECT3.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setHeight(0.05 * RECT3.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setOffsetX(0);
        LOWER_LIGHT_EFFECT.setOffsetY(0.01 * SIZE);
        LOWER_LIGHT_EFFECT.setRadius(0.05 * RECT3.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setColor(Color.WHITE);
        LOWER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        LOWER_LIGHT_EFFECT.inputProperty().set(LOWER_INNER_SHADOW);

        RECT3.setEffect(LOWER_INNER_SHADOW);
        RECT2.setEffect(LOWER_INNER_SHADOW);
        RECT1.setEffect(LOWER_INNER_SHADOW);
        RECT0.setEffect(LOWER_INNER_SHADOW);

        background.getChildren().addAll(INNER_BACKGROUND,
                                        RECT3,
                                        RECT2,
                                        RECT1,
                                        RECT0);

        background.setCache(true);
    }

    public void drawFixture() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        fixture.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        fixture.getChildren().add(IBOUNDS);

        double frameWidth    = 0.0396475771 * HEIGHT;
        double frameHeight   = 0.13656387665198239 * HEIGHT;
        double mainWidth     = 0.0308370044 * HEIGHT;
        double mainHeight    = 0.1277533039647577 * HEIGHT;
        double offset        = 0.04405286343612335 * HEIGHT;
        double posLeftFrame  = offset;
        double posLeftMain   = 0.0484581498 * HEIGHT;
        double posRightFrame = WIDTH - offset - frameWidth;
        double posRightMain  = WIDTH - posLeftMain - mainWidth;

        final Rectangle RIGHT_FRAME = new Rectangle(posRightFrame, 0.3920704845814978 * HEIGHT,
                                                    frameWidth, frameHeight);
        final Paint RIGHT_FRAME_FILL = new LinearGradient(0, 0.3920704845814978 * HEIGHT,
                                                         0, 0.5286343612334802 * HEIGHT,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                         new Stop(0.18, Color.color(0.6117647059, 0.6117647059, 0.6117647059, 1)),
                                                         new Stop(0.65, Color.color(0.1843137255, 0.1843137255, 0.1843137255, 1)),
                                                         new Stop(0.89, Color.color(0.3294117647, 0.3372549020, 0.3333333333, 1)),
                                                         new Stop(1.0, Color.color(0.2156862745, 0.2156862745, 0.2156862745, 1)));
        RIGHT_FRAME.setFill(RIGHT_FRAME_FILL);
        RIGHT_FRAME.setStroke(null);

        final Rectangle RIGHT_MAIN = new Rectangle(posRightMain, 0.3964757709251101 * HEIGHT,
                                                  mainWidth, mainHeight);
        final Paint RIGHT_MAIN_FILL = new LinearGradient(0, 0.3964757709251101 * HEIGHT,
                                                        0 * WIDTH, 0.5242290748898678 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                        new Stop(0.13, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                        new Stop(0.66, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                        new Stop(0.73, Color.color(0.2509803922, 0.2509803922, 0.2509803922, 1)),
                                                        new Stop(0.9, Color.color(0.4274509804, 0.4274509804, 0.4274509804, 1)),
                                                        new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        RIGHT_MAIN.setFill(RIGHT_MAIN_FILL);
        RIGHT_MAIN.setStroke(null);

        final Rectangle LEFT_FRAME = new Rectangle(posLeftFrame, 0.3920704845814978 * HEIGHT,
                                                   frameWidth, frameHeight);
        final Paint LEFT_FRAME_FILL = new LinearGradient(0, 0.3920704845814978 * HEIGHT,
                                                        0, 0.5286343612334802 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                        new Stop(0.18, Color.color(0.6117647059, 0.6117647059, 0.6117647059, 1)),
                                                        new Stop(0.65, Color.color(0.1843137255, 0.1843137255, 0.1843137255, 1)),
                                                        new Stop(0.89, Color.color(0.3294117647, 0.3372549020, 0.3333333333, 1)),
                                                        new Stop(1.0, Color.color(0.2156862745, 0.2156862745, 0.2156862745, 1)));
        LEFT_FRAME.setFill(LEFT_FRAME_FILL);
        LEFT_FRAME.setStroke(null);

        final Rectangle LEFT_MAIN = new Rectangle(posLeftMain, 0.3964757709251101 * HEIGHT,
                                                 mainWidth, mainHeight);
        final Paint LEFT_MAIN_FILL = new LinearGradient(0, 0.3964757709251101 * HEIGHT,
                                                       0, 0.5242290748898678 * HEIGHT,
                                                       false, CycleMethod.NO_CYCLE,
                                                       new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                       new Stop(0.13, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                       new Stop(0.66, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                       new Stop(0.73, Color.color(0.2509803922, 0.2509803922, 0.2509803922, 1)),
                                                       new Stop(0.9, Color.color(0.4274509804, 0.4274509804, 0.4274509804, 1)),
                                                       new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        LEFT_MAIN.setFill(LEFT_MAIN_FILL);
        LEFT_MAIN.setStroke(null);

        final Rectangle AXIS = new Rectangle(posLeftFrame + frameWidth, 0.4537444934 * HEIGHT,
                                             WIDTH - (2 * posLeftFrame) - (2 * frameWidth), 0.013215859 * HEIGHT);
        final Paint AXIS_FILL = new LinearGradient(0, 0.4537444934 * HEIGHT,
                                                   0, 0.4669603524 * HEIGHT,
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                   new Stop(0.41, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                   new Stop(0.72, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                   new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        AXIS.setFill(AXIS_FILL);
        AXIS.setStroke(null);

        fixture.getChildren().addAll(AXIS,
                                     RIGHT_FRAME,
                                     RIGHT_MAIN,
                                     LEFT_FRAME,
                                     LEFT_MAIN);

        fixture.setCache(true);
    }

    public final void drawFlip() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        flip.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        flip.getChildren().add(IBOUNDS);
        double offset       = 0.04405286343612335 * HEIGHT;
        double posLeft      = offset;
        double posRight     = WIDTH - offset;
        double cutOffWidth  = 0.0484581498 * HEIGHT;
        double cutOffHeight = 0.07048458149779736 * HEIGHT;

        final Rectangle CUT_UPPER_LEFT = new Rectangle(posLeft, 0.3832599118942731 * HEIGHT,
                                                       cutOffWidth, cutOffHeight);

        final Rectangle CUT_UPPER_RIGHT = new Rectangle(posRight - cutOffWidth, 0.3832599118942731 * HEIGHT,
                                                        cutOffWidth, cutOffHeight);

        final Rectangle CUT_LOWER_LEFT = new Rectangle(posLeft, 0.4669603524229075 * HEIGHT,
                                                       cutOffWidth, cutOffHeight);

        final Rectangle CUT_LOWER_RIGHT = new Rectangle(posRight - cutOffWidth, 0.4669603524229075 * HEIGHT,
                                                        cutOffWidth, cutOffHeight);

        final Rectangle UPPER_FLAP = new Rectangle(posLeft, posLeft,
                                                   WIDTH -  2 * offset, 0.40969163 * HEIGHT);
        UPPER_FLAP.setArcWidth(0.05286343612334802 * HEIGHT);
        UPPER_FLAP.setArcHeight(0.05286343612334802 * HEIGHT);

        final Rectangle LOWER_FLAP = new Rectangle(posLeft, 0.4669603524229075 * HEIGHT,
                                                   WIDTH - 2 * offset, 0.40969163 * HEIGHT);
        LOWER_FLAP.setArcWidth(0.05286343612334802 * HEIGHT);
        LOWER_FLAP.setArcHeight(0.05286343612334802 * HEIGHT);

        upper = Shape.subtract(Shape.subtract(UPPER_FLAP, CUT_UPPER_LEFT), CUT_UPPER_RIGHT);
        lower = Shape.subtract(Shape.subtract(LOWER_FLAP, CUT_LOWER_LEFT), CUT_LOWER_RIGHT);

        final Paint LOWER_FILL = new LinearGradient(0.0, 0.4669603524229075 * HEIGHT,
                                                    0.0, 0.8722466960352423 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getLowerFlapTopColor()),
                                                    new Stop(1.0, control.getLowerFlapBottomColor()));
        lower.setFill(LOWER_FILL);
        lower.setStroke(null);
        lower.setCache(true);
        lower.setCacheHint(CacheHint.QUALITY);

        final InnerShadow LOWER_INNER_SHADOW = new InnerShadow();
        LOWER_INNER_SHADOW.setWidth(0.08 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setHeight(0.08 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setOffsetX(0.0);
        LOWER_INNER_SHADOW.setOffsetY(0.0);
        LOWER_INNER_SHADOW.setRadius(0.08 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setColor(Color.BLACK);
        LOWER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow LOWER_LIGHT_EFFECT = new InnerShadow();
        LOWER_LIGHT_EFFECT.setWidth(0.055 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setHeight(0.055 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setOffsetX(0);
        LOWER_LIGHT_EFFECT.setOffsetY(0.01 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setRadius(0.055 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setColor(Color.color(1.0, 1.0, 1.0, 1.0));
        LOWER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        LOWER_LIGHT_EFFECT.inputProperty().set(LOWER_INNER_SHADOW);
        lower.setEffect(LOWER_LIGHT_EFFECT);

        final Paint UPPER_FILL = new LinearGradient(0.0, 0.04405286343612335 * HEIGHT,
                                                    0.0, 0.45374449339207046 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getUpperFlapTopColor()),
                                                    new Stop(1.0, control.getUpperFlapBottomColor()));
        upper.setFill(UPPER_FILL);
        upper.setStroke(null);

        final InnerShadow UPPER_INNER_SHADOW = new InnerShadow();
        UPPER_INNER_SHADOW.setWidth(0.08 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setHeight(0.08 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setOffsetX(0.0);
        UPPER_INNER_SHADOW.setOffsetY(0.0);
        UPPER_INNER_SHADOW.setRadius(0.08 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setColor(Color.BLACK);
        UPPER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow UPPER_LIGHT_EFFECT = new InnerShadow();
        UPPER_LIGHT_EFFECT.setWidth(0.055 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setHeight(0.055 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setOffsetX(0);
        UPPER_LIGHT_EFFECT.setOffsetY(0.01 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setRadius(0.055 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setColor(Color.color(1.0, 1.0, 1.0, 1.0));
        UPPER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        UPPER_LIGHT_EFFECT.inputProperty().set(UPPER_INNER_SHADOW);
        upper.setEffect(UPPER_LIGHT_EFFECT);
        upper.setCache(true);
        upper.setCacheHint(CacheHint.SPEED);

        final Font FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/droidsansmono.ttf"), (0.704845815 * HEIGHT));

        Rectangle upperClip = new Rectangle(0, upper.getLayoutBounds().getMinY(), WIDTH, upper.getLayoutBounds().getHeight());
        upperText.setTextOrigin(VPos.BOTTOM);
        upperText.setFont(FONT);
        upperText.setFontSmoothingType(FontSmoothingType.LCD);
        upperText.setText(control.getText());
        upperText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        upperText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        upperText.setClip(upperClip);
        LinearGradient upperTextFill = new LinearGradient(0.0, upperText.getLayoutBounds().getMinY(),
                                                          0.0, upperText.getLayoutBounds().getMaxY(),
                                                          false, CycleMethod.NO_CYCLE,
                                                          new Stop(0.0, control.getTextUpperFlapColor()),
                                                          new Stop(0.47, control.getTextColor()),
                                                          new Stop(0.5, control.getTextColor().darker()));
        upperText.setFill(upperTextFill);

        Rectangle lowerClip = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, lower.getLayoutBounds().getHeight());
        lowerText.setTextOrigin(VPos.BOTTOM);
        lowerText.setFont(FONT);
        lowerText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerText.setText(control.getText());
        lowerText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        lowerText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        lowerText.setClip(lowerClip);
        LinearGradient lowerTextFill = new LinearGradient(0.0, lowerText.getLayoutBounds().getMinY(),
                                                          0.0, lowerText.getLayoutBounds().getMaxY(),
                                                          false, CycleMethod.NO_CYCLE,
                                                          new Stop(0.5, control.getTextColor().brighter()),
                                                          new Stop(0.53, control.getTextLowerFlapColor()),
                                                          new Stop(1.0, control.getTextColor()));
        lowerText.setFill(lowerTextFill);
        lowerText.setStroke(null);

        upperNext = Shape.subtract(Shape.subtract(UPPER_FLAP, CUT_UPPER_LEFT), CUT_UPPER_RIGHT);

        final Paint UPPER_NEXT_FILL = new LinearGradient(0.0, 0.04405286343612335 * HEIGHT,
                                                         0.0, 0.49206349206349204 * HEIGHT,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, control.getUpperFlapTopColor()),
                                                         new Stop(1.0, control.getUpperFlapBottomColor()));
        upperNext.setFill(UPPER_NEXT_FILL);
        upperNext.setStroke(null);
        upperNext.setCache(true);
        upperNext.setCacheHint(CacheHint.SPEED);

        Rectangle upperNextClip = new Rectangle(0, upper.getLayoutBounds().getMinY(), WIDTH, upper.getLayoutBounds().getHeight());
        upperNextText.setTextOrigin(VPos.BOTTOM);
        upperNextText.setFont(FONT);
        upperNextText.setFontSmoothingType(FontSmoothingType.LCD);
        upperNextText.setText(control.getNextText());
        upperNextText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        upperNextText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        upperNextText.setClip(upperNextClip);
        LinearGradient upperNextTextFill = new LinearGradient(0.0, upperNextText.getLayoutBounds().getMinY(),
                                                              0.0, upperNextText.getLayoutBounds().getMaxY(),
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, control.getTextUpperFlapColor()),
                                                              new Stop(0.47, control.getTextColor()),
                                                              new Stop(0.5, control.getTextColor().darker()));
        upperNextText.setFill(upperNextTextFill);
        upperNextText.setStroke(null);

        Rectangle lowerNextClip = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, lower.getLayoutBounds().getHeight());
        lowerNextText.setTextOrigin(VPos.BOTTOM);
        lowerNextText.setFont(FONT);
        lowerNextText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerNextText.setText(control.getNextText());
        lowerNextText.setX(((WIDTH - lowerNextText.getLayoutBounds().getWidth()) / 2.0));
        lowerNextText.setY(HEIGHT * 0.04 + lowerNextText.getLayoutBounds().getHeight());
        lowerNextText.setClip(lowerNextClip);
        LinearGradient lowerNextTextFill = new LinearGradient(0.0, lowerNextText.getLayoutBounds().getMinY(),
                                                              0.0, lowerNextText.getLayoutBounds().getMaxY(),
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.5, control.getTextColor().brighter()),
                                                              new Stop(0.53, control.getTextLowerFlapColor()),
                                                              new Stop(1.0, control.getTextColor()));
        lowerNextText.setFill(lowerNextTextFill);
        lowerNextText.setStroke(null);
        lowerNextText.setVisible(false);
        lowerFlipVert = new Rotate();
        lowerFlipVert.setAxis(Rotate.X_AXIS);
        lowerFlipVert.setPivotY(HEIGHT * 0.4625550661);
        lowerFlipVert.setAngle(180);
        lowerNextText.getTransforms().add(lowerFlipVert);

        // Adjust text if longer than one character
        if (selectedSet.get(currentSelectionIndex).length() > 1) {
            double textOffset = 0.1057268722 * HEIGHT;
            upperText.setX(textOffset);
            lowerText.setX(textOffset);
        }
        if (selectedSet.get(nextSelectionIndex).length() > 1) {
            double textOffset = 0.1057268722 * HEIGHT;
            upperNextText.setX(textOffset);
            lowerNextText.setX(textOffset);
        }

        if (control.isInteractive()) {
            addMouseEventListener(upperText, 1);
            addMouseEventListener(lowerText, -1);
        }

        flip.setDepthTest(DepthTest.ENABLE);

        flip.getChildren().addAll(lower,
                                  lowerText,
                                  upperNext,
                                  upperNextText,
                                  upper,
                                  upperText,
                                  lowerNextText
                                  );
    }

    public final void drawFrame() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        frame.getChildren().clear();

        final Shape FRAME = Shape.subtract(new Rectangle(0, 0, WIDTH, HEIGHT),
                                           new Rectangle(0.0352422907 * HEIGHT, 0.0352422907 * HEIGHT,
                                                         WIDTH - 2 * 0.0352422907 * HEIGHT, 0.9207048458 * HEIGHT));
        final Paint FRAME_FILL = new LinearGradient(0.0, 0.0,
                                                    0.0, HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getFrameTopColor()),
                                                    new Stop(1.0, control.getFrameBottomColor()));
        FRAME.setFill(FRAME_FILL);
        FRAME.setStroke(null);

        frame.getChildren().addAll(FRAME);
        frame.setCache(true);
    }
}
