/**
 * MiniIconButton.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;

/**
 * The MiniIconButton is an extension of the standard button.
 * It gets a second icon, which can be placed above the normal
 * button. The icon is minified and shown
 *
 *
 * @author Andreas Billmann
 */
public class MiniIconButton extends Button {

    /**
     * Type of animation
     */
    public enum AnimationType { NONE, JUMP, BLINK };

    private ObjectProperty<ImageView> miniIcon;
    private ObjectProperty<AnimationType> animationType = new SimpleObjectProperty<AnimationType>(this, "animationType", AnimationType.NONE);
    private DoubleProperty animationDuration = new SimpleDoubleProperty(this, "animationDuration", 500);
    private ObjectProperty<Pos> miniIconPosition = new SimpleObjectProperty<Pos>(this, "miniIconPosition", Pos.TOP_RIGHT);
    private DoubleProperty miniIconRatio = new SimpleDoubleProperty(this, "miniIconRatio", 0.25);

    /**
     * creates an empty mini icon button
     */
    public MiniIconButton() {
        super();
        init();
    }

    /**
     * creates an empty button with a mini icon set
     * @param miniIcon the mini icon shown in front of the button
     */
    public MiniIconButton(final ImageView miniIcon) {
        super();
        setMiniIcon(miniIcon);
        init();
    }

    /**
     * creates a text button with a mini icon set
     * @param text A text string for its label.
     * @param miniIcon the mini icon shown in front of the button
     */
    public MiniIconButton(final String text, final ImageView miniIcon) {
        super(text);
        setMiniIcon(miniIcon);
        init();
    }

    /**
     * creates a button with icon and a mini icon set
     * @param graphic the icon for its label.
     * @param miniIcon the mini icon shown in front of the button
     */
    public MiniIconButton(final Node graphic, final ImageView miniIcon) {
        super(null, graphic);
        setMiniIcon(miniIcon);
        init();
    }


    /**
     * creates a button with text and icon and a mini icon set
     * @param text A text string for its label.
     * @param graphic the icon for its label.
     * @param miniIcon the mini icon shown in front of the button
     */
    public MiniIconButton(final String text, final Node graphic, final ImageView miniIcon) {
        super(text, graphic);
        setMiniIcon(miniIcon);
        init();
    }

    /**
     * The property of the mini icon
     * @return property of the mini icon
     */
    public final ObjectProperty<ImageView> miniIconProperty() {
        if (miniIcon == null) {
            miniIcon = new ObjectPropertyBase<ImageView>() {
                @Override
                public Object getBean() {
                    return MiniIconButton.this;
                }

                @Override
                public String getName() {
                    return "miniIcon";
                }
            };
        }
        return miniIcon;
    }

    /**
     * Sets the mini icon
     * @param value node for the mini icon
     */
    public void setMiniIcon(final ImageView value) {
        miniIconProperty().setValue(value);
    }

    /**
     * Returns the mini icon
     * @return
     */
    public ImageView getMiniIcon() {
        return miniIcon == null ? null : miniIcon.getValue();
    }

    /**
     * The property for the animation type.
     * @return property of animation type
     * @see AnimationType
     */
    public final ObjectProperty<AnimationType> animationTypeProperty() {
        return this.animationType;
    }

    /**
     * Returns the animation type. The default is AnimationType.NONE
     * @return the animation type
     * @see AnimationType
     */
    public AnimationType getAnimationType() {
        return this.animationType.getValue();
    }

    /**
     * Sets the animation type.
     * @param value
     * @see AnimationType
     */
    public void setAnimationType(final AnimationType value) {
        this.animationType.setValue(value);
    }

    /**
     * The mini-icon can be positioned with {@link Pos}
     * @return property for the position of the mini-icon
     */
    public final ObjectProperty<Pos> miniIconPositionProperty() {
        return miniIconPosition;
    }

    /**
     * Sets the position of the mini icon with {@link Pos}
     * @param value position of the mini-icon
     */
    public final void setMiniIconPosition(final Pos value) {
        miniIconPosition.set(value);
    }

    /**
     * Returns the position of the mini-icon based on {@link Pos}
     * @return position of the mini-icon
     */
    public final Pos getMiniIconPosition() {
        return miniIconPosition.getValue();
    }

    /**
     * This is the ratio of the mini-icon corresponding to the button.
     * It could be a double number between 0.01 and 1.0.
     * @return ratio of the mini-icon
     */
    public final DoubleProperty miniIconRatioProperty() {
        return miniIconRatio;
    }

    /**
     * Sets the ratio of the mini-icon corresponding to the buttons size
     * @param value could be a double number between 0.01 and 1.0 all others will be set to 0.01 or 1.0
     */
    public final void setMiniIconRatio(final double value) {
        miniIconRatio.set(value);
    }

    /**
     * Returns the value of the mini-icon ratio. The default is 0.25
     * @return the default value is 0.25
     */
    public final double getMiniIconRatio() {
        return miniIconRatio.getValue();
    }

    /**
     * This is the duration in milliseconds for the two animation types {@code AnimationType.BLINK} and
     * {@code AnimationType.JUMP} the default is 500
     * @return animation duration property
     */
    public final DoubleProperty animationDurationProperty() {
        return  animationDuration;
    }

    /**
     * Sets the animation duration in milli seconds
     * @param value animation duration in milliseconds
     */
    public final void setAnimationDuration(final double value) {
        animationDuration.set(value);
    }

    /**
     * Returns the duration in milliseconds for the two animation types {@code AnimationType.BLINK} and
     * {@code AnimationType.JUMP} the default is 500
     * @return animation duration in milliseconds, the default is 500
     */
    public final double getAnimationDuration() {
        return  animationDuration.getValue();
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("/jfxtras/labs/internal/scene/control/" + this.getClass().getSimpleName() + ".css").toString();
    }

    /**
     * sets the css class
     */
    private void init() {
        setStyle(null);
        getStyleClass().add("mini-icon-button");
        setContentDisplay(ContentDisplay.TOP);
        setMiniIconRatioRange();
    }

    /**
     * This listener guarantees that the mini icon ratio value is in the range of 0.01 and 1.0
     */
    private void setMiniIconRatioRange() {
        miniIconRatio.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (newValue.doubleValue() > 1.0) {
                    miniIconRatio.set(1.0);
                } else if (newValue.doubleValue() < 0.01) {
                    miniIconRatio.set(0.01);
                }
            }
        });
    }

}
