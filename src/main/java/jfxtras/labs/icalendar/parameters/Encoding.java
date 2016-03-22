package jfxtras.labs.icalendar.parameters;

public enum Encoding
{
    EIGHT_BIT ("8BIT"),
    BASE64 ("BASE64");
    
    private String name;
    @Override public String toString() { return name; }
    Encoding(String name)
    {
        this.name = name;
    }
}
