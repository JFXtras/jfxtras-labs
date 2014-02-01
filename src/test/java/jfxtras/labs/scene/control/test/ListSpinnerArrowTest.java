package jfxtras.labs.scene.control.test;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import jfxtras.labs.internal.scene.control.skin.ListSpinnerCaspianSkin;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.labs.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by tbee on 26-12-13.
 */
public class ListSpinnerArrowTest extends JFXtrasGuiTest {

	public Parent getRootNode()
	{
		VBox box = new VBox();

		spinner = new ListSpinner<String>("a", "b", "c");
		box.getChildren().add(spinner);

		return box;
	}
	private ListSpinner<String> spinner = null;

	@Test
	public void navigateLeftRightThroughTheValuesCyclic()
	{
		// non cyclic is the default
		spinner.cyclicProperty().set(true);

		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// move to next until we cycle over

		// select next
		click(".right-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".right-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (cyclic)
		click(".right-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// now move backwards, cycling back

		// select prev (cyclic)
		click(".left-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (cyclic)
		click(".left-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("b", spinner.getValue());
	}

	@Test
	public void navigateLeftRightThroughTheValuesNonCyclic()
	{
		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// move forward, non cyclic

		// select next
		click(".right-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".right-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (stick)
		click(".right-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// ----
		// move back, non cyclic

		// select prev
		click(".left-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".left-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (stick)
		click(".left-arrow");
		Assert.assertEquals("a", spinner.getValue());
	}

	@Test
	public void navigateUpDownThroughTheValuesCyclic()
	{
		// horizontal is the default
		PlatformUtil.runAndWait( () -> {
			// the CSS change is not processed correctly?
			//spinner.setStyle("-fxx-arrow-direction: " + ListSpinnerCaspianSkin.ArrowDirection.VERTICAL);
			((ListSpinnerCaspianSkin)spinner.getSkin()).arrowDirectionProperty().set(ListSpinnerCaspianSkin.ArrowDirection.VERTICAL);
		});

		// non cyclic is the default
		spinner.cyclicProperty().set(true);

		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// move to next until we cycle over

		// select next
		click(".up-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".up-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (cyclic)
		click(".up-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// now move backwards, cycling back

		// select prev (cyclic)
		click(".down-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (cyclic)
		click(".down-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("b", spinner.getValue());
	}

	@Test
	public void navigateUpDownThroughTheValuesNonCyclic()
	{
		// horizontal is the default
		PlatformUtil.runAndWait( () -> {
			// the CSS change is not processed correctly?
			//spinner.setStyle("-fxx-arrow-direction: " + ListSpinnerCaspianSkin.ArrowDirection.VERTICAL);
			((ListSpinnerCaspianSkin)spinner.getSkin()).arrowDirectionProperty().set(ListSpinnerCaspianSkin.ArrowDirection.VERTICAL);
		});

		// check to see what the current value is
		Assert.assertEquals("a", spinner.getValue());

		// ----
		// move forward, non cyclic

		// select next
		click(".up-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select next
		click(".up-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// select next (stick)
		click(".up-arrow");
		Assert.assertEquals("c", spinner.getValue());

		// ----
		// move back, non cyclic

		// select prev
		click(".down-arrow");
		Assert.assertEquals("b", spinner.getValue());

		// select prev
		click(".down-arrow");
		Assert.assertEquals("a", spinner.getValue());

		// select prev (stick)
		click(".down-arrow");
		Assert.assertEquals("a", spinner.getValue());
	}
}
