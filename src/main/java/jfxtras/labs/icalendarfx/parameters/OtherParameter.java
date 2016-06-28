package jfxtras.labs.icalendarfx.parameters;

/**
 * Parameter with custom name and value
 * 
 * @author David Bal
 *
 */
public class OtherParameter extends ParameterText<OtherParameter>
{
    final String name;
    public String getName() { return name; }
    
    public OtherParameter(String name)
    {
        super();
        this.name = name;
    }

    public OtherParameter(OtherParameter source)
    {
        super(source);
        this.name = source.name;
    }

    public static OtherParameter parse(String content)
    {
        String name = content.substring(0, content.indexOf('='));
        OtherParameter parameter = new OtherParameter(name);
        parameter.parseContent(content);
        return parameter;
    }
    
    @Override
    public String toContent()
    {
        return (getValue() != null) ? getName() + "=" + getValue() : null;
    }
}
