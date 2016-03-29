package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.util.List;

/**
 * MEMBER
 * Group or List Membership
 * RFC 5545, 3.2.11, page 21
 * 
 * To specify the group or list membership of the calendar user specified by the property.
 * 
 * Example:
 * ATTENDEE;MEMBER="mailto:projectA@example.com","mailto:pr
 *  ojectB@example.com":mailto:janedoe@example.com
 * 
 * @author David Bal
 *
 */
public class GroupMembership extends ParameterBase<GroupMembership, List<URI>>
{
    public GroupMembership()
    {
        super();
    }
    
    public GroupMembership(String content)
    {
        super();
        List<URI> value = myParameterEnum().parse(content);
        setValue(value);
    }
    
    public GroupMembership(GroupMembership source)
    {
        super(source);
    }
}
