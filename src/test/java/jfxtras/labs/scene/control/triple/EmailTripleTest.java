package jfxtras.labs.scene.control.triple;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import jfxtras.labs.internal.scene.control.skin.edittable.triple.TripleEditTableSkin;
import jfxtras.labs.scene.control.edittable.triple.Triple;
import jfxtras.labs.scene.control.edittable.triple.TripleEditTable;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

public class EmailTripleTest extends JFXtrasGuiTest
{
	private TripleEditTable<Email,String,String,Boolean> control;
    @Override
    public Parent getRootNode()
    {
    	control = new EmailEditTable();
    	return control;
    }
    
    @Test
    public void canCreateEmptyControl()
    {
    	Control node = (Control) find("#emailEditTable");
    	ObservableList<Triple> tableList = ((TripleEditTableSkin) node.getSkin()).getTableList();
    	assertEquals(0, tableList.size()); // has no items until beanList is set
		double w = 741.0;
		double h = 120.0;
//		AssertNode.generateSource("n", node, null, false, jfxtras.test.AssertNode.A.XYWH);
		new AssertNode(node).assertXYWH(0.0, 0.0, w, h, 0.01);
    }
    
    @Test
    public void canDisplayElements()
    {
    	Control node = (Control) find("#emailEditTable");
    	ObservableList<Triple> tableList = ((TripleEditTableSkin) node.getSkin()).getTableList();

    	List<Email> beanList = new ArrayList<>();
    	beanList.add(new Email("Work", "me@work.com", true));
    	TestUtil.runThenWaitForPaintPulse( () -> control.setBeanList(beanList));
    	assertEquals(2, tableList.size()); // 1 item is empty
    	Triple t = tableList.get(0);
		assertEquals("Work", t.getName());
    	assertEquals("me@work.com", t.getValue());
    	assertEquals(true, t.isPrimary());
    }
    
    @Test
    public void canAddElements()
    {
    	Control node = (Control) find("#emailEditTable");
    	ObservableList<Triple> tableList = ((TripleEditTableSkin) node.getSkin()).getTableList();

    	List<Email> beanList = new ArrayList<>();
    	TestUtil.runThenWaitForPaintPulse( () -> control.setBeanList(beanList));
    	assertEquals(1, tableList.size()); // 1 item is empty

    	TestUtil.runThenWaitForPaintPulse( () -> 
	    	tableList.add(0,
	    			new Triple()
	    				.withName("Personal")
	    				.withValue("me@home.com")
	    				.withPrimary(false)
	    				));
    	assertEquals(2, tableList.size());
    	Triple t = tableList.get(0);
		assertEquals("Personal", t.getName());
    	assertEquals("me@home.com", t.getValue());
    	assertEquals(false, t.isPrimary());
    }
    
    @Test
    public void canCatchInvalidValue()
    {
    }
    
    @Test
    public void canCatchInvalidName()
    {
    }
}
