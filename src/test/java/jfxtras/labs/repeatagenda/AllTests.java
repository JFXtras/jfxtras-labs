package jfxtras.labs.repeatagenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ICalendarAgendaEditTest.class
              , ICalendarAgendaRenderTest.class
              , ICalendarCopyTest.class
//              , ICalendarDateRangeTest.class
              , ICalendarDateTest.class
              , ICalendarDeleteTest.class
              , ICalendarEditTest.class
              , ICalendarEqualsTest.class
              , ICalendarMakeAppointmentsTest.class
              , ICalendarParseTest.class
              , ICalendarToStringTest.class
              })
public class AllTests {

}
