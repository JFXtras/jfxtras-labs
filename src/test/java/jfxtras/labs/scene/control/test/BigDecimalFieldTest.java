/**
 * BigDecimalFieldTest.java
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

package jfxtras.labs.scene.control.test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.BigDecimalField;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.TAB;

/**
 * GUI-Test for {@link jfxtras.labs.scene.control.BigDecimalField}
 * Created by Thomas Bolz on 01.01.14.
 */
public class BigDecimalFieldTest extends GuiTest {

    public Parent getRootNode() {
        StackPane root = new StackPane();

        nf = NumberFormat.getNumberInstance(Locale.UK);
        cf = NumberFormat.getCurrencyInstance(Locale.UK);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        bigDecimalField1 = new BigDecimalField(new BigDecimal(10), new BigDecimal(2), nf);
        bigDecimalField2 = new BigDecimalField(new BigDecimal(20), new BigDecimal(2), nf);
        bigDecimalField3 = new BigDecimalField(new BigDecimal(30), new BigDecimal(2), cf);
        root.getChildren().addAll(new VBox(bigDecimalField1, bigDecimalField2, bigDecimalField3));

        return root;
    }

    BigDecimalField bigDecimalField1;
    BigDecimalField bigDecimalField2;
    BigDecimalField bigDecimalField3;
    NumberFormat nf;
    NumberFormat cf;
    private final String NUMBER = "12345.6789";

    @Test
    public void checkArrowButtonsAndStepwidth() {
        Platform.runLater(() -> bigDecimalField1.requestFocus());
        sleep(1, TimeUnit.SECONDS);
        Assert.assertEquals(new BigDecimal(10), bigDecimalField1.getNumber());

        push(KeyCode.UP);
        Assert.assertEquals(new BigDecimal(12), bigDecimalField1.getNumber());
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        Assert.assertEquals(new BigDecimal(8), bigDecimalField1.getNumber());
    }
    @Test
    public void checkFormatting() {
        Platform.runLater(() -> bigDecimalField2.requestFocus());
        sleep(1, TimeUnit.SECONDS);
        type(NUMBER);
        push(ENTER);
        Assert.assertEquals("12,345.68", bigDecimalField2.getText());
    }

    @Test
    public void checkFormatSwitch() {
        Platform.runLater(() -> bigDecimalField1.requestFocus());
        sleep(1, TimeUnit.SECONDS);
        type(NUMBER);
        push(ENTER);
        Assert.assertEquals("12,345.68", bigDecimalField1.getText());
        nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(3);
        bigDecimalField1.setFormat(nf);
        Assert.assertEquals("12.345,679", bigDecimalField1.getText());
    }
    
    @Test
    public void checkSpecialFormattingUpDown() {
        Platform.runLater(() -> bigDecimalField3.requestFocus());
        sleep(1, TimeUnit.SECONDS);
        push(KeyCode.UP);
        Assert.assertEquals("\u00A332.00", bigDecimalField3.getText());
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        Assert.assertEquals("\u00A328.00", bigDecimalField3.getText());
    }
    
    @Test
    public void checkSpecialFormatting() {
        Platform.runLater(() -> bigDecimalField3.requestFocus());
        sleep(1, TimeUnit.SECONDS);
        type(NUMBER);
        push(ENTER);
        Assert.assertEquals("\u00A312,345.68", bigDecimalField3.getText());
    }

}


