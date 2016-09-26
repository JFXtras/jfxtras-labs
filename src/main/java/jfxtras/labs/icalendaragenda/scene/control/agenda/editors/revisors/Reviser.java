package jfxtras.labs.icalendaragenda.scene.control.agenda.editors.revisors;

import java.util.List;

import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.VCalendar;

/**
 * Interface for the edit behavior of a VComponent
 * 
 * <p>Reviser options from {@link ChangeDialogOption} include:
 * <ul>
 * <li>One
 * <li>All
 * <li>All and ignore recurrences
 * <li>This-and-Future
 * <li>This-and-Future and ignore recurrences 
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 */
public interface Reviser
{
    /** Revise list of iTIP VCalendar components that represent the changes. */
    List<VCalendar> revise();
    
    // TODO - ADD A INITIALIZE METHOD FOR AN ARRAY OF INPUT OBJECT PARAMETERS

//    public static VCalendar defaultPublishVCalendar()
//    {
//        return new VCalendar()
//                .withMethod(MethodType.PUBLISH)
//                .withProductIdentifier(ICalendarAgenda.PRODUCT_IDENTIFIER)
//                .withVersion(new Version());
//    }
//
//    public static VCalendar defaultRequestVCalendar()
//    {
//        return new VCalendar()
//                .withMethod(MethodType.REQUEST)
//                .withProductIdentifier(ICalendarAgenda.PRODUCT_IDENTIFIER)
//                .withVersion(new Version());
//    }
}
