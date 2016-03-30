package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.Participation.ParticipationStatus;

/**
 * PARTSTAT
 * Participation Status
 * RFC 5545, 3.2.12, page 22
 * 
 * To specify the language for text values in a property or property parameter.
 * 
 * Example:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * 
 * @author David Bal
 *
 */
public class Participation extends ParameterBase<Participation, ParticipationStatus>
{
    public Participation()
    {
        super();
    }
  
    public Participation(String content)
    {
        super(ParticipationStatus.valueOf(extractValue(content)));
    }

    public Participation(Participation source)
    {
        super(source);
    }

    public enum ParticipationStatus
    {
        NEEDS_ACTION ("NEEDS-ACTION"),  // VEvent, VTodo, VJournal - DEFAULT VALUE
        ACCEPTED ("ACCEPTED"),          // VEvent, VTodo, VJournal
        COMPLETED ("COMPLETED"),        // VTodo
        DECLINED ("DECLINED"),          // VEvent, VTodo, VJournal
        IN_PROCESS ("IN-PROCESS"),      // VTodo
        TENTATIVE ("TENTATIVE"),        // VEvent, VTodo
        DELEGATED ("DELEGATED");        // VEvent, VTodo
        //  x-name or iana-token must be added to enum list
        
        private String name;
        @Override public String toString() { return name; }
        ParticipationStatus(String name)
        {
            this.name = name;
        }
    }
}