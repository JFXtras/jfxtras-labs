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

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.test.TestUtil;

/**
 * 
 */
// TODO: reusableNodes and Refs
public class ResponsivePaneTest extends GuiTest {

	/**
	 * 
	 */
	public Parent getRootNode() {
		responsivePane = new ResponsivePane();
//		responsivePane.setTrace(true);
		return responsivePane;
	}
	private ResponsivePane responsivePane = null;

	/**
	 * 
	 */
	@Test
	public void empty() {
		// GIVEN a pane without any configuration
		// THEN the default (size 0.0) settings should be active
		Assert.assertEquals("0.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		Assert.assertEquals("0.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
		Assert.assertEquals("0.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
		// AND question mark label should be shown
		Assert.assertEquals(1, responsivePane.getChildren().size());
		Assert.assertEquals("?", ((Label)responsivePane.getChildren().get(0)).getText());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectLayout() {
		
		// GIVEN a pane with some layouts 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(5.0);
			responsivePane.addLayout(Diagonal.inches(400.0), new Label("400layout"));
			responsivePane.addLayout(Diagonal.inches(1.0), new Label("1layout"));
			responsivePane.addLayout(Diagonal.inches(3.0), new Label("3layout"));
			responsivePane.addLayout(Diagonal.inches(500.0), new Label("500layout"));
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("3.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		Assert.assertEquals(1, responsivePane.getChildren().size());
		Assert.assertEquals("3layout", ((Label)responsivePane.getChildren().get(0)).getText());
		
		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(2.0);
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("1.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		Assert.assertEquals(1, responsivePane.getChildren().size());
		Assert.assertEquals("1layout", ((Label)responsivePane.getChildren().get(0)).getText());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectLayoutWidth() {
	
		// GIVEN a pane with some layouts 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageSizeInInch(5.0, 10.0); // this is a diagonal size of over 11, but the width is only 5
			responsivePane.addLayout(Width.inches(400.0), new Label("400layout"));
			responsivePane.addLayout(Width.inches(1.0), new Label("1layout"));
			responsivePane.addLayout(Width.inches(3.0), new Label("3layout"));
			responsivePane.addLayout(Width.inches(10.0), new Label("10layout"));
		});
		
		// THEN even though the diagonal size is >11, because we are using width, then 3 inch layout should be active
		Assert.assertEquals("width=3.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		
		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageSizeInInch(2.0, 10.0); // this is a diagonal size of over 10, but the width is only 2
		});
		
		// THEN even though the diagonal size is >10, because we are using width, then 1 inch layout should be active
		Assert.assertEquals("width=1.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectLayoutDevice() {
	
		// GIVEN a pane with some layouts 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(9.5); 
			responsivePane.addLayout(Device.DESKTOP, new Label("desktop"));
			responsivePane.addLayout(Device.TABLET, new Label("tablet"));
			responsivePane.addLayout(Device.PHONE, new Label("phone"));
		});
		
		// THEN the tablet layout should be active
		Assert.assertEquals(responsivePane.getDeviceSize("TABLET").toString(), responsivePane.getActiveLayout().getSizeAtLeast().toString());
		
		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(4.0);
		});
		
		// THEN phone layout should be active
		Assert.assertEquals(responsivePane.getDeviceSize("PHONE").toString(), responsivePane.getActiveLayout().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectLayoutDeviceManual() {
	
		// GIVEN a pane with some layouts and one custom device size 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(9.5); 
			responsivePane.addLayout(Device.DESKTOP, new Label("desktop"));
			responsivePane.addLayout(Device.TABLET, new Label("tablet"));
			responsivePane.addLayout(Device.PHONE, new Label("phone"));
			responsivePane.setDeviceSize("PHABLET", Diagonal.inches(9.0));
			responsivePane.addLayout("PHABLET", new Label("desktop"));
		});
		
		// THEN the phablet layout should be active
		Assert.assertEquals(responsivePane.getDeviceSize("PHABLET").toString(), responsivePane.getActiveLayout().getSizeAtLeast().toString());
	}


