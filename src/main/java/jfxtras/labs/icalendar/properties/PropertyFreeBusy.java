package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.FreeBusyType;

public interface PropertyFreeBusy<T> extends Property<T>
{
    /**
     * FBTYPE: Incline Free/Busy Time Type
     * RFC 5545, 3.2.9, page 20
     * 
     * To specify the free or busy time type.
     * 
     * Values can be = "FBTYPE" "=" ("FREE" / "BUSY" / "BUSY-UNAVAILABLE" / "BUSY-TENTATIVE"
     */
    FreeBusyType getFreeBusyType();
    ObjectProperty<FreeBusyType> freeBusyTypeProperty();
    void setFreeBusyType(FreeBusyType freeBusyType);
}
