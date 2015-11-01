package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/**
 * Contains rules for repeatable appointments
 *  
 * @author David Bal
 */
public abstract class Repeat {
    
    // TODO - MAKE A WAY TO APPLY MULTIPLE RULES ON TOP OF EACH OTHER - like iCalendar
    // Use RepeatRule interface.  Make implementing class for each rule type.
    // Each provides a steam of start dates as output.  Each rule either adds or removes dates from stream.  Make new stream.
    // This class will have a list of RepeatRules to be applied sequentially
    
//    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    // Range for which appointments are to be generated.  Should match the dates displayed on the calendar.
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    public void setLocalDateTimeDisplayRange(LocalDateTimeRange dateTimeRange) { startDate = dateTimeRange.getStartLocalDateTime(); endDate = dateTimeRange.getEndLocalDateTime(); }
    public Repeat withLocalDateTimeDisplayRange(LocalDateTimeRange dateTimeRange) { setLocalDateTimeDisplayRange(dateTimeRange); return this; }
    
//    // TODO - REPLACE CALLBACKS WITH CLASSES AND NEWINSTANCE
//    /**
//     * Repeat doesn't know how to make the new appointments.  Appointment is an interface defined in Agenda.  The callback
//     * produces a new Appointment object of the implemented class.
//     */
//    private Callback<LocalDateTimeRange, Appointment> newAppointmentCallback;   // callback set in constructor
//    public Callback<LocalDateTimeRange, Appointment> getNewAppointmentCallback() { return newAppointmentCallback; }

    private Class<? extends RepeatableAppointment> appointmentClass;
    public void setAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { this.appointmentClass = appointmentClass; }
    public Class<? extends RepeatableAppointment> getAppointmentClass() { return appointmentClass; }
    public Repeat withAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { setAppointmentClass(appointmentClass); return this; }

    // sequential int key part of UID
    private static Integer nextKey = 0;
    private Integer key;
    Integer getKey() { return key; }
    void setKey(Integer value) { key = value; } 
//    public RepeatImpl withKey(Integer value) { setKey(value); return this; }
//    public boolean hasKey() { return (getKey() != null); } // new Repeat has no key
    
    /** Unique identifier */
    private String UID;
    public String getUID() { return UID; }
    public void setUID(String s) { UID = s; }
    private String UIDGenerator()
    {
        String dateTime = formatter.format(LocalDateTime.now());
        String keyString = getKey().toString();
        String domain = "jfxtras-agenda";
        return dateTime + keyString + domain;
    }
    
    final private ObjectProperty<Frequency> frequency = new SimpleObjectProperty<Frequency>();
    public ObjectProperty<Frequency> frequencyProperty() { return frequency; }
    public Frequency getFrequency() { return frequency.getValue(); }
    public void setFrequency(Frequency frequency) { this.frequency.set(frequency); }
    public Repeat withFrequency(Frequency frequency) { setFrequency(frequency); return this; }
    
    final private IntegerProperty interval = new SimpleIntegerProperty(1);
    public Integer getInterval() { return interval.getValue(); }
    public IntegerProperty intervalProperty() { return interval; }
    public void setInterval(Integer interval) {
        if (interval > 0)
        {
            this.interval.set(interval);
        } else {
            throw new InvalidParameterException("Repeat interval can't be less than 1. (" + interval + ")");
        }
    }
    public Repeat withInterval(Integer count) { setInterval(count); return this; }

    /** Map of Days of Week properties */
    final private Map<DayOfWeek, BooleanProperty> dayOfWeekMap = Arrays // Initialized map of all days of the week, each BooleanProperty is false
            .stream(DayOfWeek.values())
            .collect(Collectors.toMap(k -> k, v -> new SimpleBooleanProperty(false)));
    public Map<DayOfWeek, BooleanProperty> getDayOfWeekMap() { return dayOfWeekMap; }
    public void setDayOfWeek(DayOfWeek d, boolean value) { getDayOfWeekMap().get(d).set(value); }
    public boolean getDayOfWeek(DayOfWeek d) { return getDayOfWeekMap().get(d).get(); }
    public BooleanProperty getDayOfWeekProperty(DayOfWeek d) { return getDayOfWeekMap().get(d); }
    public Repeat withDayOfWeek(DayOfWeek d, boolean value) { setDayOfWeek(d, value); return this; }
    private boolean dayOfWeekMapEqual(Map<DayOfWeek, BooleanProperty> dayOfWeekMap2) {
        Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
            .stream(DayOfWeek.values())
            .limit(7)
            .iterator();
        while (dayOfWeekIterator.hasNext())
        {
            DayOfWeek key = dayOfWeekIterator.next();
            boolean b1 = getDayOfWeekMap().get(key).get();
            boolean b2 = dayOfWeekMap2.get(key).get();
            if (b1 != b2) return false;
        }
        return true;
    }
     
    final private BooleanProperty repeatDayOfMonth = new SimpleBooleanProperty(true); // default option
    private Boolean isRepeatDayOfMonth() { return repeatDayOfMonth.getValue(); }
    public BooleanProperty repeatDayOfMonthProperty() { return repeatDayOfMonth; }
    private void setRepeatDayOfMonth(Boolean repeatDayOfMonth) { this.repeatDayOfMonth.set(repeatDayOfMonth); }

    final private BooleanProperty repeatDayOfWeek = new SimpleBooleanProperty(false);
    private Boolean isRepeatDayOfWeek() { return repeatDayOfWeek.getValue(); }
    public BooleanProperty repeatDayOfWeekProperty() { return repeatDayOfWeek; }
    private void setRepeatDayOfWeek(Boolean repeatDayOfWeek) { this.repeatDayOfWeek.set(repeatDayOfWeek); }
    private int ordinal; // used when repeatDayOfWeek is true, this is the number of weeks into the month the date is set (i.e 3rd Wednesday -> ordinal=3).
    
    public MonthlyRepeat getMonthlyRepeat()
    { // returns MonthlyRepeat enum from boolean properties
        if (isRepeatDayOfMonth()) return MonthlyRepeat.DAY_OF_MONTH;
        if (isRepeatDayOfWeek()) return MonthlyRepeat.DAY_OF_WEEK;
        return null; // should not get here
    }
    public void setMonthlyRepeat(MonthlyRepeat monthlyRepeat)
    { // sets boolean properties from MonthlyRepeat
        switch (monthlyRepeat)
        {
        case DAY_OF_MONTH:
            setRepeatDayOfMonth(true);
            setRepeatDayOfWeek(false);
            break;
        case DAY_OF_WEEK:
            setRepeatDayOfMonth(false);
            setRepeatDayOfWeek(true);
            DayOfWeek dayOfWeek = getStartLocalDate().getDayOfWeek();
            LocalDateTime myDay = getStartLocalDate()
                    .with(TemporalAdjusters.firstDayOfMonth())
                    .with(TemporalAdjusters.next(dayOfWeek));
            ordinal = 0;
            if (dayOfWeek == myDay.getDayOfWeek()) ordinal++; // add one if first day of month is correct day of week
            while (! myDay.isAfter(getStartLocalDate()))
            { // set ordinal number for day-of-week repeat
                ordinal++;
                myDay = myDay.with(TemporalAdjusters.next(dayOfWeek));
            }
        }
    }
    public Repeat withMonthlyRepeat(MonthlyRepeat monthlyRepeat) { setMonthlyRepeat(monthlyRepeat); return this; }
    
    /** Start date/time of repeat rule */
    final private ObjectProperty<LocalDateTime> startLocalDate = new SimpleObjectProperty<LocalDateTime>();
    public ObjectProperty<LocalDateTime> startLocalDateProperty() { return startLocalDate; }
    public LocalDateTime getStartLocalDate() { return startLocalDate.getValue(); }
    public void setStartLocalDate(LocalDateTime startDate) { this.startLocalDate.set(startDate); }
    public Repeat withStartLocalDate(LocalDateTime startDate) { setStartLocalDate(startDate); return this; }
    
