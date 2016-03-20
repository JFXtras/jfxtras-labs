package jfxtras.labs.icalendaragenda.trial.controller;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.components.VComponentDisplayableOld;
import jfxtras.labs.icalendar.properties.descriptive.Description;
import jfxtras.labs.icalendar.properties.descriptive.Summary;
import jfxtras.labs.icalendar.properties.recurrence.rrule.RecurrenceRule;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Daily;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Monthly;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Weekly;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.VEventImpl;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;


/**
 * @author David Bal
 *
 * Instantiates and setups the Agenda.
 * Contains listeners to write changes due to calendar interaction.  Properties are in Agenda class.
 */
public class CalendarController
{
     public ICalendarAgenda agenda = new ICalendarAgenda();
     private final Callback<Collection<VComponentDisplayableOld<Appointment>>, Void> repeatWriteCallback = null;

    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML private BorderPane agendaBorderPane;

    final ToggleGroup skinGroup = new ToggleGroup();
    @FXML private Label dateLabel;
    @FXML private ToggleButton daySkinButton;
    @FXML private ToggleButton weekSkinButton;
    @FXML private ToggleButton monthSkinButton;
    @FXML private ToggleButton agendaSkinButton;
    
    final private LocalDatePicker localDatePicker = new LocalDatePicker(LocalDate.now());
    
    public final ObjectProperty<LocalDate> selectedLocalDateProperty = new SimpleObjectProperty<LocalDate>();
    public final ObjectProperty<LocalDateTime> selectedLocalDateTimeProperty = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
    private Period shiftDuration = Period.ofWeeks(1);
    public final TemporalField dayOfWeekField = WeekFields.of(Locale.getDefault()).dayOfWeek();
    
    boolean editDone = false;
    
    @FXML public void initialize()
    {
       
        daySkinButton.setToggleGroup(skinGroup);
        weekSkinButton.setToggleGroup(skinGroup);
        monthSkinButton.setToggleGroup(skinGroup);
        agendaSkinButton.setToggleGroup(skinGroup);
        weekSkinButton.selectedProperty().set(true);
        
        // Set I/O callbacks // TODO - NOT SUPPORTED YET
        agenda.setRepeatWriteCallback(repeatWriteCallback);
        agenda.setOneAllThisAndFutureDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK);

        // accept new appointments
        agenda.setNewAppointmentCallback((LocalDateTimeRange dateTimeRange) -> 
        {
            Temporal s = dateTimeRange.getStartLocalDateTime().atZone(ZoneId.systemDefault());
            Temporal e = dateTimeRange.getEndLocalDateTime().atZone(ZoneId.systemDefault());
            return new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(s)
                    .withEndTemporal(e)
                    .withSummary("New")
                    .withDescription("")
                    .withAppointmentGroup(agenda.appointmentGroups().get(0));
        });
                
//        // action
//        agenda.setActionCallback( (appointment) -> {
//            System.out.println("Action on " + appointment);
//            return null;
//        });
        
        agendaBorderPane.setCenter(agenda);
        dateLabel.textProperty().bind(makeLocalDateBindings(localDatePicker.localDateProperty()));
        
        localDatePicker.setPadding(new Insets(20, 0, 5, 0)); //(top/right/bottom/left)
        agendaBorderPane.setLeft(localDatePicker);

      localDatePicker.localDateProperty().addListener((observable, oldSelection, newSelection)
      -> {
          if (newSelection != null) agenda.setDisplayedLocalDateTime(newSelection.atStartOfDay());
      });

      // Enable month and year changing to move calendar
      localDatePicker.displayedLocalDateProperty().addListener((observable, oldSelection, newSelection)
              -> {
                  int dayOfMonth = localDatePicker.getLocalDate().getDayOfMonth();
                  localDatePicker.setLocalDate(newSelection.withDayOfMonth(dayOfMonth));
              });

