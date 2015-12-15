package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.VComponentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarDeleteTest extends ICalendarTestAbstract
{
    @Test
    public void deleteVComponent1()
    {
        VEventImpl vEvent = getIndividual1();
        vEvent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 8, 0, 0));
        vEvent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances();
        appointments.addAll(newAppointments);
        assertEquals(1, appointments.size());
        VEventImpl vEventOld = (VEventImpl) VComponentFactory.newVComponent(vEvent);
        LocalDateTime dateTimeStartInstanceOld = appointments.get(0).getStartLocalDateTime();

        final ICalendarUtilities.WindowCloseType result = vEvent.delete(
                dateTimeStartInstanceOld
              , vEventOld
              , appointments
              , vComponents
              , null
              , a -> true
              , null);

        assertEquals(0, vComponents.size());
        assertEquals(0, appointments.size());
    }

    @Test
    public void deleteVComponent2()
    {
        VEventImpl vEvent = getIndividual1();
        VEventImpl vEvent2 = getWeekly2();
        vEvent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 8, 0, 0));
        vEvent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent, vEvent2));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances();
        appointments.addAll(newAppointments);
        assertEquals(1, appointments.size());
        VEventImpl vEventOld = (VEventImpl) VComponentFactory.newVComponent(vEvent);
        vEvent2.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 8, 0, 0));
        vEvent2.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 15, 0, 0));
        LocalDateTime dateTimeStartInstanceOld = appointments.get(0).getStartLocalDateTime();
        Collection<Appointment> newAppointments2 = vEvent2.makeInstances();
        appointments.addAll(newAppointments2);

        final ICalendarUtilities.WindowCloseType result = vEvent.delete(
                dateTimeStartInstanceOld
              , vEventOld // may be unnecessary
              , appointments
              , vComponents
              , null
              , a -> true
              , null);

        assertEquals(1, vComponents.size());
        assertEquals(2, appointments.size());
    }
}
