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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 28.12.11
 * Time: 10:04
 */
public class Marker {
    private DoubleProperty        value   = new SimpleDoubleProperty();
    private ObjectProperty<Color> color   = new SimpleObjectProperty<Color>(Color.TRANSPARENT);
    private StringProperty        text    = new SimpleStringProperty("");
    private BooleanProperty       visible = new SimpleBooleanProperty();


    // ******************** Constructors **************************************
    public Marker() {
    }

    public Marker(final double VALUE, final Color COLOR) {
        this(VALUE, COLOR, Double.toString(VALUE));
    }

    public Marker(final double VALUE, final Color COLOR, final String TEXT) {
        this(VALUE, COLOR, TEXT, true);
    }

    public Marker(final double VALUE, final Color COLOR, final String TEXT, final boolean VISIBLE) {
        setValue(VALUE);
        setColor(COLOR);
        setText(TEXT);
        setVisible(VISIBLE);
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<MarkerEvent>> onMarkerEventProperty() {
        return onMarkerEvent;
    }

    public final void setOnMarkerEvent(final EventHandler<MarkerEvent> HANDLER) {
        onMarkerEventProperty().set(HANDLER);
    }

    public final EventHandler<MarkerEvent> getOnMarkerEvent() {
        return onMarkerEventProperty().get();
    }

    private ObjectProperty<EventHandler<MarkerEvent>> onMarkerEvent = new SimpleObjectProperty<EventHandler<MarkerEvent>>();

    public void fireMarkerEvent(final MarkerEvent MARKER_EVENT) {
        final EventHandler<MarkerEvent> MARKER_EVENT_HANDLER = getOnMarkerEvent();
        if (MARKER_EVENT_HANDLER != null) {
            MARKER_EVENT_HANDLER.handle(MARKER_EVENT);
        }
    }


    // ******************** Methods *******************************************
    public final double getValue() {
        return value.get();
    }

    public final void setValue(final double VALUE) {
        value.set(VALUE);
    }

    public final DoubleProperty valueProperty() {
        return value;
    }

    public final Color getColor() {
        return color.get();
    }

    public final void setColor(final Color COLOR) {
        color.set(COLOR);
    }

    public final ObjectProperty<Color> colorProperty() {
        return color;
    }

    public final String getText() {
        return text.get();
    }

    public final void setText(final String TEXT) {
        text.set(TEXT);
    }

    public final StringProperty textProperty() {
        return text;
    }

    public final boolean isVisible() {
        return visible.get();
    }

    public final void setVisible(final boolean VISIBLE) {
        visible.set(VISIBLE);
    }

    public final BooleanProperty visibleProperty() {
        return visible;
    }

    public final boolean equals(final Marker MARKER) {
        return Double.compare(MARKER.getValue(), getValue()) == 0 &&
               MARKER.getColor().equals(getColor()) &&
               MARKER.getText().equals(getText());
    }
}
