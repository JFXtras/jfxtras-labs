package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.mocks.VTimeZoneMock;

public class VTimeZoneTest
{
    @Test
    public void canBuildBase()
    {        
        ObjectProperty<String> s = new SimpleObjectProperty<>("start");
        s.set(null);
        
        VTimeZoneMock builtComponent = new VTimeZoneMock()
                .withNonStandardProperty("X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au")
                .withIANAProperty("TESTPROP2:CASUAL")
                .withNonStandardProperty("X-TEST-OBJ:testid");
        builtComponent.propertySortOrder().put("X-ABC-MMSUBJ", 0);
        builtComponent.propertySortOrder().put("TESTPROP2", 1);
        builtComponent.propertySortOrder().put("X-TEST-OBJ", 2);
        String componentName = builtComponent.componentType().toString();
        
        String content = "BEGIN:" + componentName + System.lineSeparator() +
                "X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.org/mysubj.au" + System.lineSeparator() +
                "TESTPROP2:CASUAL" + System.lineSeparator() +
                "X-TEST-OBJ:testid" + System.lineSeparator() +
                "END:" + componentName;
                
        VTimeZoneMock madeComponent = new VTimeZoneMock(content);
        assertEquals(madeComponent, builtComponent);
        assertEquals(content, builtComponent.toContentLines());
    }
}
