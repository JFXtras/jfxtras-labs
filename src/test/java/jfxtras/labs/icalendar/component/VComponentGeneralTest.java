package jfxtras.labs.icalendar.component;

import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.components.VComponent;
import jfxtras.labs.icalendar.components.VComponentPrimaryBase;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDate;

public class VComponentGeneralTest
{
    @Test
    public void canParseAttendee1() throws URISyntaxException
    {
        VComponentPrimaryBase<VComponent> v = new VComponentPrimaryBase<VComponent>();
//        DTStartZonedDateTime dateTimeStart = new DTStartZonedDateTime("DTSTART;TZID=America/Los_Angeles:20160306T043000");
        DTStartLocalDate dateTimeStart = new DTStartLocalDate("DTSTART;VALUE=DATE:20160322");
        v.setDateTimeStart(dateTimeStart);
        String content = "COMMENT;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:The meeting needs to be canceled";
        Comment madeProperty = new Comment(content);
        v.comments().add(madeProperty);
        v.comments().add(new Comment("The meeting needs to be canceled again"));
        
        System.out.println(v.toContentLines());
    }
}
