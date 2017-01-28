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

package jfxtras.labs.scene.control;

import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.test.TestUtil;

/**
 * 
 */
// TODO: reusableNodes and Refs
public class HarmonicaTest extends GuiTest {

	/**
	 * 
	 */
	public Parent getRootNode() {
		harmonica = new Harmonica();
		harmonica.setPrefSize(800.0, 600.0);
		return harmonica;
	}
	private Harmonica harmonica = null;

	/**
	 * 
	 */
	@Test
	public void empty() {
		// GIVEN a harmonica with no contents
		// THEN the skin should have no nodes
		Pane lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(0, lHarmonicaContents.getChildren().size());
		Assert.assertNull(harmonica.getVisibleTab());
	}

	/**
	 * 
	 */
	@Test
	public void one() {
		// GIVEN a harmonica with one tab
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.addTab("test1", new Label("test 1"));
		});
		Assert.assertEquals(1, harmonica.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(harmonica.tabs().get(0), harmonica.getVisibleTab());
		
		// THEN the skin should have two controls
		Pane lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(2, lHarmonicaContents.getChildren().size());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(0).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(0).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(1).getClass().getSimpleName());		Assert.assertTrue(lHarmonicaContents.getChildren().get(1).isVisible());
	}

	/**
	 * 
	 */
	@Test
	public void two() {
		// GIVEN a harmonica with two tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.addTab("test1", new Label("test 1"));
			harmonica.addTab("test2", new Label("test 2"));
		});
		Assert.assertEquals(2, harmonica.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(harmonica.tabs().get(0), harmonica.getVisibleTab());
		
		// THEN the first tabpane is visible, and the other is not
		Pane lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(4, lHarmonicaContents.getChildren().size());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(0).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(0).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(1).getClass().getSimpleName());		Assert.assertTrue(lHarmonicaContents.getChildren().get(1).isVisible());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(2).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(2).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(3).getClass().getSimpleName());		Assert.assertFalse(lHarmonicaContents.getChildren().get(3).isVisible());
	}

	/**
	 * 
	 */
	@Test
	public void three() {
		// GIVEN a harmonica with three tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.addTab("test1", new Label("test 1"));
			harmonica.addTab("test2", new Label("test 2"));
			harmonica.addTab("test3", new Label("test 3"));
		});
		Assert.assertEquals(3, harmonica.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(harmonica.tabs().get(0), harmonica.getVisibleTab());
		
		// THEN the first tabpane is visible, and the others are not
		Pane lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(6, lHarmonicaContents.getChildren().size());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(0).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(0).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(1).getClass().getSimpleName());		Assert.assertTrue(lHarmonicaContents.getChildren().get(1).isVisible());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(2).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(2).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(3).getClass().getSimpleName());		Assert.assertFalse(lHarmonicaContents.getChildren().get(3).isVisible());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(4).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(4).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(5).getClass().getSimpleName());		Assert.assertFalse(lHarmonicaContents.getChildren().get(5).isVisible());
	}

	/**
	 * 
	 */
	@Test
	public void adding() {
		// GIVEN a harmonica with no contents
		Assert.assertNull(harmonica.getVisibleTab());

		// WHEN adding one tab
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.addTab("test1", new Label("test 1"));
		});
		Assert.assertEquals(1, harmonica.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(harmonica.tabs().get(0), harmonica.getVisibleTab());
		
		// THEN the skin should have two controls
		Pane lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(2, lHarmonicaContents.getChildren().size());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(0).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(0).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(1).getClass().getSimpleName());		Assert.assertTrue(lHarmonicaContents.getChildren().get(1).isVisible());
		
		// WHEN adding a second one
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.addTab("test2", new Label("test 2"));
		});
		Assert.assertEquals(2, harmonica.tabs().size());
		
		// THEN the first tab is visible per default
		Assert.assertEquals(harmonica.tabs().get(0), harmonica.getVisibleTab());
		
		// THEN the first tabpane is visible, and the other is not
		lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(4, lHarmonicaContents.getChildren().size());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(0).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(0).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(1).getClass().getSimpleName());		Assert.assertTrue(lHarmonicaContents.getChildren().get(1).isVisible());
		Assert.assertEquals("TabButton", lHarmonicaContents.getChildren().get(2).getClass().getSimpleName());	Assert.assertTrue(lHarmonicaContents.getChildren().get(2).isVisible());
		Assert.assertEquals("TabPane", lHarmonicaContents.getChildren().get(3).getClass().getSimpleName());		Assert.assertFalse(lHarmonicaContents.getChildren().get(3).isVisible());
	}

	/**
	 * 
	 */
	@Test
	public void changeVisibleTabProperty() {
		// GIVEN a harmonica with three tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.addTab("test1", new Label("test 1"));
			harmonica.addTab("test2", new Label("test 2"));
			harmonica.addTab("test3", new Label("test 3"));
		});
		
		// WHEN the second tab is made visible
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.setVisibleTab(harmonica.tabs().get(1));
		});
		
		// THEN the second tabpane is visible, and the others are not
		Pane lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(6, lHarmonicaContents.getChildren().size());
		Assert.assertFalse(lHarmonicaContents.getChildren().get(1).isVisible());
		Assert.assertTrue(lHarmonicaContents.getChildren().get(3).isVisible());
		Assert.assertFalse(lHarmonicaContents.getChildren().get(5).isVisible());
		
		// WHEN the third tab is made visible
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.setVisibleTab(harmonica.tabs().get(2));
		});
		
		// THEN the third tabpane is visible, and the others are not
		lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(6, lHarmonicaContents.getChildren().size());
		Assert.assertFalse(lHarmonicaContents.getChildren().get(1).isVisible());
		Assert.assertFalse(lHarmonicaContents.getChildren().get(3).isVisible());
		Assert.assertTrue(lHarmonicaContents.getChildren().get(5).isVisible());
	}
	

	/**
	 * 
	 */
	@Test
	public void removing() {
		// GIVEN a harmonica with three tabs
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.addTab("test1", new Label("test 1"));
			harmonica.addTab("test2", new Label("test 2"));
			harmonica.addTab("test3", new Label("test 3"));
		});
		
		// WHEN the first (visible) tab is removed
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.tabs().remove(harmonica.tabs().get(0));
		});
		
		// THEN the now first tab is visible
		Assert.assertEquals(harmonica.tabs().get(0), harmonica.getVisibleTab());
		
		// THEN the second tabpane is visible, and the others are not
		Pane lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(4, lHarmonicaContents.getChildren().size());
		Assert.assertTrue(lHarmonicaContents.getChildren().get(1).isVisible());
		Assert.assertFalse(lHarmonicaContents.getChildren().get(3).isVisible());
		
		// WHEN the other tabs are removed
		TestUtil.runThenWaitForPaintPulse( () -> {
			harmonica.tabs().clear();
		});
		
		// THEN no tab is visible
		Assert.assertNull(harmonica.getVisibleTab());
		lHarmonicaContents = (Pane)harmonica.getChildrenUnmodifiable().get(0);
		Assert.assertEquals(0, lHarmonicaContents.getChildren().size());
	}

	// TODO: click on button
	
	// ==========================================================================================================================================================================================================================================
	// SUPPORT
	
	private void setStageSizeInInch(double width, double height) {
	}
}
