/**
 * BigDecimalFieldDemo.java
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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarTextField;


/**
 * Small JavaFX app that showcases the usage of {@link jfxtras.labs.scene.control.BigDecimalField},
 * {@link jfxtras.labs.scene.control.BigDecimalLabel} and {@link CalendarLabel}.
 *
 * @author Thomas Bolz
 */
public class BigDecimalFieldDemo extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX BigDecimalField Demo");
        ObjectProperty<DateFormat> dateFormatProperty = new SimpleObjectProperty<>(DateFormat.getDateInstance());
        ObjectProperty<NumberFormat> numberFormatProperty = new SimpleObjectProperty<>(NumberFormat.getNumberInstance());
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        final BigDecimalField defaultSpinner = new BigDecimalField();
        final BigDecimalField decimalFormat = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.05"), new DecimalFormat("#,##0.00"));
        final BigDecimalField percent = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getPercentInstance());
        final BigDecimalField localizedCurrency = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getCurrencyInstance(Locale.UK));
        final BigDecimalField promptText = new BigDecimalField();
        promptText.setNumber(null);
        promptText.setPromptText("Enter something");
        int rowIndex = 1;
        root.addRow(rowIndex++, new Label("default"), defaultSpinner);
        root.addRow(rowIndex++, new Label("custom decimal format"), decimalFormat);
        root.addRow(rowIndex++, new Label("percent"), percent);
        root.addRow(rowIndex++, new Label("localized currency"), localizedCurrency);
        final BigDecimalField disabledField = new BigDecimalField();
        disabledField.setDisable(true);
        root.addRow(rowIndex++, new Label("disabled field"), disabledField);
        TextField textField;
        root.addRow(rowIndex++, new Label("regular TextField"), textField = new TextField("1.000,1234"));
        root.addRow(rowIndex++, new Label("with promptText"), promptText);
        CalendarTextField calendarTextField = new CalendarTextField();
        root.addRow(rowIndex++, new Label("CalendarTextField"), calendarTextField);
        ComboBox<Locale> cmbLocales = new ComboBox<>(FXCollections.observableArrayList(Locale.GERMANY, Locale.UK, Locale.FRANCE));
        cmbLocales.setOnAction(event -> {
            dateFormatProperty.set(DateFormat.getDateInstance(DateFormat.MEDIUM, cmbLocales.getValue()));
            numberFormatProperty.set(NumberFormat.getNumberInstance(cmbLocales.getValue()));
        });
        root.addRow(rowIndex++, new Label("Locale"), cmbLocales);

        root.addRow(rowIndex++, new Label("Field with boundaries (0,100%)"),
                BigDecimalFieldBuilder.create()
                        .number(new BigDecimal("0.1"))
                        .minValue(BigDecimal.ZERO)
                        .maxValue(BigDecimal.ONE)
                        .stepwidth(new BigDecimal("0.01"))
                        .format(DecimalFormat.getPercentInstance())
                        .build()
        );

        promptText.numberProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observableValue, BigDecimal o, BigDecimal o1) {
                System.out.println(o1);
            }
        });

        Button button = new Button("Reset fields");
        button.setOnAction((ActionEvent event) -> {
            defaultSpinner.setNumber(new BigDecimal(Math.random() * 1000));
            decimalFormat.setNumber(new BigDecimal(Math.random() * 1000));
            percent.setNumber(new BigDecimal(Math.random()));
            localizedCurrency.setNumber(new BigDecimal(Math.random() * 1000));
            disabledField.setNumber(new BigDecimal(Math.random() * 1000));
            promptText.setNumber(null);
            calendarTextField.setCalendar(Calendar.getInstance());
            decimalFormat.requestFocus();
        });
        root.addRow(rowIndex++, new Label(), button);
        BigDecimalLabel bigDecimalLabel = new BigDecimalLabel();
        bigDecimalLabel.numberProperty().bind(defaultSpinner.numberProperty());
        bigDecimalLabel.formatProperty().bind(numberFormatProperty);
        root.addRow(rowIndex++, new Label("BigDecimalLabel"), bigDecimalLabel);

        CalendarLabel calendarLabel = new CalendarLabel();
        calendarLabel.valueProperty().bind(calendarTextField.calendarProperty());
        calendarLabel.formatProperty().bind(dateFormatProperty);
        root.addRow(rowIndex++, new Label("CalendarLabel"), calendarLabel);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                decimalFormat.requestFocus();
//                defaultSpinner.requestFocus();
//                calendarTextField.requestFocus();
            }
        });
        decimalFormat.focusedProperty().addListener((observableValue, oldValue, newValue) -> System.out.println("focus changed on decimalFormat from " + oldValue + " to " + newValue));
        calendarTextField.focusedProperty().addListener((observableValue, oldValue, newValue) -> System.out.println("focus changed on calendarTextField from " + oldValue + " to " + newValue));
        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> System.out.println("focus changed on textField from " + oldValue + " to " + newValue));
        decimalFormat.requestFocus();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
