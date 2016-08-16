package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract;
import jfxtras.labs.icalendarfx.components.SimpleVComponentFactory;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship;
import jfxtras.labs.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action.ActionType;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;

public class ParseIteratorTest extends ICalendarTestAbstract
{

    @Test
    public void canParseWithFoldedLines()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
                          + "DTEND;VALUE=DATE:20151112" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "COMMENT:This is a multiline " + System.lineSeparator()
                          + " comment." + System.lineSeparator()
                          + "DESCRIPTION:A dog ran " + System.lineSeparator()
                          + "" + System.lineSeparator() // ignore blank line
                          + " far away." + System.lineSeparator()
                          + "END:VEVENT";
//    Iterator<String> iterator = Arrays.stream(vEventString.split(System.lineSeparator())).iterator();
    VComponent vEvent = SimpleVComponentFactory.newVComponent(vEventString);
//    VEvent vEvent = VEvent.parse(iterator);
    VEvent expectedVEvent = getWholeDayDaily1()
            .withComments("This is a multiline comment.")
            .withDescription("A dog ran far away.");
    assertEquals(expectedVEvent, vEvent);
    }
    
//    @Test
//    public void canParseWithSubComponent()
//    {
//    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
//                          + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
//                          + "DTEND;VALUE=DATE:20151112" + System.lineSeparator()
//                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
//                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
//                          + "COMMENT:This is a multiline " + System.lineSeparator()
//                          + " comment." + System.lineSeparator()
//                          + "BEGIN:VALARM" + System.lineSeparator()
//                          + "ACTION:DISPLAY" + System.lineSeparator()
//                          + "DESCRIPTION:This is a multiline " + System.lineSeparator()
//                          + " description." + System.lineSeparator()
//                          + "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator()
//                          + "END:VALARM" + System.lineSeparator()
//                          + "END:VEVENT";
//    Iterator<String> iterator = Arrays.stream(vEventString.split(System.lineSeparator())).iterator();
//    VEvent vEvent = VEvent.parse(iterator);
//    VEvent expectedVEvent = getWholeDayDaily1()
//            .withComments("This is a multiline comment.")
//            .withVAlarms(new VAlarm()
//                    .withAction(ActionType.DISPLAY)
//                    .withDescription("This is a multiline description.")
//                    .withTrigger(new Trigger<Duration>(Duration.ofMinutes(-30))
//                            .withAlarmTrigger(new AlarmTriggerRelationship(AlarmTriggerRelationshipType.START))));
//
//    assertEquals(expectedVEvent, vEvent);
//    }
    
    @Test
    public void canParseWithSubComponent()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTSTART;VALUE=DATE:20151109" + System.lineSeparator()
                          + "DTEND;VALUE=DATE:20151112" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "COMMENT:This is a multiline " + System.lineSeparator()
                          + " comment." + System.lineSeparator()
                          + "BEGIN:VALARM" + System.lineSeparator()
                          + "ACTION:DISPLAY" + System.lineSeparator()
                          + "DESCRIPTION:This is a multiline " + System.lineSeparator()
                          + " description." + System.lineSeparator()
                          + "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator()
                          + "END:VALARM" + System.lineSeparator()
                          + "END:VEVENT";
//    Iterator<String> iterator = Arrays.stream(vEventString.split(System.lineSeparator())).iterator();
    VComponent vEvent = SimpleVComponentFactory.newVComponent(vEventString);
    VEvent expectedVEvent = getWholeDayDaily1()
            .withComments("This is a multiline comment.")
            .withVAlarms(new VAlarm()
                    .withAction(ActionType.DISPLAY)
                    .withDescription("This is a multiline description.")
                    .withTrigger(new Trigger<Duration>(Duration.ofMinutes(-30))
                            .withAlarmTrigger(new AlarmTriggerRelationship(AlarmTriggerRelationshipType.START))));

    assertEquals(expectedVEvent, vEvent);
    }
    
}
