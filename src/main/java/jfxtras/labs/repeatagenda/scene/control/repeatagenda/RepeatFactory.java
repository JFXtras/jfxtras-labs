package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

public final class RepeatFactory {

////    static LocalDateTimeRange dateTimeRange; // range of current skin
//    private RepeatFactory() { }
//
//    public static <T extends Repeat> T newRepeat(Class<T> repeatClass) throws InstantiationException, IllegalAccessException
//    {
//        return repeatClass.newInstance();
//    }
//       
//    /**
//     * Constructor for newly created repeatable appointments.  The date range of displayed skin
//     * is necessary.
//     * 
//     * @param dateTimeRange
//     * @return
//     * @throws IllegalAccessException 
//     * @throws InstantiationException 
//     */
//    public static <T extends Repeat> T newRepeat(Class<T> repeatClass, LocalDateTimeRange dateTimeRange) throws InstantiationException, IllegalAccessException
//    {
//        T repeat = newRepeat(repeatClass);
//        repeat.setLocalDateTimeRange(dateTimeRange);
//        return repeat;
//    }
//
//    public static <T extends Repeat> T newRepeat(Class<T> repeatClass, Repeat r) throws InstantiationException, IllegalAccessException
//    {
//        T repeat = newRepeat(repeatClass);
//        r.copyInto(repeat);
//        return repeat;
//    }
    
//  static LocalDateTimeRange dateTimeRange; // range of current skin
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
//  public static RepeatImpl newRepeat(LocalDateTimeRange dateTimeRange) {
//      return new RepeatImpl(dateTimeRange);
//  }
  
  public static RepeatImpl newRepeat(Repeat r) {
      return new RepeatImpl(r);
  }
}
