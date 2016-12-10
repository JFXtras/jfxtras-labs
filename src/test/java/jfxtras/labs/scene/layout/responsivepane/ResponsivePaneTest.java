/**
 * VBoxTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
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

package jfxtras.labs.scene.layout.responsivepane;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.layout.VBox;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;
import jfxtras.test.AssertNode.A;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class ResponsivePaneTest extends GuiTest {

	/**
	 * 
	 */
	public Parent getRootNode() {
		responsivePane = new ResponsivePane();
		responsivePane.addReusableNode("CalendarPicker", new CalendarPicker());
		responsivePane.addReusableNode("TreeView", new TreeView());
		responsivePane.addReusableNode("TableView", new TableView());
		responsivePane.addReusableNode("save", new Button("save"));
		responsivePane.addReusableNode("saveAndTomorrow", new Button("saveAndTomorrow"));
		responsivePane.addReusableNode("-", new Button("-"));
		responsivePane.addReusableNode("+", new Button("+"));
		responsivePane.addReusableNode("Logo", new Button("Logo"));
		responsivePane.addReusableNode("version", new Label("v1.0"));
		responsivePane.setPrefSize(300.0, 300.0);
		return responsivePane;
	}
	private ResponsivePane responsivePane = null;

	/**
	 * 
	 */
	@Test
	public void empty() {
		Assert.assertEquals("0.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		Assert.assertEquals("0.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
		Assert.assertEquals("0.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
		Assert.assertEquals(1, responsivePane.getChildren().size());
		Assert.assertEquals("?", ((Label)responsivePane.getChildren().get(0)).getText());
	}
	

	/**
	 * 
	 */
	@Test
	public void pickRightLayout() {
		TestUtil.runThenWaitForPaintPulse( () -> {
			responsivePane.addLayout(Diagonal.inches(40.0), new Label("40layout"));
			responsivePane.addLayout(Diagonal.inches(1.0), new Label("1layout"));
			responsivePane.addLayout(Diagonal.inches(400.0), new Label("400layout"));
		});
		// the right layout should be active
		Assert.assertEquals("1.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		Assert.assertEquals(1, responsivePane.getChildren().size());
		Assert.assertEquals("1layout", ((Label)responsivePane.getChildren().get(0)).getText());
	}

	/**
	 * 
	 */
	@Test
	public void pickRightSceneStylesheet() {
		TestUtil.runThenWaitForPaintPulse( () -> {
			responsivePane.addSceneStylesheet(Diagonal.inches(40.0), getClass().getResource("phone.css"));
			responsivePane.addSceneStylesheet(Diagonal.inches(1.0), getClass().getResource("tablet.css"));
			responsivePane.addSceneStylesheet(Diagonal.inches(400.0), getClass().getResource("desktop.css"));
		});
		// the right layout should be active
		Assert.assertEquals("1.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
		Assert.assertTrue(responsivePane.getScene().getStylesheets().get(0).endsWith("tablet.css"));
		Assert.assertEquals("0.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickRightMyStylesheet() {
		TestUtil.runThenWaitForPaintPulse( () -> {
			responsivePane.addMyStylesheet(Diagonal.inches(40.0), getClass().getResource("phone.css"));
			responsivePane.addMyStylesheet(Diagonal.inches(1.0), getClass().getResource("tablet.css"));
			responsivePane.addMyStylesheet(Diagonal.inches(400.0), getClass().getResource("desktop.css"));
		});
		// the right layout should be active
		Assert.assertEquals("1.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
		Assert.assertTrue(responsivePane.getStylesheets().get(0).endsWith("tablet.css"));
		Assert.assertEquals("0.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
	}
}