    /** Seconds duration of appointments */
    final private ObjectProperty<Integer> durationInSeconds = new SimpleObjectProperty<Integer>(this, "durationProperty");
    public ObjectProperty<Integer> durationInSecondsProperty() { return durationInSeconds; }
    public Integer getDurationInSeconds() { return durationInSeconds.getValue(); }
    public void setDurationInSeconds(Integer value) { durationInSeconds.setValue(value); }
    public Repeat withDurationInSeconds(Integer value) { setDurationInSeconds(value); return this; } 
    
    final private ObjectProperty<EndCriteria> endCriteria = new SimpleObjectProperty<EndCriteria>();
    public ObjectProperty<EndCriteria> endCriteriaProperty() { return endCriteria; }
    public EndCriteria getEndCriteria() { return endCriteria.getValue(); }
    public void setEndCriteria(EndCriteria endCriteria) { this.endCriteria.set(endCriteria); }
    public Repeat withEndCriteria(EndCriteria endCriteria){ setEndCriteria(endCriteria); return this; }
    
    final private IntegerProperty count = new SimpleIntegerProperty();
    public Integer getCount() { return count.getValue(); }
    public IntegerProperty countProperty() { return count; }
    public void setCount(Integer count)
    {
        this.count.setValue(count);
        if (count != null) makeUntilFromCount();
    }
    public Repeat withCount(Integer endAfterEvents)
    {
        if (getEndCriteria() != EndCriteria.AFTER
                || getFrequency() == null
                || getInterval() == null)
        {
            throw new InvalidParameterException
            ("Count must be set after EndCriteria is set to AFTER and intervalUnit and repeatFrequency are set");
        }
        setCount(endAfterEvents);
        return this;
    }
    /**
     * find end date from start date and number of events,  Value put into endOnDate.
     */
    public void makeUntilFromCount()
    {
        Iterator<LocalDateTime> validDateIterator = streamOfDatesEndless()
                .limit(getCount())
                .iterator();
        LocalDateTime myDate = null;
        while (validDateIterator.hasNext())
        {
            myDate = validDateIterator.next();
        }
        setUntilLocalDateTime(myDate);
    }
    /**
     * Find number of events from end date.  Value put into endAfterEvents
     */
    public void makeCountFromUntil()
    {
        if (getEndCriteria() != EndCriteria.AFTER) throw new InvalidParameterException
            ("Can't Calculate endAfterEvents with " + getEndCriteria() + " criteria");
        int eventCounter = 0;
        LocalDateTime myDate = getStartLocalDate().minusDays(1);
        System.out.println("makeEndAfterEventsFromEndOnDate " + getUntilLocalDateTime());
        while (myDate.isBefore(getUntilLocalDateTime()))
        {
            myDate = myDate.with(new NextAppointment());
            eventCounter++;
        }
        count.set(eventCounter);
    }
    
    final private ObjectProperty<LocalDateTime> untilLocalDateTime = new SimpleObjectProperty<LocalDateTime>();
    public ObjectProperty<LocalDateTime> untilLocalDateTimeProperty() { return untilLocalDateTime; }
    public LocalDateTime getUntilLocalDateTime() { return untilLocalDateTime.getValue(); }
    public void setUntilLocalDateTime(LocalDateTime dateTime) { this.untilLocalDateTime.set(dateTime); }
    public Repeat withUntilLocalDateTime(LocalDateTime dateTime)
    {
        if (getEndCriteria() != EndCriteria.UNTIL) throw new InvalidParameterException("EndCriteria must be set to ON before endOnDate is set");
        setUntilLocalDateTime(dateTime);
        return this;
    }

    // events represented by a individual appointment with some unique data
    private Set<LocalDateTime> individualRecurrences = new HashSet<LocalDateTime>();
    public Set<LocalDateTime> getIndividualRecurrences() { return exceptions; }
    public void setIndividualRecurrences(Set<LocalDateTime> dates) { exceptions = dates; }
    public Repeat withIndividualRecurrences(Set<LocalDateTime> dates) { setIndividualRecurrences(dates); return this; }
    private boolean individualRecurrencesEquals(Collection<LocalDateTime> individualRecurrencesTest)
    {
        individualRecurrencesTest.stream().forEach(a -> System.out.println("test " + a));
        Iterator<LocalDateTime> dateIterator = getExceptions().iterator();
        while (dateIterator.hasNext())
        {
            LocalDateTime myDate = dateIterator.next();
            System.out.println(myDate);
            if (! individualRecurrencesTest.contains(myDate)) return false;
        }
        return true;
    }
    
    // deleted appointments - skip these when making appointments from the repeat rule
    private Set<LocalDateTime> exceptions = new HashSet<LocalDateTime>();
    public Set<LocalDateTime> getExceptions() { return exceptions; }
    public void setExceptions(Set<LocalDateTime> dates) { exceptions = dates; }
    public Repeat withExceptions(Set<LocalDateTime> dates) { setExceptions(dates); return this; }
    private boolean exceptionsEquals(Collection<LocalDateTime> exceptionsTest)
    {
        exceptionsTest.stream().forEach(a -> System.out.println("test " + a));
        Iterator<LocalDateTime> dateIterator = getExceptions().iterator();
        while (dateIterator.hasNext())
        {
            LocalDateTime myDate = dateIterator.next();
            System.out.println(myDate);
            if (! exceptionsTest.contains(myDate)) return false;
        }
        return true;
    }
    
    /** Appointment-specific data */
    private RepeatableAppointment appointmentData = null; //AppointmentFactory.newAppointment();
    public RepeatableAppointment getAppointmentData() { return appointmentData; }
    public void setAppointmentData(RepeatableAppointment appointment) { appointmentData = appointment; }
    public Repeat withAppointmentData(RepeatableAppointment appointment) { setAppointmentData(appointment); return this; }
//    public void setAppointmentData(Appointment appointment) { appointment.copyNonDateFieldsInto(appointmentData); }

    /** Appointments generated from this repeat rule.  Objects are a subset of appointments in main appointments list
     * used in the Agenda calendar.  Names myAppointments to differentiate it from main name appointments */
    final private Set<RepeatableAppointment> myAppointments = new HashSet<RepeatableAppointment>();

//    private Set<Appointment> myAppointments;
    public Set<RepeatableAppointment> getAppointments() { return myAppointments; }
    public Repeat withAppointments(Collection<RepeatableAppointment> s) { getAppointments().addAll(s); return this; }
//    public Repeat withAppointments(Collection<Appointment> s) {myAppointments = new HashSet<Appointment>(s); return this; }
//    public boolean isNew() { return getAppointments().size() <= 1; }

//    private boolean isNew = true; // new Repeat objects start set as new
    public boolean isNew() { // return isNew(); 
//        System.out.println("getAppointmentData().getStartLocalDateTime() == null " + (getAppointmentData().getStartLocalDateTime() == null) + " " + getAppointments().size());
//  return getAppointmentData().getStartLocalDateTime() == null;
        return getAppointments().size() == 0;
    }
   
