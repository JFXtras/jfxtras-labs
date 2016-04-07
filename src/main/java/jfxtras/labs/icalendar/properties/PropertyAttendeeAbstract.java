package jfxtras.labs.icalendar.properties;

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

public abstract class PropertyAttendeeAbstract<T, U> extends PropertyCalendarUserAddress<T, U>
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
    public T withCalendarUser(CalendarUser type) { setCalendarUser(type); return (T) this; }
    public T withCalendarUser(CalendarUserType type) { setCalendarUser(new CalendarUser(type)); return (T) this; }
    public T withCalendarUser(String content) { setCalendarUser(content); return (T) this; }    

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
    public void setDelegators(String content) { setDelegators(new Delegators(content)); }
    public T withDelegators(Delegators delegators) { setDelegators(delegators); return (T) this; }
    public T withDelegators(List<URI> delegators) { setDelegators(new Delegators(delegators)); return (T) this; }
    public T withDelegators(String content) { setDelegators(content); return (T) this; }    

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
    public Delegatees getDelegatees() { return (delegatees == null) ? null : delegatees.get(); }
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
    public void setDelegatees(String content) { setDelegatees(new Delegatees(content)); }
    public T withDelegatees(Delegatees delegatees) { setDelegatees(delegatees); return (T) this; }
    public T withDelegatees(List<URI> values) { setDelegatees(new Delegatees(values)); return (T) this; }
    public T withDelegatees(String content) { setDelegatees(content); return (T) this; }    

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
    public GroupMembership getGroupMembership() { return (groupMembership == null) ? null : groupMembership.get(); }
    public ObjectProperty<GroupMembership> groupMembershipProperty()
    {
        if (groupMembership == null)
        {
            groupMembership = new SimpleObjectProperty<>(this, ParameterEnum.GROUP_OR_LIST_MEMBERSHIP.toString());
        }
        return groupMembership;
    }
    private ObjectProperty<GroupMembership> groupMembership;
    public void setGroupMembership(GroupMembership groupMembership)
    {
        if (groupMembership != null)
        {
            groupMembershipProperty().set(groupMembership);
        }
    }
    public void setGroupMembership(String content) { setGroupMembership(new GroupMembership(content)); }
    public T withGroupMembership(GroupMembership groupMembership) { setGroupMembership(groupMembership); return (T) this; }
    public T withGroupMembership(List<URI> values) { setGroupMembership(new GroupMembership(values)); return (T) this; }
    public T withGroupMembership(String content) { setGroupMembership(content); return (T) this; }    

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
    public RSVP getRSVP() { return (rsvp != null) ? rsvp.get() : null; }
    public ObjectProperty<RSVP> rsvpProperty()
    {
        if (rsvp == null)
        {
            rsvp = new SimpleObjectProperty<>(this, ParameterEnum.RSVP_EXPECTATION.toString());
        }
        return rsvp;
    }
    private ObjectProperty<RSVP> rsvp;
    public void setRSVP(RSVP rsvp)
    {
        if (rsvp != null)
        {
            rsvpProperty().set(rsvp);
        }
    }
    public void setRSVP(String content) { setRSVP(new RSVP(content)); }
    public T withRSVP(RSVP type) { setRSVP(type); return (T) this; }
    public T withRSVP(Boolean type) { setRSVP(new RSVP(type)); return (T) this; }
    public T withRSVP(String content) { setRSVP(content); return (T) this; }   
    
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
    public Participation getParticipation() { return (participation != null) ? participation.get() : null; }
    public ObjectProperty<Participation> participationProperty()
    {
        if (participation == null)
        {
            participation = new SimpleObjectProperty<>(this, ParameterEnum.PARTICIPATION_STATUS.toString());
        }
        return participation;
    }
    private ObjectProperty<Participation> participation;
    public void setParticipation(Participation participation)
    {
        if (participation != null)
        {
            participationProperty().set(participation);
        }
    }
    public void setParticipation(String content) { setParticipation(new Participation(content)); }
    public T withParticipation(Participation type) { setParticipation(type); return (T) this; }
    public T withParticipation(ParticipationStatus type) { setParticipation(new Participation(type)); return (T) this; }
    public T withParticipation(String content) { setParticipation(content); return (T) this; }  

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
    public ParticipationRole getParticipationRole() { return (participationRole != null) ? participationRole.get() : null; }
    public ObjectProperty<ParticipationRole> participationRoleProperty()
    {
        if (participationRole == null)
        {
            participationRole = new SimpleObjectProperty<>(this, ParameterEnum.PARTICIPATION_ROLE.toString());
        }
        return participationRole;
    }
    private ObjectProperty<ParticipationRole> participationRole;
    public void setParticipationRole(ParticipationRole participationRole)
    {
        if (participationRole != null)
        {
            participationRoleProperty().set(participationRole);
        }
    }
    public void setParticipationRole(String content) { setParticipationRole(new ParticipationRole(content)); }
    public T withParticipationRole(ParticipationRole type) { setParticipationRole(type); return (T) this; }
    public T withParticipationRole(ParticipationRoleType type) { setParticipationRole(new ParticipationRole(type)); return (T) this; }
    public T withParticipationRole(String content) { setParticipationRole(content); return (T) this; }  

    /*
     * CONSTRUCTORS
     */
    
    public PropertyAttendeeAbstract(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyAttendeeAbstract(U value)
    {
        super(value);
    }
    
    public PropertyAttendeeAbstract(PropertyAttendeeAbstract<T, U> source)
    {
        super(source);
    }
}
