package jfxtras.labs.icalendar.parameters;

import java.net.URI;

/**
 * DIR
 * Directory Entry Reference
 * RFC 5545, 3.2.6, page 18
 * 
 * To specify reference to a directory entry associated with
 *     the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;DIR="ldap://example.com:6666/o=ABC%20Industries,
 *  c=US???(cn=Jim%20Dolittle)":mailto:jimdo@example.com
 * 
 * @author David Bal
 *
 */
public class DirectoryEntryReference extends ParameterBase<DirectoryEntryReference, URI>
{
    public DirectoryEntryReference()
    {
        super();
    }
  
    public DirectoryEntryReference(String content)
    {
        super(content);
    }

    public DirectoryEntryReference(DirectoryEntryReference source)
    {
        super(source);
    }
}
