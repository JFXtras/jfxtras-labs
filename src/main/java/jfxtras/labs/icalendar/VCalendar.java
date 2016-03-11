package jfxtras.labs.icalendar;

import java.util.Collection;

import jfxtras.labs.icalendar.components.VComponent;

/**
 * Parent calendaring and scheduling core object.
 * It contains two required properties, one optional property,
 * and collections of calendar components.
 * 
 *        calprops   = *(
                  ;
                  ; The following are REQUIRED,
                  ; but MUST NOT occur more than once.
                  ;
                  prodid / version /
                  ;
                  ; The following are OPTIONAL,
                  ; but MUST NOT occur more than once.
                  ;
                  calscale / method /
                  ;
                  ; The following are OPTIONAL,
                  ; and MAY occur more than once.
                  ;
                  x-prop / iana-prop
                  ;
                  )

       component  = 1*(eventc / todoc / journalc / freebusyc /
                    timezonec / iana-comp / x-comp)

       iana-comp  = "BEGIN" ":" iana-token CRLF
                    1*contentline
                    "END" ":" iana-token CRLF

       x-comp     = "BEGIN" ":" x-name CRLF
                    1*contentline
                    "END" ":" x-name CRLF
 * 
 * @author David Bal
 *
 */
public class VCalendar
{
    private String version; // required
    
    private String prodID; // required
    
    private String calScale; // optional
    
    private Collection<VComponent<?>> vComponents; // VEVENT, VTODO, VJOURNAL
}
