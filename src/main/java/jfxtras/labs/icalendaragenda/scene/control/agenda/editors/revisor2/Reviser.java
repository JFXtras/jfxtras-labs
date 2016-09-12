package jfxtras.labs.icalendaragenda.scene.control.agenda.editors.revisor2;

import java.util.List;

import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

/**
 * Interface for the edit behavior of a VComponent
 * 
 * <p>Reviser options include:
 * <ul>
 * <li>One
 * <li>All
 * <li>This-and-Future
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 */
public interface Reviser
{
    /** Revise a calendar component.  Returns the result of the revision */
    List<VCalendar> revise();
    
    // TODO - ADD A INITIALIZE METHOD FOR AN ARRAY OF INPUT OBJECT PARAMETERS

    public static VCalendar defaultPublishVCalendar()
    {
        return new VCalendar()
                .withMethod(MethodType.PUBLISH)
                .withProductIdentifier(ICalendarAgenda.PRODUCT_IDENTIFIER)
                .withVersion(new Version());
    }
    
    public static VCalendar defaultCancelVCalendar()
    {
        return new VCalendar()
                .withMethod(MethodType.CANCEL)
                .withProductIdentifier(ICalendarAgenda.PRODUCT_IDENTIFIER)
                .withVersion(new Version());
    }
}
