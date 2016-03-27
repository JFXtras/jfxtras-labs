package jfxtras.labs.icalendar.parameters;

public class Encoding extends ParameterBase
{
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