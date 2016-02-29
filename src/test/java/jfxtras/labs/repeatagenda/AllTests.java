package jfxtras.labs.repeatagenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
//                AgendaRenderVComponentsTest.class
//              , AgendaRenderAppointmentsTest.class
//              , AgendaChangeTest.class
//              , AgendaEditPopupTest.class
               ICalendarCopyTest.class
              , ICalendarDateTest.class
              , ICalendarEditTest.class
              , ICalendarEqualsTest.class
              , ICalendarParseTest.class
              , ICalendarToStringTest.class
              , MakeAppointmentsTest.class
              })
public class AllTests {

}
