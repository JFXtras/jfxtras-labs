package jfxtras.labs.scene.control.test;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.util.StringConverterFactory;
import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 * Created by user on 26-12-13.
 */
public class ListSpinnerTest1 extends GuiTest {
	public Parent getRootNode()
	{
		VBox box = new VBox();

		lSpinner = new ListSpinner<String>("a", "b", "c")
				.withEditable(true).withStringConverter(StringConverterFactory.forString())
				.withCyclic(true)
				;
		box.getChildren().add(lSpinner);
		box.getChildren().add(new Button("focus helper"));

		return box;
	}
	ListSpinner<String> lSpinner = null;

	@Test
	public void shouldBeAbleToDragFileToTrashCan()
	{
		// check to see what the current value is
		Assert.assertEquals("a", lSpinner.getValue());

		// select next
		click(".right-arrow");
		Assert.assertEquals("b", lSpinner.getValue());

		// select next
		click(".right-arrow");
		Assert.assertEquals("c", lSpinner.getValue());

		// select next (cyclic)
		click(".right-arrow");
		Assert.assertEquals("a", lSpinner.getValue());
	}
}
