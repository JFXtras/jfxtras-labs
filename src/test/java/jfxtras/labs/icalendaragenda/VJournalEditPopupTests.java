package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVJournalTabPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.DefaultVComponentFromAppointment;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.VComponentFactory;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

/**
 * Tests the edit popup without an Agenda instance.
 * 
 * @author David Bal
 *
 */
public class VJournalEditPopupTests extends JFXtrasGuiTest
{
    private EditVJournalTabPane editComponentPopup;
    
    @Override
    public Parent getRootNode()
    {
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);
        editComponentPopup = new EditVJournalTabPane();
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        editComponentPopup.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        return editComponentPopup;
    }
    
    @Test
    public void canDisplayPopupWithVJournal()
    {
        VComponentFactory<Appointment> vComponentStore = new DefaultVComponentFromAppointment(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentStore.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        vComponentStore.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        VJournal vjournal = new VJournal()
                .withDateTimeStart("20160518T110000")
                .withSummary("test journal")
                .withDateTimeStamp("20160518T232502Z")
                .withUniqueIdentifier("20160518T232502-0@jfxtras.org");
        List<Appointment> newAppointments = vComponentStore.makeRecurrences(vjournal);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vjournal,
                    Arrays.asList(vjournal),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });

        TextField summary = find("#summaryTextField");
        assertEquals("test journal", summary.getText());

        LocalDateTimeTextField start = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2016, 5, 18, 11, 0), start.getLocalDateTime());
    }
}
