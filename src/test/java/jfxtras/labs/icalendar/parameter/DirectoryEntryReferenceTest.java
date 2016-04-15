package jfxtras.labs.icalendar.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.DirectoryEntryReference;

public class DirectoryEntryReferenceTest
{
    @Test
    public void canCopyDirectory()
    {
        DirectoryEntryReference parameter = new DirectoryEntryReference("\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\"");
        DirectoryEntryReference parameter2 = new DirectoryEntryReference(parameter);
        assertEquals(parameter, parameter2);
    }
}
