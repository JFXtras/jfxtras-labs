package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.CalendarUser.CalendarUserType;

public class CalendarUser extends ParameterBase<CalendarUser, CalendarUserType>
{
    /*
     * CONSTRUCTORS
     */
    public CalendarUser()
    {
        super();
    }
  
    public CalendarUser(String content)
    {
        super(content);
    }

    public CalendarUser(CalendarUser source)
    {
        super(source);
    }
    
    public enum CalendarUserType
    {
        INDIVIDUAL, // default is INDIVIDUAL
        GROUP,
        RESOURCE,
        ROOM,
        UNKNOWN;

    }
}
