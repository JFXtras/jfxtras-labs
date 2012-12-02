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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Shape;
import jfxtras.labs.util.Util;


/**
 *
 * @author hansolo
 */
public class Section {
    private DoubleProperty         start;
    private DoubleProperty         stop;
    private ObjectProperty<Color>  color;
    private StringProperty         cssColor;
    private ObjectProperty<Color>  transparentColor;
    private ObjectProperty<Color>  highlightColor;
    private ObjectProperty<Color>  transparentHighlightColor;
    private ObjectProperty<Paint>  paint;
    private ObjectProperty<Shape>  sectionArea;
    private ObjectProperty<Shape>  filledArea;
    private StringProperty         text;


    // ******************** Constructors **************************************
    public Section() {
        this(-1, -1, Color.RED, "");
    }

    public Section(final double START, final double STOP, final Color COLOR) {
        this(START, STOP, COLOR, "");
    }

    public Section(final double START, final double STOP, final Color COLOR, final String TEXT) {
        this(START, STOP, COLOR, COLOR.brighter().brighter(), null, null, TEXT);
    }

    public Section(final double START, final double STOP, final Color COLOR, final Color HIGHLIGHT_COLOR) {
        this(START, STOP, COLOR, HIGHLIGHT_COLOR, null, null, "");
    }

    public Section(final double START, final double STOP, final Color COLOR, final Color HIGHLIGHT_COLOR, final String TEXT) {
        this(START, STOP, COLOR, HIGHLIGHT_COLOR, null, null, TEXT);
    }

    public Section(final double START, final double STOP, final Color COLOR, final Arc FILLED_AREA) {
        this(START, STOP, COLOR, null, FILLED_AREA);
    }

    public Section(final double START, final double STOP, final Color COLOR, final Shape SECTION_AREA, final Arc FILLED_AREA) {
        this(START, STOP, COLOR, COLOR.brighter().brighter(), SECTION_AREA, FILLED_AREA, "");
    }

    public Section(final double START, final double STOP, final Color COLOR, final Color HIGHLIGHT_COLOR, final Shape SECTION_AREA, final Shape FILLED_AREA, final String TEXT) {
        start                     = new SimpleDoubleProperty(START);
        stop                      = new SimpleDoubleProperty(STOP);
        color                     = new SimpleObjectProperty<Color>(COLOR);
        transparentColor          = new SimpleObjectProperty<Color>(Color.color(COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), 0.3));
        highlightColor            = new SimpleObjectProperty<Color>(HIGHLIGHT_COLOR);
        transparentHighlightColor = new SimpleObjectProperty<Color>(Color.color(COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), 0.5));
        sectionArea               = new SimpleObjectProperty<Shape>(SECTION_AREA);
        filledArea                = new SimpleObjectProperty<Shape>(FILLED_AREA);
        paint                     = new SimpleObjectProperty<Paint>(COLOR);
        text                      = new SimpleStringProperty(TEXT);
        cssColor                  = new SimpleStringProperty(Util.colorToCssColor(COLOR));
    }


    // ******************** Methods *******************************************
    public final double getStart() {
        return start.get();
    }

    public final void setStart(final double START) {
        start.set(START);
    }

    public final DoubleProperty startProperty() {
        return start;
    }

    public final double getStop() {
        return stop.get();
    }

    public final void setStop(final double STOP) {
        stop.set(STOP);
    }

    public final DoubleProperty stopProperty() {
        return stop;
    }

    public final Color getColor() {
        return color.get();
    }

    public final void setColor(final Color COLOR) {
        color.set(COLOR);
        transparentColor.set(Color.color(COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), 0.25));
        cssColor.set(Util.colorToCssColor(COLOR));
    }

    public final ObjectProperty<Color> colorProperty() {
        return color;
    }

    public final Color getTransparentColor() {
        return transparentColor.get();
    }

    public final ObjectProperty<Color> transparentColorProperty() {
        return transparentColor;
    }

    public final Color getHighlightColor() {
        return highlightColor.get();
    }

    public final void setHighlightColor(final Color COLOR) {
        highlightColor.set(COLOR);
        transparentHighlightColor.set(Color.color(COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), 0.5));
    }

    public final ObjectProperty<Color> highlightColorProperty() {
        return highlightColor;
    }

    public final Color getTransparentHighlightColor() {
        return transparentHighlightColor.get();
    }

    public final ObjectProperty<Color> transparentHighlightColorProperty() {
        return transparentHighlightColor;
    }

    public final String getCssColor() {
        return cssColor.get();
    }

    public final StringProperty cssColorProperty() {
        return cssColor;
    }

    public final Shape getSectionArea() {
        return sectionArea.get();
    }

    public final void setSectionArea(final Shape SECTION_AREA) {
        sectionArea.set(SECTION_AREA);
    }

    public final ObjectProperty<Shape> sectionAreaProperty() {
        return sectionArea;
    }

    public final Shape getFilledArea() {
        return filledArea.get();
    }

    public final void setFilledArea(final Shape FILLED_AREA) {
        filledArea.set(FILLED_AREA);
    }

    public final ObjectProperty<Shape> filledAreaProperty() {
        return filledArea;
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

    public final Paint getPaint() {
        return paint.get();
    }

    public final void setPaint(final Paint PAINT) {
        paint.set(PAINT);
    }

    public final ObjectProperty<Paint> paintProperty() {
        return paint;
    }

    public boolean contains(final double VALUE) {
        return ((Double.compare(VALUE, start.get()) >= 0 && Double.compare(VALUE, stop.get()) <= 0));
    }

    public boolean equals(final Section SECTION) {
        return (Double.compare(SECTION.getStart(), getStart()) == 0 &&
                Double.compare(SECTION.getStop(), getStop()) == 0 &&
                SECTION.getColor().equals(getColor()) &&
                SECTION.getText().equals(getText()));
    }

    @Override public String toString() {
        final StringBuilder NAME = new StringBuilder();
        NAME.append("Section: ").append("\n");
        NAME.append("startValue: ").append(start.get()).append("\n");
        NAME.append("stopValue : ").append(stop.get()).append("\n");
        NAME.append("color     : ").append(color.toString());
        return NAME.toString();
    }
}
