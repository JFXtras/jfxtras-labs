package jfxtras.labs.test;

import org.loadui.testfx.GuiTest;

import javafx.scene.input.KeyCode;

/**
 * 
 * @author tbee
 * https://github.com/SmartBear/TestFX/blob/master/src/main/java/org/loadui/testfx/GuiTest.java
 */
public abstract class JFXtrasGuiTest extends org.loadui.testfx.GuiTest {
	
	/**
	 * Click with a qualifier pressed
	 * @param matcher
	 * @param keyCode
	 * @param keyCodes
	 */
	public GuiTest click(String matcher, KeyCode keyCode, KeyCode... keyCodes) {
		press(keyCode);
		for (int i = 0; i < keyCodes.length; i++) {
			press(keyCodes[i]);
		}
		click(matcher);
		for (int i = keyCodes.length - 1; i >=0 ; i--) {
			release(keyCodes[i]);
		}
		return release(keyCode);
	}
}
