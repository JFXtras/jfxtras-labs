package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;

/**
 * Provides new Appointment factory for implementation of Appointment interface
 * Also, provides location to specify read and write methods for appointment collections
 * @author David Bal
 *
 */
public final class AppointmentFactory {
    
    private AppointmentFactory() {}

    public static <T extends RepeatableAppointment> T newAppointment(Class<T> appointmentClass) throws InstantiationException, IllegalAccessException
    {
        return appointmentClass.newInstance();
    }

    public static <T extends RepeatableAppointment, U extends Repeat> T newAppointment(
            Class<T> appointmentClass
          , Class<U> repeatClass
          , T appointment) throws InstantiationException, IllegalAccessException
    {
        T newAppointment = newAppointment(appointmentClass);
        appointment.copyInto(newAppointment);
//        appointment.setRepeat(RepeatFactory.newRepeat(repeatClass, appointment.getRepeat()));
        return newAppointment;
    }

    public static RepeatableAppointment newAppointment(RepeatableAppointment appointment) {
        return new RepeatableAppointmentImpl(appointment);
    }
//    public static RepeatableAppointment newAppointment(RepeatableAppointment appointment) {
//        return new RepeatableAppointmentImpl(appointment);
//    }

//    public static RepeatableAppointmentImpl returnConcreteAppointment(Appointment myAppointment) {
//        return (RepeatableAppointmentImpl) myAppointment;
//    }


    
//    /**
//     * writes appointmentList to file
//     */
//    public static void writeToFile(Collection<Appointment> appointments) {
////        System.out.println("writeToFile");
//        RepeatableAppointmentImpl.writeToFile(appointments, Settings.APPOINTMENTS_FILE);
//    }
//
//    /**
//     * writes appointmentList to file - temporary work around for type problem
//     */
//    public static void writeToFile(List<Appointment> appointments) {
//        System.out.println("write appointmetns");
////        List<RepeatableAppointment> appointments2 = appointments
////                .stream()
////                .map(a -> ((RepeatableAppointment) a)) // down cast all appointmetns to RepeatableAppointments
////                .collect(Collectors.toList());
//        System.out.println("repeat made = " + appointments.stream().filter(a -> ((RepeatableAppointment) a).isRepeatMade()).count());
//        RepeatableAppointmentImpl.writeToFile(appointments, Settings.APPOINTMENTS_FILE);
//    }
//    
//
//    /**
//     * reads appointmentList from file
//     * @param appointmentsPath 
//     * @param appointmentGroups 
//     * @throws IOException 
//     * @throws SAXException 
//     * @throws ParserConfigurationException 
//     */
//    public static Collection<RepeatableAppointment> readFromFile(Path appointmentsPath
//            , ObservableList<AppointmentGroup> appointmentGroups
//            , Collection<RepeatableAppointment> appointments)
//            throws ParserConfigurationException, SAXException, IOException
//    {
////        System.out.println("readFromFile");
//        return RepeatableAppointmentImpl.readFromFile(appointmentsPath.toFile(), appointmentGroups, appointments);
//    }
    
//    public static void setupRepeats(Collection<MyRepeat> repeats) {
//        MyAppointment.setupRepeats(repeats);
//    }
  
}