	/**
	 * 
	 */
	@Test
	public void moveReusableNode() {
		
		// GIVEN a pane with some layouts and on each a ref 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(5.0);
			responsivePane.addReusableNode("CalendarPicker", new CalendarPicker());
			responsivePane.addLayout(Diagonal.inches(1.0), new Ref("CalendarPicker", "ref1"));
			responsivePane.addLayout(Diagonal.inches(3.0), new Ref("CalendarPicker", "ref2"));
		});
		Node lCalendarPickerNode = responsivePane.getReusableNodes().get(0);
		Ref lRef1 = (Ref)responsivePane.getLayouts().get(0).getRoot();
		Ref lRef2 = (Ref)responsivePane.getLayouts().get(1).getRoot();
		
		// THEN the second layout should be active
		Assert.assertEquals("3.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		// AND the second ref should hold the calendar picker
		Assert.assertEquals(0, lRef1.getChildren().size());
		Assert.assertEquals(1, lRef2.getChildren().size());
		Assert.assertEquals(lCalendarPickerNode, lRef2.getChildren().get(0));
		
		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(2.0);
		});
		
		// THEN the first layout should be active
		Assert.assertEquals("1.0in", responsivePane.getActiveLayout().getSizeAtLeast().toString());
		// AND the first ref should now hold the calendar picker
		Assert.assertEquals(1, lRef1.getChildren().size());
		Assert.assertEquals(0, lRef2.getChildren().size());
		Assert.assertEquals(lCalendarPickerNode, lRef1.getChildren().get(0));
	}
	
	// ---------------------------------------
	// scene stylesheets
	
	/**
	 * 
	 */
	@Test
	public void pickCorrectSceneStylesheet() {
		
		// GIVEN a pane with some scene stylesheets 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(5.0);
			responsivePane.addSceneStylesheet(Diagonal.inches(40.0), getClass().getResource("desktop.css"));
			responsivePane.addSceneStylesheet(Diagonal.inches(1.0), getClass().getResource("phone.css"));
			responsivePane.addSceneStylesheet(Diagonal.inches(3.0), getClass().getResource("tablet.css"));
			responsivePane.addSceneStylesheet(Diagonal.inches(400.0), getClass().getResource("desktop.css"));
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("3.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
		Assert.assertTrue(responsivePane.getScene().getStylesheets().get(0).endsWith("tablet.css"));
		Assert.assertEquals("0.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());

		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(2.0);
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("1.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
		Assert.assertTrue(responsivePane.getScene().getStylesheets().get(0).endsWith("phone.css"));
		Assert.assertEquals("0.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectSceneStylesheetWidth() {
		
		// GIVEN a pane with some scene stylesheets 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageSizeInInch(5.0, 10.0); // this is a diagonal size of over 11, but the width is only 5
			responsivePane.addSceneStylesheet(Width.inches(40.0), getClass().getResource("desktop.css"));
			responsivePane.addSceneStylesheet(Width.inches(1.0), getClass().getResource("phone.css"));
			responsivePane.addSceneStylesheet(Width.inches(3.0), getClass().getResource("tablet.css"));
			responsivePane.addSceneStylesheet(Width.inches(10.0), getClass().getResource("desktop.css"));
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("width=3.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());

		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageSizeInInch(2.0, 10.0); // this is a diagonal size of over 10, but the width is only 2
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("width=1.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectSceneStylesheetDevice() {
	
		// GIVEN a pane with some scene stylesheets  
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(9.5); 
			responsivePane.addSceneStylesheet(Device.DESKTOP, getClass().getResource("desktop.css"));
			responsivePane.addSceneStylesheet(Device.TABLET, getClass().getResource("tablet.css"));
			responsivePane.addSceneStylesheet(Device.PHONE, getClass().getResource("phone.css"));
		});
		
		// THEN the tablet stylesheet should be active
		Assert.assertEquals(responsivePane.getDeviceSize("TABLET").toString(), responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
		
		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(4.0);
		});
		
		// THEN phone stylesheet should be active
		Assert.assertEquals(responsivePane.getDeviceSize("PHONE").toString(), responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectSceneStylesheetDeviceManual() {
	
		// GIVEN a pane with some layouts and one custom device size 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(9.5); 
			responsivePane.addSceneStylesheet(Device.DESKTOP, getClass().getResource("desktop.css"));
			responsivePane.addSceneStylesheet(Device.TABLET, getClass().getResource("tablet.css"));
			responsivePane.addSceneStylesheet(Device.PHONE, getClass().getResource("phone.css"));
			responsivePane.setDeviceSize("PHABLET", Diagonal.inches(9.0));
			responsivePane.addSceneStylesheet("PHABLET", getClass().getResource("desktop.css"));
		});
		
		// THEN the phablet layout should be active
		Assert.assertEquals(responsivePane.getDeviceSize("PHABLET").toString(), responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
	}

	// ---------------------------------------
	// my stylesheets
	
	/**
	 * 
	 */
	@Test
	public void pickCorrectMyStylesheet() {
		
		// GIVEN a pane with some own stylesheets 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(5.0);
			responsivePane.addMyStylesheet(Diagonal.inches(40.0), getClass().getResource("desktop.css"));
			responsivePane.addMyStylesheet(Diagonal.inches(1.0), getClass().getResource("phone.css"));
			responsivePane.addMyStylesheet(Diagonal.inches(3.0), getClass().getResource("tablet.css"));
			responsivePane.addMyStylesheet(Diagonal.inches(400.0), getClass().getResource("desktop.css"));
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("3.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
		Assert.assertTrue(responsivePane.getStylesheets().get(0).endsWith("tablet.css"));
		Assert.assertEquals("0.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());

		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(2.0);
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("1.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
		Assert.assertTrue(responsivePane.getStylesheets().get(0).endsWith("phone.css"));
		Assert.assertEquals("0.0in", responsivePane.getActiveSceneStylesheet().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectMyStylesheetWidth() {
		
		// GIVEN a pane with some My stylesheets 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageSizeInInch(5.0, 10.0); // this is a diagonal size of over 11, but the width is only 5
			responsivePane.addMyStylesheet(Width.inches(40.0), getClass().getResource("desktop.css"));
			responsivePane.addMyStylesheet(Width.inches(1.0), getClass().getResource("phone.css"));
			responsivePane.addMyStylesheet(Width.inches(3.0), getClass().getResource("tablet.css"));
			responsivePane.addMyStylesheet(Width.inches(10.0), getClass().getResource("desktop.css"));
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("width=3.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());

		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageSizeInInch(2.0, 10.0); // this is a diagonal size of over 10, but the width is only 2
		});
		
		// THEN the correct one should be active
		Assert.assertEquals("width=1.0in", responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectMyStylesheetDevice() {
	
		// GIVEN a pane with some My stylesheets  
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(9.5); 
			responsivePane.addMyStylesheet(Device.DESKTOP, getClass().getResource("desktop.css"));
			responsivePane.addMyStylesheet(Device.TABLET, getClass().getResource("tablet.css"));
			responsivePane.addMyStylesheet(Device.PHONE, getClass().getResource("phone.css"));
		});
		
		// THEN the tablet stylesheet should be active
		Assert.assertEquals(responsivePane.getDeviceSize("TABLET").toString(), responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
		
		// WHEN the pane is sized smaller 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(4.0);
		});
		
		// THEN phone stylesheet should be active
		Assert.assertEquals(responsivePane.getDeviceSize("PHONE").toString(), responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
	}

	/**
	 * 
	 */
	@Test
	public void pickCorrectMyStylesheetDeviceManual() {
	
		// GIVEN a pane with some layouts and one custom device size 
		TestUtil.runThenWaitForPaintPulse( () -> {
			setStageDiagonalSizeInInch(9.5); 
			responsivePane.addMyStylesheet(Device.DESKTOP, getClass().getResource("desktop.css"));
			responsivePane.addMyStylesheet(Device.TABLET, getClass().getResource("tablet.css"));
			responsivePane.addMyStylesheet(Device.PHONE, getClass().getResource("phone.css"));
			responsivePane.setDeviceSize("PHABLET", Diagonal.inches(9.0));
			responsivePane.addMyStylesheet("PHABLET", getClass().getResource("desktop.css"));
		});
		
		// THEN the phablet layout should be active
		Assert.assertEquals(responsivePane.getDeviceSize("PHABLET").toString(), responsivePane.getActiveMyStylesheet().getSizeAtLeast().toString());
	}


	/**
	 * 
	 */
	@Test
	public void overrideDeviceSize() {
	
		// GIVEN a pane with an overridden device size 
		TestUtil.runThenWaitForPaintPulse( () -> {
			responsivePane.setDeviceSize("desktop", Diagonal.inches(13.0));
		});
		
		// THEN the device size should be the overridden value
		Assert.assertEquals("13.0in", responsivePane.getDeviceSize(Device.DESKTOP).toString());
	}

	// ==========================================================================================================================================================================================================================================
	// SUPPORT
	
	private void setStageSizeInInch(double width, double height) {
		double lPPI = responsivePane.determinePPI();
		stage.setWidth(width * lPPI);
		stage.setHeight(height * lPPI);
	}
	
	private void setStageDiagonalSizeInInch(double diagonal) {
		double lSideInInch = Math.sqrt((diagonal * diagonal) / 2);
		setStageSizeInInch(lSideInInch, lSideInInch);
	}
}
