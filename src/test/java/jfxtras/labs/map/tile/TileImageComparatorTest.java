package jfxtras.labs.map.tile;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
