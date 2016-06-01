package jfxtras.labs.icalendaragenda.trial;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.AgendaTestAbstract;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
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

        VEvent vevent = new VEvent()
                .withCategories(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(5).getDescription())
                .withDateTimeStart(LocalDateTime.of(2016, 5, 16, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 16, 11, 0))
                .withDescription("Daily1 Description")
                .withSummary("Daily1 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
                        .withCount(10));
//                .withRecurrenceRule(new RecurrenceRule2()
//                        .withFrequency(FrequencyType.DAILY)
//                        .withInterval(3)
//                        .withCount(10));
//        VJournal vevent = new VJournal()
//                .withDateTimeStart("20160518T110000")
//                .withSummary("test journal")
//                .withDateTimeStamp("20160518T232502Z")
//                .withUniqueIdentifier("20160518T232502-0@jfxtras.org");
//        VTodo vevent = new VTodo()
//                .withDateTimeStart("20160518T110000")
//                .withDuration(Duration.ofHours(1))
//                .withSummary("test todo")
//                .withDateTimeStamp("20160518T232502Z")
//                .withUniqueIdentifier("20160518T232502-0@jfxtras.org");
        
        ObservableList<VEvent> vEvents = FXCollections.observableArrayList(vevent);
//        ObservableList<VJournal> vEvents = FXCollections.observableArrayList(vevent);
//        ObservableList<VTodo> vEvents = FXCollections.observableArrayList(vevent);
//        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_VJOURNAL);
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        System.out.println("appoingment:" + appointment.getStartLocalDateTime());

        EditVEventTabPane popup = new EditVEventTabPane();
//        EditVJournalTabPane popup = new EditVJournalTabPane();
//        EditVTodoTabPane popup = new EditVTodoTabPane();
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
//            System.out.println("hide:"+ vEvents.size());
            vEvents.stream().forEach(System.out::println);
            primaryStage.hide();
        });
//        vEvents.addListener((InvalidationListener) (obs) ->
//        {
//            System.out.println("VEVENTS:" + vEvents.size());
//            vEvents.stream().forEach(System.out::println);
//        });
    }
}
