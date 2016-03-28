package jfxtras.labs.icalendar.parameters;

import java.net.URI;

public class DirectoryEntryReference extends ParameterBase<DirectoryEntryReference, URI>
{
    public DirectoryEntryReference()
    {
        super();
    }
  
    public DirectoryEntryReference(String content)
    {
        super(content);
    }

    public DirectoryEntryReference(DirectoryEntryReference source)
    {
        super(source);
    }
}
