package jfxtras.labs.icalendar.mocks;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Iterator;

import javafx.util.Pair;
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

    @Override
    public boolean isValid()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<InstanceMock> makeInstances(Temporal start, Temporal end)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<InstanceMock> makeInstances()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<InstanceMock> instances()
    {
        // TODO Auto-generated method stub
        return null;
    }

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
