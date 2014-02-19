/**
 * BigDecimalLabel.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Label implementation that displays a formatted BigDecimal.
 *
 * @author Thomas Bolz
 */
public class BigDecimalLabel extends Label {

    private ObjectProperty<BigDecimal> number = new SimpleObjectProperty();

    public final BigDecimal getNumber() {
        return number.get();
    }

    public final void setNumber(BigDecimal value) {
        number.set(value);
    }

    public ObjectProperty<BigDecimal> numberProperty() {
        return number;
    }

    final private ObjectProperty<NumberFormat> format = new SimpleObjectProperty();
    public NumberFormat getFormat() {return format.getValue();}
    public final void setFormat(NumberFormat value) {format.set(value);}
    public ObjectProperty<NumberFormat> formatProperty() {return format;}

    public BigDecimalLabel() {
        this(BigDecimal.ZERO);
    }

    public BigDecimalLabel(BigDecimal value) {
        this(value, NumberFormat.getInstance());
    }

    public BigDecimalLabel(BigDecimal value, NumberFormat nf) {
        super();
        setFormat(nf);
        initHandlers();
        setNumber(value);
    }

    private void initHandlers() {

        // Text is formatted and displayed if the underlying number or format changes.
        numberProperty().addListener(new ChangeListener<BigDecimal>() {

            @Override
            public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldValue, BigDecimal newValue) {
                setText(getFormattedString());
            }
        });
        // Text is formatted and displayed if the underlying number or format changes.
        formatProperty().addListener(new ChangeListener<NumberFormat>() {

            @Override
            public void changed(ObservableValue<? extends NumberFormat> observable, NumberFormat olValue, NumberFormat newValue) {
                setText(getFormattedString());
            }

        });
    }
    private String getFormattedString() {
        if(getNumber() == null || getFormat() == null) {
            return null;
        }
        try {
            String formattedString = getFormat().format(getNumber());
            return formattedString;
        } catch (Exception e) {
            setText("n/a");
            e.printStackTrace();
            return null;
        }
    }

}
