package jfxtras.labs.icalendar.mocks;

import java.time.DateTimeException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javafx.util.Pair;
import jfxtras.labs.icalendar.ICalendarUtilities;
import jfxtras.labs.icalendar.VComponent;
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

    @Override
    public boolean isValid()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public List<InstanceMock> makeInstances(Temporal startRange, Temporal endRange)
    {
        if (VComponent.isAfter(startRange, endRange)) throw new DateTimeException("endRange must be after startRange");
        setEndRange(endRange);
        setStartRange(startRange);
        return makeInstances();
    }

    @Override
    public List<InstanceMock> makeInstances()
    {
        if ((getStartRange() == null) || (getEndRange() == null)) throw new RuntimeException("Can't make instances without setting date/time range first");
        List<InstanceMock> madeInstances = new ArrayList<>();
        Stream<Temporal> removedTooEarly = stream(getStartRange()).filter(d -> ! VComponent.isBefore(d, getStartRange())); // inclusive
        Stream<Temporal> removedTooLate = ICalendarUtilities.takeWhile(removedTooEarly, a -> VComponent.isBefore(a, getEndRange())); // exclusive
        removedTooLate.forEach(temporalStart ->
        {
            TemporalAmount duration = endPriority().getDuration(this);
            Temporal temporalEnd = temporalStart.plus(duration);
            InstanceMock instance = null;
            try
            {
                instance = getInstanceClass().newInstance();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            instance.setStartTemporal(temporalStart);
            instance.setEndTemporal(temporalEnd);
            instance.setSummary(getSummary());
            madeInstances.add(instance);
            instances().add(instance);
      });
      return madeInstances;
    }

//    /**
//     * The currently generated instances of the recurrence set.
//     * 3.8.5.2 defines the recurrence set as the complete set of recurrence instances for a
//     * calendar component.  As many RRule definitions are infinite sets, a complete representation
//     * is not possible.  The set only contains the events inside the bounds of 
//     */
//    @Override
//    public List<InstanceMock> instances() { return instances; }
//    final private List<InstanceMock> instances = new ArrayList<>();
    
    public Class<? extends InstanceMock> getInstanceClass() { return instanceClass; }
    private Class<? extends InstanceMock> instanceClass = InstanceMock.class; // default instance class
    public void setInstanceClass(Class<? extends InstanceMock> instanceClass) { this.instanceClass = instanceClass; }
    public VEventMock withInstanceClass(Class<? extends InstanceMock> instanceClass) { setInstanceClass(instanceClass); return this; }

    /*
     * CONSTRUCTORS
     */
    public VEventMock() { }
    
    /** Copy constructor */
    public VEventMock(VEventMock vevent)
    {
        super(vevent);
//        copy(vevent, this);
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
}
