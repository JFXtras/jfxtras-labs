package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.util.List;

public class Delegators extends ParameterBase<Delegators, List<URI>>
{
    public Delegators()
    {
        super();
    }
    
    public Delegators(String content)
    {
        super();
        List<URI> value = myParameterEnum().parse(content);
        setValue(value);
    }
    
    public Delegators(Delegators source)
    {
        super(source);
    }
}
