package jfxtras.labs.repeatagenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
                AgendaRenderVComponentsTest.class
              , AgendaRenderAppointmentsTest.class
              , AgendaChangeTest.class
              , AgendaEditPopupTest.class
              , ICalendarCopyTest.class
              , ICalendarDateTest.class
              , ICalendarEqualsTest.class
              , MakeAppointmentsTest.class
              , ICalendarParseTest.class
              , ICalendarToStringTest.class
              })
public class AllTests {

}
