package jfxtras.labs.icalendar.properties;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.CommonName;
import jfxtras.labs.icalendar.parameters.DirectoryEntryReference;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.SentBy;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;

/**
 * Abstract class for properties with a CAL-ADDRESS value.
 * The value is stored as a URI object
 * 
 * CAL-ADDRESS
 * Calendar User Address
 * RFC 5545, 3.3.3, page 31
 *  
 * This value type is used to identify properties that contain a calendar user address.
 * The email address of a calendar user.
 * 
 * Example: mailto:jane_doe@example.com
 * 
 * @author David Bal
 *
 * @param <T>
 * @param <U>
 * 
 * concrete subclasses
 * @see Organizer
 * @see Attendee
 */
public abstract class PropertyCalendarUserAddress<T> extends PropertyLanguage<T, URI>
{
    /**
     * CN
     * Common Name
     * RFC 5545, 3.2.2, page 15
     * 
     * To specify the common name to be associated with the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
     */
    public CommonName getCommonName() { return (commonName == null) ? null : commonName.get(); }
    public ObjectProperty<CommonName> commonNameProperty()
    {
        if (commonName == null)
        {
            commonName = new SimpleObjectProperty<>(this, ParameterEnum.COMMON_NAME.toString());
        }
        return commonName;
    }
    private ObjectProperty<CommonName> commonName;
    public void setCommonName(CommonName commonName)
    {
        if (commonName != null)
        {
            commonNameProperty().set(commonName);
        }
    }
    public void setCommonName(String content) { setCommonName(new CommonName(content)); }
    public T withCommonName(CommonName commonName) { setCommonName(commonName); return (T) this; }
    public T withCommonName(String content) { setCommonName(content); return (T) this; }    

    /**
     * DIR
     * Directory Entry Reference
     * RFC 5545, 3.2.6, page 18
     * 
     * To specify reference to a directory entry associated with
     *     the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;DIR="ldap://example.com:6666/o=ABC%20Industries,
     *  c=US???(cn=Jim%20Dolittle)":mailto:jimdo@example.com
     */
    public DirectoryEntryReference getDirectoryEntryReference() { return (directoryEntryReference == null) ? null : directoryEntryReference.get(); }
    public ObjectProperty<DirectoryEntryReference> directoryEntryReferenceProperty()
    {
        if (directoryEntryReference == null)
        {
            directoryEntryReference = new SimpleObjectProperty<>(this, ParameterEnum.COMMON_NAME.toString());
        }
        return directoryEntryReference;
    }
    private ObjectProperty<DirectoryEntryReference> directoryEntryReference;
    public void setDirectoryEntryReference(DirectoryEntryReference directoryEntryReference)
    {
        if (directoryEntryReference != null)
        {
            directoryEntryReferenceProperty().set(directoryEntryReference);
        }
    }
    public void setDirectoryEntryReference(String content) { setDirectoryEntryReference(new DirectoryEntryReference(content)); }
    public T withDirectoryEntryReference(DirectoryEntryReference directoryEntryReference) { setDirectoryEntryReference(directoryEntryReference); return (T) this; }
    public T withDirectoryEntryReference(URI uri) { setDirectoryEntryReference(new DirectoryEntryReference(uri)); return (T) this; }
    public T withDirectoryEntryReference(String content) { setDirectoryEntryReference(content); return (T) this; }

    /**
     * SENT-BY
     * RFC 5545, 3.2.18, page 27
     * 
     * To specify the calendar user that is acting on behalf of
     * the calendar user specified by the property.
     * 
     * Example:
     * ORGANIZER;SENT-BY="mailto:sray@example.com":mailto:
     *  jsmith@example.com
     */
    public SentBy getSentBy() { return (sentBy == null) ? null : sentBy.get(); }
    public ObjectProperty<SentBy> sentByProperty()
    {
        if (sentBy == null)
        {
            sentBy = new SimpleObjectProperty<>(this, ParameterEnum.SENT_BY.toString());
        }
        return sentBy;
    }
    private ObjectProperty<SentBy> sentBy;
    public void setSentBy(SentBy sentBy)
    {
        if (sentBy != null)
        {
            sentByProperty().set(sentBy);
        }
    }
    public void setSentBy(String content) { setSentBy(new SentBy(content)); }
    public T withSentBy(SentBy sentBy) { setSentBy(sentBy); return (T) this; }
    public T withSentBy(URI uri) { setSentBy(new SentBy(uri)); return (T) this; }
    public T withSentBy(String content) { setSentBy(content); return (T) this; }    

    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyCalendarUserAddress(CharSequence contentLine)
    {
        super(contentLine);
    }

    // copy constructor
    public PropertyCalendarUserAddress(PropertyCalendarUserAddress<T> property)
    {
        super(property);
    }
    
    public PropertyCalendarUserAddress(URI value)
    {
        super(value);
    }
    
//    public PropertyCalendarUserAddress() { super(); }
}
