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

import javafx.scene.control.SkinBase;
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
public class SplitFlapSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<SplitFlap, SplitFlapBehavior> {
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
    private Rotate            rotate;
    private Rotate            lowerFlipVert;
    private double            angleStep;
    private double            currentAngle;
    private boolean           flipping;
    private int               lastFlapDirection;
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
        rotate                 = new Rotate();
        angleStep              = 180.0 / ((control.getFlipTimeInMs() * 1000000) / (MIN_FLIP_TIME));
        currentAngle           = 0;
        flipping               = false;
        lastFlapDirection      = 0;
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

        if (control.getMinWidth() < 0 | control.getMinHeight() < 0) {
            control.setMinSize(13, 22);
        }

        if (control.getMaxWidth() < 0 | control.getMaxHeight() < 0) {
            control.setMaxSize(660, 1135);
        }

        rotate.setAxis(Rotate.X_AXIS);
        rotate.setPivotY(control.getPrefHeight() * 0.4625550661);

        lowerFlipVert = new Rotate();

        selectedSet.clear();
        selectedSet.addAll(control.getSelectedSet());

        upperText.setVisible(!control.isImageMode());
        upperNextText.setVisible(!control.isImageMode());
        lowerText.setVisible(!control.isImageMode());
        lowerNextText.setVisible(!control.isImageMode());

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.textColorProperty(), "TEXT_COLOR");
        registerChangeListener(control.textProperty(), "TEXT");
        registerChangeListener(control.flipTimeInMsProperty(), "FLIP_TIME");
        registerChangeListener(control.frameVisibleProperty(), "FRAME_VISIBILITY");
        registerChangeListener(control.backgroundVisibleProperty(), "BACKGROUND_VISIBILITY");
        registerChangeListener(control.countdownModeProperty(), "COUNTDOWN_MODE");
        registerChangeListener(control.imageModeProperty(), "IMAGE_MODE");

        control.selectionProperty().addListener(new ChangeListener<String[]>() {
            @Override public void changed(ObservableValue<? extends String[]> ov, String[] oldValue, String[] newValue) {
                selectedSet.clear();
                for (String text : newValue) {
                    selectedSet.add(text);
                }
            }
        });

        frame.setVisible(control.isFrameVisible());
        background.setVisible(control.isBackgroundVisible());

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("COLOR".equals(PROPERTY)) {
            repaint();
        } else if ("TEXT_COLOR".equals(PROPERTY)) {
            repaint();
        } else if ("TEXT".equals(PROPERTY)) {
            if (control.getText() != selectedSet.get(currentSelectionIndex)) {
                timer.stop();
                flipping = true;
                timer.start();
            }
        } else if ("FLIP_TIME".equals(PROPERTY)) {
            angleStep = 180.0 / ((control.getFlipTimeInMs() * 1000000) / (MIN_FLIP_TIME));
        } else if ("FRAME_VISIBILITY".equals(PROPERTY)) {
            frame.setVisible(control.isFrameVisible());
        } else if ("BACKGROUND_VISIBILITY".equals(PROPERTY)) {
            background.setVisible(control.isBackgroundVisible());
        } else if ("COUNDOWN_MODE".equals(PROPERTY)) {
            currentAngle = 180;
        } else if ("SELECTION".equals(PROPERTY)) {
            selectedSet.clear();
            selectedSet.addAll(control.getSelectedSet());
        } else if ("IMAGE_MODE".equals(PROPERTY)) {
            upperText.setVisible(!control.isImageMode());
            upperNextText.setVisible(!control.isImageMode());
            lowerText.setVisible(!control.isImageMode());
            lowerNextText.setVisible(!control.isImageMode());
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
        }
    }

    public final void repaint() {
        isDirty = true;
        getSkinnable().requestLayout();
    }

