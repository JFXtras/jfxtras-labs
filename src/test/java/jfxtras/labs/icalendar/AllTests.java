package jfxtras.labs.icalendar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendar.component.ICalendarParseVEventTest;
import jfxtras.labs.icalendar.component.ICalendarVEventToStringTest;
import jfxtras.labs.icalendar.parameter.CopyParameterTest;
import jfxtras.labs.icalendar.parameter.ParseParameterTest;
import jfxtras.labs.icalendar.property.CategoriesTest;
import jfxtras.labs.icalendar.property.DateTimeStartTest;
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
              // parameter tests
              , CopyParameterTest.class
              , ParseParameterTest.class
              
              , CategoriesTest.class
              , DateTimeStartTest.class
              , DescriptionTest.class
              , RDateTest.class
              , SummaryTest.class
              
              })
public class AllTests {

}
