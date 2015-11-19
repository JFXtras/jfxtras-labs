package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;

/** BYMONTH from RFC 5545, iCalendar 3.3.10, page 42 */
public class ByMonth extends ByRuleAbstract
{
    /** sorted array of months to be included
     * January = 1 - December = 12
     * Uses a varargs parameter to allow any number of months
     */
    public Month[] getMonths() { return months; }
    private Month[] months;
    private void setMonths(Month... months) { this.months = months; }

    public ByMonth(Frequency frequency, Month... months)
    {
        super(frequency);
        setMonths(months);
    }

    @Override
    public void copyTo(Rule destination)
    {
        ByMonth destination2 = (ByMonth) destination;
        destination2.months = months;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByMonth testObj = (ByMonth) obj;
        boolean monthEquals = getMonths().equals(testObj.getMonths());
        return monthEquals;
    }
    
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        switch (getFrequency().getChronoUnit())
        {
        case DAYS:
        case WEEKS:
        case MONTHS:
            getFrequency().setChronoUnit(MONTHS);
            return inStream.filter(date ->
            { // filter out all but qualifying days
                Month myMonth = date.toLocalDate().getMonth();
                for (Month month : getMonths())
                {
                    if (month == myMonth) return true;
                }
                return false;
            });
        case YEARS:
            getFrequency().setChronoUnit(MONTHS);
            return inStream.flatMap(date -> 
            { // Expand to include matching days in all months
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
                int monthNum = date.toLocalDate().getMonth().getValue();
                for (Month month : getMonths())
                {
                    int myMonthNum = month.getValue();
                    int monthShift = myMonthNum - monthNum;
                    dates.add(date.plus(monthShift, MONTHS));
                }
                return dates.stream();
            });
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new RuntimeException("Not implemented ChronoUnit: " + getFrequency().getChronoUnit()); // probably same as DAILY
        default:
            break;
        }
        return null;    
    }
}
