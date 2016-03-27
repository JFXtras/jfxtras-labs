package jfxtras.labs.icalendar.parameters;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Format Type
 * FMTYPE
 * RFC 5545 iCalendar 3.2.8 page 19
 * 
 * To specify the content type of a referenced object.
 * 
 *  Example:
 *  ATTACH;FMTTYPE=application/msword:ftp://example.com/pub/docs/
 *   agenda.doc
 * 
 */
public class FormatType extends ParameterBase
{
    public String getTypeName() { return typeName.get(); }
    StringProperty typeNameProperty() { return typeName; }
    private StringProperty typeName = new SimpleStringProperty(this, ParameterEnum.FORMAT_TYPE.toString() + "_TYPE_NAME");
    public void setTypeName(String typeName) { this.typeName.set(typeName); }

    public String getSubtypeName() { return subtypeName.get(); }
    StringProperty subtypeNameProperty() { return subtypeName; }
    private StringProperty subtypeName = new SimpleStringProperty(this, ParameterEnum.FORMAT_TYPE.toString() + "_SUBTYPE_NAME");
    public void setSubtypeName(String subtypeName) { this.subtypeName.set(subtypeName); }
}
