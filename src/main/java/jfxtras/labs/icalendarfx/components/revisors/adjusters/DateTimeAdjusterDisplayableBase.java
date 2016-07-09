package jfxtras.labs.icalendarfx.components.revisors.adjusters;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public abstract class DateTimeAdjusterDisplayableBase<T, U extends VComponentDisplayable<U>> implements DateTimeAdjuster
{
    final private U vComponent;
//    private U vComponentEditedCopy;
    final private Temporal startEdited;
    final private Temporal startOriginal;
    final private EditState myState;
    
    public DateTimeAdjusterDisplayableBase(
            U vComponent,
            Temporal startOriginal,
            Temporal startEdited)
    {
        this.vComponent = vComponent;
        this.startEdited = startEdited;
        this.startOriginal = startOriginal;
        
        myState = EditState.getEditState(startOriginal, startEdited);
    }

    private void applyShift()
    {
        TemporalAmount shiftAmount = DateTimeUtilities.temporalAmountBetween(startOriginal, startEdited);
        Temporal newStart = vComponent.getDateTimeStart().getValue().plus(shiftAmount);
        vComponent.setDateTimeStart(newStart);        
    }
    
    private void applyTypeChange()
    {
        // TODO 
    }
    
    @Override
    public void adjustDateTime()
    {
        switch (myState)
        {
        case DATETIME_TO_DATE:
            break;
        case DATETIME_TO_DATETIME:
            break;
        case DATE_TO_DATE:
            break;
        case DATE_TO_DATETIME:
            break;
        default:
            throw new RuntimeException("unsupported edit state" + myState);
        }
        
        // MAKE STATE CLASS PATTERN
        // STATE IS DEFINED AS COMBO OF 
        //    DATE-TIME CHANGE (DATETIME TO DATE),
        //    CHANGE TYPE (THIS-AND-FUTURE) AND
        //    RRULE STATE (WITH EXISTING_REPEAT)
        
        // for ALL just apply shift and type change
        // for THIS-AND-FUTURE put startEdited into DTSTART
        // for ONE put startEdited into DTSTART
        TemporalAmount shiftAmount = DateTimeUtilities.temporalAmountBetween(startOriginal, startEdited);
        Temporal newStart = vComponent.getDateTimeStart().getValue().plus(shiftAmount);
        vComponent.setDateTimeStart(newStart);
    }
    
    enum EditState
    {
        DATETIME_TO_DATETIME,
        DATETIME_TO_DATE,
        DATE_TO_DATETIME,
        DATE_TO_DATE;
        
        public static EditState getEditState(Temporal startOriginal, Temporal startEdited)
        {
            boolean isStartWholeDay = startEdited instanceof LocalDate;
            boolean isOriginalWholeDay = startOriginal instanceof LocalDate;
            if (isStartWholeDay && isOriginalWholeDay)
            {
                return DATE_TO_DATE;
            } else if (isStartWholeDay)
            {
                return DATETIME_TO_DATE;
            } else if (isOriginalWholeDay)
            {
                return DATE_TO_DATETIME;                
            } else
            {
                return DATETIME_TO_DATETIME;
            }
        }
    }
}
