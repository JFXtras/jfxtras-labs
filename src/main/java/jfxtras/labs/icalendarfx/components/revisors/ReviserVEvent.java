package jfxtras.labs.icalendarfx.components.revisors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Revise VEvent behavior
 * 
 * @author David Bal
 *
 */
public class ReviserVEvent extends ReviserLocatable<ReviserVEvent, VEvent>
{
    public ReviserVEvent(VEvent component)
    {
        super(component);
    }
    
    @Override
    public void adjustDateTime()
    {
        super.adjustDateTime();
        adjustDateTimeEndOrDuration();
    }

    private void adjustDateTimeEndOrDuration()
    {
        TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        if (getVComponentEdited().getDuration() != null)
        {
            getVComponentEdited().setDuration(duration);
        } else if (getVComponentEdited().getDateTimeEnd() != null)
        {
            Temporal dtend = getVComponentEdited().getDateTimeStart().getValue().plus(duration);
            getVComponentEdited().setDateTimeEnd(dtend);
        } else
        {
            throw new RuntimeException("Either DTEND or DURATION must be set");
        }
    }
    
    @Override
    List<PropertyType> findChangedProperties()
    {
        List<PropertyType> changedProperties = super.findChangedProperties();
//                getVComponentEdited(), getVComponentOriginal(), getStartRecurrence(), getEndRecurrence());
//        System.out.println("duration temporals:" + startRecurrence + " + " + endRecurrence);
        TemporalAmount durationNew = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        TemporalAmount durationOriginal = getVComponentEdited().getActualDuration();
        if (! durationOriginal.equals(durationNew))
        {
            if (getVComponentEdited().getDateTimeEnd() != null)
            {
                changedProperties.add(PropertyType.DATE_TIME_END);                    
            } else if (getVComponentEdited().getDuration() == null)
            {
                changedProperties.add(PropertyType.DURATION);                    
            }
        }      
        return changedProperties;
    }
    
    @Override
    public List<PropertyType> dialogRequiredProperties()
    {
        List<PropertyType> list = super.dialogRequiredProperties();
        list.addAll(Arrays.asList(
                        PropertyType.DESCRIPTION,
                        PropertyType.DURATION,
                        PropertyType.GEOGRAPHIC_POSITION,
                        PropertyType.LOCATION,
                        PropertyType.PRIORITY,
                        PropertyType.RESOURCES
                        ));
        return list;
    }
    
    @Override
    void editOne()
    {
        super.editOne();
        adjustDateTimeEndOrDuration();
    }
}
