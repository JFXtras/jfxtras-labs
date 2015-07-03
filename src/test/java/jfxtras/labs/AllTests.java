package jfxtras.labs;

import jfxtras.labs.scene.control.gauge.linear.test.SimpleMetroArcGaugeTest;
import jfxtras.labs.scene.control.test.BigDecimalFieldTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BigDecimalFieldTest.class
              , SimpleMetroArcGaugeTest.class 
})
public class AllTests {

}
