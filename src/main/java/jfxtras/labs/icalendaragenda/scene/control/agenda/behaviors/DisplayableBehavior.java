package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

public abstract class DisplayableBehavior<T extends VComponentDisplayable<T>> implements Behavior<T>
{
    ICalendarAgenda agenda;
//    private EditComponentPopupStage<?> editPopup;
    
    public DisplayableBehavior(ICalendarAgenda agenda) { this.agenda = agenda; }
    
//    abstract EditComponentPopupStage<?> getEditStage(Appointment appointment);
    
//    @Override
//    public void iCalendarEditBehavior(Appointment appointment)
//    {
//        EditComponentPopupStage<?> editPopup = getEditStage(appointment);
//
//        editPopup.getScene().getStylesheets().addAll(agenda.getUserAgentStylesheet(), ICalendarAgenda.ICALENDAR_STYLE_SHEET);
//
//        // remove listeners during edit (to prevent creating extra vEvents when making appointments)
//        editPopup.setOnShowing((windowEvent) -> agenda.appointments().removeListener(agenda.appointmentsListChangeListener));
//        
//        /* POSITION POPUP
//         * Position popup to left or right of bodyPane, where there is room.
//         * Note: assumes the control is displayed at its preferred height and width */
//        Pane bodyPane = (Pane) ((AgendaSkin) agenda.getSkin()).getNodeForPopup(appointment);
//        double prefHeightControl = ((Control) editPopup.getScene().getRoot()).getPrefHeight();
//        double prefWidthControl = ((Control) editPopup.getScene().getRoot()).getPrefWidth();
//        double xLeft = NodeUtil.screenX(bodyPane) - prefWidthControl - 5;
//        double xRight = NodeUtil.screenX(bodyPane) + bodyPane.getWidth() + 5;
//        double x = (xLeft > 0) ? xLeft : xRight;
//        double y = NodeUtil.screenY(bodyPane) - prefHeightControl/2;
//        editPopup.setX(x);
//        editPopup.setY(y);
//        editPopup.show();
//        
//        editPopup.getEditDisplayableTabPane().isFinished().addListener((obs) -> editPopup.hide());
//        // return listener after edit
//        editPopup.setOnHiding((windowEvent) ->  agenda.appointments().addListener(agenda.appointmentsListChangeListener));
//    }
    
//    public void appointmentChangedBehavior(Appointment appointment)
//    {
//        Temporal startOriginalRecurrence = agenda.appointmentStartOriginalMap().get(System.identityHashCode(appointment));
//        final Temporal startRecurrence;
//        final Temporal endRecurrence;
//    
//        boolean wasDateType = DateTimeType.of(startOriginalRecurrence).equals(DateTimeType.DATE);
//        boolean isNotDateType = ! DateTimeType.of(appointment.getStartTemporal()).equals(DateTimeType.DATE);
//        boolean isChangedToTimeBased = wasDateType && isNotDateType;
//        boolean isChangedToWholeDay = appointment.isWholeDay() && isNotDateType;
//        if (isChangedToTimeBased)
//        {
//            startRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getStartTemporal(), ZoneId.systemDefault());
//            endRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getEndTemporal(), ZoneId.systemDefault());
//        } else if (isChangedToWholeDay)
//        {
//            startRecurrence = LocalDate.from(appointment.getStartTemporal());
//            Temporal endInstanceTemp = LocalDate.from(appointment.getEndTemporal());
//            endRecurrence = (endInstanceTemp.equals(startRecurrence)) ? endInstanceTemp.plus(1, ChronoUnit.DAYS) : endInstanceTemp; // make period between start and end at least one day
//        } else
//        {
//            startRecurrence = appointment.getStartTemporal();
//            endRecurrence = appointment.getEndTemporal();            
//        }
//    
//        System.out.println("drag-n-drop" + startOriginalRecurrence + " " + startRecurrence + " " + endRecurrence);
//    //    System.out.println("change localdatetime:" + appointment.getStartLocalDateTime() + " " + appointment.getEndLocalDateTime() + " " + appointment.isWholeDay());
//        agenda.appointments().removeListener(agenda.appointmentsListChangeListener);
//        agenda.getVCalendar().getVEvents().removeListener(agenda.vComponentsChangeListener);
//        
//        VComponentDisplayable<?> vComponent = agenda.appointmentVComponentMap.get(appointment);
//        try
//        {
//            VComponentDisplayable<?> vComponentOriginalCopy = vComponent.getClass().newInstance();
//            vComponentOriginalCopy.copyComponentFrom(vComponent);
//            Collection<?> newVComponents = vComponent.newRevisor()
//                    .revise();
//    
//    //        Collection<VComponentDisplayable<?>> newVComponents = ReviseComponentHelper.handleEdit(
//    ////                vComponentOriginalCopy,
//    //                vComponent,
//    //                startOriginalRecurrence,
//    //                startRecurrence,
//    //                endRecurrence,
//    ////                editDescriptiveVBox.shiftAmount,
//    //                EditChoiceDialog.EDIT_DIALOG_CALLBACK
//    //                );
//            boolean changed = newVComponents.isEmpty(); // temp
//            appointments().addListener(appointmentsListChangeListener);
//            getVCalendar().getVEvents().addListener(vComponentsChangeListener);
//            
//    //        System.out.println("vComponents changed - added:******************************" + vComponents.size());       
//            
//            if (! changed) refresh(); // refresh if canceled (undo drag effect, if edited a refresh occurred when updating Appointments)
//    //        System.out.println("map4:" + System.identityHashCode(appointment)+ " " +  appointment.getStartTemporal());
//            appointmentStartOriginalMap.put(System.identityHashCode(appointment), appointment.getStartTemporal()); // update start map
//            
//        } catch (InstantiationException | IllegalAccessException e)
//        {
//            e.printStackTrace();
//        }
//    }

}
