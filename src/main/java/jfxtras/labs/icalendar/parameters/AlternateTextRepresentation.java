package jfxtras.labs.icalendar.parameters;

import java.net.URI;

public class AlternateTextRepresentation extends ParameterBase<AlternateTextRepresentation, URI>
{
    public AlternateTextRepresentation()
    {
        super();
    }
  
    public AlternateTextRepresentation(String content)
    {
        super(content);
    }

    // copy constructor
    public AlternateTextRepresentation(AlternateTextRepresentation source)
    {
        super(source);
    }
}
