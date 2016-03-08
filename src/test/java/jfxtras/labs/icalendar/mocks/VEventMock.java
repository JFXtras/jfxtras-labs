package jfxtras.labs.icalendar.mocks;

import java.time.DateTimeException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.ICalendarUtilities;
import jfxtras.labs.icalendar.VComponentProperty;
import jfxtras.labs.icalendar.VEvent;
import jfxtras.labs.icalendar.VEventProperty;

/**
 * Mock VEvent class for testing
 * 
 * @author David Bal
 *
 */
public class VEventMock extends VEvent<InstanceMock, VEventMock>
{
    private final static Callback<StartEndRange, InstanceMock> NEW_INSTANCE = (p) ->
    {
        return new InstanceMock()
                .withStartTemporal(p.getDateTimeStart())
                .withEndTemporal(p.getDateTimeEnd());
    };
    
    @Override
    public List<InstanceMock> makeInstances(Temporal startRange, Temporal endRange)
    {
        if (DateTimeUtilities.isAfter(startRange, endRange)) throw new DateTimeException("endRange must be after startRange");
        setEndRange(endRange);
        setStartRange(startRange);
        System.out.println("ranges:" + getStartRange() + " " + getEndRange());
        return makeInstances();
    }

    @Override
    public List<InstanceMock> makeInstances()
    {
        if ((getStartRange() == null) || (getEndRange() == null)) throw new RuntimeException("Can't make instances without setting date/time range first");
        List<InstanceMock> madeInstances = new ArrayList<>();
        Stream<Temporal> removedTooEarly = stream(getStartRange()).filter(d -> ! DateTimeUtilities.isBefore(d, getStartRange())); // inclusive
        Stream<Temporal> removedTooLate = ICalendarUtilities.takeWhile(removedTooEarly, a -> DateTimeUtilities.isBefore(a, getEndRange())); // exclusive
        removedTooLate.forEach(temporalStart ->
        {
            TemporalAmount duration = endType().getDuration(this);
            Temporal temporalEnd = temporalStart.plus(duration);
            InstanceMock instance = new InstanceMock()
                .withStartTemporal(temporalStart)
                .withEndTemporal(temporalEnd)
                .withSummary(getSummary());
            madeInstances.add(instance);
            instances().add(instance);
      });
      return madeInstances;
    }

    /*
     * CONSTRUCTORS
     */
    public VEventMock() { }
    
    /** Copy constructor */
    public VEventMock(VEventMock vevent)
    {
        super(vevent);
    }
    
    /** Make new VEventMock and populate properties by parsing a string of line-separated
     * content lines
     *  */
    public static VEventMock parse(String string)
    {
        VEventMock vEvent = new VEventMock();
        Iterator<Pair<String, String>> i = ICalendarUtilities.ComponentStringToPropertyNameAndValueList(string).iterator();
        while (i.hasNext())
        {
            Pair<String, String> propertyValuePair = i.next();
            
            // parse each property-value pair by all associated property enums
            VEventProperty.parse(vEvent, propertyValuePair);
            VComponentProperty.parse(vEvent, propertyValuePair);
        }
        return vEvent;
    }
    
    public static boolean isEqualTo(VEventMock v1, VEventMock v2)
    {
        return VEventProperty.isEqualTo(v1, v2, true);
    }
    public static boolean isEqualTo(VEventMock v1, VEventMock v2, boolean verbose)
    {
        return VEventProperty.isEqualTo(v1, v2, verbose);
    }
}
