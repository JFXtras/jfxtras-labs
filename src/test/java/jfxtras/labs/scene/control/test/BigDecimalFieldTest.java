package jfxtras.labs.scene.control.test;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import jfxtras.labs.scene.control.BigDecimalField;
import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import static javafx.scene.input.KeyCode.ENTER;
/**
 * GUI-Test for {@link jfxtras.labs.scene.control.BigDecimalField}
 * Created by Thomas Bolz on 01.01.14.
 */
public class BigDecimalFieldTest extends GuiTest {

    public Parent getRootNode() {
        StackPane root = new StackPane();

        nf = NumberFormat.getNumberInstance(Locale.UK);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        bigDecimalField = new BigDecimalField(new BigDecimal(10), new BigDecimal(2), nf);
        root.getChildren().add(bigDecimalField);

        return root;
    }

    BigDecimalField bigDecimalField;
    NumberFormat nf;
    private final String NUMBER = "12345.6789";

    @Test
    public void checkArrowButtonsAndStepwidth() {
        Assert.assertEquals(new BigDecimal(10), bigDecimalField.getNumber());

        click(bigDecimalField).push(KeyCode.UP);
        Assert.assertEquals(new BigDecimal(12), bigDecimalField.getNumber());
        click(bigDecimalField).push(KeyCode.DOWN);
        click(bigDecimalField).push(KeyCode.DOWN);
        Assert.assertEquals(new BigDecimal(8), bigDecimalField.getNumber());
    }
    @Test
    public void checkFormatting() {
        click(bigDecimalField);
        type(NUMBER);
        click(bigDecimalField).push(ENTER);
        Assert.assertEquals("12,345.68", bigDecimalField.getText());
        Assert.assertEquals(new BigDecimal(NUMBER), bigDecimalField.getNumber());
    }

    @Test
    public void checkFormatSwitch() {
        click(bigDecimalField);
        type(NUMBER);
        click(bigDecimalField).push(ENTER);
        Assert.assertEquals("12,345.68", bigDecimalField.getText());
        nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(3);
        bigDecimalField.setFormat(nf);
        Assert.assertEquals("12.345,679", bigDecimalField.getText());
    }
}