    public Repeat() { }
    
//    /**
//     * Constructor with range - used in factory (new Repeat objects need range to make appointments)
//     */
//    public Repeat(LocalDateTimeRange dateTimeRange, Callback<LocalDateTimeRange, Appointment> newAppointmentCallback)
//    {
//        this(newAppointmentCallback);
//        startDate = dateTimeRange.getStartLocalDateTime();
//        endDate = dateTimeRange.getEndLocalDateTime();
//    }
//
//
    /**
     * Copy constructor (range comes from copy)
     * 
     * @param oldRepeat
     * @param newAppointmentCallback
     */
    public Repeat(Repeat oldRepeat, Class<? extends RepeatableAppointment> appointmentClass)
    {
        this(appointmentClass);
//        setAppointmentClass(appointmentClass);
//        RepeatableAppointment appt = AppointmentFactory.newAppointment(appointmentClass);
//        setAppointmentData(appt); // initialize appointmentData
//        this(newAppointmentCallback); // setup callback to make new appointments, initialize appointmentData
        if (oldRepeat != null) {
            oldRepeat.copyInto(this);
        }
    }

//    /**
//     * Constructor only with callback, range needed too
//     */
//    protected Repeat(Callback<LocalDateTimeRange, Appointment> newAppointmentCallback)
//    {
//        this.newAppointmentCallback = newAppointmentCallback;
//        RepeatableAppointment appt = (RepeatableAppointment) getNewAppointmentCallback()
//                .call(new LocalDateTimeRange(null, null));
//        this.setAppointmentData(appt); // initialize appointmentData
//    }
    
    public Repeat(Class<? extends RepeatableAppointment> appointmentClass)
    {
        setAppointmentClass(appointmentClass);
        RepeatableAppointment appt = AppointmentFactory.newAppointment(appointmentClass);
        setAppointmentData(appt); // initialize appointmentData
    }
    // TODO - CONSIDER USING COPY CONSTRUCTOR INSTEAD
    /**
     * Copy's current object's fields into passed parameter
     * 
     * @param repeat
     * @return
     * @throws CloneNotSupportedException
     */
    public Repeat copyInto(Repeat repeat) {
        repeat.setFrequency(getFrequency());
        repeat.setRepeatDayOfMonth(isRepeatDayOfMonth());
        repeat.setRepeatDayOfWeek(isRepeatDayOfWeek());
        repeat.setInterval(getInterval());
        getDayOfWeekMap().entrySet()
                         .stream()
                         .forEach(a -> {
                             DayOfWeek d = a.getKey();
                             boolean value = a.getValue().get();
                             repeat.setDayOfWeek(d, value);   
                         });
        repeat.setExceptions(getExceptions());
        repeat.setStartLocalDate(getStartLocalDate());
        repeat.setDurationInSeconds(getDurationInSeconds());
//        repeat.setStartLocalTime(getStartLocalTime());
//        repeat.setEndLocalTime(getEndLocalTime());
        if (getEndCriteria() == EndCriteria.AFTER) repeat.setCount(getCount());
        repeat.setEndCriteria(getEndCriteria());
        repeat.setUntilLocalDateTime(getUntilLocalDateTime());
//        System.out.println("this.getAppointmentData() " + this.getAppointmentData());
        getAppointmentData().copyNonDateFieldsInto(repeat.getAppointmentData());
        getAppointments().stream().forEach(a -> repeat.getAppointments().add(a));
        repeat.startDate = startDate;
        repeat.endDate = endDate;
        return repeat;
    }
    
//    Appointment copyInto(Appointment a) {
//        getAppointmentData().copyNonDateFieldsInto(a);
//        LocalDate myDate = a.getStartLocalDateTime().toLocalDate();
//        a.setStartLocalDateTime(myDate.atTime(this.getStartLocalTime()));
//        a.setEndLocalDateTime(myDate.atTime(this.getEndLocalTime()));
//        return a;
//    }
    
    @Override
    public boolean equals(Object obj) {
//        System.out.println("repeat equals " + (obj == this) + " " + (obj == null));
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        Repeat testObj = (Repeat) obj;

      Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
          .stream(DayOfWeek.values())
          .limit(7)
          .iterator();
      while (dayOfWeekIterator.hasNext())
      {
          DayOfWeek key = dayOfWeekIterator.next();
          boolean b1 = getDayOfWeekMap().get(key).get();
          boolean b2 = testObj.getDayOfWeekMap().get(key).get();
          System.out.println("day of week " + key + " " + b1 + " " + b2);
      }

//        System.out.println(" getEndAfterEvents " + getCount() + " " + testObj.getCount());
        System.out.println(this.getStartLocalDate() + " " + testObj.getStartLocalDate());
//        System.out.println(getDurationInSeconds()+ " " + (testObj.getDurationInSeconds()));
        System.out.println("repeat " + getStartLocalDate().equals(testObj.getStartLocalDate())
            + " " + getDurationInSeconds().equals(testObj.getDurationInSeconds())
            + " " + getCount().equals(testObj.getCount())
            + " " + (getEndCriteria() == testObj.getEndCriteria())
            + " " + isRepeatDayOfMonth().equals(testObj.isRepeatDayOfMonth())
            + " " + isRepeatDayOfWeek().equals(testObj.isRepeatDayOfWeek())
            + " " + getInterval().equals(testObj.getInterval())
            + " " + getAppointmentData().equals(testObj.getAppointmentData())
            + " " + dayOfWeekMapEqual(testObj.getDayOfWeekMap())
            + " " + exceptionsEquals(testObj.getExceptions()));

        return getStartLocalDate().equals(testObj.getStartLocalDate())
            && getDurationInSeconds().equals(testObj.getDurationInSeconds())
            && getCount().equals(testObj.getCount())
            && getEndCriteria() == testObj.getEndCriteria()
            && getFrequency() == testObj.getFrequency()
            && isRepeatDayOfMonth().equals(testObj.isRepeatDayOfMonth()) 
            && isRepeatDayOfWeek().equals(testObj.isRepeatDayOfWeek())
            && getInterval().equals(testObj.getInterval())
            && getAppointmentData().equals(testObj.getAppointmentData())
            && dayOfWeekMapEqual(testObj.getDayOfWeekMap())
            && exceptionsEquals(testObj.getExceptions());
    }
    
    /**
     * Determines if repeat rules make sense (true) or can't define a series (false)
     * Need to generate string of repeat rule
     * TODO - add more features
     * 
     * @return
     */
    public boolean isValid()
    {
        if (getFrequency() == Frequency.WEEKLY) {
            if (! isWeeklyValid()) return false;
        } else if (getFrequency() == Frequency.MONTHLY) {
            if (! isMonthlyValid()) return false;
        }
        return true;
    }
    
    
    /**
     * Default settings for a new Repeat rule, set after repeatable checkBox is checked.
     * 
     */
    public Repeat setDefaults() {
        setFrequency(Frequency.WEEKLY);
        setInterval(1);
        setEndCriteria(EndCriteria.NEVER);
        return this;
    }

    /**
     * Creates a string describing the repeat rule.  The string gets displayed in the summary field
     * of the Repeat control
     * 
     * @return
     */
    public String makeSummary() {
        return null;
    }
    
    /**
     * Types of time intervals allowed for repeat rules
     * 
     * @author David Bal
     *
     */
    public enum Frequency
    {
        DAILY(Period.ofDays(1)) // value is adjustment subtracted from start date to allow NextAppointment to find start date
      , WEEKLY(Period.ofDays(1)) // keep as Period.ofDays(1)
      , MONTHLY(Period.ofMonths(1))
      , YEARLY(Period.ofYears(1));
        
        private final Period value;
        
        private Frequency(Period value) {
            this.value = value;
        }
        
        public Period getValue() {
            return value;
        }
        
        public String toStringSingular() {
            switch (this) {
            case DAILY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(DAILY);
            case WEEKLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(WEEKLY);
            case MONTHLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(MONTHLY);
            case YEARLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(YEARLY);
            default:
                return null;                
            }
        }
        
        public String toStringPlural() {
            switch (this) {
            case DAILY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(DAILY);
            case WEEKLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(WEEKLY);
            case MONTHLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(MONTHLY);
            case YEARLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(YEARLY);
            default:
                return null;                
            }
        }
        
