package jfxtras.labs.icalendaragenda.trial;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVJournalTabPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper.CallbackTwoParameters;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Demo of edit VEvent popup
 * 
 * @author David Bal
 *
 */
public class EditComponentPopupTrial extends Application
{
    
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage primaryStage)
	{
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);

//        VEvent vevent = ICalendarStaticComponents.getDaily1();
        VJournal vevent = new VJournal()
                .withDateTimeStart("20160518T110000")
                .withSummary("test journal")
                .withDateTimeStamp("20160518T232502Z")
                .withUniqueIdentifier("20160518T232502-0@jfxtras.org");
//        ObservableList<VEvent> vEvents = FXCollections.observableArrayList(vevent);
        ObservableList<VJournal> vEvents = FXCollections.observableArrayList(vevent);
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(MAKE_APPOINTMENT_TEST_CALLBACK_VJOURNAL);
//        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

//        EditVEventTabPane popup = new EditVEventTabPane();
        EditVJournalTabPane popup = new EditVJournalTabPane();
        popup.setupData(
                appointment,
                vevent,
                vEvents,
                ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS
                );

        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        popup.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        Scene scene = new Scene(popup);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Edit Popup Demo");
        primaryStage.show();
        
        popup.isFinished().addListener((obs, oldValue, newValue) -> 
        {
            System.out.println("hide:");
            primaryStage.hide();
        });
        vEvents.addListener((InvalidationListener) (obs) ->
        {
            System.out.println("VEVENTS:" + vEvents.size());
            vEvents.stream().forEach(System.out::println);
        });
    }
	
    /** Callback to make appointment from VComponent and Temporal */
    public static final CallbackTwoParameters<VComponentRepeatable<?>, Temporal, Appointment> MAKE_APPOINTMENT_TEST_CALLBACK_VJOURNAL = (vComponentEdited, startTemporal) ->
    {
        Boolean isWholeDay = vComponentEdited.getDateTimeStart().getValue() instanceof LocalDate;
        VJournal vComponent = (VJournal) vComponentEdited;
//        final TemporalAmount adjustment = vComponentLocatable.getActualDuration();
//        Temporal endTemporal = startTemporal.plus(adjustment);

        // Make appointment
        Appointment appt = new Agenda.AppointmentImplTemporal()
                .withStartTemporal(startTemporal)
//                .withEndTemporal(endTemporal)
                .withDescription( (vComponent.getDescriptions() != null) ? vComponent.getDescriptions().get(0).getValue() : null )
                .withSummary( (vComponent.getSummary() != null) ? vComponent.getSummary().getValue() : null)
                .withWholeDay(isWholeDay);
        return appt;
    };
    
    /** Callback to make appointment from VComponent and Temporal */
    public static final CallbackTwoParameters<VComponentRepeatable<?>, Temporal, Appointment> MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE = (vComponentEdited, startTemporal) ->
    {
        Boolean isWholeDay = vComponentEdited.getDateTimeStart().getValue() instanceof LocalDate;
        VComponentLocatable<?> vComponentLocatable = (VComponentLocatable<?>) vComponentEdited;
        final TemporalAmount adjustment = vComponentLocatable.getActualDuration();
        Temporal endTemporal = startTemporal.plus(adjustment);

        // Make appointment
        Appointment appt = new Agenda.AppointmentImplTemporal()
                .withStartTemporal(startTemporal)
                .withEndTemporal(endTemporal)
                .withDescription( (vComponentLocatable.getDescription() != null) ? vComponentLocatable.getDescription().getValue() : null )
                .withSummary( (vComponentLocatable.getSummary() != null) ? vComponentLocatable.getSummary().getValue() : null)
                .withLocation( (vComponentLocatable.getLocation() != null) ? vComponentLocatable.getLocation().getValue() : null)
                .withWholeDay(isWholeDay);
        return appt;
    };
}
