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

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    public MiniIconButtonSkin(MiniIconButton miniIconButton) {
        super(miniIconButton);
        createMiniIconButton();
        addImageViewSizeBindings();
        addAnimation();
    }

    @Override
    protected void layoutChildren() {

        // layouts the other children
        super.layoutChildren();

        // layouts the mini icon
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        final ImageView miniIcon = miniIconButton.getMiniIcon();

        final double width = getWidth();
        final double height = getHeight();
        final double top = 0.0;
        final double left = 0.0;
        final double baselineOffset = getBaselineOffset();

        final Pos childAlignment = StackPane.getAlignment(miniIcon);
        layoutInArea(miniIcon, left, top,
                width, height,
                baselineOffset, getMargin(miniIcon),
                childAlignment != null? childAlignment.getHpos() : getAlignment().getHpos(),
                childAlignment != null? childAlignment.getVpos() : getAlignment().getVpos());
    }

    private void createMiniIconButton() {
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        final ImageView miniIcon = miniIconButton.getMiniIcon();
        StackPane.setAlignment(miniIcon, miniIconButton.getMiniIconPosition());
        StackPane.setMargin(miniIcon, new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
        getChildren().addAll(miniIcon);
    }

    /**
     * bind the size of the mini-icon to the button size
     */
    private void addImageViewSizeBindings() {
        widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                calculateAndSetNewMiniIconSize();
            }
        });

        heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal) {
                calculateAndSetNewMiniIconSize();
            }
        });
    }

    /**
     * Calculates the mini icons size.
     * The max height and width are the original height and width of the image
     * to avoid pixel blocks instead of a good looking icon.
     */
    private void calculateAndSetNewMiniIconSize() {
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        final ImageView miniIcon = miniIconButton.getMiniIcon();

        // multiply the button width and height with the ratio
        final double buttonWidth = getWidth() * miniIconButton.getMiniIconRatio();
        final double buttonHeight = getHeight() * miniIconButton.getMiniIconRatio();

        double fitWidth = buttonWidth;
        double fitHeight = buttonHeight;

        // If there is an original image, try to use the original size
        // as maximum.
        // I am not sure if this is a legal case, but the ImageView
        // can have an empty image. Perhaps there is an usecase I can´t
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
     */
    private void addAnimation() {
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        switch (miniIconButton.getAnimationType()) {
            case BLINK:
                addBlinkingAnimation();
                break;
            case JUMP:
                addJumpingAnimation();
                break;
            case NONE:
                // none is the default case
            default:
                // noting to animate
                break;
        }
    }

    /**
     * The jump animation changes the position of the mini-icon.
     */
    private void addJumpingAnimation() {
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        final ImageView miniIcon = miniIconButton.getMiniIcon();
        final TranslateTransition translateTransition =  new TranslateTransition(Duration.millis(miniIconButton.getAnimationDuration()), miniIcon);
        final double start = 0.0;
        final double end = -JUMP_DISTANCE;
        translateTransition.setFromY(start);
        translateTransition.setToY(end);
        translateTransition.setCycleCount(-1);
        translateTransition.setAutoReverse(true);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        translateTransition.play();
    }

    /**
     * Blinking animation changes the opacity of the mini-icon.
     */
    private void addBlinkingAnimation() {
        final MiniIconButton miniIconButton = (MiniIconButton)getSkinnable();
        final ImageView miniIcon = miniIconButton.getMiniIcon();
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(miniIcon.opacityProperty(), MINIMUM_OPACITY_FOR_BLINKING);
        final KeyFrame kf = new KeyFrame(Duration.millis(miniIconButton.getAnimationDuration()), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
}