//    @Override public void layoutChildren() {
//        if (!isDirty) {
//            return;
//        }
//        if (!initialized) {
//            init();
//        }
//        if (control.getScene() != null) {
//            drawBackground();
//            drawFixture();
//            drawFlip();
//            drawFrame();
//            getChildren().setAll(background,
//                fixture,
//                flip,
//                frame);
//        }
//        isDirty = false;
//
//        super.layoutChildren();
//    }
//
//    @Override public final SplitFlap getSkinnable() {
//        return control;
//    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 132;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getSkinnable().getInsets().getLeft() - getSkinnable().getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 227;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getSkinnable().getInsets().getTop() - getSkinnable().getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private void flipForward(final double ANGLE_STEP) {
        currentAngle += ANGLE_STEP;
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
            lowerNextText.getTransforms().clear();            currentSelectionIndex++;
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
            lowerNextText.setVisible(false);
            lowerFlipVert.setAxis(Rotate.X_AXIS);
            lowerFlipVert.setPivotY(control.getPrefHeight() * 0.4625550661);
            lowerFlipVert.setAngle(180);
            lowerNextText.getTransforms().add(lowerFlipVert);
            upperText.setVisible(true);

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
            upper.getTransforms().remove(rotate);
            upperText.getTransforms().remove(rotate);
            lowerNextText.getTransforms().remove(rotate);
            rotate.setAngle(currentAngle);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
    }

    private void flipBackward(final double ANGLE_STEP) {
        currentAngle -= ANGLE_STEP;
        if (Double.compare(currentAngle, 0) <= 0) {
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
            currentAngle = 180;
            upper.getTransforms().clear();
            upperText.getTransforms().clear();
            lowerNextText.getTransforms().clear();
            lowerNextText.setVisible(true);
            lowerFlipVert.setAxis(Rotate.X_AXIS);
            lowerFlipVert.setPivotY(control.getPrefHeight() * 0.4625550661);
            lowerFlipVert.setAngle(180);
            lowerNextText.getTransforms().add(lowerFlipVert);
            upperText.setVisible(false);

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
            upperText.setText(selectedSet.get(nextSelectionIndex));
            lowerText.setText(selectedSet.get(nextSelectionIndex));
            upperNextText.setText(selectedSet.get(currentSelectionIndex));
            lowerNextText.setText(selectedSet.get(currentSelectionIndex));
            if (selectedSet.get(currentSelectionIndex).length() > 1) {
                double textOffset = 0.1057268722 * getSkinnable().getPrefHeight();
                upperText.setX(textOffset);
                lowerText.setX(textOffset);
                upperNextText.setX(textOffset);
                lowerNextText.setX(textOffset);
            }
            rotate.setAngle(currentAngle);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
        if (currentAngle < 90) {
            upperText.setVisible(true);
            lowerNextText.setVisible(false);
        }
        if (flipping) {
            upper.getTransforms().remove(rotate);
            upperText.getTransforms().remove(rotate);
            lowerNextText.getTransforms().remove(rotate);
            rotate.setAngle(currentAngle);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
    }


    // ******************** Mouse event handling ******************************
    private void addMouseEventListener(final Shape FLAP, final int FLAP_DIRECTION) {
        FLAP.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(final MouseEvent EVENT) {
                switch(FLAP_DIRECTION) {
                    case 1:
                        currentAngle = 0;
                        checkLastFlapDirection(FLAP_DIRECTION);
                        control.flipForward();
                        lastFlapDirection = FLAP_DIRECTION;
                        break;
                    case -1:
                        currentAngle = 180;
                        checkLastFlapDirection(FLAP_DIRECTION);
                        control.flipBackward();
                        lastFlapDirection = FLAP_DIRECTION;
                        break;
                }
            }
        });
    }

    private void checkLastFlapDirection(final int CURRENT_FLAP_DIRECTION) {
        if (CURRENT_FLAP_DIRECTION == 1 && lastFlapDirection == -1) {
            // changed from backward to forward
            System.out.println("changed from backward to forward");
        } else if (CURRENT_FLAP_DIRECTION == -1 && lastFlapDirection == 1) {
            // changed from forward to backward
            rotate.setAngle(currentAngle);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
            System.out.println("changed from forward to backward");
        }
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
                                                    new Stop(1.0, control.getLowerFlapBottomColor()));
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
                                                    new Stop(1.0, control.getLowerFlapBottomColor()));
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
                                                    new Stop(1.0, control.getLowerFlapBottomColor()));
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
                                                    new Stop(1.0, control.getLowerFlapBottomColor()));
        RECT0.setFill(RECT0_FILL);
        RECT0.setStroke(null);

        final InnerShadow LOWER_INNER_SHADOW = new InnerShadow();
        LOWER_INNER_SHADOW.setWidth(0.01 * RECT3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setHeight(0.01 * RECT3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setOffsetX(0.0);
        LOWER_INNER_SHADOW.setOffsetY(-0.066 * RECT3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setRadius(0.001 * RECT3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setColor(Color.BLACK);
        LOWER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow LOWER_LIGHT_EFFECT = new InnerShadow();
        LOWER_LIGHT_EFFECT.setWidth(0.04 * RECT3.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setHeight(0.04 * RECT3.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setOffsetX(0);
        LOWER_LIGHT_EFFECT.setOffsetY(0.01 * SIZE);
        LOWER_LIGHT_EFFECT.setRadius(0.04 * RECT3.getLayoutBounds().getHeight());
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

        final Rectangle RIGHT_FRAME = new Rectangle(posRightFrame, 0.3920704845814978 * HEIGHT, frameWidth, frameHeight);
        final Paint RIGHT_FRAME_FILL;
        final Rectangle RIGHT_MAIN = new Rectangle(posRightMain, 0.3964757709251101 * HEIGHT, mainWidth, mainHeight);
        final Paint RIGHT_MAIN_FILL;
        if (control.isDarkFixtureEnabled()) {
            RIGHT_FRAME_FILL = new LinearGradient(0, 0.3920704845814978 * HEIGHT,
                                                  0, 0.5286343612334802 * HEIGHT,
                                                  false, CycleMethod.NO_CYCLE,
                                                  new Stop(0.0, Color.rgb(26, 26, 26)),
                                                  new Stop(0.18, Color.rgb(86, 86, 86)),
                                                  new Stop(0.65, Color.rgb(17, 17, 17)),
                                                  new Stop(0.89, Color.rgb(24, 24, 24)),
                                                  new Stop(1.0, Color.rgb(25, 24, 24)));

            RIGHT_MAIN_FILL = new LinearGradient(0, 0.3964757709251101 * HEIGHT,
                                                 0 * WIDTH, 0.5242290748898678 * HEIGHT,
                                                 false, CycleMethod.NO_CYCLE,
                                                 new Stop(0.0, Color.rgb(66, 66, 66)),
                                                 new Stop(0.13, Color.rgb(153, 153, 153)),
                                                 new Stop(0.66, Color.rgb(6, 6, 6)),
                                                 new Stop(0.73, Color.rgb(14, 14, 14)),
                                                 new Stop(0.9, Color.rgb(39, 39, 39)),
                                                 new Stop(1.0, Color.rgb(23, 23, 23)));
        } else {
            RIGHT_FRAME_FILL = new LinearGradient(0, 0.3920704845814978 * HEIGHT,
                                                  0, 0.5286343612334802 * HEIGHT,
                                                  false, CycleMethod.NO_CYCLE,
                                                  new Stop(0.0, Color.rgb(56, 56, 56)),
                                                  new Stop(0.18, Color.rgb(156, 156, 156)),
                                                  new Stop(0.65, Color.rgb(47,47, 47)),
                                                  new Stop(0.89, Color.rgb(84,84, 84)),
                                                  new Stop(1.0, Color.rgb(55,55, 55)));

            RIGHT_MAIN_FILL = new LinearGradient(0, 0.3964757709251101 * HEIGHT,
                                                 0 * WIDTH, 0.5242290748898678 * HEIGHT,
                                                 false, CycleMethod.NO_CYCLE,
                                                 new Stop(0.0, Color.rgb(116, 116, 116)),
                                                 new Stop(0.13, Color.rgb(213, 213, 213)),
                                                 new Stop(0.66, Color.rgb(56, 56, 56)),
                                                 new Stop(0.73, Color.rgb(64, 64, 64)),
                                                 new Stop(0.9, Color.rgb(109, 109, 109)),
                                                 new Stop(1.0, Color.rgb(83, 83, 83)));
        }
        RIGHT_FRAME.setFill(RIGHT_FRAME_FILL);
        RIGHT_FRAME.setStroke(null);
        RIGHT_MAIN.setFill(RIGHT_MAIN_FILL);
        RIGHT_MAIN.setStroke(null);

        final Rectangle LEFT_FRAME = new Rectangle(posLeftFrame, 0.3920704845814978 * HEIGHT, frameWidth, frameHeight);
        final Paint LEFT_FRAME_FILL;
        final Rectangle LEFT_MAIN = new Rectangle(posLeftMain, 0.3964757709251101 * HEIGHT, mainWidth, mainHeight);
        final Paint LEFT_MAIN_FILL;
        if (control.isDarkFixtureEnabled()) {
            LEFT_FRAME_FILL = new LinearGradient(0, 0.3920704845814978 * HEIGHT,
                                                 0, 0.5286343612334802 * HEIGHT,
                                                 false, CycleMethod.NO_CYCLE,
                                                 new Stop(0.0, Color.rgb(26, 26, 26)),
                                                 new Stop(0.18, Color.rgb(86, 86, 86)),
                                                 new Stop(0.65, Color.rgb(17, 17, 17)),
                                                 new Stop(0.89, Color.rgb(24, 24, 24)),
                                                 new Stop(1.0, Color.rgb(25, 24, 24)));

            LEFT_MAIN_FILL = new LinearGradient(0, 0.3964757709251101 * HEIGHT,
                                                0, 0.5242290748898678 * HEIGHT,
                                                false, CycleMethod.NO_CYCLE,
                                                new Stop(0.0, Color.rgb(66, 66, 66)),
                                                new Stop(0.13, Color.rgb(153, 153, 153)),
                                                new Stop(0.66, Color.rgb(6, 6, 6)),
                                                new Stop(0.73, Color.rgb(14, 14, 14)),
                                                new Stop(0.9, Color.rgb(39, 39, 39)),
                                                new Stop(1.0, Color.rgb(23, 23, 23)));
        } else {
            LEFT_FRAME_FILL = new LinearGradient(0, 0.3920704845814978 * HEIGHT,
                                                 0, 0.5286343612334802 * HEIGHT,
                                                 false, CycleMethod.NO_CYCLE,
                                                 new Stop(0.0, Color.rgb(56, 56, 56)),
                                                 new Stop(0.18, Color.rgb(156, 156, 156)),
                                                 new Stop(0.65, Color.rgb(47,47, 47)),
                                                 new Stop(0.89, Color.rgb(84,84, 84)),
                                                 new Stop(1.0, Color.rgb(55,55, 55)));

            LEFT_MAIN_FILL = new LinearGradient(0, 0.3964757709251101 * HEIGHT,
                                                0, 0.5242290748898678 * HEIGHT,
                                                false, CycleMethod.NO_CYCLE,
                                                new Stop(0.0, Color.rgb(116, 116, 116)),
                                                new Stop(0.13, Color.rgb(213, 213, 213)),
                                                new Stop(0.66, Color.rgb(56, 56, 56)),
                                                new Stop(0.73, Color.rgb(64, 64, 64)),
                                                new Stop(0.9, Color.rgb(109, 109, 109)),
                                                new Stop(1.0, Color.rgb(83, 83, 83)));
        }
        LEFT_FRAME.setFill(LEFT_FRAME_FILL);
        LEFT_FRAME.setStroke(null);
        LEFT_MAIN.setFill(LEFT_MAIN_FILL);
        LEFT_MAIN.setStroke(null);

        fixture.getChildren().addAll(RIGHT_FRAME,
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
        double offset           = 0.04405286343612335 * HEIGHT;
        double posLeft          = offset;
        double posRight         = WIDTH - offset;
        double cutOffWidth      = 0.0484581498 * HEIGHT;
        double cutOffHeight     = 0.0748898678 * HEIGHT;
        double flapWidth        = WIDTH -  2 * offset;
        double flapHeight       = 0.4140969163 * HEIGHT;
        double flapCornerRadius = (control.getFlapCornerRadius() / 227) * HEIGHT;

        final Rectangle CUT_UPPER_LEFT = new Rectangle(posLeft, 0.3832599118942731 * HEIGHT, cutOffWidth, cutOffHeight);
        final Rectangle CUT_UPPER_RIGHT = new Rectangle(posRight - cutOffWidth, 0.3832599118942731 * HEIGHT, cutOffWidth, cutOffHeight);
        final Rectangle CUT_LOWER_LEFT = new Rectangle(posLeft, 0.4625550661 * HEIGHT, cutOffWidth, cutOffHeight);
        final Rectangle CUT_LOWER_RIGHT = new Rectangle(posRight - cutOffWidth, 0.4625550661 * HEIGHT, cutOffWidth, cutOffHeight);

        final Rectangle UPPER_FLAP = new Rectangle(posLeft, posLeft, flapWidth, flapHeight);
        UPPER_FLAP.setArcWidth(flapCornerRadius);
        UPPER_FLAP.setArcHeight(flapCornerRadius);

        final Rectangle LOWER_FLAP = new Rectangle(posLeft, 0.4625550661 * HEIGHT, flapWidth, flapHeight);
        LOWER_FLAP.setArcWidth(flapCornerRadius);
        LOWER_FLAP.setArcHeight(flapCornerRadius);

        upper = Shape.subtract(Shape.subtract(UPPER_FLAP, CUT_UPPER_LEFT), CUT_UPPER_RIGHT);
        lower = Shape.subtract(Shape.subtract(LOWER_FLAP, CUT_LOWER_LEFT), CUT_LOWER_RIGHT);

        final Paint LOWER_FILL = new LinearGradient(0.0, lower.getLayoutBounds().getMinY(),
                                                    0.0, lower.getLayoutBounds().getMaxY(),
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getLowerFlapTopColor().brighter().brighter()),
                                                    new Stop(0.05, control.getLowerFlapTopColor()),
                                                    new Stop(0.99, control.getLowerFlapBottomColor()),
                                                    new Stop(0.0, control.getLowerFlapBottomColor().darker().darker()));
        lower.setFill(LOWER_FILL);
        lower.setStroke(null);
        lower.setCache(true);
        lower.setCacheHint(CacheHint.QUALITY);

        final InnerShadow LOWER_INNER_SHADOW = new InnerShadow();
        LOWER_INNER_SHADOW.setWidth(0.01 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setHeight(0.01 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setOffsetX(0.0);
        LOWER_INNER_SHADOW.setOffsetY(-0.022 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setRadius(0.01 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setColor(Color.BLACK);
        LOWER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow LOWER_LIGHT_EFFECT = new InnerShadow();
        LOWER_LIGHT_EFFECT.setWidth(0.04 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setHeight(0.04 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setOffsetX(0);
        LOWER_LIGHT_EFFECT.setOffsetY(0.015 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setRadius(0.04 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setColor(Color.color(1.0, 1.0, 1.0, 0.8));
        LOWER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        LOWER_LIGHT_EFFECT.inputProperty().set(LOWER_INNER_SHADOW);
        if (control.isLowerFlapHighlightEnabled()) {
            lower.setEffect(LOWER_LIGHT_EFFECT);
        } else {
            lower.setEffect(LOWER_INNER_SHADOW);
        }

        final Paint UPPER_FILL = new LinearGradient(0.0, upper.getLayoutBounds().getMinY(),
                                                    0.0, upper.getLayoutBounds().getMaxY(),
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getUpperFlapTopColor().darker()),
                                                    new Stop(0.01, control.getUpperFlapTopColor()),
                                                    new Stop(0.95, control.getUpperFlapBottomColor()),
                                                    new Stop(1.0, control.getUpperFlapBottomColor().darker()));
        upper.setFill(UPPER_FILL);
        upper.setStroke(null);

        final InnerShadow UPPER_INNER_SHADOW = new InnerShadow();
        UPPER_INNER_SHADOW.setWidth(0.02 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setHeight(0.02 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setOffsetX(0.0);
        UPPER_INNER_SHADOW.setOffsetY(0.022 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setRadius(0.02 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setColor(Color.BLACK);
        UPPER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow UPPER_LIGHT_EFFECT = new InnerShadow();
        UPPER_LIGHT_EFFECT.setWidth(0.04 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setHeight(0.04 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setOffsetX(0);
        UPPER_LIGHT_EFFECT.setOffsetY(0.01 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setRadius(0.04 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setColor(Color.color(1.0, 1.0, 1.0, 0.8));
        UPPER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        UPPER_LIGHT_EFFECT.inputProperty().set(UPPER_INNER_SHADOW);
        if (control.isUpperFlapHighlightEnabled()) {
            upper.setEffect(UPPER_LIGHT_EFFECT);
        } else {
            upper.setEffect(UPPER_INNER_SHADOW);
        }
        upper.setCache(true);
        upper.setCacheHint(CacheHint.SPEED);

        final Font FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/droidsansmono.ttf"), (0.704845815 * HEIGHT));

        final Rectangle UPPER_CLIP = new Rectangle(0, upper.getLayoutBounds().getMinY(), WIDTH, upper.getLayoutBounds().getHeight());
        upperText.setTextOrigin(VPos.BOTTOM);
        upperText.setFont(FONT);
        upperText.setFontSmoothingType(FontSmoothingType.LCD);
        upperText.setText(control.getText());
        upperText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        upperText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        upperText.setClip(UPPER_CLIP);
        final LinearGradient UPPER_TEXT_FILL = new LinearGradient(0.0, upperText.getLayoutBounds().getMinY(),
                                                                  0.0, upperText.getLayoutBounds().getMaxY(),
                                                                  false, CycleMethod.NO_CYCLE,
                                                                  new Stop(0.0, control.getTextUpperFlapColor()),
                                                                  new Stop(0.49, control.getTextColor()),
                                                                  new Stop(0.5, control.getTextColor().darker()));
        upperText.setFill(UPPER_TEXT_FILL);
        upperText.setStroke(null);

        final Rectangle LOWER_CLIP = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, lower.getLayoutBounds().getHeight());
        lowerText.setTextOrigin(VPos.BOTTOM);
        lowerText.setFont(FONT);
        lowerText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerText.setText(control.getText());
        lowerText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        lowerText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        lowerText.setClip(LOWER_CLIP);
        final LinearGradient LOWER_TEXT_FILL = new LinearGradient(0.0, lower.getLayoutBounds().getMinY(),
                                                                  0.0, lowerText.getLayoutBounds().getMaxY(),
                                                                  false, CycleMethod.NO_CYCLE,
                                                                  new Stop(0.0, control.getTextColor().darker()),
                                                                  new Stop(0.05, control.getTextLowerFlapColor()),
                                                                  new Stop(1.0, control.getTextColor()));
        lowerText.setFill(LOWER_TEXT_FILL);
        lowerText.setStroke(null);

        upperNext = Shape.subtract(Shape.subtract(UPPER_FLAP, CUT_UPPER_LEFT), CUT_UPPER_RIGHT);

        final Paint UPPER_NEXT_FILL = new LinearGradient(0.0, upperNext.getLayoutBounds().getMinY(),
                                                         0.0, upperNext.getLayoutBounds().getMaxY(),
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, control.getUpperFlapTopColor().darker()),
                                                         new Stop(0.01, control.getUpperFlapTopColor()),
                                                         new Stop(0.95, control.getUpperFlapBottomColor()),
                                                         new Stop(1.0, control.getUpperFlapBottomColor().darker()));
        upperNext.setFill(UPPER_NEXT_FILL);
        upperNext.setStroke(null);
        upperNext.setCache(true);
        upperNext.setCacheHint(CacheHint.SPEED);

        final Rectangle UPPER_NEXT_CLIP = new Rectangle(0, upper.getLayoutBounds().getMinY(), WIDTH, upper.getLayoutBounds().getHeight());
        upperNextText.setTextOrigin(VPos.BOTTOM);
        upperNextText.setFont(FONT);
        upperNextText.setFontSmoothingType(FontSmoothingType.LCD);
        upperNextText.setText(control.getNextText());
        upperNextText.setX(((WIDTH - upperNextText.getLayoutBounds().getWidth()) / 2.0));
        upperNextText.setY(HEIGHT * 0.04 + upperNextText.getLayoutBounds().getHeight());
        upperNextText.setClip(UPPER_NEXT_CLIP);
        LinearGradient upperNextTextFill = new LinearGradient(0.0, upperNextText.getLayoutBounds().getMinY(),
                                                              0.0, upperNextText.getLayoutBounds().getMaxY(),
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, control.getTextUpperFlapColor()),
                                                              new Stop(0.47, control.getTextColor()),
                                                              new Stop(0.5, control.getTextColor().darker()));
        upperNextText.setFill(upperNextTextFill);
        upperNextText.setStroke(null);

        final Rectangle LOWER_NEXT_CLIP = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, lower.getLayoutBounds().getHeight());
        lowerNextText.setTextOrigin(VPos.BOTTOM);
        lowerNextText.setFont(FONT);
        lowerNextText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerNextText.setText(control.getNextText());
        lowerNextText.setX(((WIDTH - lowerNextText.getLayoutBounds().getWidth()) / 2.0));
        lowerNextText.setY(HEIGHT * 0.04 + lowerNextText.getLayoutBounds().getHeight());
        lowerNextText.setClip(LOWER_NEXT_CLIP);
        final LinearGradient LOWER_NEXT_TEXT_FILL = new LinearGradient(0.0, lowerNextText.getLayoutBounds().getMinY(),
                                                                       0.0, lowerNextText.getLayoutBounds().getMaxY(),
                                                                       false, CycleMethod.NO_CYCLE,
                                                                       new Stop(0.5, control.getTextColor().brighter()),
                                                                       new Stop(0.53, control.getTextLowerFlapColor()),
                                                                       new Stop(1.0, control.getTextColor()));
        lowerNextText.setFill(LOWER_NEXT_TEXT_FILL);
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
            addMouseEventListener(upperNextText, 1);
            addMouseEventListener(lowerText, -1);
            addMouseEventListener(lowerNextText, -1);
        }

        flip.setDepthTest(DepthTest.ENABLE);

        flip.getChildren().addAll(lower,
                                  lowerText,
                                  upperNext,
                                  upperNextText,
                                  upper,
                                  upperText,
                                  lowerNextText);
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
