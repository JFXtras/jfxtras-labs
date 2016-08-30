package jfxtras.labs.icalendarfx.parameters;

import java.util.List;

/**
 * Some other IANA-registered iCalendar parameter.
 * 
 * @author David Bal
 *
 */
public class IANAParameter extends ParameterBase<IANAParameter, String>
{
    /** get list of registered IANA parameter names */
    public static List<String> getRegisteredIANAParameters()
    {
        return registeredIANAParameters2;
    }
    /** set list of registered IANA parameter names */
    public static void setRegisteredIANAParameters(List<String> registeredIANAParameters)
    {
        registeredIANAParameters2 = registeredIANAParameters;
    }
    private static List<String> registeredIANAParameters2;
    
    final String name;
    @Override
    public String name() { return name; }
    
    /*
     * CONSTRUCTORS
     */
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
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getRegisteredIANAParameters() == null)
        {
            errors.add("There are no registered IANA parameter names");
        } else if (name() != null && ! getRegisteredIANAParameters().contains(name()))
        {
            errors.add(name() + " is not a registereed IANA parameter name");
        }
        return errors;
    }
}
