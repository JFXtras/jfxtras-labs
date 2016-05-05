package jfxtras.labs.icalendarfx.properties.component.descriptive;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.parameters.Encoding;
import jfxtras.labs.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.labs.icalendarfx.parameters.FormatType;
import jfxtras.labs.icalendarfx.parameters.ParameterType;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.PropertyAttachment;
import jfxtras.labs.icalendarfx.properties.PropertyBase;

/**
 * ATTACH
 * Attachment
 * RFC 5545, 3.8.1.1, page 80
 * 
 * This property provides the capability to associate a document object with a calendar component.
 * 
 * Examples:
 * ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
 * ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/
 *  reports/r-960812.ps
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 */
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
           formatType = new SimpleObjectProperty<>(this, ParameterType.FORMAT_TYPE.toString());
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
   public Attachment<T> withFormatType(String format) { setFormatType(FormatType.parse(format)); return this; }
   
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
           encoding = new SimpleObjectProperty<>(this, ParameterType.INLINE_ENCODING.toString());
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
   
   public Attachment(Class<T> clazz, String contentLine)
   {
       super(clazz, contentLine);
       clazz.cast(getValue()); // ensure value class type matches parameterized type
   }
   
   public Attachment(Attachment<T> source)
   {
       super(source);
   }
   
   public Attachment(T value)
   {
       super(value);
//       clazz.cast(getValue()); // ensure value class type matches parameterized type
   }
   
   Attachment()
   {
       super();
   }
   
   @Override
   protected void setConverterByClass(Class<T> clazz)
   {
       if (clazz.equals(URI.class))
       {
           setConverter(ValueType.UNIFORM_RESOURCE_IDENTIFIER.getConverter());
       } else if (clazz.equals(String.class))
       {
           setConverter(ValueType.BINARY.getConverter());           
       } else
       {
           throw new IllegalArgumentException("Only parameterized types of URI and String supported.");           
       }
   }

   
//   @Override
//   public void setValue(T value)
//   {
//       System.out.println(value + " " + value.getClass());
//       if (value instanceof URI)
//       {
////           setValueParameter(ValueType.UNIFORM_RESOURCE_IDENTIFIER); // default value
//       } else if (value instanceof String)
//       {
//           System.out.println("set to binary:");
//           setValueParameter(ValueType.BINARY);           
//       } else
//       {
//           throw new IllegalArgumentException("Only parameterized types of URI and String supported.");
//       }
//       super.setValue(value);
//   }

   
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
   
    public static <U> Attachment<U> parse(String string)
    {
        Attachment<U> property = new Attachment<U>();
        property.parseContent(string);
        return property;
    }
}
