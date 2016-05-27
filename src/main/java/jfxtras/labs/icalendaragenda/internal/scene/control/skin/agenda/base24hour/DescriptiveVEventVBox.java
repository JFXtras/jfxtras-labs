package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import jfxtras.labs.icalendarfx.components.VEvent;

public class DescriptiveVEventVBox extends DescriptiveLocatableVBox<VEvent>
{
    public DescriptiveVEventVBox()
    {
        super();
        endLabel.setText(getResources().getString("end.time"));
    }
    
//    @Override
//    void handleWholeDayChange(VEvent vComponent, Boolean newSelection)
//    {
////        System.out.println("whole day2:" + newSelection);
//        super.handleWholeDayChange(vComponent, newSelection);
//        if (newSelection)
//        {
////          LocalDate newDateTimeEnd = LocalDate.from(vComponent.getDateTimeEnd().getValue()).plus(1, ChronoUnit.DAYS);
////          vComponent.setDateTimeEnd(newDateTimeEnd);          
////          LocalDate end = LocalDate.from(endDateTextField.getLocalDate()).plus(1, ChronoUnit.DAYS);
////          endDateTextField.setLocalDate(end);
//        } else
//        {
////            LocalDateTime start = startDateTimeTextField.getLocalDateTime();
////            final Temporal newDateTimeEnd = vComponent.getDateTimeStart().getValue().plus(lastDuration);
////            LocalDateTime end = start.plus(lastDuration);
////            endDateTimeTextField.setLocalDateTime(end);
////            vComponent.setDateTimeEnd(newDateTimeEnd);
//
//        }
//    }
}
