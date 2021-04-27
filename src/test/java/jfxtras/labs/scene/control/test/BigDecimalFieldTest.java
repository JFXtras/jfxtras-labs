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
package jfxtras.labs.scene.control.test;

import static javafx.scene.input.KeyCode.ENTER;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.labs.test.JFXtrasGuiTest;

/**
 * GUI-Test for {@link jfxtras.labs.scene.control.BigDecimalField}
 * Created by Thomas Bolz on 01.01.14.
 */
@Ignore // tests fail with null pointer exceptions because it is looking in target/test-classes not classes. But this is labs so I'm not going to fix this, that's up to the original developer
public class BigDecimalFieldTest extends JFXtrasGuiTest {

    public Parent getRootNode() {
        StackPane root = new StackPane();

        nf = NumberFormat.getNumberInstance(Locale.UK);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        bigDecimalField = new BigDecimalField(new BigDecimal(10), new BigDecimal(2), nf);
        root.getChildren().addAll(bigDecimalField);

        return root;
    }

    BigDecimalField bigDecimalField;
    BigDecimalField bigDecimalField2;
    NumberFormat nf;
    private final String NUMBER = "12345.6789";

    @Test
    public void checkArrowButtonsAndStepwidth() {
        Assert.assertEquals(new BigDecimal(10), bigDecimalField.getNumber());

        push(KeyCode.UP);
        Assert.assertEquals(new BigDecimal(12), bigDecimalField.getNumber());
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        Assert.assertEquals(new BigDecimal(8), bigDecimalField.getNumber());
    }

    @Test
    public void checkFormatting() {
        type(NUMBER);
        push(ENTER);
        Assert.assertEquals("12,345.68", bigDecimalField.getText());
    }

    @Test
    public void checkFormatSwitch() {
        type(NUMBER);
        push(ENTER);
        Assert.assertEquals("12,345.68", bigDecimalField.getText());
        nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(3);
        bigDecimalField.setFormat(nf);
        Assert.assertEquals("12.345,679", bigDecimalField.getText());
    }

}


