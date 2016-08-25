package jfxtras.labs.icalendarfx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.labs.icalendarfx.calendar.CalendarScaleTest;
import jfxtras.labs.icalendarfx.calendar.CopyCalendarTest;
import jfxtras.labs.icalendarfx.calendar.GeneralCalendarTest;
import jfxtras.labs.icalendarfx.calendar.OrdererTest;
import jfxtras.labs.icalendarfx.calendar.ReadICSFileTest;
import jfxtras.labs.icalendarfx.calendar.VCalendarRecurrenceIDTest;
import jfxtras.labs.icalendarfx.component.BaseTest;
import jfxtras.labs.icalendarfx.component.CopyComponentTest;
import jfxtras.labs.icalendarfx.component.DaylightSavingsTimeTest;
import jfxtras.labs.icalendarfx.component.DeleteComponentTest;
import jfxtras.labs.icalendarfx.component.DisplayableTest;
import jfxtras.labs.icalendarfx.component.EqualsTest;
import jfxtras.labs.icalendarfx.component.GeneralComponentTest;
import jfxtras.labs.icalendarfx.component.LocatableTest;
import jfxtras.labs.icalendarfx.component.ParseIteratorTest;
import jfxtras.labs.icalendarfx.component.ParseContentTest;
import jfxtras.labs.icalendarfx.component.PersonalTest;
import jfxtras.labs.icalendarfx.component.PrimaryTest;
import jfxtras.labs.icalendarfx.component.RecurrenceIDComponentEditTest;
import jfxtras.labs.icalendarfx.component.RepeatableTest;
import jfxtras.labs.icalendarfx.component.ReviseComponentTest;
import jfxtras.labs.icalendarfx.component.ScheduleConflictTest;
import jfxtras.labs.icalendarfx.component.StandardOrDaylightTimeTest;
import jfxtras.labs.icalendarfx.component.VAlarmTest;
import jfxtras.labs.icalendarfx.component.VEventTest;
import jfxtras.labs.icalendarfx.component.VFreeBusyTest;
import jfxtras.labs.icalendarfx.component.VJournalTest;
import jfxtras.labs.icalendarfx.component.VTimeZoneTest;
import jfxtras.labs.icalendarfx.component.VTodoTest;
import jfxtras.labs.icalendarfx.parameter.DelegateesTest;
import jfxtras.labs.icalendarfx.parameter.DirectoryEntryReferenceTest;
import jfxtras.labs.icalendarfx.parameter.ParseDateTest;
import jfxtras.labs.icalendarfx.parameter.ParseParameterTest;
import jfxtras.labs.icalendarfx.property.ActionTest;
import jfxtras.labs.icalendarfx.property.AttachmentTest;
import jfxtras.labs.icalendarfx.property.AttendeeTest;
import jfxtras.labs.icalendarfx.property.CategoriesTest;
import jfxtras.labs.icalendarfx.property.ClassificationTest;
import jfxtras.labs.icalendarfx.property.CommentTest;
import jfxtras.labs.icalendarfx.property.ContactTest;
import jfxtras.labs.icalendarfx.property.DateTimeCompletedTest;
import jfxtras.labs.icalendarfx.property.DateTimeCreatedTest;
import jfxtras.labs.icalendarfx.property.DateTimeDueTest;
import jfxtras.labs.icalendarfx.property.DateTimeEndTest;
import jfxtras.labs.icalendarfx.property.DateTimeStampTest;
import jfxtras.labs.icalendarfx.property.DateTimeStartTest;
import jfxtras.labs.icalendarfx.property.DescriptionTest;
import jfxtras.labs.icalendarfx.property.DurationTest;
import jfxtras.labs.icalendarfx.property.ExceptionsTest;
import jfxtras.labs.icalendarfx.property.FreeBusyTimeTest;
import jfxtras.labs.icalendarfx.property.GeneralPropertyTest;
import jfxtras.labs.icalendarfx.property.IANATest;
import jfxtras.labs.icalendarfx.property.LocationTest;
import jfxtras.labs.icalendarfx.property.NonStandardTest;
import jfxtras.labs.icalendarfx.property.OrganizerTest;
import jfxtras.labs.icalendarfx.property.PriorityTest;
import jfxtras.labs.icalendarfx.property.RecurrenceIdTest;
import jfxtras.labs.icalendarfx.property.RecurrenceRuleTest;
import jfxtras.labs.icalendarfx.property.RecurrencesTest;
import jfxtras.labs.icalendarfx.property.RepeatCountTest;
import jfxtras.labs.icalendarfx.property.RequestStatusTest;
import jfxtras.labs.icalendarfx.property.ResourcesTest;
import jfxtras.labs.icalendarfx.property.SequenceTest;
import jfxtras.labs.icalendarfx.property.StatusTest;
import jfxtras.labs.icalendarfx.property.SummaryTest;
import jfxtras.labs.icalendarfx.property.TimeTransparencyTest;
import jfxtras.labs.icalendarfx.property.TimeZoneIdentifierTest;
import jfxtras.labs.icalendarfx.property.TimeZoneNameTest;
import jfxtras.labs.icalendarfx.property.TimeZoneOffsetTest;
import jfxtras.labs.icalendarfx.property.TimeZoneURLTest;
import jfxtras.labs.icalendarfx.property.TriggerTest;
import jfxtras.labs.icalendarfx.property.URLTest;
import jfxtras.labs.icalendarfx.property.UniqueIdentifierTest;
import jfxtras.labs.icalendarfx.property.rrule.ByDayTest;
import jfxtras.labs.icalendarfx.property.rrule.ByMonthTest;
import jfxtras.labs.icalendarfx.property.rrule.ByWeekNumberTest;
import jfxtras.labs.icalendarfx.property.rrule.ByYearDayTest;
import jfxtras.labs.icalendarfx.property.rrule.FrequencyTest;
import jfxtras.labs.icalendarfx.property.rrule.RRuleErrorTest;
import jfxtras.labs.icalendarfx.property.rrule.RecurrenceRuleParseTest;
import jfxtras.labs.icalendarfx.property.rrule.RecurrenceRuleStreamTest;

