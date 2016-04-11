package jfxtras.labs.icalendar.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.Delegatees;
import jfxtras.labs.icalendar.parameters.DirectoryEntryReference;

public class CopyParameterTest
{
    @Test
    public void canCopyDirectory()
    {
        DirectoryEntryReference parameter = new DirectoryEntryReference("\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\"");
        DirectoryEntryReference parameter2 = new DirectoryEntryReference(parameter);
        assertEquals(parameter, parameter2);
    }
    
    @Test // can copy list
    public void canCopyDelegatees()
    {
        Delegatees parameter = new Delegatees("DELEGATED-TO=\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\"");
        Delegatees parameter2 = new Delegatees(parameter);
        assertEquals(parameter, parameter2);
    }
}
