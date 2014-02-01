package jfxtras.labs.css.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.css.ParsedValue;
import javafx.scene.text.Font;

import com.sun.javafx.css.StyleConverterImpl;

public class SimpleDateFormatConverter extends StyleConverterImpl<String, DateFormat> {

    @Override
    public SimpleDateFormat convert(ParsedValue<String, DateFormat> value, Font not_used) {
        String str = value.getValue();
        return new SimpleDateFormat(str);
    }

    @Override
    public String toString() {
        return SimpleDateFormatConverter.class.getSimpleName();
    }
}
