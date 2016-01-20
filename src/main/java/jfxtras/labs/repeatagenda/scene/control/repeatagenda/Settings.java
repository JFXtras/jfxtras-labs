package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOption;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency.FrequencyType;


public final class Settings
{
    public Settings() {}

    // below variables are probably obsolete since implementing iCalendar
    public final static Path APPOINTMENTS_FILE = Paths.get("src/jfxtras.labs.samples.repeatagenda.appointments.xml");
    public final static Path APPOINTMENT_GROUPS_FILE = Paths.get("data/appointments/appointmentGroups.xml");
    public final static Path APPOINTMENT_REPEATS_FILE = Paths.get("appointmentRepeats.xml");
    public static final boolean PRETTY_XML = true;  // true for readable indented XML output, false for small files
    
    // defaults can be overridden by resource bundle by running setup method
    public static DateTimeFormatter DATE_FORMAT1 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // format for output files
    public static DateTimeFormatter DATE_FORMAT2 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"); // fancy format for displaying
    public static DateTimeFormatter DATE_FORMAT_AGENDA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_EXCEPTION = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_DATEONLY = DateTimeFormatter.ofPattern("MMM d, yyyy");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_MONTHDAY = DateTimeFormatter.ofPattern("MMMM d"); // for yearly summary
    public static DateTimeFormatter DATE_FORMAT_AGENDA_START = DateTimeFormatter.ofPattern("EEE, dd MMMM, hh:mm a");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_END = DateTimeFormatter.ofPattern("-hh:mm a");
    public static DateTimeFormatter TIME_FORMAT_AGENDA = DateTimeFormatter.ofPattern("HH:mm");

    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES = defaultRepeatFrequenciesMap();
    private static Map<FrequencyType, String> defaultRepeatFrequenciesMap()
    {
        Map<FrequencyType, String> map = new HashMap<>();
        map.put(FrequencyType.DAILY, "Daily");
        map.put(FrequencyType.WEEKLY, "Weekly");
        map.put(FrequencyType.MONTHLY, "Monthly");
        map.put(FrequencyType.YEARLY, "Yearly");
        return map;
    }
    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES_PLURAL = defaultRepeatFrequenciesPluralMap();
    private static Map<FrequencyType, String> defaultRepeatFrequenciesPluralMap()
    {
        Map<FrequencyType, String> map = new HashMap<>();
        map.put(FrequencyType.DAILY, "days");
        map.put(FrequencyType.WEEKLY, "weeks");
        map.put(FrequencyType.MONTHLY, "months");
        map.put(FrequencyType.YEARLY, "years");
        return map;
    }
    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES_SINGULAR = defaultRepeatFrequenciesSingluarMap();
    private static Map<FrequencyType, String> defaultRepeatFrequenciesSingluarMap()
    {
        Map<FrequencyType, String> map = new HashMap<>();
        map.put(FrequencyType.DAILY, "day");
        map.put(FrequencyType.WEEKLY, "week");
        map.put(FrequencyType.MONTHLY, "month");
        map.put(FrequencyType.YEARLY, "year");
        return map;
    }
    public static final Map<ChangeDialogOption, String> REPEAT_CHANGE_CHOICES = defaultRepeatChangeChoicesMap();
    private static Map<ChangeDialogOption, String> defaultRepeatChangeChoicesMap()
    {
        Map<ChangeDialogOption, String> map = new HashMap<>();
        map.put(ChangeDialogOption.ONE, "This event only");
        map.put(ChangeDialogOption.ALL, "All events");
        map.put(ChangeDialogOption.THIS_AND_FUTURE, "This and future events");
        return map;
    }

    public static final Map<Integer, String> ORDINALS = defaultOrdinalsMap();
    private static Map<Integer, String> defaultOrdinalsMap()
    {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "third");
        map.put(4, "fourth");
        map.put(5, "fifth");
        return map;
    }
    public static final Map<DayOfWeek, String> DAYS_OF_WEEK_MAP = defaultDaysOfWeekMap();
    private static Map<DayOfWeek, String> defaultDaysOfWeekMap()
    {
        Map<DayOfWeek, String> map = new HashMap<>();
        map.put(DayOfWeek.MONDAY, "Monday");
        map.put(DayOfWeek.TUESDAY, "Tuesday");
        map.put(DayOfWeek.WEDNESDAY, "Wednesday");
        map.put(DayOfWeek.THURSDAY, "Thursday");
        map.put(DayOfWeek.FRIDAY, "Friday");
        map.put(DayOfWeek.SATURDAY, "Saturday");
        map.put(DayOfWeek.SUNDAY, "Sunday");
        return map;
    }
    
    public static ResourceBundle resources;
   
    public static void setup(ResourceBundle resourcesIn)
    {
        resources = resourcesIn;
        
        DATE_FORMAT1 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format1"));
        DATE_FORMAT2 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format2"));
        DATE_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda"));
        DATE_FORMAT_AGENDA_EXCEPTION = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.exception"));
        DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY = DateTimeFormatter.ofPattern(resources.getString("date.format.agenda.exception.dateonly"));

        DATE_FORMAT_AGENDA_DATEONLY = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.dateonly"));
        DATE_FORMAT_AGENDA_MONTHDAY = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.monthday"));
        DATE_FORMAT_AGENDA_START = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.start"));
        DATE_FORMAT_AGENDA_END = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.end"));
        TIME_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("time.format.agenda"));
        
        REPEAT_FREQUENCIES.put(FrequencyType.DAILY, resourcesIn.getString("daily"));
        REPEAT_FREQUENCIES.put(FrequencyType.WEEKLY, resourcesIn.getString("weekly"));
        REPEAT_FREQUENCIES.put(FrequencyType.MONTHLY, resourcesIn.getString("monthly"));
        REPEAT_FREQUENCIES.put(FrequencyType.YEARLY, resourcesIn.getString("yearly"));
        
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.DAILY, resourcesIn.getString("days"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.WEEKLY, resourcesIn.getString("weeks"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.MONTHLY, resourcesIn.getString("months"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.YEARLY, resourcesIn.getString("years"));
        
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.DAILY, resourcesIn.getString("day"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.WEEKLY, resourcesIn.getString("week"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.MONTHLY, resourcesIn.getString("month"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.YEARLY, resourcesIn.getString("year"));

        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.ONE, resources.getString("dialog.repeat.change.one"));
        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.ALL, resources.getString("dialog.repeat.change.all"));
        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.THIS_AND_FUTURE, resources.getString("dialog.repeat.change.future"));
        
        ORDINALS.put(1, resourcesIn.getString("first"));
        ORDINALS.put(2, resourcesIn.getString("second"));
        ORDINALS.put(3, resourcesIn.getString("third"));
        ORDINALS.put(4, resourcesIn.getString("fourth"));
        ORDINALS.put(5, resourcesIn.getString("fifth"));

        DAYS_OF_WEEK_MAP.put(DayOfWeek.MONDAY, resourcesIn.getString("monday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.TUESDAY, resourcesIn.getString("tuesday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.WEDNESDAY, resourcesIn.getString("wednesday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.THURSDAY, resourcesIn.getString("thursday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.FRIDAY, resourcesIn.getString("friday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.SATURDAY, resourcesIn.getString("saturday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.SUNDAY, resourcesIn.getString("sunday"));
    }

}
