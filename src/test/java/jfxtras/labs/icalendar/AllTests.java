package jfxtras.labs.icalendar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendar.component.ICalendarParseVEventTest;
import jfxtras.labs.icalendar.component.ICalendarVEventToStringTest;
import jfxtras.labs.icalendar.property.DescriptionTest;
import jfxtras.labs.icalendar.property.RDateTest;
import jfxtras.labs.icalendar.property.SummaryTest;

@RunWith(Suite.class)
@SuiteClasses({ 
                ICalendarCopyTest.class
              , ICalendarDateTest.class
              , ICalendarDeleteTest.class
              , ICalendarEditTest.class
              , ICalendarEqualsTest.class
              , ICalendarMakeInstancesTest.class
              , ICalendarParseVEventTest.class
              , ICalendarVEventToStringTest.class
              , ICalendarReadICSTest.class
              , DescriptionTest.class
              , RDateTest.class
              , SummaryTest.class
              })
public class AllTests {

}
