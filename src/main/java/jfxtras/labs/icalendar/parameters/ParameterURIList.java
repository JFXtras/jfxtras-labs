package jfxtras.labs.icalendar.parameters;

import java.net.URI;
import java.util.List;

/**
 * Parameter with value of List<URI>
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see Delegatees
 * @see Delegators
 * @see GroupMembership
 */
public abstract class ParameterURIList<T> extends ParameterBase<T, List<URI>>
{
    public ParameterURIList()
    {
        super();
    }

    public ParameterURIList(List<URI> values)
    {
        super(values);
    }
    
    public ParameterURIList(String content)
    {
        super(makeURIList(content));
    }
    
    public ParameterURIList(ParameterURIList<T> source)
    {
        super(source);
    }
}
