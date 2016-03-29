package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.Participation.ParticipationStatus;

public class Participation extends ParameterBase<Participation, ParticipationStatus>
{
    public Participation()
    {
        super();
    }
  
    public Participation(String content)
    {
        super(content);
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