package jfxtras.labs.repeatagenda.trial.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.labs.repeatagenda.ICalendarTestAbstract;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
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
public class CalendarController {

//    private MyData data;

     public ICalendarAgenda agenda = new ICalendarAgenda();
//     private final Callback<Collection<Appointment>, Void> appointmentWriteCallback =
//             a -> { RepeatableAppointmentImpl.writeToFile(a, Settings.APPOINTMENTS_FILE); return null; };
     private final Callback<Collection<VComponent<Appointment>>, Void> repeatWriteCallback = null;
//             r -> { RepeatImpl.writeToFile(r); return null; };

     private LocalDateTimeRange dateTimeRange;
//     private RepeatMenuOld repeatMenu;
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML private BorderPane agendaBorderPane;

    final ToggleGroup skinGroup = new ToggleGroup();
    @FXML private Label dateLabel;
    @FXML private ToggleButton daySkinButton;
    @FXML private ToggleButton weekSkinButton;
    @FXML private ToggleButton monthSkinButton;
    @FXML private ToggleButton agendaSkinButton;
    
    final private LocalDatePicker localDatePicker = new LocalDatePicker(LocalDate.now());
//    private LocalDate startDate;
//    private LocalDate endDate;
    
    public final ObjectProperty<LocalDate> selectedLocalDateProperty = new SimpleObjectProperty<LocalDate>();
    public final ObjectProperty<LocalDateTime> selectedLocalDateTimeProperty = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
    private Period shiftDuration = Period.ofWeeks(1);
    public final TemporalField dayOfWeekField = WeekFields.of(Locale.getDefault()).dayOfWeek();
    
    boolean editDone = false;
    
    @FXML public void initialize()
    {
//        agenda.setKeyHandler();
        // ResouceBundle
//        Locale myLocale = Locale.getDefault();
//        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.samples.repeatagenda.Bundle", myLocale);
//        agenda.setResourceBundle(resources);
//        Settings.setup(resources);
        
        daySkinButton.setToggleGroup(skinGroup);
        weekSkinButton.setToggleGroup(skinGroup);
        monthSkinButton.setToggleGroup(skinGroup);
        agendaSkinButton.setToggleGroup(skinGroup);
        weekSkinButton.selectedProperty().set(true);
        
        // Set I/O callbacks
//        agenda.setAppointmentWriteCallback(appointmentWriteCallback);
        agenda.setRepeatWriteCallback(repeatWriteCallback);

        //        // setup appointment groups
//        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
//        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
//            lAppointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
//        }

        // accept new appointments
        agenda.setNewAppointmentCallback((LocalDateTimeRange dateTimeRange) -> 
        {
            Appointment appointment = new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime())
                .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime())
                .withSummary("New")
                .withDescription("")
                .withAppointmentGroup(agenda.appointmentGroups().get(0));
            return appointment;
        });

//        agenda.setEditAppointmentCallback((AppointmentEditData a) -> {
//            System.out.println("start edit callback");
//            Stage stage = new RepeatMenuStage2(a);
//            stage.show();
//            System.out.println("end edit callback");
//            return null;
//        });

//        agenda.setEditAppointmentCallback((Appointment appointment) -> {
//            repeatMenu = new RepeatMenu(
//                    (RepeatableAppointment) appointment
//                    , agenda.dateTimeRange()
//                    , agenda.appointments()
//                    , agenda.getRepeats()
//                    , agenda.appointmentGroups()
//                    , a -> { AppointmentFactory.writeToFile(a); return null; }
//                    , r -> { RepeatImpl.writeToFile(r); return null; }); // make new object when closed (problem with passing pane - null for now)
//            repeatMenu.show();
//            return null;
//        });
        
