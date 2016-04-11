package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.CalendarUser;
import jfxtras.labs.icalendar.parameters.CalendarUser.CalendarUserType;
import jfxtras.labs.icalendar.parameters.Delegatees;
import jfxtras.labs.icalendar.parameters.Delegators;
import jfxtras.labs.icalendar.parameters.GroupMembership;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.Participation;
import jfxtras.labs.icalendar.parameters.Participation.ParticipationStatus;
import jfxtras.labs.icalendar.parameters.ParticipationRole;
import jfxtras.labs.icalendar.parameters.ParticipationRole.ParticipationRoleType;
import jfxtras.labs.icalendar.parameters.RSVP;
import jfxtras.labs.icalendar.properties.PropertyAttendee;

/**
 * Abstract class for Attendee and unknown properties
 * 
 * @author David Bal
 *
 * @param <U> - subclass
 * @param <T> - property value type
 */
public abstract class PropertyBaseAttendee<U, T> extends PropertyBaseCalendarUser<U, T> implements PropertyAttendee<T>
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
    @Override
    public CalendarUser getCalendarUser() { return (calendarUser != null) ? calendarUser.get() : null; }
    @Override
    public ObjectProperty<CalendarUser> calendarUserProperty()
    {
        if (calendarUser == null)
        {
            calendarUser = new SimpleObjectProperty<>(this, ParameterEnum.CALENDAR_USER_TYPE.toString());
        }
        return calendarUser;
    }
    private ObjectProperty<CalendarUser> calendarUser;
    @Override
    public void setCalendarUser(CalendarUser calendarUser)
    {
        if (calendarUser != null)
        {
            calendarUserProperty().set(calendarUser);
        }
    }
    public void setCalendarUser(String value) { setCalendarUser(new CalendarUser(value)); }
    public U withCalendarUser(CalendarUser type) { setCalendarUser(type); return (U) this; }
    public U withCalendarUser(CalendarUserType type) { setCalendarUser(new CalendarUser(type)); return (U) this; }
    public U withCalendarUser(String content) { setCalendarUser(content); return (U) this; }    

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
    @Override
    public Delegators getDelegators() { return (delegators != null) ? delegators.get() : null; }
    @Override
    public ObjectProperty<Delegators> delegatorsProperty()
    {
        if (delegators == null)
        {
            delegators = new SimpleObjectProperty<>(this, ParameterEnum.DELEGATORS.toString());
        }
        return delegators;
    }
    private ObjectProperty<Delegators> delegators;
    @Override
    public void setDelegators(Delegators delegators)
    {
        if (delegators != null)
        {
            delegatorsProperty().set(delegators);
        }
    }
    public void setDelegators(String content) { setDelegators(new Delegators(content)); }
    public U withDelegators(Delegators delegators) { setDelegators(delegators); return (U) this; }
    public U withDelegators(List<URI> delegators) { setDelegators(new Delegators(delegators)); return (U) this; }
    public U withDelegators(String content) { setDelegators(content); return (U) this; }    

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
    @Override
    public Delegatees getDelegatees() { return (delegatees == null) ? null : delegatees.get(); }
    @Override
    public ObjectProperty<Delegatees> delegateesProperty()
    {
        if (delegatees == null)
        {
            delegatees = new SimpleObjectProperty<>(this, ParameterEnum.DELEGATEES.toString());
        }
        return delegatees;
    }
    private ObjectProperty<Delegatees> delegatees;
    @Override
    public void setDelegatees(Delegatees delegatees)
    {
        if (delegatees != null)
        {
            delegateesProperty().set(delegatees);
        }
    }
    public void setDelegatees(String content) { setDelegatees(new Delegatees(content)); }
    public U withDelegatees(Delegatees delegatees) { setDelegatees(delegatees); return (U) this; }
    public U withDelegatees(List<URI> values) { setDelegatees(new Delegatees(values)); return (U) this; }
    public U withDelegatees(String content) { setDelegatees(content); return (U) this; }    

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
     */
    @Override
    public GroupMembership getGroupMembership() { return (groupMembership == null) ? null : groupMembership.get(); }
    @Override
    public ObjectProperty<GroupMembership> groupMembershipProperty()
    {
        if (groupMembership == null)
        {
            groupMembership = new SimpleObjectProperty<>(this, ParameterEnum.GROUP_OR_LIST_MEMBERSHIP.toString());
        }
        return groupMembership;
    }
    private ObjectProperty<GroupMembership> groupMembership;
    @Override
    public void setGroupMembership(GroupMembership groupMembership)
    {
        if (groupMembership != null)
        {
            groupMembershipProperty().set(groupMembership);
        }
    }
    public void setGroupMembership(String content) { setGroupMembership(new GroupMembership(content)); }
    public U withGroupMembership(GroupMembership groupMembership) { setGroupMembership(groupMembership); return (U) this; }
    public U withGroupMembership(List<URI> values) { setGroupMembership(new GroupMembership(values)); return (U) this; }
    public U withGroupMembership(String content) { setGroupMembership(content); return (U) this; }    

    /**
     * RSVP
     * RSVP Expectation
     * RFC 5545, 3.2.17, page 26
     * 
     * To specify whether there is an expectation of a favor of a reply from the calendar user specified by the property value.
     * 
     * Example:
     * ATTENDEE;RSVP=TRUE:mailto:jsmith@example.com
     */
    @Override
    public RSVP getRSVP() { return (rsvp != null) ? rsvp.get() : null; }
    @Override
    public ObjectProperty<RSVP> rsvpProperty()
    {
        if (rsvp == null)
        {
            rsvp = new SimpleObjectProperty<>(this, ParameterEnum.RSVP_EXPECTATION.toString());
        }
        return rsvp;
    }
    private ObjectProperty<RSVP> rsvp;
    @Override
    public void setRSVP(RSVP rsvp)
    {
        if (rsvp != null)
        {
            rsvpProperty().set(rsvp);
        }
    }
    public void setRSVP(String content) { setRSVP(new RSVP(content)); }
    public U withRSVP(RSVP type) { setRSVP(type); return (U) this; }
    public U withRSVP(Boolean type) { setRSVP(new RSVP(type)); return (U) this; }
    public U withRSVP(String content) { setRSVP(content); return (U) this; }   
    
    /**
     * PARTSTAT
     * Participation Status
     * RFC 5545, 3.2.12, page 22
     * 
     * To specify the participation role for the calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;PARTSTAT=DECLINED:mailto:jsmith@example.com
     */
    @Override
    public Participation getParticipation() { return (participation != null) ? participation.get() : null; }
    @Override
    public ObjectProperty<Participation> participationProperty()
    {
        if (participation == null)
        {
            participation = new SimpleObjectProperty<>(this, ParameterEnum.PARTICIPATION_STATUS.toString());
        }
        return participation;
    }
    private ObjectProperty<Participation> participation;
    @Override
    public void setParticipation(Participation participation)
    {
        if (participation != null)
        {
            participationProperty().set(participation);
        }
    }
    public void setParticipation(String content) { setParticipation(new Participation(content)); }
    public U withParticipation(Participation type) { setParticipation(type); return (U) this; }
    public U withParticipation(ParticipationStatus type) { setParticipation(new Participation(type)); return (U) this; }
    public U withParticipation(String content) { setParticipation(content); return (U) this; }  

    /**
     * ROLE
     * Participation Role
     * RFC 5545, 3.2.16, page 25
     * 
     * To specify the participation role for the calendar user specified by the property.
     * 
     * Example:
     * ATTENDEE;ROLE=CHAIR:mailto:mrbig@example.com
     */
    @Override
    public ParticipationRole getParticipationRole() { return (participationRole != null) ? participationRole.get() : null; }
    @Override
    public ObjectProperty<ParticipationRole> participationRoleProperty()
    {
        if (participationRole == null)
        {
            participationRole = new SimpleObjectProperty<>(this, ParameterEnum.PARTICIPATION_ROLE.toString());
        }
        return participationRole;
    }
    private ObjectProperty<ParticipationRole> participationRole;
    @Override
    public void setParticipationRole(ParticipationRole participationRole)
    {
        if (participationRole != null)
        {
            participationRoleProperty().set(participationRole);
        }
    }
    public void setParticipationRole(String content) { setParticipationRole(new ParticipationRole(content)); }
    public U withParticipationRole(ParticipationRole type) { setParticipationRole(type); return (U) this; }
    public U withParticipationRole(ParticipationRoleType type) { setParticipationRole(new ParticipationRole(type)); return (U) this; }
    public U withParticipationRole(String content) { setParticipationRole(content); return (U) this; }  

    /*
     * CONSTRUCTORS
     */
    
    public PropertyBaseAttendee(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyBaseAttendee(T value)
    {
        super(value);
    }
    
    public PropertyBaseAttendee(PropertyBaseAttendee<U, T> source)
    {
        super(source);
    }
}