@RunWith(Suite.class)
@SuiteClasses({ 
        
        // general tests
        OrdererTest.class,
    
        // calendar tests
        CalendarScaleTest.class,
        CopyCalendarTest.class,
        GeneralCalendarTest.class,
        ReadICSFileTest.class,
        VCalendarRecurrenceIDTest.class,
                
        //component tests
        BaseTest.class,
        CopyComponentTest.class,
        DateTimeEndTest.class,
        DaylightSavingsTimeTest.class,
        DeleteComponentTest.class,
        DisplayableTest.class,
        EqualsTest.class,
        GeneralComponentTest.class,
        LocatableTest.class,
        ParseIteratorTest.class,
        ParseContentTest.class,
        PrimaryTest.class,
        PersonalTest.class,
        RecurrenceIDComponentEditTest.class,
        RepeatableTest.class,
        ReviseComponentTest.class,
        ScheduleConflictTest.class,
        StandardOrDaylightTimeTest.class,
        VAlarmTest.class,
        VEventTest.class,
        VFreeBusyTest.class,
        VJournalTest.class,
        VTimeZoneTest.class,
        VTodoTest.class,
       
       // property tests
        GeneralPropertyTest.class,
        ActionTest.class,
        AttachmentTest.class,
        AttendeeTest.class,
        CategoriesTest.class,
        ClassificationTest.class,
        CommentTest.class,
        ContactTest.class,
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
        IANATest.class,
        LocationTest.class,
        NonStandardTest.class,
        OrganizerTest.class,
        PriorityTest.class,
        RecurrenceIdTest.class,
        RecurrenceRuleTest.class,
        RecurrenceRuleParseTest.class,
        RecurrencesTest.class,
        RepeatCountTest.class,
        RequestStatusTest.class,
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
        DelegateesTest.class,
        DirectoryEntryReferenceTest.class,
        ParseDateTest.class,
        ParseParameterTest.class,
        
        // Recurrence Rule tests
        RecurrenceRuleParseTest.class,
        RecurrenceRuleStreamTest.class,
        RRuleErrorTest.class,
        FrequencyTest.class,
        ByDayTest.class,
        ByMonthTest.class,
        ByWeekNumberTest.class,
        ByYearDayTest.class
              
              })

public class AllTests {

}
