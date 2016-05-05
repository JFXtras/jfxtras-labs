package jfxtras.labs.icalendarfx.parameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
    
    ParameterURIList(ParameterURIList<T> source)
    {
        super(source);
    }
    
    @Override
    public void parseContent(String content)
    {
        List<URI> uriList = new ArrayList<>();
        Iterator<String> i = Arrays.stream(content.split(",")).iterator();
        while (i.hasNext())
        {
            String item = i.next();
            URI uri = null;
            try
            {
                uri = new URI(removeDoubleQuote(item));
            } catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
            uriList.add(uri);
        }
        setValue(uriList);
    } 
}
