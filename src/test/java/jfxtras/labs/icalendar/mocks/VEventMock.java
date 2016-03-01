package jfxtras.labs.icalendar.mocks;

import java.time.temporal.Temporal;
import java.util.Collection;

import jfxtras.labs.icalendar.VEvent;

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

}