        public static StringConverter<Repeat.Frequency> stringConverter
            = new StringConverter<Repeat.Frequency>() {
            @Override public String toString(Repeat.Frequency object) {
                switch (object) {
                case DAILY:
                    return Settings.REPEAT_FREQUENCIES.get(Frequency.DAILY);
                case WEEKLY:
                    return Settings.REPEAT_FREQUENCIES.get(Frequency.WEEKLY);
                case MONTHLY:
                    return Settings.REPEAT_FREQUENCIES.get(Frequency.MONTHLY);
                case YEARLY:
                    return Settings.REPEAT_FREQUENCIES.get(Frequency.YEARLY);
                default:
                    return null;                
                }
            }
            @Override public Repeat.Frequency fromString(String string) {
                throw new RuntimeException("not required for non editable ComboBox");
            }
        };
    }
    
    /**
     * Checks if the WEEKLY IntervalUnit is valid (has at least one day selected)
     * 
     * @return
     */
    public boolean isWeeklyValid()
    {
        boolean weekly = (getFrequency() == Frequency.WEEKLY);
        boolean anyDay = getDayOfWeekMap().entrySet()
                                               .stream()
                                               .anyMatch(a -> a.getValue().get() == true);
        return weekly && anyDay;
    }
    /**
     * Checks if the MONTHLY IntervalUnit is valid (has one of the options for selecting next
     * month's day selected)
     * 
     * @return
     */
    public boolean isMonthlyValid()
    {
        boolean monthly = (getFrequency() == Frequency.MONTHLY);
        boolean dayOptionSelected = (isRepeatDayOfMonth() || isRepeatDayOfWeek());
        return monthly && dayOptionSelected;
    }
    
    public enum EndCriteria {
        NEVER
      , AFTER
      , UNTIL;
    }
    
    public enum MonthlyRepeat {
        DAY_OF_MONTH, DAY_OF_WEEK;
    }



    /**
     * Adds appointments as members of this repeat rule to myAppointments collection
     * All appointments must be RepeatableAppointments or method will throw an exception.
     * 
     * @param appointments
     */
    public void collectAppointments(
            Collection<? extends RepeatableAppointment> appointments)
    {
        Set<RepeatableAppointment> s = appointments.stream()
                                         .map(a -> (RepeatableAppointment) a)
//                                         .filter(a -> repeatMap.get(a) != null)
//                                         .filter(a -> repeatMap.get(a).equals(this))
                                         .filter(a -> a.getRepeat() != null)
                                         .filter(a -> a.getRepeat().equals(this))
                                         .collect(Collectors.toSet());
        getAppointments().addAll(s);
    }
    
    /**
     * Make appointments that should exist between startDate and endDate based on Repeat rules.
     * Adds those appointments to the input parameter appointments Collection.
     * Doesn't make Appointment for dates that are already represented as individual appointments
     * as specified in usedDates.
     * Uses startDate and endDate set from previous makeAppointments or updateAppointments calls
     * 
     * @param appointments
     * @return
     */
    public Collection<RepeatableAppointment> makeAppointments()
    {
        return makeAppointments(startDate, endDate); // use current startDate and endDate
    }
    /**
     * Make appointments that should exist between startDate and endDate based on Repeat rules.
     * Adds those appointments to the input parameter appointments Collection.
     * Doesn't make Appointment for dates that are already represented as individual appointments
     * as specified in usedDates.
     * sets startDate and endDate to private fields
     * 
     * @param appointments
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public Collection<RepeatableAppointment> makeAppointments(
            LocalDateTime startDateTime
          , LocalDateTime endDateTime)
    {
        Set<RepeatableAppointment> appointments = new HashSet<RepeatableAppointment>();
        this.endDate = endDateTime;
        this.startDate = startDateTime;

        final LocalDateTime myEndDate;
        if (getUntilLocalDateTime() == null) {
            myEndDate = endDateTime;
        } else {
//            System.out.println(endDate + " " + getEndOnDate());
//            LocalDateTime endOnDateTime = getEndOnDate().plusDays(1).atStartOfDay().minusNanos(1);
            myEndDate = (endDateTime.isBefore(getUntilLocalDateTime())) ? endDateTime : getUntilLocalDateTime();
        }
        LocalDateTime myStartDate = nextValidDateSlow(startDateTime.minusNanos(1));
        System.out.println("myStartDate " + myStartDate + " " + myEndDate);
//        System.out.println("StartDate " + startDate + " " + endDate + " " + this.getStartLocalDate() + " " +  getEndOnDate() );

        if (! myStartDate.isAfter(myEndDate))
        { // create set of appointment dates already used, to be skipped in making more
//            System.out.println("make appointments");
            // TODO - going to change from searching appointments to adding exceptions and individual recurrences
            final Set<LocalDateTime> usedDates = getAppointments()
                    .stream()
                    .map(a -> a.getStartLocalDateTime())
                    .peek(a -> System.out.println("used " + a))
                    .collect(Collectors.toSet());
            
            final Iterator<RepeatableAppointment> i = Stream                              // appointment iterator
                    .iterate(myStartDate, (a) -> a.with(new NextAppointment())) // infinite stream of valid dates
                    .filter(a -> ! usedDates.contains(a))                       // filter out dates already used
                    .filter(a -> ! getExceptions().contains(a))               // filter out deleted dates
                    .map(myStartDateTime -> {                                                 // make new appointment
//                        LocalDateTime myStartDateTime = a;
                        LocalDateTime myEndDateTime = myStartDateTime.plusSeconds(getDurationInSeconds());
                        RepeatableAppointment appt = AppointmentFactory.newAppointment(appointmentClass);
//                        RepeatableAppointment appt = (RepeatableAppointment) getNewAppointmentCallback()
//                            .call(new LocalDateTimeRange(myStartDateTime, myEndDateTime));
                        appt.setStartLocalDateTime(myStartDateTime);
                        appt.setEndLocalDateTime(myEndDateTime);
                        appt.setRepeat(this);
                        appt.setRepeatMade(true);
                        appt.setAppointmentGroup(getAppointmentData().getAppointmentGroup());
                        appt.setDescription(getAppointmentData().getDescription());
                        appt.setSummary(getAppointmentData().getSummary());

                            //                        RepeatableAppointment appt = AppointmentFactory
//                                .newAppointment()
//                                .withStartLocalDateTime(myStartDateTime)
//                                .withEndLocalDateTime(myEndDateTime)
//                                .withRepeat(this)
//                                .withRepeatMade(true)
//                                .withAppointmentGroup(getAppointmentData().getAppointmentGroup())
//                                .withDescription(getAppointmentData().getDescription())
//                                .withSummary(getAppointmentData().getSummary());
//                        repeatMap.put(appt, this);
                        return appt;
                    })
                    .iterator();                                                // make iterator
            
            while (i.hasNext())
            { // Process new appointments
                final RepeatableAppointment a = i.next();
//                System.out.println("a --- " + a + " " + a.getStartLocalDateTime());
//                System.out.println("times " + a.getStartLocalDateTime().toLocalDate() + " " + (myEndDate));
                if (a.getStartLocalDateTime().isAfter(myEndDate)) break; // exit loop when at end
//                System.out.println("add " + a.getStartLocalDateTime());
//                repeatMap.add(a, this);                                                // add appointment and repeat to repeatMap
                appointments.add(a);                                                   // add appointments to main collection
                getAppointments().add(a);                                              // add appointments to this repeat's collection
//                repeatMap.put(a, this);
            }
//            isNew = false; // when makeAppointments is run first time set isNew to false
        }
        
        return appointments;
    }
    
    /**
     * Removes appointments that were made by this repeat rule and are now outside the startDate and endDate
     * values (startDate and endDate are private and set by calls to makeAppointments).  Removes appointments
     * from both the input parameter appointments and this repeat object's appointments collection as well.
     * Sets private fields startDate and endDate with parameters.
     * 
     * @param appointments
     * @param startDate
     * @param endDate
     * @return
     */
    public Repeat removeOutsideRangeAppointments(Collection<RepeatableAppointment> appointments
            , LocalDateTime startDate
            , LocalDateTime endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        return removeOutsideRangeAppointments(appointments);
    }
    /**
     * Used for removing repeat-generated appointments that are no longer in range for the display of Agenda.
     * Removes appointments that were made by this repeat rule and are now outside the startDate and endDate
     * values (startDate and endDate are private and set by calls to makeAppointments).  Removes appointments
     * from both the input parameter appointments and this repeat object's appointments collection as well.
     * 
     * Uses private fields startDate and endDate for date ranges.  Must be previously set from a makeAppointments,
     * updateAppointments, or removeOutsideRangeAppointments call with those parameters present.
     * 
     * @param appointments
     * @return
     */
    public Repeat removeOutsideRangeAppointments(Collection<RepeatableAppointment> appointments)
    {
        Iterator<RepeatableAppointment> i = getAppointments().stream()
            .filter(a -> {
                boolean tooEarly = a.getStartLocalDateTime().isBefore(startDate);
                boolean tooLate = a.getStartLocalDateTime().isAfter(endDate);
                return tooEarly || tooLate;
            })
            .collect(Collectors.toList())
            .iterator();
        
        while (i.hasNext())
        {
            final RepeatableAppointment a = i.next();
            RepeatableUtilities.removeOne(appointments, a);
            RepeatableUtilities.removeOne(getAppointments(), a);
        }
        return this;
    }

    
//    /**
//     * Copies appointment data from this objects appointmentData field into the appointmentData
//     * argument unless the data in appointmentData is unique
//     * 
//     * @param appointmentData
//     * @param appointmentOld
//     * @return
//     */
//    public Appointment copyAppointmentInto(Appointment appointmentData, Appointment appointmentOld) {
//    if (appointmentData.getAppointmentGroup().equals(appointmentOld.getAppointmentGroup())) {
//        appointmentData.setAppointmentGroup(getAppointmentData().getAppointmentGroup());            
//    }
//    if (appointmentData.getDescription().equals(appointmentOld.getDescription())) {
//        appointmentData.setDescription(getAppointmentData().getDescription());            
//    }
////  if (appointmentData.getLocationKey().equals(appointmentOld.getLocationKey())) {
////      appointmentData.setLocationKey(getLocationKey());
////  }
////  if (appointmentData.getStaffKeys().equals(appointmentOld.getStaffKeys())) {
////      appointmentData.getStaffKeys().addAll(getStaffKeys());
////  }
////  if (appointmentData.getStyleKey().equals(appointmentOld.getStyleKey())) {
////      appointmentData.setStyleKey(getStyleKey());
////  }
//    if (appointmentData.getSummary().equals(appointmentOld.getSummary())) {
//        appointmentData.setSummary(getAppointmentData().getSummary());
//    }
//    return appointmentData;
//}
    
