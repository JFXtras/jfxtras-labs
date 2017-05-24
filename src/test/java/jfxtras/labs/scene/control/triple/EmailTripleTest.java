package jfxtras.labs.scene.control.triple;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import jfxtras.labs.internal.scene.control.skin.triple.TripleEditTableSkin;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

public class EmailTripleTest extends JFXtrasGuiTest
{
	private TripleEditTable<Email> control;
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
    }
}
