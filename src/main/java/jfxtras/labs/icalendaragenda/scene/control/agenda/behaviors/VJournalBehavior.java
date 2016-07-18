package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVComponentScene;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVJournalScene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class VJournalBehavior extends DisplayableBehavior<VJournal>
{
    @Override
    public EditVComponentScene getEditPopupScene(ICalendarAgenda agenda, Appointment appointment)
    {
        VJournal vComponent = (VJournal) agenda.appointmentVComponentMap().get(System.identityHashCode(appointment));
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
            return null;
        } else
        {
            return new EditVJournalScene(
                    vComponent,
                    agenda.getVCalendar().getVJournals(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    agenda.getCategories());
        }
    }

    @Override
    public void callRevisor(ICalendarAgenda agenda, Appointment appointment)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void callDeleter(VComponent vComponent, List<? extends VComponent> vComponents,
            Temporal startOriginalRecurrence)
    {
        // TODO Auto-generated method stub
        
    }

//    @Override
//    public void callRevisor(ICalendarAgenda agenda, Appointment appointment)
//    {
//        throw new RuntimeException("not implemented");
//        // TODO Auto-generated method stub
//        
//    }

//    @Override
//    public void callDeleter(ICalendarAgenda agenda, Appointment appointment)
//    {
//        throw new RuntimeException("not implemented");
//        // TODO Auto-generated method stub
//        
//    }
}