    /**
     * Modifies old dates and times by a start and end TemporalAdjuster in an attempt to convert invalid
     * dates/times to valid ones.
     * Updates repeat-rule appointments with new repeat rule from startDate on.
     * Deletes repeat-rule generated appointments that don't meet the current repeat rule.
     * Changes the attached Repeat for non-repeat generated appointments that are now invalid to null
     * (prevents them from being deleted)
     * Adds new repeat-rule appointments as needed
     * @param appointments 
     * @param appointmentOld 
     * 
     * @param appointment: already modified appointment
     * @param startTemporalAdjuster: adjusts startLocalDateTime
     * @param endTemporalAdjuster: adjusts endLocalDateTime
     * @return
     */
    protected void updateAppointments(Collection<Appointment> appointments
            , RepeatableAppointment appointment
            , RepeatableAppointment appointmentOld
            , TemporalAdjuster startTemporalAdjuster
            , TemporalAdjuster endTemporalAdjuster)
    {
        // Modify old date time to new, so I can keep as many modified appointments as possible
        getAppointments()
                .stream()
                .filter(a -> a != appointment) // filter already changed appointment
                .sequential()
                .forEach(a -> {                       // adjust date and time
                    LocalDateTime newStart = a.getStartLocalDateTime().with(startTemporalAdjuster);
                    LocalDateTime newEnd = a.getEndLocalDateTime().with(endTemporalAdjuster);
                    a.setStartLocalDateTime(newStart);
                    a.setEndLocalDateTime(newEnd);
                    if (a.isRepeatMade())
                    { // copy all changed data
                        getAppointmentData().copyNonDateFieldsInto(a);
                    } else { // copy only non-unique data
                        getAppointmentData().copyNonDateFieldsInto(a, appointmentOld);
                    }
                });
        updateAppointments(appointments, appointment);
    }
    
    // TODO - REMOVE UPDATE APPOINTMENTS
    /**
     * Used when editing a repeating appointment.  Updates repeat-rule appointments with new repeat rule from
     * startDate on.  Deletes repeat-rule generated appointments that don't meet the current repeat rule.
     * Changes the attached Repeat for non-repeat generated appointments that are now invalid to null (prevents them
     * from being deleted)
     * Adds new repeat-rule appointments as needed

     * @param appointments 
     * @param appointment: already modified appointment
     * @return
     */
    @Deprecated
    public void updateAppointments(Collection<Appointment> appointments
            , RepeatableAppointment appointment)
    {
        // Identify invalid repeat appointments
        final LocalDateTime firstDateTime = getStartLocalDate();
        final Iterator<LocalDateTime> validDateTimeIterator = Stream                      // iterator
                .iterate(firstDateTime, (d) -> { return d.with(new NextAppointment()); }) // generate infinite stream of valid dates
                .iterator();                                                              // make iterator
        final Iterator<RepeatableAppointment> appointmentIterator = getAppointments()
                .stream() // appointments sorted by date
                .sorted(Comparator.comparing(a -> a.getStartLocalDateTime()))
                .iterator();
        Set<Appointment> invalidAppointments = new HashSet<Appointment>();
        LocalDateTime validDateTime = validDateTimeIterator.next();
        while (appointmentIterator.hasNext())
        {
            Appointment myAppointment = appointmentIterator.next();
            LocalDateTime appointmentDateTime = myAppointment.getStartLocalDateTime();
            while (validDateTime.isBefore(appointmentDateTime))
            { // advance valid dates to get to myDateTime
                validDateTime = validDateTimeIterator.next();
//                System.out.println("getEndCriteria " + getEndCriteria() + " " + validDateTime + " " + getEndOnDate() + " " + validDateTime.isAfter(endDate.atTime(getStartLocalTime())) + " " + // after displayed date interval
//                        validDateTime.toLocalDate().isAfter(getEndOnDate()));
                if (getEndCriteria() != EndCriteria.NEVER)
                {
//                    if (validDateTime.isAfter(endDate.atTime(getStartLocalTime())) || // after displayed date interval
                    if (validDateTime.isAfter(endDate) || // after displayed date interval
                            validDateTime.isAfter(getUntilLocalDateTime())) // after end of repeat rule
                    { // appointment is invalid - too late
                        invalidAppointments.add(myAppointment);
                        break;
                    }
                }
            }
            if (! validDateTime.equals(appointmentDateTime))
            { // appointment is invalid - start time doesn't match
                invalidAppointments.add(myAppointment);
            }
        }
        System.out.println("invalidAppointments " + invalidAppointments.size() + " " + appointments.size());
        // TODO - EVALUATE THE EXISTANCE OF THE BELOW VARIABLES
        // Change unique appointment to individual
        boolean writeAppointmentsNeeded = getAppointments()
                .stream()
                .filter(a -> ! isValidAppointment(a.getStartLocalDateTime())) // invalid date time
                .filter(a -> ! a.isRepeatMade())                              // is not repeat made
                .peek(a -> a.setRepeat(null))                                 // reset Repeat to null (make moved appointments individual when date is now invalid due to repeat change)
//                .peek(a -> repeatMap.remove(a))                                 // reset Repeat to null (make moved appointments individual when date is now invalid due to repeat change)
                .anyMatch(a -> true);                                         // if any appointments past filters set flag to write appointments

        // Check for any appointments that have the Repeat, but are not repeat made.
        boolean writeAppointmentsNeeded2 = getAppointments()
                .stream()
                .anyMatch(a -> (a.getRepeat() != null) && (! a.isRepeatMade()));
//                .anyMatch(a -> (repeatMap.get(a) != null) && (! a.isRepeatMade()));
        
        for (Appointment a : invalidAppointments)
        {
//            if (a.getStudentKeys().isEmpty())
//            { // DELETE EXISTING INVALID APPOINTMENT
            System.out.println("delete " + a.getStartLocalDateTime());
                appointments.remove(a);
                getAppointments().remove(a);
//            } else { // LEAVE EXISTING APPOINTMENT BECAUSE HAS ATTENDANCE
//                getAppointmentData().copyInto(a);
//            }
        }
        appointments.addAll(makeAppointments()); // add any new appointments needed
        System.out.println("make new appointments " + appointments.size());
        
//        if (writeAppointmentsNeeded || writeAppointmentsNeeded2) AppointmentFactory.writeToFile(appointments); //  DO THIS ELSEWHERE WHEN I HAVE THE ACTUAL REPEATABLEAPPOINTMENT COLLECTION
    }

