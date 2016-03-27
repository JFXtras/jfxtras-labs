package jfxtras.labs.icalendar.parameters;

public class ParticipationRole extends ParameterBase
{
    public enum ParticipationRoleType
    {
        CHAIR ("CHAIR"),
        REQUIRED_PARTICIPANT ("REQ-PARTICIPANT"),
        OPTIONAL_PARTICIPANT ("OPT-PARTICIPANT"),
        NON_PARTICIPANT ("NON-PARTICIPANT");
        
        private String name;
        @Override public String toString() { return name; }
        ParticipationRoleType(String name)
        {
            this.name = name;
        }
    }
}
