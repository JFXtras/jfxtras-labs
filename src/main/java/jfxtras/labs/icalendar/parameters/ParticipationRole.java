package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.ParticipationRole.ParticipationRoleType;

public class ParticipationRole extends ParameterBase<ParticipationRole, ParticipationRoleType>
{
    public ParticipationRole()
    {
        super();
    }
  
    public ParticipationRole(String content)
    {
        super(content);
    }

    public ParticipationRole(ParticipationRole source)
    {
        super(source);
    }
    
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
