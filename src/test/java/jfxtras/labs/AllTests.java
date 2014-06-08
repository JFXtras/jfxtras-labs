package jfxtras.labs;

import jfxtras.labs.scene.layout.test.CircularPaneTest;
import jfxtras.labs.scene.menu.test.CornerMenuTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CircularPaneTest.class
              , CornerMenuTest.class
	          })
public class AllTests {

}
