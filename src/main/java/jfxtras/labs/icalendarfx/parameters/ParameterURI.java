package jfxtras.labs.icalendarfx.parameters;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Parameter with value of URI
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see SentBy
 * @see DirectoryEntryReference
 */
public abstract class ParameterURI<T> extends ParameterBase<T, URI>
{
    ParameterURI()
    {
        super();
    }

    ParameterURI(URI values)
    {
        super(values);
    }
    
    ParameterURI(ParameterURI<T> source)
    {
        super(source);
    }
    
    @Override
    public void parseContent(String content)
    {
        URI uri = null;
        try
        {
            uri = new URI(removeDoubleQuote(content));
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        setValue(uri);
    }
}
