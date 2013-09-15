/**
 * TileImageComparatorTest.java
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

package jfxtras.labs.map.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class TileImageComparatorTest {
    
    private TileImageComparator classUnderTest;
    
    private TileImage left, right;
    
    @Before
    public void setUp() throws Exception {
        classUnderTest = new TileImageComparator();
        left = new TileImage(null, 0, 0);
        right = new TileImage(null, 0, 0);
    }

    @Test
    public void testCompare() {

        assertEquals(0, classUnderTest.compare(left, right));
        
        left.setTileX(-1);
        assertEquals(-1, classUnderTest.compare(left, right));
        
        left.setTileX(1);
        assertEquals(1, classUnderTest.compare(left, right));
        
        left.setTileX(0);
        assertEquals(0, classUnderTest.compare(left, right));
        
        left.setTileX(0);
        left.setTileY(-1);
        assertEquals(-1, classUnderTest.compare(left, right));
    }

    @Test
    public void testSorting(){
        //construct 2x2 square
        right.setTileX(1);
        TileImage third = new TileImage(null, 0, 0);
        third.setTileY(1);
        TileImage fourth = new TileImage(null, 0, 0);
        fourth.setTileX(1);
        fourth.setTileY(1);
        
        List<TileImage> list = new ArrayList<>();
        list.add(third);
        list.add(right);
        list.add(fourth);
        list.add(left);
        
        Collections.sort(list, classUnderTest);
        
        assertEquals(0, list.get(0).getTileX());
        assertEquals(0, list.get(0).getTileY());
        assertEquals(1, list.get(1).getTileX());
        assertEquals(0, list.get(1).getTileY());
        assertEquals(0, list.get(2).getTileX());
        assertEquals(1, list.get(2).getTileY());
        assertEquals(1, list.get(3).getTileX());
        assertEquals(1, list.get(3).getTileY());
        
    }
}
