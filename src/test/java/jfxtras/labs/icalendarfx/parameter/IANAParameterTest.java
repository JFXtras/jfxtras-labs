package jfxtras.labs.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.IANAParameter;

public class IANAParameterTest
{
    @Test
    public void canParseIANAParameter()
    {
        String expectedContent = "Color=RED";
        IANAParameter.getRegisteredIANAParameters().add("COLOR");
        IANAParameter parameter = IANAParameter.parse(expectedContent);
        assertEquals(expectedContent, parameter.toContent());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canDetectUnregisteredIANAParameter()
    {
        String expectedContent = "COLOR=RED";
        IANAParameter parameter = IANAParameter.parse(expectedContent);
    }
}
