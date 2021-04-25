/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.internal.scene.control.skin;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import jfxtras.labs.scene.control.MiniIconButton;

/**
 * Skin implementation for {@link MiniIconButton}.
 *
 * @author Andreas Billmann
 */
public class MiniIconButtonSkin extends ButtonSkin {

    /**
     * transition for the mini-icon jump
     */
    private final TranslateTransition jumpTransition = new TranslateTransition();

    /**
     * the timline for blinking
     */
    private final Timeline blinkTimeline = new Timeline();

    /**
     * remember the used keyframe, it is easier to change
     */
    private KeyFrame kf;

    /**
     * Fixed distance for the first version
     */
    private static final double JUMP_DISTANCE = 4.0;

    /**
     * Set the margin to JUMP_DISTANCE + 1 to have enough space for the jump
     */
    private static final double MARGIN = JUMP_DISTANCE + 2.0;

    /**
     * Fixed minimum of zero for the first version
     */
    private static final double MINIMUM_OPACITY_FOR_BLINKING = 0.0;

    public MiniIconButtonSkin(final MiniIconButton miniIconButton) {
        super(miniIconButton);
        setMiniIcon(miniIconButton.getMiniIcon());
        positionMiniIcon(miniIconButton);
        calculateAndSetNewMiniIconSize(miniIconButton);
        defaultConfigJumpingAnimation();
        defaultConfigBlinkingAnimation();
        configureJumping(miniIconButton);
        configureBlinking(miniIconButton);
        startAnimation(miniIconButton);
        addImageViewSizeBindings();
        addChangeListeners();
    }

//    @Override
//    protected void layoutChildren() {
//
//        // layouts the other children
//        super.layoutChildren();
//
//        // layouts the mini icon
//        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
//        final ImageView miniIcon = miniIconButton.getMiniIcon();
//
//        final double width = getWidth();
//        final double height = getHeight();
//        final double top = 0.0;
//        final double left = 0.0;
//        final double baselineOffset = getBaselineOffset();
//
//        final Pos childAlignment = StackPane.getAlignment(miniIcon);
//        layoutInArea(miniIcon, left, top,
//                width, height,
//                baselineOffset, getMargin(miniIcon),
//                childAlignment != null ? childAlignment.getHpos() : getAlignment().getHpos(),
//                childAlignment != null ? childAlignment.getVpos() : getAlignment().getVpos());
//    }

