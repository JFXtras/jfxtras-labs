package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.CalendarUser;
import jfxtras.labs.icalendar.parameters.Delegatees;
import jfxtras.labs.icalendar.parameters.Delegators;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.CalendarUserAddressBase;

/**
 * ATTENDEE
 * Attendee
 * RFC 5545, 3.8.4.1
 * 
 * This property defines an "Attendee" within a calendar component.
 * 
 * Example:
 * ATTENDEE;ROLE=REQ-PARTICIPANT;DELEGATED-FROM="mailto:bob@
 *  example.com";PARTSTAT=ACCEPTED;CN=Jane Doe:mailto:jdoe@
 *  example.com
 * 
 * @author David Bal
 *
 */
public class Attendee extends CalendarUserAddressBase<Organizer,URI>
{
    /**
     * CUTYPE
     * Calendar User Type
     * RFC 5545, 3.2.3, page 16
     * 
     * To identify the type of calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;CUTYPE=GROUP:mailto:ietf-calsch@example.org
     */
    public CalendarUser getCalendarUser() { return (calendarUser != null) ? calendarUser.get() : null; }
    public ObjectProperty<CalendarUser> calendarUserProperty()
    {
        if (calendarUser == null)
        {
            calendarUser = new SimpleObjectProperty<>(this, ParameterEnum.CALENDAR_USER_TYPE.toString());
        }
        return calendarUser;
    }
    private ObjectProperty<CalendarUser> calendarUser;
    public void setCalendarUser(CalendarUser calendarUser)
    {
        if (calendarUser != null)
        {
            calendarUserProperty().set(calendarUser);
        }
    }
    public void setCalendarUser(String value) { setCalendarUser(new CalendarUser(value)); }
    public Attendee withCalendarUser(CalendarUser calendarUser) { setCalendarUser(calendarUser); return this; }
    public Attendee withCalendarUser(String content) { ParameterEnum.CALENDAR_USER_TYPE.parse(this, content); return this; }    

    /**
     * DELEGATED-FROM
     * Delegators
     * RFC 5545, 3.2.4, page 17
     * 
     * To specify the calendar users that have delegated their
     *    participation to the calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;DELEGATED-FROM="mailto:jsmith@example.com":mailto:
     *  jdoe@example.com
     */
    public Delegators getDelegators() { return (delegators != null) ? delegators.get() : null; }
    public ObjectProperty<Delegators> delegatorsProperty()
    {
        if (delegators == null)
        {
            delegators = new SimpleObjectProperty<>(this, ParameterEnum.DELEGATORS.toString());
        }
        return delegators;
    }
    private ObjectProperty<Delegators> delegators;
    public void setDelegators(Delegators delegators)
    {
        if (delegators != null)
        {
            delegatorsProperty().set(delegators);
        }
    }
    public void setDelegators(String value) { setDelegators(new Delegators(value)); }
    public Attendee withDelegators(Delegators delegators) { setDelegators(delegators); return this; }
    public Attendee withDelegators(String content) { ParameterEnum.DELEGATORS.parse(this, content); return this; }    

    /**
     * DELEGATED-TO
     * Delegatees
     * RFC 5545, 3.2.5, page 17
     * 
     * To specify the calendar users to whom the calendar user
     *    specified by the property has delegated participation.
     * 
     * Example:
     * ATTENDEE;DELEGATED-TO="mailto:jdoe@example.com","mailto:jqpublic
     *  @example.com":mailto:jsmith@example.com
     * 
     */
    public Delegatees getDelegatees() { return (delegatees != null) ? delegatees.get() : null; }
    public ObjectProperty<Delegatees> delegateesProperty()
    {
        if (delegatees == null)
        {
            delegatees = new SimpleObjectProperty<>(this, ParameterEnum.DELEGATEES.toString());
        }
        return delegatees;
    }
    private ObjectProperty<Delegatees> delegatees;
    public void setDelegatees(Delegatees delegatees)
    {
        if (delegatees != null)
        {
            delegateesProperty().set(delegatees);
        }
    }
    public void setDelegatees(String value) { setDelegatees(new Delegatees(value)); }
    public Attendee withDelegatees(Delegatees delegatees) { setDelegatees(delegatees); return this; }
    public Attendee withDelegatees(String content) { ParameterEnum.DELEGATEES.parse(this, content); return this; }    

    
    /*
     * CONSTRUCTORS
     */
    
    public Attendee(String propertyString) throws URISyntaxException
    {
        super(propertyString);
        setValue(new URI(getPropertyValueString()));
    }
    
    public Attendee(Attendee source)
    {
        super(source);
    }
    
    public Attendee()
    {
        super();
    }
}
