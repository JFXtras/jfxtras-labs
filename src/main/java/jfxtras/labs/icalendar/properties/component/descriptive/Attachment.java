package jfxtras.labs.icalendar.properties.component.descriptive;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.Encoding;
import jfxtras.labs.icalendar.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendar.parameters.FormatType;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.PropertyAttachment;
import jfxtras.labs.icalendar.properties.PropertyBase;

public class Attachment<T> extends PropertyBase<T, Attachment<T>> implements PropertyAttachment<T>
{
    /**
    * FMTTYPE: Format type parameter
    * RFC 5545, 3.2.8, page 19
    * specify the content type of a referenced object.
    */
   @Override
public FormatType getFormatType() { return (formatType == null) ? null : formatType.get(); }
   @Override
public ObjectProperty<FormatType> formatTypeProperty()
   {
       if (formatType == null)
       {
           formatType = new SimpleObjectProperty<>(this, ParameterEnum.FORMAT_TYPE.toString());
       }
       return formatType;
   }
   private ObjectProperty<FormatType> formatType;
   @Override
public void setFormatType(FormatType formatType)
   {
       if (formatType != null)
       {
           formatTypeProperty().set(formatType);
       }
   }
   public Attachment<T> withFormatType(FormatType format) { setFormatType(format); return this; }
   public Attachment<T> withFormatType(String format) { setFormatType(new FormatType(format)); return this; }
   
   /**
    * ENCODING: Incline Encoding
    * RFC 5545, 3.2.7, page 18
    * 
    * Specify an alternate inline encoding for the property value.
    * Values can be "8BIT" text encoding defined in [RFC2045]
    *               "BASE64" binary encoding format defined in [RFC4648]
    *
    * If the value type parameter is ";VALUE=BINARY", then the inline
    * encoding parameter MUST be specified with the value" ;ENCODING=BASE64".
    */
   @Override
public Encoding getEncoding() { return (encoding == null) ? null : encoding.get(); }
   @Override
public ObjectProperty<Encoding> encodingProperty()
   {
       if (encoding == null)
       {
           encoding = new SimpleObjectProperty<>(this, ParameterEnum.INLINE_ENCODING.toString());
       }
       return encoding;
   }
   private ObjectProperty<Encoding> encoding;
   @Override
public void setEncoding(Encoding encoding)
   {
       if (encoding.getValue() != EncodingType.BASE64)
       {
           throw new IllegalArgumentException("Attachment property only allows ENCODING to be set to" + EncodingType.BASE64);
       }

       if (encoding != null)
       {
           encodingProperty().set(encoding);
       }
   }
   public Attachment<T> withEncoding(Encoding encoding) { setEncoding(encoding); return this; }
   public Attachment<T> withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return this; }

   
   /*
    * CONSTRUCTORS
    */
   
   public Attachment(Class<T> clazz, CharSequence contentLine)
   {
       super(contentLine);
       clazz.cast(getValue()); // ensure value class type matches parameterized type
   }
   
   public Attachment(Attachment<T> source)
   {
       super(source);
   }
   
   public Attachment(Class<T> clazz, T value)
   {
       super(value);
       clazz.cast(getValue()); // ensure value class type matches parameterized type
   }
   
   @Override
   public void setValue(T value)
   {
       if (value instanceof URI)
       {
//           setValueParameter(ValueType.UNIFORM_RESOURCE_IDENTIFIER); // default value
       } else if (value instanceof String)
       {
           System.out.println("set to binary:");
           setValueParameter(ValueType.BINARY);           
       } else
       {
           throw new IllegalArgumentException("Only parameterized types of URI and String supported.");
       }
       super.setValue(value);
   }

   
   @Override
   public boolean isValid()
   {
//       boolean isEncodingNull = getEncoding() == null;
//       boolean isValueTypeNull = getValueParameter() == null;
//       System.out.println("attachment isValid:" + isEncodingNull + " " + isValueTypeNull);
//       if (isEncodingNull && isValueTypeNull)
//       {
//           return true;
//       }
//       if (isEncodingNull || isValueTypeNull)
//       { // both ENCODING and VALUE must be set or not set, only one is not allowed
//           return false;
//       }
       boolean isBase64Type = (getEncoding() == null) ? false : getEncoding().getValue() != EncodingType.BASE64;
       boolean isBinaryValue = (getValueParameter() == null) ? false : getValueParameter().getValue() != ValueType.BINARY;
       if (isBinaryValue && ! isBase64Type)
       { // invalid ValueType
           return false;
       }
       return true && super.isValid();
   }
}
