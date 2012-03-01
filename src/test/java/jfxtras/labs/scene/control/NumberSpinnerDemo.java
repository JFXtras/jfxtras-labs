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
package jfxtras.labs.scene.control;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Thomas Bolz
 */
public class NumberSpinnerDemo extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Spinner Demo");
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        final NumberSpinner defaultSpinner = new NumberSpinner();
        final NumberSpinner decimalFormat = new NumberSpinner(BigDecimal.ZERO, new BigDecimal("0.05"), new DecimalFormat("#,##0.00"));
        final NumberSpinner percent = new NumberSpinner(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getPercentInstance());
        final NumberSpinner localizedCurrency = new NumberSpinner(BigDecimal.ZERO, new BigDecimal("0.01"), NumberFormat.getCurrencyInstance(Locale.UK));
        root.addRow(1, new Label("default"), defaultSpinner);
        root.addRow(2, new Label("custom decimal format"), decimalFormat);
        root.addRow(3, new Label("percent"), percent);
        root.addRow(4, new Label("localized currency"), localizedCurrency);
        Button button = new Button("Dump layout bounds");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                defaultSpinner.dumpSizes();
            }
        });
        root.addRow(5, new Label(), button);

        final ChoiceBox styles = new ChoiceBox(FXCollections.observableArrayList("squared", "rounded"));
        styles.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("index" + arg2.intValue());
                Scene scene = styles.getScene();
                if (arg2.intValue() == 0) {
                    String path = NumberSpinner.class.getResource("NumberSpinnerSquared.css").toExternalForm();
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(path);
                }
                if (arg2.intValue() == 1) {
                    String path = NumberSpinner.class.getResource("NumberSpinnerRounded.css").toExternalForm();
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(path);
                }
            }
        });

        root.addRow(6, new Label("change css"), styles);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        styles.getSelectionModel().select(0);
    }
}
