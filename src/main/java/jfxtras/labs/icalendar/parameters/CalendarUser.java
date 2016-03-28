package jfxtras.labs.icalendar.parameters;

public class CalendarUser extends ParameterBase<CalendarUser, String>
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

    // copy constructor
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
