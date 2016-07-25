package jfxtras.labs.icalendaragenda.trial;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.AgendaTestAbstract;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors.EditDisplayableScene;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors.SimpleEditSceneFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.DefaultRecurrenceFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.RecurrenceFactory;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
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

        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
//        VEvent vevent = ICalendarStaticComponents.getWholeDayDaily4();
        myCalendar.addVComponent(vevent);
//        VEvent vevent = new VEvent()
//                .withCategories(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(5).getDescription())
//                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 16, 10, 0), ZoneId.of("America/Los_Angeles")))
//                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 16, 11, 0), ZoneId.of("America/Los_Angeles")))
//                .withDescription("Daily1 Description")
//                .withSummary("Daily1 Summary")
//                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
//                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
//                .withRecurrenceRule(new RecurrenceRule2()
//                        .withFrequency(FrequencyType.WEEKLY)
//                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
//                        .withCount(10));
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
        
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS); // default VComponent store - for Appointments, if other implementation used make new store
        recurrenceFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        
        List<String> categories = IntStream.range(0, 24)
              .mapToObj(i -> new String("group" + (i < 10 ? "0" : "") + i))
              .collect(Collectors.toList());
        Object[] params = new Object[] {
                myCalendar,
                appointment.getStartTemporal(),
                appointment.getEndTemporal(),
                categories
                };
        EditDisplayableScene scene = SimpleEditSceneFactory.newScene(vevent, params);

        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        scene.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ICalendar Edit Popup Demo");
        primaryStage.show();
        
        scene.getEditDisplayableTabPane().newVComponentsProperty().addListener((obs, oldValue, newValue) ->
        {
            myCalendar.getVEvents().stream().forEach(System.out::println);
            primaryStage.hide();
        });
    }
}
