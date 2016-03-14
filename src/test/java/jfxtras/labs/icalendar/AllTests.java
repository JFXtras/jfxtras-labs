package jfxtras.labs.icalendar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
                ICalendarCopyTest.class
              , ICalendarDateTest.class
              , ICalendarDeleteTest.class
              , ICalendarEditTest.class
              , ICalendarEqualsTest.class
              , ICalendarMakeInstancesTest.class
              , ICalendarParseComponentTest.class
              , ICalendarParseDateTest.class
              , ICalendarParsePropertyTest.class
              , ICalendarReadICSTest.class
              , ICalendarToStringTest.class
              })
public class AllTests {

}
