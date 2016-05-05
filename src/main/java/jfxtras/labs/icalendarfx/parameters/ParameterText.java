package jfxtras.labs.icalendarfx.parameters;

/**
 * Text (String) based parameter
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see CommonName
 * @see FormatType
 * @see Language
 */
public abstract class ParameterText<T> extends ParameterBase<T,String>
{
    ParameterText()
    {
        super();
    }
  
    ParameterText(String content)
    {
        super(removeDoubleQuote(content));
    }

    ParameterText(ParameterText<T> source)
    {
        super(source);
    }
        
    @Override
    public void parseContent(String content)
    {
        setValue(removeDoubleQuote(content));
    } 
}
