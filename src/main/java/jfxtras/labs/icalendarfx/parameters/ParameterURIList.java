package jfxtras.labs.icalendarfx.parameters;

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
    ParameterURIList()
    {
        super();
    }

    ParameterURIList(List<URI> values)
    {
        super(values);
    }
    
    ParameterURIList(String content)
    {
        super(makeURIList(content));
    }
    
    ParameterURIList(ParameterURIList<T> source)
    {
        super(source);
    }
}
