package jfxtras.labs.map.tile.attribution;

import java.util.List;
import jfxtras.labs.map.ApiKeys;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mario Schröder
 */
public class BingAttributionLoaderTest {

    /**
     * Test of load method, of class BingAttributionLoader.
     */
    @Test
    public void testLoadMapMetadata() {

        BingMetaDataHandler handler = new BingMapMetaDataHandler(ApiKeys.Bing.toString());

        BingAttributionLoader classUnderTest = new BingAttributionLoader(handler);
        List<Attribution> result = classUnderTest.load();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Test of load method, of class BingAttributionLoader.
     */
    @Test
    public void testLoadImageMetadata() {

        BingMetaDataHandler handler = new BingImageMetaDataHandler(ApiKeys.Bing.toString());

        BingAttributionLoader classUnderTest = new BingAttributionLoader(handler);
        List<Attribution> result = classUnderTest.load();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() > 2);
    }
}