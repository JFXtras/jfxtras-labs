package jfxtras.labs.icalendar.parameters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import javafx.util.StringConverter;

public class DefaultStringConverter<T> extends StringConverter<T>
{
    @Override
    public String toString(T object)
    {
        return object.toString();
    }

    @Override
    public T fromString(String string)
    {
//        Type[] types = ((ParameterizedType)getProperty().getClass().getGenericSuperclass())
//                .getActualTypeArguments();
        Type[] types = ((ParameterizedType)getClass().getGenericSuperclass())
                .getActualTypeArguments();
        System.out.println("class2:" + getClass());
        Arrays.stream(types).forEach(System.out::println);
//         Class<T> myClass = (Class<T>) types[types.length-1]; // get last parameterized type
         Class<T> myClass = (Class<T>) types[0]; // get last parameterized type
         System.out.println("class2:" + " " + myClass);
         if (myClass.equals(String.class))
         {
             return (T) string;            
         }
         throw new RuntimeException("can't convert property value to type: " + myClass.getSimpleName() +
                 ". You need to override string converter for class " + getClass().getSimpleName());
    }
}