//        // manage repeat-made appointments when the range changes
//        agenda.setLocalDateTimeRangeCallback(dateTimeRange -> {
//            this.dateTimeRange = dateTimeRange;
//            LocalDate startDate = dateTimeRange.getStartLocalDateTime().toLocalDate();
//            LocalDate endDate = dateTimeRange.getEndLocalDateTime().toLocalDate();
//            System.out.println("dates changed " + startDate + " " + endDate);
//            System.out.println("2agenda.appointments().size() " + appointments().size());
//            appointments().removeIf(a -> ((RepeatableAppointment) a).isRepeatMade());
//            getRepeats().stream().forEach(r -> r.getAppointments().clear());
//            getRepeats().stream().forEach(r ->
//            { // Make new repeat-made appointments inside range
//                Collection<RepeatableAppointment> newAppointments = r.makeAppointments(startDate, endDate);
//                appointments().addAll(newAppointments);
////                agenda.appointments().addAll(newAppointments);
//                System.out.println("newAppointments " + newAppointments.size());
////                r.removeOutsideRangeAppointments(data.getAppointments());                 // remove outside range appointments
//            });
//            System.out.println("3agenda.appointments().size() " + appointments().size());
////            System.exit(0);
//            return null; // return argument for the Callback
//        });
                
        // action
        agenda.setActionCallback( (appointment) -> {
            System.out.println("Action on " + appointment);
            return null;
        });
        
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

    public void setupData(LocalDate startDate, LocalDate endDate) {

//        this.data = data;
//        this.startDate = startDate;
//        this.endDate = endDate;
        
        Class<Agenda.AppointmentImplLocal> clazz = Agenda.AppointmentImplLocal.class;
        
//        VEventImpl vEvent = new VEventImpl(agenda.appointmentGroups());
//        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0));
//        vEvent.setCategories(data.appointmentGroups().get(3).getDescription());
//        vEvent.setDurationInNanos(2700L * Duration.ofSeconds(1).toNanos());
//        vEvent.setDescription("Weekly1 Description");
//        vEvent.setSummary("Weekly1 Summary");
//        vEvent.setAppointmentClass(clazz);
//        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 11, 9, 8, 30));
//        vEvent.setUniqueIdentifier("20151109T082900-0@jfxtras.org");
//        RRule rule = new RRule();
//        vEvent.setRRule(rule);
//        Frequency daily = new Daily();
//        rule.setFrequency(daily);

//        RRule rule = new RRule();
//        vEvent.setRRule(rule);
//        Frequency weekly = new Weekly().withInterval(2);
//        rule.setFrequency(weekly);
//        Rule byRule = new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
//        weekly.addByRule(byRule);
//        
//        data.getVComponents().add(vEvent);
//        
//        if (! data.appointmentGroups().isEmpty()) 
//        { // overwrite default appointmentGroups with ones read from file if not empty
//            agenda.appointmentGroups().clear();
//            agenda.appointmentGroups().addAll(data.appointmentGroups());
//        }
//        agenda.vComponents().add(ICalendarTestAbstract.getDailyWithException1());
//        VEventImpl daily1 = ICalendarTestAbstract.getDaily1();
//        agenda.vComponents().add(daily1);
        
        agenda.vComponents().add(ICalendarTestAbstract.getDaily1());
        // replace Agenda's appointmentGroups with the ones used in the test events.
        agenda.appointmentGroups().clear();
        agenda.appointmentGroups().addAll(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        

        //        agenda.vComponents().addAll(data.getVComponents());
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
        AgendaSkin skin = new AgendaWeekSkin(agenda);
//        shiftDuration = skin.shiftDuration();
        shiftDuration = Period.ofWeeks(1);
        agenda.setSkin(new AgendaWeekSkin(agenda));
    }

    @FXML private void handleDaySkin() {
        AgendaSkin skin = new AgendaWeekSkin(agenda);
//        shiftDuration = skin.shiftDuration();
        shiftDuration = Period.ofDays(1);
        agenda.setSkin(new AgendaDaySkin(agenda));
    }
    
    public static StringBinding makeLocalDateBindings(ObjectProperty<LocalDate> p)
    {
        final DateTimeFormatter DATE_FORMAT2 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        return Bindings.createStringBinding(() -> DATE_FORMAT2.format(p.get()), p);
    }
    
}
