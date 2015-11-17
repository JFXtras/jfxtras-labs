package jfxtras.labs.repeatagenda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RepeatCopyTest.class
              , RepeatDatesTest.class
              , RepeatDeleteTest.class
              , RepeatEditTest.class
              , RepeatMakeAppointmentsTest.class
              , ICalendarDateTest.class
              , ICalendarMiscTest.class
              , ICalendarDateRangeTest.class
              })
public class AllTests {

}