        agenda.setPadding(new Insets(0, 0, 0, 5)); //(top/right/bottom/left)            
    }

    public void setupData(LocalDate startDate, LocalDate endDate)
    {
        VEventImpl vEventSplit = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(8))
                .withDateTimeEnd(LocalDateTime.of(endDate, LocalTime.of(5, 45)))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(endDate.minusDays(1), LocalTime.of(15, 45)))
                .withDescription(new Description("Split Description"))
                .withSummary(new Summary("Split"))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
            agenda.vComponents().add(vEventSplit);
        
        VEventImpl vEventZonedUntil = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(10))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(9, 45)), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")))
                .withDescription(new Description("WeeklyZoned Description"))
                .withRRule(new RecurrenceRule()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(15), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")).withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))))
                .withSummary(new Summary("WeeklyZoned Ends"))
                .withUniqueIdentifier("20150110T080000-1@jfxtras.org");
            agenda.vComponents().add(vEventZonedUntil);
        
        VEventImpl vEventZonedInfinite = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
            .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3))
            .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(12, 00)), ZoneId.of("America/Los_Angeles")))
            .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
            .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(7, 30)), ZoneId.of("America/Los_Angeles")))
            .withDescription(new Description("WeeklyZoned Description"))
            .withRRule(new RecurrenceRule()
                    .withFrequency(new Weekly()
                            .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))))
            .withSummary(new Summary("WeeklyZoned Infinite"))
            .withUniqueIdentifier("20150110T080000-2@jfxtras.org");
        agenda.vComponents().add(vEventZonedInfinite);
        
        VEventImpl vEventLocalDate = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(15))
                .withDateTimeStart(startDate)
                .withDateTimeEnd(startDate.plusDays(1))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription(new Description("LocalDate Description"))
                .withSummary(new Summary("LocalDate"))
                .withUniqueIdentifier("20150110T080000-3@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily()
                                .withInterval(3)));
        agenda.vComponents().add(vEventLocalDate);        

        VEventImpl vEventLocalDateTime = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withDateTimeStart(LocalDateTime.of(startDate, LocalTime.of(11, 00)))
                .withDateTimeEnd(LocalDateTime.of(startDate, LocalTime.of(13, 0)))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription(new Description("LocalDateTime Daily Description"))
                .withSummary(new Summary("LocalDateTime Daily"))
                .withUniqueIdentifier("20150110T080000-4@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily()));
        agenda.vComponents().add(vEventLocalDateTime); 
        
        VEventImpl vEventLocalDateTimeMonthly = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(17))
                .withDateTimeStart(LocalDateTime.of(startDate, LocalTime.of(14, 00)))
                .withDateTimeEnd(LocalDateTime.of(startDate, LocalTime.of(15, 0)))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription(new Description("Monthly Description"))
                .withSummary(new Summary("Monthly"))
                .withUniqueIdentifier("20150110T080000-5@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()));
        agenda.vComponents().add(vEventLocalDateTimeMonthly); 
        
        DayOfWeek dayOfWeek = DayOfWeek.from(startDate.plusDays(2));
        int ordinalWeekNumber = DateTimeUtilities.weekOrdinalInMonth(startDate.plusDays(2));
        VEventImpl vEventLocalDateMonthlyOrdinal = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(5))
                .withDateTimeStart(startDate.plusDays(2))
                .withDateTimeEnd(startDate.plusDays(3))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription(new Description("Monthly Ordinal Description " + dayOfWeek + "#" + ordinalWeekNumber + " in month"))
                .withSummary(new Summary("Monthly Ordinal"))
                .withUniqueIdentifier("20150110T080000-6@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()
                                .withByRules(new ByDay(new ByDay.ByDayPair(dayOfWeek, ordinalWeekNumber)))));
        agenda.vComponents().add(vEventLocalDateMonthlyOrdinal);
        
        // replace Agenda's appointmentGroups with the ones used in the test events.
        agenda.appointmentGroups().clear();
        agenda.appointmentGroups().addAll(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
    }
    
    @FXML private void handleDateIncrement() {
        LocalDate oldLocalDate = localDatePicker.getLocalDate();
        localDatePicker.localDateProperty().set(oldLocalDate.plus(shiftDuration));
    }
    
    @FXML private void handleDateDecrement() {
        LocalDate oldLocalDate = localDatePicker.getLocalDate();
        localDatePicker.localDateProperty().set(oldLocalDate.minus(shiftDuration));
    }
    
    @FXML private void handleWeekSkin() {
        shiftDuration = Period.ofWeeks(1);
        agenda.setSkin(new AgendaWeekSkin(agenda));
    }

    @FXML private void handleDaySkin() {
        shiftDuration = Period.ofDays(1);
        agenda.setSkin(new AgendaDaySkin(agenda));
    }
    
    public static StringBinding makeLocalDateBindings(ObjectProperty<LocalDate> p)
    {
        final DateTimeFormatter DATE_FORMAT2 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        return Bindings.createStringBinding(() -> DATE_FORMAT2.format(p.get()), p);
    }
    
}
