/**
 * BingAttributionLoaderTest.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.map.tile.attribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import jfxtras.labs.JavaFXPlatformAbstractTest;
import jfxtras.labs.map.ApiKeys;
import jfxtras.labs.map.tile.Attribution;
import jfxtras.labs.map.tile.bing.BingAttributionLoader;
import jfxtras.labs.map.tile.bing.BingImageMetaDataHandler;
import jfxtras.labs.map.tile.bing.BingMapMetaDataHandler;
import jfxtras.labs.map.tile.bing.BingMetaDataHandler;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Mario Schroeder
 */
public class BingAttributionLoaderTest extends JavaFXPlatformAbstractTest {
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