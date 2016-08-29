package jfxtras.labs.icalendarfx.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.parameters.CalendarUser;
import jfxtras.labs.icalendarfx.parameters.CommonName;
import jfxtras.labs.icalendarfx.parameters.Delegatees;
import jfxtras.labs.icalendarfx.parameters.Delegators;
import jfxtras.labs.icalendarfx.parameters.DirectoryEntryReference;
import jfxtras.labs.icalendarfx.parameters.GroupMembership;
import jfxtras.labs.icalendarfx.parameters.Language;
import jfxtras.labs.icalendarfx.parameters.ParticipationRole;
import jfxtras.labs.icalendarfx.parameters.ParticipationStatus;
import jfxtras.labs.icalendarfx.parameters.RSVP;
import jfxtras.labs.icalendarfx.parameters.SentBy;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

/**
 * <h2>3.8.4.1.  Attendee</h2>

   <p>Property Name:  ATTENDEE</p>

   <p>Purpose:  This property defines an "Attendee" within a calendar
      component.</p>

   <p>Value Type:  CAL-ADDRESS</p>

   <p>Property Parameters:  {@link IANAProperty IANA}, {@link NonStandardProperty non-standard},
      {@link Language language}, {@link CalendarUser calendar user type},
      {@link GroupMembership group or list membership}, {@link ParticipationRole participation role},
      {@link ParticipationStatus participation status}, {@link RSVP RSVP expectation},
      {@link Delegatees delegatee}, {@link Delegators delegator}, {@link SentBy sent by},
      {@link CommonName common name}, or {@link DirectoryEntryReference directory entry reference} property parameters can be
      specified on this property.</p>

   <p>Conformance:  This property MUST be specified in an iCalendar object
      that specifies a group-scheduled calendar entity.  This property
      MUST NOT be specified in an iCalendar object when publishing the
      calendar information (e.g., NOT in an iCalendar object that
      specifies the publication of a calendar user's busy time, event,
      to-do, or journal).  This property is not specified in an
      iCalendar object that specifies only a time zone definition or
      that defines calendar components that are not group-scheduled
      components, but are components only on a single user's calendar.</p>

   <p>Description:  This property MUST only be specified within calendar
      components to specify participants, non-participants, and the
      chair of a group-scheduled calendar entity.  The property is
      specified within an "EMAIL" category of the "VALARM" calendar
      component to specify an email address that is to receive the email
      type of iCalendar alarm.</p>

      <p>The property parameter "CN" is for the common or displayable name
      associated with the calendar address; "ROLE", for the intended
      role that the attendee will have in the calendar component;
      "PARTSTAT", for the status of the attendee's participation;
      "RSVP", for indicating whether the favor of a reply is requested;
      "CUTYPE", to indicate the type of calendar user; "MEMBER", to
      indicate the groups that the attendee belongs to; "DELEGATED-TO",
      to indicate the calendar users that the original request was
      delegated to; and "DELEGATED-FROM", to indicate whom the request
      was delegated from; "SENT-BY", to indicate whom is acting on
      behalf of the "ATTENDEE"; and "DIR", to indicate the URI that
      points to the directory information corresponding to the attendee.
      These property parameters can be specified on an "ATTENDEE"
      property in either a "VEVENT", "VTODO", or "VJOURNAL" calendar
      component.  They MUST NOT be specified in an "ATTENDEE" property
      in a "VFREEBUSY" or "VALARM" calendar component.  If the
      "LANGUAGE" property parameter is specified, the identified
      language applies to the "CN" parameter.</p>

      <p>A recipient delegated a request MUST inherit the "RSVP" and "ROLE"
      values from the attendee that delegated the request to them.</p>

      <p>Multiple attendees can be specified by including multiple
      "ATTENDEE" properties within the calendar component.</p>

   <p>Format Definition:  This property is defined by the following
      notation:

       attendee   = "ATTENDEE" attparam ":" cal-address CRLF

       attparam   = *(
                  ;
                  ; The following are OPTIONAL,
                  ; but MUST NOT occur more than once.
                  ;
                  (";" cutypeparam) / (";" memberparam) /
                  (";" roleparam) / (";" partstatparam) /
                  (";" rsvpparam) / (";" deltoparam) /
                  (";" delfromparam) / (";" sentbyparam) /
                  (";" cnparam) / (";" dirparam) /
                  (";" languageparam) /
                  ;
                  ; The following is OPTIONAL,
                  ; and MAY occur more than once.
                  ;
                  (";" other-param)
                  ;
                  )

   Example:  The following are examples of this property's use for a
      to-do:

       ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":
        mailto:joecool@example.com
       ATTENDEE;DELEGATED-FROM="mailto:immud@example.com":
        mailto:ildoit@example.com

      The following is an example of this property used for specifying
      multiple attendees to an event:

       ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=TENTATIVE;CN=Henry
        Cabot:mailto:hcabot@example.com
       ATTENDEE;ROLE=REQ-PARTICIPANT;DELEGATED-FROM="mailto:bob@
        example.com";PARTSTAT=ACCEPTED;CN=Jane Doe:mailto:jdoe@
        example.com

      The following is an example of this property with a URI to the
      directory information associated with the attendee:

       ATTENDEE;CN=John Smith;DIR="ldap://example.com:6666/o=ABC%
        20Industries,c=US???(cn=Jim%20Dolittle)":mailto:jimdo@
        example.com

      The following is an example of this property with "delegatee" and
      "delegator" information for an event:

       ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=TENTATIVE;DELEGATED-FROM=
        "mailto:iamboss@example.com";CN=Henry Cabot:mailto:hcabot@
        example.com
       ATTENDEE;ROLE=NON-PARTICIPANT;PARTSTAT=DELEGATED;DELEGATED-TO=
        "mailto:hcabot@example.com";CN=The Big Cheese:mailto:iamboss
        @example.com
       ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe
        :mailto:jdoe@example.com

   Example:  The following is an example of this property's use when
      another calendar user is acting on behalf of the "Attendee":

       ATTENDEE;SENT-BY=mailto:jan_doe@example.com;CN=John Smith:
        mailto:jsmith@example.com

 * ATTENDEE
 * Attendee
 * RFC 5545, 3.8.4.1, page 107
 * 
 * This property defines an "Attendee" within a calendar component.
 * 
 * Example:
 * ATTENDEE;ROLE=REQ-PARTICIPANT;DELEGATED-FROM="mailto:bob@
 *  example.com";PARTSTAT=ACCEPTED;CN=Jane Doe:mailto:jdoe@
 *  example.com
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 */
public class Attendee extends PropertyBaseAttendee<URI, Attendee>
{
    public Attendee(URI value)
    {
        super(value);
    }
    
    public Attendee(Attendee source)
    {
        super(source);
    }
    
    Attendee() { }

    public static Attendee parse(String propertyContent)
    {
        Attendee property = new Attendee();
        property.parseContent(propertyContent);
        URI.class.cast(property.getValue()); // ensure value class type matches parameterized type
        return property;
    }
}
