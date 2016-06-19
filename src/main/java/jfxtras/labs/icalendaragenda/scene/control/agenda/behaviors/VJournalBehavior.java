package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.CreateEditComponentPopupScene;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVJournalPopupScene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class VJournalBehavior extends DisplayableBehavior<VJournal>
{
    public VJournalBehavior(ICalendarAgenda agenda)
    {
        super(agenda);
    }
    
    @Override
    public CreateEditComponentPopupScene getEditScene(Appointment appointment)
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
            return new EditVJournalPopupScene(
                    vComponent,
                    agenda.getVCalendar().getVJournals(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    agenda.getCategories());
        }
    }

    @Override
    public void callRevisor(Appointment appointment)
    {
        // TODO Auto-generated method stub
        
    }
}
