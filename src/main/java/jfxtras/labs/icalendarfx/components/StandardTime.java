package jfxtras.labs.icalendarfx.components;

public class StandardTime extends StandardOrDaylightBase<StandardTime>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.STANDARD;
    }
    
    /*
     * CONSTRUCTORS
     */
    public StandardTime() { }
    
    public StandardTime(String contentLines)
    {
        super(contentLines);
    }

    public StandardTime(StandardTime source)
    {
        // TODO Auto-generated constructor stub
    }

    /** Parse content lines into calendar component object */
    public static StandardTime parse(String contentLines)
    {
        StandardTime component = new StandardTime();
        component.parseContent(contentLines);
        return component;
    }
}