    /**
     * adds the change listeners to the {@link MiniIconButton} properties
     */
    private void addChangeListeners() {
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        miniIconButton.animationDurationProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observableValue,
                                final Number oldDuraction,
                                final Number newDuration) {
                stopAnimation(miniIconButton);
                configureJumping(miniIconButton);
                configureBlinking(miniIconButton);
                startAnimation(miniIconButton);
                getSkinnable().requestLayout();
            }
        });

        miniIconButton.animationTypeProperty().addListener(new ChangeListener<MiniIconButton.AnimationType>() {
            @Override
            public void changed(final ObservableValue<? extends MiniIconButton.AnimationType> observableValue,
                                final MiniIconButton.AnimationType oldAnimationType,
                                final MiniIconButton.AnimationType newAnimationType) {
                startAnimation(miniIconButton);
                getSkinnable().requestLayout();
            }
        });

        miniIconButton.miniIconPositionProperty().addListener(new ChangeListener<Pos>() {
            @Override
            public void changed(final ObservableValue<? extends Pos> observableValue,
                                final Pos oldPosition,
                                final Pos newPosition) {
                StackPane.setAlignment(miniIconButton.getMiniIcon(), newPosition);
                getSkinnable().requestLayout();
            }
        });

        miniIconButton.miniIconProperty().addListener(new ChangeListener<ImageView>() {
            @Override
            public void changed(final ObservableValue<? extends ImageView> observableValue,
                                final ImageView oldMiniIcon,
                                final ImageView newMiniIcon) {
                stopAnimation(miniIconButton);
                changeMiniIcon(oldMiniIcon, newMiniIcon);
                positionMiniIcon(miniIconButton);
                configureJumping(miniIconButton);
                configureBlinking(miniIconButton);
                calculateAndSetNewMiniIconSize(miniIconButton);
                startAnimation(miniIconButton);
                getSkinnable().requestLayout();
            }
        });

        miniIconButton.miniIconRatioProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observableValue,
                                final Number oldNumber,
                                final Number newNumber) {
                stopAnimation(miniIconButton);
                calculateAndSetNewMiniIconSize(miniIconButton);
                startAnimation(miniIconButton);
                getSkinnable().requestLayout();
            }
        });
    }

    /**
     * removes the old mini icon and sets the new one
     * @param oldMiniIcon the old mini icon
     * @param newMiniIcon the new mini icon
     */
    private void changeMiniIcon(final ImageView oldMiniIcon, final ImageView newMiniIcon) {
        getChildren().remove(oldMiniIcon);
        setMiniIcon(newMiniIcon);
    }

    /**
     * sets the given mini icon
     * @param miniIcon the mini icon
     */
    private void setMiniIcon(final ImageView miniIcon) {
        getChildren().add(miniIcon);
    }

    /**
     * configure the blinking has some steps to do
     * <ol>
     *     <li>remove the old {@link KeyFrame} from the timeline</li>
     *     <li>create a new {@link KeyValue}</li>
     *     <li>create a new {@link KeyFrame}</li>
     *     <li>add new {@link KeyFrame} to the timeline</li>
     * </ol>
     * @param miniIconButton the mini icon button
     */
    private void configureBlinking(final MiniIconButton miniIconButton) {
        blinkTimeline.getKeyFrames().remove(kf);
        final KeyValue kv = new KeyValue(miniIconButton.getMiniIcon().opacityProperty(), MINIMUM_OPACITY_FOR_BLINKING);
        kf = new KeyFrame(Duration.millis(miniIconButton.getAnimationDuration()), kv);
        blinkTimeline.getKeyFrames().add(kf);
    }

    /**
     * configure the jumping animation
     * @param miniIconButton the mini icon button
     */
    private void configureJumping(final MiniIconButton miniIconButton) {
        final ImageView miniIcon = miniIconButton.getMiniIcon();
        jumpTransition.setNode(miniIcon);
        jumpTransition.setDuration(Duration.millis(miniIconButton.getAnimationDuration()));
    }

    /**
     * positions the mini icon on the given {@link Pos}
     * @param miniIconButton
     */
    private void positionMiniIcon(final MiniIconButton miniIconButton) {
        final ImageView miniIcon = miniIconButton.getMiniIcon();
        StackPane.setAlignment(miniIcon, miniIconButton.getMiniIconPosition());
        StackPane.setMargin(miniIcon, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
    }

    /**
     * bind the size of the mini-icon to the button size
     */
    private void addImageViewSizeBindings() {
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        getSkinnable().widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(final ObservableValue o,
                                final Object oldVal,
                                final Object newVal) {
                calculateAndSetNewMiniIconSize(miniIconButton);
                getSkinnable().requestLayout();
            }
        });

        getSkinnable().heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(final ObservableValue o,
                                final Object oldVal,
                                final Object newVal) {
                calculateAndSetNewMiniIconSize(miniIconButton);
                getSkinnable().requestLayout();
            }
        });
    }

    /**
     * Calculates the mini icons size.
     * The max height and width are the original height and width of the image
     * to avoid pixel blocks instead of a good looking icon.
     * @param miniIconButton the mini icon button
     */
    private void calculateAndSetNewMiniIconSize(final MiniIconButton miniIconButton) {
        final ImageView miniIcon = miniIconButton.getMiniIcon();

        // multiply the button width and height with the ratio
        final double buttonWidth = getSkinnable().getWidth() * miniIconButton.getMiniIconRatio();
        final double buttonHeight = getSkinnable().getHeight() * miniIconButton.getMiniIconRatio();

        double fitWidth = buttonWidth;
        double fitHeight = buttonHeight;

        // If there is an original image, try to use the original size
        // as maximum.
        // I am not sure if this is a legal case, but the ImageView
        // can have an empty image. Perhaps there is an usecase I can not
        // think of. Therefor this code avoids an NPE and calculates
        // the width and height only based on the buttons size.
        if (miniIcon.getImage() != null) {
            // the original values of the image
            final double originalWidth = miniIcon.getImage().getWidth();
            final double originalHeight = miniIcon.getImage().getHeight();

            // set the minimum as the requested width and height
            fitWidth = Math.min(originalWidth, buttonWidth);
            fitHeight = Math.min(originalHeight, buttonHeight);
        }

        // check if a value is zero or negative, ignore this value
        miniIcon.setPreserveRatio(true);
        if (fitWidth > 0) {
            miniIcon.setFitWidth(fitWidth);
        }

        if (fitHeight > 0) {
            miniIcon.setFitHeight(fitHeight);
        }

    }

    /**
     * Add the animation based on the animation type.
     * @param miniIconButton the mini icon button
     */
    private void startAnimation(final MiniIconButton miniIconButton) {

        switch (miniIconButton.getAnimationType()) {
            case BLINK:
                jumpTransition.stop();
                blinkTimeline.play();
                break;
            case JUMP:
                blinkTimeline.stop();
                jumpTransition.play();
                break;
            case NONE:
            default:
                blinkTimeline.stop();
                jumpTransition.stop();
                break;
        }
    }

    /**
     * Stops the animation and resets the start values
     * @param miniIconButton the mini icon button
     */
    private void stopAnimation(final MiniIconButton miniIconButton) {
        final ImageView miniIcon = miniIconButton.getMiniIcon();
        jumpTransition.stop();
        blinkTimeline.stop();

        miniIcon.setOpacity(1.0);
        miniIcon.setTranslateY(0.0);
    }

    /**
     * The jump animation changes the position of the mini-icon.
     */
    private void defaultConfigJumpingAnimation() {
        final double start = 0.0;
        final double end = -JUMP_DISTANCE;
        jumpTransition.setFromY(start);
        jumpTransition.setToY(end);
        jumpTransition.setCycleCount(-1);
        jumpTransition.setAutoReverse(true);
        jumpTransition.setInterpolator(Interpolator.EASE_BOTH);
    }

    /**
     * Blinking animation changes the opacity of the mini-icon.
     */
    private void defaultConfigBlinkingAnimation() {
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.setAutoReverse(true);
    }
}
