package jfxtras.labs.icalendar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendar.component.ParsePropertiesTest;
import jfxtras.labs.icalendar.parameter.CopyParameterTest;
import jfxtras.labs.icalendar.parameter.ParseParameterTest;
import jfxtras.labs.icalendar.property.ActionTest;
import jfxtras.labs.icalendar.property.AttachmentTest;
import jfxtras.labs.icalendar.property.AttendeeTest;
import jfxtras.labs.icalendar.property.CategoriesTest;
import jfxtras.labs.icalendar.property.ClassificationTest;
import jfxtras.labs.icalendar.property.CommentTest;
import jfxtras.labs.icalendar.property.DateTimeCompletedTest;
import jfxtras.labs.icalendar.property.DateTimeCreatedTest;
import jfxtras.labs.icalendar.property.DateTimeDueTest;
import jfxtras.labs.icalendar.property.DateTimeEndTest;
import jfxtras.labs.icalendar.property.DateTimeStampTest;
import jfxtras.labs.icalendar.property.DateTimeStartTest;
import jfxtras.labs.icalendar.property.DescriptionTest;
import jfxtras.labs.icalendar.property.DurationTest;
import jfxtras.labs.icalendar.property.ExceptionsTest;
import jfxtras.labs.icalendar.property.FreeBusyTimeTest;
import jfxtras.labs.icalendar.property.GeneralPropertyTest;
import jfxtras.labs.icalendar.property.IANATest;
import jfxtras.labs.icalendar.property.LocationTest;
import jfxtras.labs.icalendar.property.NonStandardTest;
import jfxtras.labs.icalendar.property.OrganizerTest;
import jfxtras.labs.icalendar.property.ParsePropertyTest;
import jfxtras.labs.icalendar.property.RecurrenceIdTest;
import jfxtras.labs.icalendar.property.RecurrenceRuleTest;
import jfxtras.labs.icalendar.property.RecurrencesTest;
import jfxtras.labs.icalendar.property.RepeatCountTest;
import jfxtras.labs.icalendar.property.ResourcesTest;
import jfxtras.labs.icalendar.property.SequenceTest;
import jfxtras.labs.icalendar.property.StatusTest;
import jfxtras.labs.icalendar.property.SummaryTest;
import jfxtras.labs.icalendar.property.TimeTransparencyTest;
import jfxtras.labs.icalendar.property.TimeZoneIdentifierTest;
import jfxtras.labs.icalendar.property.TimeZoneNameTest;
import jfxtras.labs.icalendar.property.TimeZoneOffsetTest;
import jfxtras.labs.icalendar.property.TimeZoneURLTest;
import jfxtras.labs.icalendar.property.TriggerTest;
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
        
        //component tests
        ParsePropertiesTest.class,
       
       // property tests
        ActionTest.class,
        AttachmentTest.class,
        AttendeeTest.class,
        CategoriesTest.class,
        ClassificationTest.class,
        CommentTest.class,
        DateTimeCompletedTest.class,
        DateTimeCreatedTest.class,
        DateTimeDueTest.class,
        DateTimeEndTest.class,
        DateTimeStampTest.class,
        DateTimeStartTest.class,
        DescriptionTest.class,
        DurationTest.class,
        ExceptionsTest.class,
        FreeBusyTimeTest.class,
        GeneralPropertyTest.class,
        LocationTest.class,
        NonStandardTest.class,
        OrganizerTest.class,
        ParsePropertyTest.class,
        RecurrenceIdTest.class,
        RecurrenceRuleTest.class,
        RecurrencesTest.class,
        RepeatCountTest.class,
        ResourcesTest.class,
        SequenceTest.class,
        StatusTest.class,
        SummaryTest.class,
        TimeTransparencyTest.class,
        TimeZoneIdentifierTest.class,
        TimeZoneNameTest.class,
        TimeZoneOffsetTest.class,
        TimeZoneURLTest.class,
        TriggerTest.class,
        UniqueIdentifierTest.class,
        URLTest.class,
        
        // parameter tests
        CopyParameterTest.class, 
        ParseParameterTest.class
              
              })

public class AllTests {

}
