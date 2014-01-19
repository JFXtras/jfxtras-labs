package jfxtras.labs.scene.layout.test;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import jfxtras.labs.scene.layout.HBox;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 * Created by tbee on 26-12-13.
 */
public class HBoxTest extends GuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		hbox = new HBox(5.0);
		hbox.add(new Button("grow"), new HBox.C().hgrow(Priority.ALWAYS));
		hbox.add(new Button("margin 5 grow"), new HBox.C().margin(new Insets(5.0)).hgrow(Priority.ALWAYS));
		hbox.getChildren().add(new Button("old style"));
		hbox.add(new Button("margin 20 nogrow"), new HBox.C().margin(new Insets(20.0)));
		hbox.add(new Button("grow maxheight 50"), new HBox.C().hgrow(Priority.ALWAYS).maxHeight(50.0));

		return hbox;
	}
	private HBox hbox = null;

	/**
	 * 
	 */
	@Test
	public void checkPositions()
	{
		int lIdx = 0;
		assertButton((Button)hbox.getChildren().get(lIdx++), "grow", 0, 0, 43, 400);
		assertButton((Button)hbox.getChildren().get(lIdx++), "margin 5 grow", 53, 5, 94, 390);
		assertButton((Button)hbox.getChildren().get(lIdx++), "old style", 157, 0, 61, 25);
		assertButton((Button)hbox.getChildren().get(lIdx++), "margin 20 nogrow", 243, 20, 114, 25);
		assertButton((Button)hbox.getChildren().get(lIdx++), "grow maxheight 50", 382, 0, 119, 50);
	}
	
	private void assertButton(Button lButton, String title, double x, double y, double w, double h) {
		System.out.println(lButton.getText() + ": X=" + lButton.getLayoutX() + ", Y=" + lButton.getLayoutY() + ", W=" + lButton.getWidth() + ", H=" + lButton.getHeight());
		Assert.assertEquals(title, lButton.getText());
		Assert.assertEquals(x, lButton.getLayoutX(), 0.001);
		Assert.assertEquals(y, lButton.getLayoutY(), 0.001);
		Assert.assertEquals(w, lButton.getWidth(), 0.001);
		Assert.assertEquals(h, lButton.getHeight(), 0.001);
	}
	
}
