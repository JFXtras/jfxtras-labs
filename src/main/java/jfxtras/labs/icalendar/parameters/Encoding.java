package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.Encoding.EncodingEnum;

public class Encoding extends ParameterBase<Encoding, EncodingEnum>
{
    /*
     * CONSTRUCTORS
     */
    public Encoding()
    {
        super();
    }
  
    public Encoding(String content)
    {
        super(content);
    }

    public Encoding(Encoding source)
    {
        super(source);
    }
    
    public enum EncodingEnum
    {
        EIGHT_BIT ("8BIT"),
        BASE64 ("BASE64");
        
        private String name;
        @Override public String toString() { return name; }
        EncodingEnum(String name)
        {
            this.name = name;
        }
    }
}