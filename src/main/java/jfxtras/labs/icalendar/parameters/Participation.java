package jfxtras.labs.icalendar.parameters;

public class Participation extends ParameterBase
{
    // Default ParticipationStatus is NEEDS_ACTION
    public enum ParticipationStatus
    {
        NEEDS_ACTION ("NEEDS-ACTION"),  // VEvent, VTodo, VJournal
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