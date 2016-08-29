package jfxtras.labs.icalendarfx.parameters;

/**
 * A non-standard, experimental parameter.
 * 
 * @author David Bal
 *
 */
public class NonStandardParameter extends ParameterBase<NonStandardParameter, String>
{
    final String name;
    @Override
    public String name() { return name; }
    
    public NonStandardParameter(String content)
    {
        super();
        int equalsIndex = content.indexOf('=');
        name = (equalsIndex >= 0) ? content.substring(0, equalsIndex) : content;
        String value = (equalsIndex >= 0) ? content.substring(equalsIndex+1) : null;
        setValue(value);
    }

    public NonStandardParameter(NonStandardParameter source)
    {
        super(source);
        this.name = source.name;
    }

    public static NonStandardParameter parse(String content)
    {
        return new NonStandardParameter(content);
    }
    
    @Override
    public String toContent()
    {
        return (getValue() != null) ? name() + "=" + getValue() : null;
    }
}
