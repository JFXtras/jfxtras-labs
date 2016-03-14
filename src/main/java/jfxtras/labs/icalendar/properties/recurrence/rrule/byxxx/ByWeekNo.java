package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;

import static java.time.temporal.ChronoUnit.WEEKS;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

/** BYWEEKNO from RFC 5545, iCalendar 3.3.10, page 42 */
public class ByWeekNo extends ByRuleAbstract
{
    private final static ByRuleParameter MY_RULE = ByRuleParameter.BY_WEEK_NUMBER;

    /** sorted array of weeks of the year
     * (i.e. 5, 10 = 5th and 10th weeks of the year, -3 = 3rd from last week of the year)
     * Uses a varargs parameter to allow any number of value.
     */
    public int[] getWeekNumbers() { return weekNumbers; }
    private int[] weekNumbers;
    public void setWeekNumbers(int... weekNumbers)
    {
        for (int w : weekNumbers)
        {
            if (w < -53 || w > 53 || w == 0) throw new IllegalArgumentException("Invalid BYWEEKNO value (" + w + "). Valid values are 1 to 53 or -53 to -1.");
        }
        this.weekNumbers = weekNumbers;
    }
    public ByWeekNo withWeekNumbers(int... weekNumbers) { setWeekNumbers(weekNumbers); return this; }

    /** Start of week - default start of week is Monday */
    public DayOfWeek getWeekStart() { return weekStart; }
    private DayOfWeek weekStart = DayOfWeek.MONDAY; // default to start on Monday
    public void setWeekStart(DayOfWeek weekStart) { this.weekStart = weekStart; }
    public ByWeekNo withWeekStart(DayOfWeek weekStart) { this.weekStart = weekStart; return this; }

    // CONSTRUCTORS
    /** takes String of comma-delimited integers, parses it to array of ints 
     * This constructor is REQUIRED by 
     * {@link jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.Rule.ByRuleParameter#newInstance(String)}
     */
    public ByWeekNo(String weekNumbersString)
    {
        this();
        int[] days = Arrays
                .stream(weekNumbersString.split(","))
                .mapToInt(s -> Integer.parseInt(s))
                .toArray();
        setWeekNumbers(days);
    }

    /**
     * This constructor is required by {@link jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Frequency#copy}
     */
    public ByWeekNo()
    {
        super(MY_RULE);
    }
    
    /** Constructor requires weeks of the year int value(s) */
    public ByWeekNo(int...weekNumbers)
    {
        this();
        setWeekNumbers(weekNumbers);
    }

    @Override
    public void copyTo(Rule destination)
    {
        ByWeekNo destination2 = (ByWeekNo) destination;
        destination2.weekNumbers = new int[weekNumbers.length];
        for (int i=0; i<weekNumbers.length; i++)
        {
            destination2.weekNumbers[i] = weekNumbers[i];
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByWeekNo testObj = (ByWeekNo) obj;
        
        boolean weekNumbersEquals = Arrays.equals(getWeekNumbers(), testObj.getWeekNumbers());
        return weekNumbersEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = (31 * hash) + getWeekNumbers().hashCode();
        return hash;
    }
    
    @Override
    public String toString()
    {
        String days = Arrays.stream(getWeekNumbers())
                .mapToObj(d -> d + ",")
                .collect(Collectors.joining());
        return ByRuleParameter.BY_WEEK_NUMBER + "=" + days.substring(0, days.length()-1); // remove last comma
    }
    
    @Override
    public Stream<Temporal> stream(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit, Temporal startTemporal)
    {
        ChronoUnit originalChronoUnit = chronoUnit.get();
        chronoUnit.set(WEEKS);
        switch (originalChronoUnit)
        {
        case YEARS:
            Locale oldLocale = null;
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            DayOfWeek firstDayOfWeek = weekFields.getFirstDayOfWeek();
            if (firstDayOfWeek != getWeekStart())
            {
                switch (weekStart)
                { // Pick a Locale that matches the first day of week specified.
                case MONDAY:
                    oldLocale = Locale.getDefault();
                    Locale.setDefault(Locale.FRANCE);
                    break;
                case SUNDAY:
                    oldLocale = Locale.getDefault();
                    Locale.setDefault(Locale.US);
                    break;
                case FRIDAY:
                case SATURDAY:
                case THURSDAY:
                case TUESDAY:
                case WEDNESDAY:
                default:
                    throw new RuntimeException("Not implemented start of week " + weekStart);
                }
            }
            WeekFields weekFields2 = WeekFields.of(Locale.getDefault());
            if (weekFields2.getFirstDayOfWeek() != getWeekStart()) throw new RuntimeException("Can't match first day of week " + getWeekStart());

            // Make output stream
            Stream<Temporal> outStream = inStream.flatMap(date -> 
            { // Expand to include matching days in all months
                DayOfWeek dayOfWeek = DayOfWeek.from(startTemporal);
                List<Temporal> dates = new ArrayList<>();
                for (int myWeekNumber: getWeekNumbers())
                {
                    Temporal newTemporal = date.with(TemporalAdjusters.next(dayOfWeek));
                    int newDateWeekNumber = newTemporal.get(weekFields2.weekOfWeekBasedYear());
                    int weekShift = myWeekNumber - newDateWeekNumber;
                    dates.add(newTemporal.plus(weekShift, ChronoUnit.WEEKS));
                }
                return dates.stream();
            });
            if (oldLocale != null) Locale.setDefault(oldLocale); // if changed, return Locale to former setting
            return outStream;
        case DAYS:
        case WEEKS:
        case MONTHS:
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new IllegalArgumentException("BYWEEKNO is not available for " + chronoUnit.get() + " frequency."); // Not available
        default:
            break;
        }
        return null;    
    }

}
