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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 21.03.12
 * Time: 11:52
 */
public class StepIndicator extends Control {
    private static final String                     DEFAULT_STYLE_CLASS = "step-indicator";
    private ObjectProperty<EventHandler<StepEvent>> onStepEvent         = new SimpleObjectProperty<EventHandler<StepEvent>>();
    private ObjectProperty<Color>                   color;
    private IntegerProperty                         noOfSteps;
    private IntegerProperty                         currentStep;
    private int                                     selectedStep;


    // ******************** Constructors **************************************
    public StepIndicator() {
        this(Color.rgb(138, 205, 250));
    }

    public StepIndicator(final Color COLOR) {
        color       = new SimpleObjectProperty<Color>(COLOR);
        noOfSteps = new SimpleIntegerProperty(5);
        currentStep = new SimpleIntegerProperty(0);

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

    public final int getNoOfSteps() {
        return noOfSteps.get();
    }

    public final void setNoOfSteps(final int NO_OF_STEPS) {
        noOfSteps.set(NO_OF_STEPS < 1 ? 1 : (NO_OF_STEPS > 20 ? 20 : NO_OF_STEPS));
    }

    public final IntegerProperty noOfStepsProperty() {
        return noOfSteps;
    }

    public final int getCurrentStep() {
        return currentStep.get();
    }

    public final void setCurrentStep(final int CURRENT_STEP) {
        currentStep.set(CURRENT_STEP < 0 ? 0 : (CURRENT_STEP > noOfSteps.get() ? noOfSteps.get() : CURRENT_STEP));
    }

    public final IntegerProperty currentStepProperty() {
        return currentStep;
    }

    public final void next() {
        if (currentStep.get() < noOfSteps.get()) {
            currentStep.set(currentStep.get() + 1);
        }
    }

    public final void back() {
        if (currentStep.get() > 0) {
            currentStep.set(currentStep.get() - 1);
        }
    }

    public final void setSelectedStep(final int SELECTED_STEP) {
        selectedStep = SELECTED_STEP;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        final double SIZE = WIDTH < (HEIGHT) ? (WIDTH) : HEIGHT;
        super.setPrefSize(noOfSteps.get() * SIZE, SIZE);
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<StepEvent>> onStepEventProperty() {
        return onStepEvent;
    }

    public final void setOnStepEvent(final EventHandler<StepEvent> HANDLER) {
        onStepEventProperty().set(HANDLER);
    }

    public final EventHandler<StepEvent> getOnStepEvent() {
        return onStepEventProperty().get();
    }

    public void fireStepEvent() {
        if (selectedStep > 0) {
            final EventHandler<StepEvent> MODEL_EVENT_HANDLER = getOnStepEvent();
            if (MODEL_EVENT_HANDLER != null) {
                final StepEvent Step_EVENT = new StepEvent(selectedStep);
                MODEL_EVENT_HANDLER.handle(Step_EVENT);
            }
        }
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }


    // ******************** Custom event **************************************
    public class StepEvent extends Event {
        private final int INDEX;


        // ******************** Constructors **************************************
        public StepEvent(final int INDEX) {
            super(new EventType<StepEvent>());
            this.INDEX = INDEX;
        }

        public StepEvent(final Object source, final EventTarget target, final int INDEX) {
            super(source, target, new EventType<StepEvent>());
            this.INDEX = INDEX;
        }

        public final int getIndex() {
            return INDEX;
        }
    }
}
