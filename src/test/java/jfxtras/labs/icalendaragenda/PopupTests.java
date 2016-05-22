package jfxtras.labs.icalendaragenda;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AppointmentEditLoader;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

public class PopupTests extends JFXtrasGuiTest
{
    
    @Override
    public Parent getRootNode()
    {
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);
        return new Label("");
    }
    
    @Test
    public void makeEditPopup()
    {
        // I/O callbacks
        Callback<Collection<VComponentDisplayable<?>>, Void> repeatWriteCallback = null;
        Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback = null;

        final Collection<Appointment> appointments = new ArrayList<>();
        final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>();    
        final Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VComponent that made it */

        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
//                appointments,
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK
//                vComponentAppointmentMap,
//                appointmentVComponentMap
                );
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        
        VEvent vevent = ICalendarComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        TestUtil.runThenWaitForPaintPulse( () -> {            
        Stage editPopup = new AppointmentEditLoader(
              appointment,
              vevent,
              appointments,
              ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS,
              appointmentGroupWriteCallback,
              repeatWriteCallback);

        editPopup.show();
        });
        TestUtil.sleep(3000);
    }
}
