package jfxtras.labs.icalendar.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.AlternateTextRepresentation;
import jfxtras.labs.icalendar.parameters.CalendarUser;
import jfxtras.labs.icalendar.parameters.CommonName;
import jfxtras.labs.icalendar.parameters.Delegatees;
import jfxtras.labs.icalendar.parameters.DirectoryEntryReference;
import jfxtras.labs.icalendar.parameters.FormatType;

public class ParseParameterTest
{
    @Test // tests String as value
    public void canParseCommonName()
    {
        CommonName parameter = new CommonName("David Bal");
        String expectedContent = ";CN=David Bal";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests enum as value
    public void canParseCalendarUser()
    {
        CalendarUser parameter = new CalendarUser("GROUP");
        String expectedContent = ";CUTYPE=GROUP";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests list of URI value
    public void canParseDelegatees()
    {
        Delegatees parameter = new Delegatees("DELEGATED-TO=\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\"");
        String expectedContent = ";DELEGATED-TO=\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\"";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests single URI as value
    public void canParseAlternateText()
    {
        AlternateTextRepresentation parameter = new AlternateTextRepresentation(";ALTREP=\"CID:part3.msg.970415T083000@example.com\"");
        String expectedContent = ";ALTREP=\"CID:part3.msg.970415T083000@example.com\"";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests list as value
    public void canParseDirectory()
    {
        DirectoryEntryReference parameter = new DirectoryEntryReference(";DIR=\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\"");
        String expectedContent = ";DIR=\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\"";
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test // tests two-value parameter
    public void canParseFormatType()
    {
        FormatType parameter = new FormatType(";FMTTYPE=application/msword");
        String expectedContent = ";FMTTYPE=application/msword";
        assertEquals(expectedContent, parameter.toContent());
        assertEquals("application", parameter.getTypeName());
        assertEquals("msword", parameter.getSubtypeName());
    }
}
