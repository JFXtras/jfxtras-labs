package jfxtras.labs.repeatagenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendar.ICalendarCopyTest;
import jfxtras.labs.icalendar.ICalendarDateTest;
import jfxtras.labs.icalendar.ICalendarEditTest;
import jfxtras.labs.icalendar.ICalendarEqualsTest;
import jfxtras.labs.icalendar.ICalendarParseTest;
import jfxtras.labs.icalendar.ICalendarToStringTest;
import jfxtras.labs.icalendar.MakeAppointmentsTest;

@RunWith(Suite.class)
@SuiteClasses({ 
                AgendaRenderVComponentsTest.class
              , AgendaRenderAppointmentsTest.class
              , AgendaChangeTest.class
              , AgendaEditPopupTest.class
              , ICalendarCopyTest.class
              , ICalendarDateTest.class
              , ICalendarEditTest.class
              , ICalendarEqualsTest.class
              , ICalendarParseTest.class
              , ICalendarToStringTest.class
              , MakeAppointmentsTest.class
              })
public class AllTests {

}
