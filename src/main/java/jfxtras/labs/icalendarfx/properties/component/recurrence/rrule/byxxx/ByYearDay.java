package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

public class ByYearDay extends ByRuleIntegerListAbstract<ByYearDay>
{    
//    @Override
//    public void setValue(ObservableList<Integer> yearDays)
//    {
//        super.setValue(yearDays);
//        getValue().addListener(validValueListener);
//    }
//    public void setValue(Integer... yearDays)
//    {
//        setValue(FXCollections.observableArrayList(yearDays));
//    }
//    public void setValue(String months)
//    {
//        parseContent(months);
//    }
//    public ByYearDay withValue(Integer... yearDays)
//    {
//        setValue(yearDays);
//        return this;
//    }
//    public ByYearDay withValue(String yearDays)
//    {
//        setValue(yearDays);
//        return this;
//    }
    
    
    public ByYearDay()
    {
        super();
//        setValue(FXCollections.observableArrayList());
    }
    
    public ByYearDay(Integer... yearDays)
    {
        super(yearDays);
//        this();
//        setValue(yearDays);
    }
    
    public ByYearDay(ByYearDay source)
    {
        super(source);
    }
    
    @Override
    Predicate<Integer> isValidValue()
    {
        return (value) -> (value < -366) || (value > 366) || (value == 0);
    }

    
//    /**
//     * Listener to validate additions to value list
//     */
//    private ListChangeListener<Integer> validValueListener = (ListChangeListener.Change<? extends Integer> change) ->
//    {
//        while (change.next())
//        {
//            if (change.wasAdded())
//            {
//                Iterator<? extends Integer> i = change.getAddedSubList().iterator();
//                while (i.hasNext())
//                {
//                    int value = i.next();
//                    if ((value < -366) || (value > 366) || (value == 0))
//                    {
//                        throw new IllegalArgumentException("Invalid " + elementType().toString() + " value (" + value + "). Valid values are 1 to 366 or -366 to -1.");
//                    }
//                }
//            }
//        }
//    };
    

    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit,
            Temporal startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toContent()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
//    @Override
//    public void parseContent(String content)
//    {
//        Integer[] monthDayArray = Arrays.asList(content.split(","))
//                .stream()
//                .map(s -> Integer.parseInt(s))
//                .toArray(size -> new Integer[size]);
//        setValue(FXCollections.observableArrayList(monthDayArray));
//    }
    
    public static ByMonthDay parse(String content)
    {
        ByMonthDay element = new ByMonthDay();
        element.parseContent(content);
        return element;
    }





}
