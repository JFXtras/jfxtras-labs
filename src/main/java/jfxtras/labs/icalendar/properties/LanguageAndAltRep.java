package jfxtras.labs.icalendar.properties;

import javafx.beans.property.StringProperty;

public interface LanguageAndAltRep
{
    StringProperty textProperty();
    String getText();
    void setText(String text);
    
    StringProperty languageProperty();
    String getLanguage();
    void setLanguage(String language);
    
    StringProperty alternateTextRepresentationProperty();
    String getAlternateTextRepresentation();
    void setAlternateTextRepresentation(String alternateTextRepresentation);
}
