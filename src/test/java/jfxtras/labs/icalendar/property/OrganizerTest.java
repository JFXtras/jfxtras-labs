package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.relationship.Organizer;

public class OrganizerTest
{
    @Test
    public void canParseOrganizer() throws URISyntaxException
    {
        String content = "ORGANIZER;CN=John Smith:mailto:jsmith@example.com";
        Organizer madeProperty = new Organizer(content);
        Organizer expectedProperty = new Organizer("mailto:jsmith@example.com")
                .withCommonName("John Smith");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContentLine());
    }
    
    @Test
    public void canParseOrganizer2() throws URISyntaxException
    {
        String content = "ORGANIZER;CN=John Smith;DIR=\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\";LANGUAGE=en;SENT-BY=\"mailto:sray@example.com\":mailto:jsmith@example.com";
        Organizer madeProperty = new Organizer(content);
        Organizer expectedProperty = new Organizer("mailto:jsmith@example.com")
                .withCommonName("John Smith")
                .withDirectoryEntryReference("ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)")
                .withLanguage("en")
                .withSentBy("mailto:sray@example.com");
//        System.out.println(expectedProperty.toContentLine());
//        System.exit(0);
//        
//        System.out.println(expectedProperty.toContentLine());
//        System.out.println(madeProperty.toContentLine());
//        URI u = expectedProperty.getDirectoryEntryReference().getValue();
//        URI u2 = new URI("ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)");
//        uriSplitter(u2);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContentLine());
    }
    
    private void uriSplitter(URI uri)
    {
        URI u = uri;
          System.out.println("The URI is " + u);
          if (u.isOpaque( )) {
            System.out.println("This is an opaque URI."); 
            System.out.println("The scheme is " + u.getScheme( ));        
            System.out.println("The scheme specific part is " 
             + u.getSchemeSpecificPart( ));        
            System.out.println("The fragment ID is " + u.getFragment( ));        
          }
          else {
            System.out.println("This is a hierarchical URI."); 
            System.out.println("The scheme is " + u.getScheme( ));        
            try {       
              u = u.parseServerAuthority( );
              System.out.println("The host is " + u.getUserInfo( ));        
              System.out.println("The user info is " + u.getUserInfo( ));        
              System.out.println("The port is " + u.getPort( ));        
            }
            catch (URISyntaxException ex) {
              // Must be a registry based authority
              System.out.println("The authority is " + u.getAuthority( ));        
            }
            System.out.println("The path is " + u.getPath( ));        
            System.out.println("The query string is " + u.getQuery( ));        
            System.out.println("The fragment ID is " + u.getFragment( )); 
          } // end else       
          System.out.println(u.getSchemeSpecificPart() );
        System.out.println(u.toASCIIString() );
    }
}
