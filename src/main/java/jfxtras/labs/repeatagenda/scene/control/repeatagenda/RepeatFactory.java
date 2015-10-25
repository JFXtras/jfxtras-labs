package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public final class RepeatFactory {

//    static LocalDateTimeRange dateTimeRange; // range of current skin
    private RepeatFactory() { }

    public static RepeatImpl newRepeat() {
        return new RepeatImpl();
    }
    
    /**
     * Constructor for newly created repeatable appointments.  The date range of displayed skin
     * is necessary.
     * 
     * @param dateTimeRange
     * @return
     */
    public static RepeatImpl newRepeat(LocalDateTimeRange dateTimeRange) {
        return new RepeatImpl(dateTimeRange);
    }
    
    public static RepeatImpl newRepeat(Repeat r) {
        return new RepeatImpl(r);
    }
}
