package jfxtras.labs.icalendarfx.parameters;

import java.util.Arrays;
import java.util.List;

/**
 * Some other IANA-registered iCalendar parameter.
 * 
 * @author David Bal
 *
 */
public class IANAParameter extends ParameterBase<IANAParameter, String>
{
    public static final List<String> REGISTERED_IANA_PARAMETER_NAMES = 
            Arrays.asList("TESTPARAM1", "TESTPARAM2");
    
    final String name;
    @Override
    public String name() { return name; }
    
    public IANAParameter(String content)
    {
        super();
        int equalsIndex = content.indexOf('=');
        name = (equalsIndex >= 0) ? content.substring(0, equalsIndex) : content;
        String value = (equalsIndex >= 0) ? content.substring(equalsIndex+1) : null;
        setValue(value);
    }

    public IANAParameter(IANAParameter source)
    {
        super(source);
        this.name = source.name;
    }

    public static IANAParameter parse(String content)
    {
        return new IANAParameter(content);
    }
    
    @Override
    public String toContent()
    {
        return (getValue() != null) ? name() + "=" + getValue() : null;
    }
}
