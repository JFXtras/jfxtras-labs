package jfxtras.labs.icalendar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendar.parameter.CopyParameterTest;
import jfxtras.labs.icalendar.parameter.ParseParameterTest;
import jfxtras.labs.icalendar.property.AttachmentTest;
import jfxtras.labs.icalendar.property.AttendeeTest;
import jfxtras.labs.icalendar.property.CategoriesTest;
import jfxtras.labs.icalendar.property.ClassificationTest;
import jfxtras.labs.icalendar.property.CommentTest;
import jfxtras.labs.icalendar.property.DateTimeCompletedTest;
import jfxtras.labs.icalendar.property.DateTimeDueTest;
import jfxtras.labs.icalendar.property.DateTimeEndTest;
import jfxtras.labs.icalendar.property.DateTimeStartTest;
import jfxtras.labs.icalendar.property.DescriptionTest;
import jfxtras.labs.icalendar.property.DurationTest;
import jfxtras.labs.icalendar.property.ExceptionsTest;
import jfxtras.labs.icalendar.property.FreeBusyTimeTest;
import jfxtras.labs.icalendar.property.GeneralPropertyTest;
import jfxtras.labs.icalendar.property.LocationTest;
import jfxtras.labs.icalendar.property.NonStandardPropertyTest;
import jfxtras.labs.icalendar.property.OrganizerTest;
import jfxtras.labs.icalendar.property.ParsePropertyTest;
import jfxtras.labs.icalendar.property.RecurrencesTest;
import jfxtras.labs.icalendar.property.StatusTest;
import jfxtras.labs.icalendar.property.SummaryTest;
import jfxtras.labs.icalendar.property.TimeTransparencyTest;
import jfxtras.labs.icalendar.property.TimeZoneIdentifierTest;
import jfxtras.labs.icalendar.property.TimeZoneNameTest;
import jfxtras.labs.icalendar.property.TimeZoneOffsetTest;
import jfxtras.labs.icalendar.property.TimeZoneURLTest;
import jfxtras.labs.icalendar.property.URLTest;
import jfxtras.labs.icalendar.property.UniqueIdentifierTest;

@RunWith(Suite.class)
@SuiteClasses({ 
                ICalendarCopyTest.class,
                ICalendarDateTest.class,
                ICalendarDeleteTest.class,
                ICalendarEditTest.class,
                ICalendarEqualsTest.class,
                ICalendarMakeInstancesTest.class,
//              , ICalendarParseVEventTest.class
//                ICalendarVEventToStringTest.class,
//              , ICalendarReadICSTest.class,
              // parameter tests
        CopyParameterTest.class, 
        ParseParameterTest.class,
       
       // property tests
        AttachmentTest.class,
        AttendeeTest.class,
        CategoriesTest.class,
        ClassificationTest.class,
        CommentTest.class,
        DateTimeCompletedTest.class,
        DateTimeDueTest.class,
        DateTimeEndTest.class,
        DateTimeStartTest.class,
        DescriptionTest.class,
        DurationTest.class,
        ExceptionsTest.class,
        FreeBusyTimeTest.class,
        GeneralPropertyTest.class,
        LocationTest.class,
        NonStandardPropertyTest.class,
        OrganizerTest.class,
        ParsePropertyTest.class,
        RecurrencesTest.class,
        StatusTest.class,
        SummaryTest.class,
        TimeTransparencyTest.class,
        TimeZoneIdentifierTest.class,
        TimeZoneNameTest.class,
        TimeZoneOffsetTest.class,
        TimeZoneURLTest.class,
        UniqueIdentifierTest.class,
        URLTest.class
        
              
              })

public class AllTests {

}
