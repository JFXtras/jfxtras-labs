package jfxtras.labs.icalendar.parameters;

/**
 * Text (String) based parameter
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see CommonName
 */
public abstract class ParameterText<T> extends ParameterBase<T,String>
{
    public ParameterText()
    {
        super();
    }
  
    public ParameterText(String content)
    {
        super(removeDoubleQuote(content));
    }

    public ParameterText(ParameterText<T> source)
    {
        super(source);
    }
}