    /**
     * Checks if repeat contains only one appointment and converts the appointment to an individual appointment.
     * 
     * @param repeats
     * @param appointments 
     * @return
     */
    public boolean oneAppointmentToIndividual(Collection<Repeat> repeats
            , Collection<Appointment> appointments)
    {
        if (getEndCriteria() != EndCriteria.NEVER)
        { // Count number of valid appointment start dates, stop when after end date or more than one appointment date
            final Iterator<LocalDateTime> i = streamOfDatesEndless().iterator();
//            final Iterator<LocalDateTime> i = Stream                                            // appointment iterator
//                    .iterate(getStartLocalDate(), (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
//                    .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
//                    .iterator();                                                            // make iterator
            int appointmentCounter = 0;
            while (i.hasNext())
            { // find date
                final LocalDateTime s = i.next();
//                System.out.println("getEndOnDate() " + getEndOnDate() + " " + getEndCriteria());
                if (s.isAfter(getUntilLocalDateTime())) break; // exit loop when beyond date without match
                if (appointmentCounter > 1) break;
                appointmentCounter++;
            }
            
            if (appointmentCounter == 1)
            {
                RepeatableUtilities.removeOne(repeats, this);
                if (getAppointments().size() == 1)
                {
                    RepeatableAppointment myAppointment = getAppointments().iterator().next();
                    myAppointment.setRepeatMade(false);
                    myAppointment.setRepeat(null);
//                    repeatMap.remove(myAppointment);
                    getAppointments().clear();
                } else if (getAppointments().size() == 0)
                { // make individual appointment because it is not in current date range
                    appointments.addAll(makeAppointments(getStartLocalDate(), getUntilLocalDateTime()));
                    getAppointments().iterator().next().setRepeatMade(false);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns a stream of valid start dates.  Ends when endOnDate is exceeded
     * I wonder if it could be done more efficiently without the iterator.
     * 
     * @return
     */
    public Stream<LocalDateTime> streamOfDates()
    {
        List<LocalDateTime> startDateList = new ArrayList<LocalDateTime>();
        final Iterator<LocalDateTime> i = Stream                                            // appointment iterator
                .iterate(getStartLocalDate(), (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                .filter(a -> ! getExceptions().contains(a))                             // filter out deleted dates
                .iterator();                                                            // make iterator
        while (i.hasNext())
        { // find date
            final LocalDateTime s = i.next();
            if (s.isAfter(getUntilLocalDateTime())) break; // exit loop when beyond date without match
            startDateList.add(s);
        }
        return startDateList.stream();
    }

    /**
     * Returns a stream of valid start dates.
     * 
     * @return
     */
    public Stream<LocalDateTime> streamOfDatesEndless()
    {
        return Stream
            .iterate(getStartLocalDate(), (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
            .filter(a -> ! getExceptions().contains(a));                             // filter out deleted dates
    }
    
    /**
     * Checks appointment LocalDateTime to see if it follows the repeat rules.
     * true = valid date, false = invalid
     * 
     * @param initialDateTime
     * @return
     */
    private boolean isValidAppointment(LocalDateTime startDateTime)
    {
        final Iterator<LocalDateTime> i = streamOfDatesEndless().iterator();
//        LocalDateTime firstMatchDateTime = getStartLocalDate();
//            final Iterator<LocalDateTime> i = Stream                                            // appointment iterator
//                    .iterate(firstMatchDateTime, (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
//    //                .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
//                    .iterator();                                                            // make iterator
    
            while (i.hasNext())
            { // Check date
                final LocalDateTime s = i.next();
//                final LocalDateTime e = i.next().atTime(getEndLocalTime());
                if (s.isAfter(startDateTime)) return false; // exit loop when beyond date without match
//                if (s.equals(startDateTime) && ((endDateTime == null) || e.equals(endDateTime))) return true;
            }
        return false;
    }
//    private boolean isValidAppointment(LocalDateTime initialDateTime)
//    {
//        return isValidAppointment(initialDateTime, null);
//    }
//    private boolean isValidAppointment(LocalDate initialDate)
//    {
//        return isValidAppointment(initialDate.atTime(getStartLocalTime()), null);
//    }
    private boolean isValidAppointment(Appointment myAppointment)
    {
        boolean tooLate = (getEndCriteria() == EndCriteria.NEVER) ? false :
            myAppointment.getStartLocalDateTime().isAfter(getUntilLocalDateTime());
        if (tooLate) {
            return false;
        } else {
            return isValidAppointment(myAppointment.getStartLocalDateTime());
        }
    }
    
    public LocalDateTime nextValidDate(LocalDateTime inputDate)
    {
        return nextValidDateSlow(inputDate); // TODO - replace with nextValidDateFast when finished
    }

    /**
     * Faster version without iterating
     * DOES NOT WORK PROPERLY - MONTHLY DAY OF WEEK IS BROKEN (use slow one for now)
     * 
     * @param inputDate
     * @return
     */
    @Deprecated
    public LocalDateTime nextValidDateFast(LocalDateTime inputDate)
    {
        LocalDateTime firstMatchDate = null;
        firstMatchDate = inputDate;//.minusDays(getIntervalUnit().getValue() * this.getRepeatFrequency());
        switch (getFrequency())
        {
            case DAILY:
                break;
            case WEEKLY:
                firstMatchDate = inputDate.minusDays(7 * this.getInterval());
                break;
            case MONTHLY:
                long mod = 0;
                long totalMonths = 0;
            switch (this.getMonthlyRepeat())
                {
                case DAY_OF_MONTH:
                    totalMonths = (inputDate.getMonthValue() - getStartLocalDate().getMonthValue())
                        + (inputDate.getYear() - getStartLocalDate().getYear()) * 12;
                    mod = (totalMonths % getInterval());
                    if (mod == 0 && inputDate.getDayOfMonth() <= getStartLocalDate().getDayOfMonth()) mod = getInterval(); // adjust mod to repeatFrequency if date is before match for current month
                    int dayOfMonth = getStartLocalDate().getDayOfMonth();
                    firstMatchDate = firstMatchDate
                            .minusMonths(mod)
                            .withDayOfMonth(dayOfMonth)
                            .with(new NextAppointment());
                    break;
                case DAY_OF_WEEK: // TODO - PAIN IN THE ASS
                    // get date of ordinal match for current month
//                    totalMonths = (inputDate.getMonthValue() - getStartLocalDate().getMonthValue())
//                    + (inputDate.getYear() - getStartLocalDate().getYear()) * 12;
//                    totalMonths = ChronoUnit.MONTHS.between
//                        (getStartLocalDate().with(TemporalAdjusters.firstDayOfMonth())
//                        , inputDate.with(TemporalAdjusters.firstDayOfMonth()));
//                    totalMonths = ChronoUnit.MONTHS.between
//                            (getStartLocalDate()
//                            , inputDate);
                    totalMonths = ChronoUnit.MONTHS.between
                    (getStartLocalDate() //.withDayOfMonth(d)
                    , inputDate);
                    
                    mod = (totalMonths % getInterval());

                    LocalDateTime myDate = inputDate
                            .minusMonths(mod)
                            .with(TemporalAdjusters.firstDayOfMonth());
                    final DayOfWeek dayOfWeek = getStartLocalDate().getDayOfWeek();
                    int o = (myDate.getDayOfWeek() == dayOfWeek) ? 1 : 0;
                    for (; o < ordinal; o++) {
                        myDate = myDate.with(TemporalAdjusters.next(dayOfWeek));
                    }
                    int d = myDate.getDayOfMonth();
                    boolean pastOrdinal = (inputDate.getDayOfMonth() >= d);


                    totalMonths = ChronoUnit.MONTHS.between
                            (getStartLocalDate() //.withDayOfMonth(d)
                            , inputDate);

                    mod = (totalMonths % getInterval());

                    if (pastOrdinal) {
//                        totalMonths++;
                        mod++;
                        firstMatchDate = firstMatchDate
                                .minusMonths(mod)
//                                .with(TemporalAdjusters.firstDayOfMonth())
                                .with(new NextAppointment());
                    } else {
                        firstMatchDate = firstMatchDate
                                .minusMonths(mod)
                                .with(new NextAppointment());
                    }
                    
                    
//                    firstMatchDate = firstMatchDate
//                        .minusMonths(mod)
////                        .with(TemporalAdjusters.firstDayOfMonth())
//                        .with(new NextAppointment());
                    
                    System.out.println(inputDate + " totalMonths " + totalMonths + " mod " + mod +
                            " firstMatchDate " + firstMatchDate + " actual " + nextValidDateSlow(inputDate)
                            + " " + pastOrdinal + " " + myDate);
//                    firstMatchDate = firstMatchDate
                    break;
                default:
                    break;
                }
//            System.out.println(inputDate + " mod " + mod + " " + firstMatchDate.minusMonths(mod) + " totalMonths " + totalMonths + " " );
//            System.out.println("getStartLocalDate(), inputDate  " + getStartLocalDate() + " " + inputDate);
//            System.out.println("getStartLocalDate(), inputDateA " + getStartLocalDate().with(TemporalAdjusters.firstDayOfMonth()) + " " + inputDate.with(TemporalAdjusters.firstDayOfMonth()));

                break;
            case YEARLY:
                break;
            default:
                throw new InvalidParameterException("Unknown intervalUnit " + getFrequency());
        }
//        firstMatchDate = getStartLocalDate();
        final Iterator<LocalDateTime> i = Stream                                            // appointment iterator
                .iterate(firstMatchDate, (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                .filter(a -> ! getExceptions().contains(a))                             // filter out deleted dates
//                .limit(3)
                .iterator();                                                            // make iterator

        while (i.hasNext())
        { // find date
            final LocalDateTime s = i.next();
//            System.out.println(s + " " + inputDate + " " + i.next());
            if (! s.isBefore(inputDate)) return s; // exit loop when beyond date without match
//            break;
        }
        return null; // should never get here
    }

    /**
     * Returns next valid date time starting with inputed date.  If inputed date is valid it is returned.
     * Iterates from first date until it passes the inputDate.  This make take a long time if the date
     * is far in the future.
     * 
     * @param inputDate
     * @return
     */
    public LocalDateTime nextValidDateSlow(LocalDateTime inputDate)
    {
        if (inputDate.isBefore(getStartLocalDate())) return getStartLocalDate();
        LocalDateTime firstMatchDate = getStartLocalDate();
        final Iterator<LocalDateTime> i = Stream                                            // appointment iterator
                .iterate(firstMatchDate, (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                .filter(a -> ! getExceptions().contains(a))                             // filter out deleted dates
                .iterator();                                                            // make iterator
        while (i.hasNext())
        { // find date
            LocalDateTime s = i.next();
            if (s.isAfter(inputDate)) return s; // exit loop when beyond date without match
        }
        throw new InvalidParameterException("Can't find valid date starting at " + inputDate);
    }
    
    /**
     * Returns previous valid date starting from inputDate and going backward.
     * 
     * @param startDate2
     * @return
     */
    public LocalDateTime previousValidDate(LocalDateTime inputDate)
    {
        LocalDateTime inputDateAdjusted = inputDate.minusDays(1);
        for (int i=0; i<this.getInterval(); i++)
        {
            inputDateAdjusted = inputDateAdjusted.minus(getFrequency().getValue());            
        }
//        System.out.println("inputDateAdjusted " + inputDateAdjusted);
        return nextValidDate(inputDateAdjusted);
    }

    /**
     * Removes the deleted dates and appointments outside the start and end dates
     * @param appointments 
     */
    public void fixCollectionDates(Collection<Appointment> appointments, Map<Appointment, Repeat> repeatMap)
    {
        // keep only deletedDates within start and end dates
        final Set<LocalDateTime> dates = getExceptions()
                .stream()
                .filter(a -> ! a.isAfter(getUntilLocalDateTime()))        // keep dates before and equal to endOnDate
                .filter(a -> ! a.isBefore(getStartLocalDate()))  // keep dates after and equal to startLocalDate
                .filter(a -> isValidAppointment(a))      // keep valid dates
                .collect(Collectors.toSet());                    // make new set
        setExceptions(dates);                                  // keep new set
          
          // keep only appointments within start and end dates
          Iterator<RepeatableAppointment> i = getAppointments().iterator();
          while (i.hasNext())
          {
              Appointment a = i.next();
              LocalDateTime s = a.getStartLocalDateTime();
              boolean tooEarly = s.isBefore(getStartLocalDate());
              boolean tooLate = (getEndCriteria() == EndCriteria.NEVER) ? false : s.isAfter(getUntilLocalDateTime());
              boolean notValid = ! isValidAppointment(a.getStartLocalDateTime());
              if (tooEarly || tooLate || notValid) i.remove();
          }
  
          getAppointments().stream()
                  .forEach(a -> repeatMap.put(a, this));
//                           .forEach(a -> a.setRepeat(this));
    }

//    /**
//     * Adjust start date, time and end time due to editing repeatable appointment
//     * 
//     * @param startTemporalAdjuster
//     * @param endTemporalAdjuster
//     */
//    public void adjustDateTime(TemporalAdjuster startTemporalAdjuster, TemporalAdjuster endTemporalAdjuster)
//    {
//        adjustDateTime(getStartLocalDate(), startTemporalAdjuster, endTemporalAdjuster);
//    }
    
    /**
     * Adjust start date time and end time due to editing repeatable appointment
     * 
     * @param adjustStartDate: when true shift startDate by startTemporalAdjuster
     * @param startTemporalAdjuster
     * @param endTemporalAdjuster
     */
    public void adjustDateTime(Boolean adjustStartDate, TemporalAdjuster startTemporalAdjuster, TemporalAdjuster endTemporalAdjuster)
    {
        // time adjustments
        System.out.println("time adjustments");
        final LocalDateTime newStartLocalDateTime = getStartLocalDate()
                .with(startTemporalAdjuster);

        //                .atTime(getStartLocalTime())
//        final LocalTime newEndLocalTime = getStartLocalDate()
////                .atTime(getEndLocalTime())
//                .with(endTemporalAdjuster)
//                .toLocalTime();
        final LocalDateTime oldEndLocalDateTime = getStartLocalDate().plusSeconds(getDurationInSeconds());
        final LocalDateTime newEndLocalDateTime = oldEndLocalDateTime.with(endTemporalAdjuster);
        int newDuration = (int) ChronoUnit.SECONDS.between(newStartLocalDateTime, newEndLocalDateTime);
//        System.out.println("start " + getStartLocalDate() + " old " + oldEndLocalDateTime + " new " + newEndLocalDateTime);
//        System.out.println("Duration " + this.getDurationInSeconds() + " " + newDuration);
//        System.exit(0);
        setDurationInSeconds(newDuration);
        
        
//        final LocalDateTime newEndOnLocalDateTime = this.getEndOnDate().with(startTemporalAdjuster);
//        System.out.println("adjust endOnDate " + getEndOnDate() + " " + newEndOnLocalDateTime);
//        this.setEndOnDate(newEndOnLocalDateTime);

        
//        setStartLocalDate(newStartLocalDateTime);

        //        final LocalDateTime newEndLocalDateTime = getStartLocalDate().plusSeconds(getDuration());
//        setEndLocalTime(newEndLocalTime);

        // TODO - ALLOW ADJUSTING END DATE COMPLETELY - ADD ADJUSTER
//        if (getEndCriteria() != EndCriteria.NEVER)
//        {
//            LocalTime newStartTime = newStartLocalDateTime.toLocalTime();
//            LocalDateTime newEndOnDateTime = getLastStartDateTime().toLocalDate().atTime(newStartTime);
//            System.out.println("adjustment endTime " + newStartTime + " " + newEndOnDateTime);;
//            setLastStartDateTime(newEndOnDateTime);
//        }
        
        if (adjustStartDate)
        {
//            if (getIntervalUnit() == IntervalUnit.WEEKLY)
//            { // if new start has shifted then move it
//                final LocalDateTime earliestDate = newStartLocalDateTime;
//                final DayOfWeek d1 = earliestDate.getDayOfWeek();
//                final Iterator<DayOfWeek> daysIterator = Stream
//                    .iterate(d1, (a) ->  a.plus(1))              // infinite stream of days of the week
//                    .limit(7)                                    // next valid day should be found within 7 days
//                    .iterator();
//                int dayShift = 0;
//                while (daysIterator.hasNext())
//                {
//                    DayOfWeek d = daysIterator.next();
//                    if (getDayOfWeek(d)) break;
//                    dayShift++;
//                }
//                setStartLocalDate(earliestDate.plusDays(dayShift));
//            } else { // edit startDate for all other IntervalUnit types
                setStartLocalDate(newStartLocalDateTime);
//            }
        }
    }
    
    /**
     * If editing a weekly repeat, moving one day past a different day can change the start date.  The method adjusts
     * the start date if necessary
     */
    public void adjustStartDateTimeWeekly()
    {
        if (getFrequency() == Frequency.WEEKLY)
        { // if new start has shifted then move it
            final LocalDateTime earliestDate = getStartLocalDate();
            final DayOfWeek d1 = earliestDate.getDayOfWeek();
            final Iterator<DayOfWeek> daysIterator = Stream
                .iterate(d1, (a) ->  a.plus(1))              // infinite stream of days of the week
                .limit(7)                                    // next valid day should be found within 7 days
                .iterator();
            int dayShift = 0;
            while (daysIterator.hasNext())
            {
                DayOfWeek d = daysIterator.next();
                if (getDayOfWeek(d)) break; // check if day of week is true
                dayShift++;
            }
            setStartLocalDate(earliestDate.plusDays(dayShift));
        }
    }

    
//    /**
//     * Returns new Repeat object with all fields copied from input parameter myRepeat
//     * 
//     * @param myRepeat
//     * @return
//     * @throws CloneNotSupportedException
//     */
//    public static Repeat copy(Repeat myRepeat)
//    {
////        if(!(myRepeat instanceof Cloneable)) throw new CloneNotSupportedException("Invalid cloning");
//        Repeat copyRepeat = new Repeat(myRepeat);
////        myRepeat.copyInto(copyRepeat);
//        myRepeat.getAppointmentData().copyInto(copyRepeat.getAppointmentData());
//        return copyRepeat;
//    }


//
//    private boolean myEquals(Object o1, Object o2)
//    {
//        if ((o1 == null) && (o2 == null)) return true; // both null
//        if (o1 == null || o2 == null) return false; // one null
//        return o1.equals(o2);
//    }


    /**
     * removes bindings on all properties, including the embedded appointment object
     * Bindings are used in the edit popup
     */
    public void unbindAll() {
        countProperty().unbind();
        endCriteriaProperty().unbind();
        untilLocalDateTimeProperty().unbind();
        frequencyProperty().unbind();
        repeatDayOfMonthProperty().unbind();
        repeatDayOfWeekProperty().unbind();
        intervalProperty().unbind();
        getDayOfWeekMap().entrySet()
                                   .stream()
                                   .forEach(a -> a.getValue().unbind());
        startLocalDateProperty().unbind();
    }
    
    /**
     * Adjust date to become next date based on the Repeat rule.  Needs a input temporal on a valid date.
     * 
     * @return
     */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
            final int maxI = getInterval();
            final TemporalField weekOfYear;
            final int initialWeek;
            int currentWeek = 0;
            if (getFrequency() == Frequency.WEEKLY)
            {
                weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                initialWeek = LocalDate.from(temporal).get(weekOfYear);
                currentWeek = initialWeek;
            } else { // variables not used in not a WEEKLY repeat, but still must be initialized
                weekOfYear = null;
                initialWeek = -1;
            }
            int i=0;
            do
            { // loop that counts number of valid dates for total time interval (repeatFrequency)
                temporal = temporal.with(new TemporalAdjuster()
                { // anonymous inner class that finds next valid date
                    @Override
                    public Temporal adjustInto(Temporal temporal)
                    {
                        LocalDate inputDate = LocalDate.from(temporal);
                        switch (getFrequency())
                        {
                        case DAILY:
                            return temporal.plus(Period.ofDays(1));
                        case WEEKLY:
                            final DayOfWeek d1 = inputDate.plusDays(1).getDayOfWeek();
//                            System.out.println("d1 " + d1);
                            final Iterator<DayOfWeek> daysIterator = Stream
                                .iterate(d1, (a) ->  a.plus(1))              // infinite stream of valid days of the week
                                .limit(7)                                    // next valid day should be found within 7 days
                                .iterator();
                            while (daysIterator.hasNext()) {
                                DayOfWeek d = daysIterator.next();
                                if (getDayOfWeek(d)) return temporal.with(TemporalAdjusters.next(d));
                            }
                            return temporal; // only happens if no day of the week are selected (true)
                            case MONTHLY:
                                switch (getMonthlyRepeat())
                                {
                                case DAY_OF_MONTH:
                                    return temporal.plus(Period.ofMonths(1));
                                case DAY_OF_WEEK:
                                    DayOfWeek dayOfWeek = getStartLocalDate().getDayOfWeek();
                                    return temporal.plus(Period.ofMonths(1))
                                            .with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek));
                                }
                        case YEARLY:
                            return temporal.plus(Period.ofYears(1));
                        default:
                            return temporal;
                        }
                    }
                }); // end of anonymous inner TemporalAdjuster class
                // Increment repeat frequency counter
                if (getFrequency() == Frequency.WEEKLY)
                { // increment counter for weekly repeat when week number changes
                    int newWeekNumber = LocalDate.from(temporal).get(weekOfYear);
                    if (newWeekNumber == initialWeek) return temporal; // return new temporal if still in current week (assumes temporal starts on valid date)
                    if (newWeekNumber != currentWeek)
                    {
                        currentWeek = newWeekNumber;
                        i++;
                    }
                } else
                { // all other IntervalUnit types (not WEEKLY) increment counter i for every cycle of anonymous inner class TemporalAdjuster
                    i++;
                }
            } while (i < maxI); // end of while looping anonymous inner class
        return temporal;
        }
    }
    
}
