package jfxtras.labs.repeatagenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ICalendarDateTest.class
              , ICalendarParseTest.class
              , ICalendarDateRangeTest.class
              , ICalendarMakeAppointmentsTest.class
              , ICalendarEditTest.class
              , ICalendarToStringTest.class
              , ICalendarCopyTest.class
              , ICalendarEqualsTest.class
              })
public class AllTests {

}
