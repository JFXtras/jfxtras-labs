package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.util.List;

public class Delegatees extends ParameterBase<Delegatees, List<URI>>
{
    public Delegatees()
    {
        super();
    }
    
    public Delegatees(String content)
    {
        super();
        List<URI> value = myParameterEnum().parse(content);
        setValue(value);
    }
    
    public Delegatees(Delegatees source)
    {
        super(source);
    }
}
