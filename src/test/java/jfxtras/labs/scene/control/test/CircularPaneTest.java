package jfxtras.labs.scene.control.test;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.labs.test.TestUtil;
import jfxtras.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;

public class CircularPaneTest extends JFXtrasGuiTest {

	@Override
	protected Parent getRootNode() {
		// use a pane to force the scene large enough, but the child is placed top-left
		Pane lPane = new Pane();
		lPane.setMinSize(600, 600);
		label = new Label();
		label.setLayoutY(lPane.getMinHeight() - 20);
		lPane.getChildren().add(label);		
		
		// add the circularePane (this is what we want to test)
		circularPane = new CircularPane();
		circularPane.setShowDebug(Color.GREEN);
		circularPane.getChildren().add(new javafx.scene.shape.Circle(15));
		lPane.getChildren().add(circularPane);		
		circularPane.setStyle("-fx-border-color:black;");
		return lPane;
	}
	private CircularPane circularPane = null;
	private Label label = null;
	
	@Test
	public void singleNode() {
		setLabel("singleNode");
		
		Assert.assertEquals(43, circularPane.getWidth(), 0.01);
		Assert.assertEquals(43, circularPane.getHeight(), 0.01);
		//generateNodeAsserts(circularPane.getChildren());
		List<NodeAssert> lNodeAsserts = Arrays.asList(new NodeAssert[]{ new NodeAssert(0, 21.5, 21.213203435596427, 15.0, 15.0, javafx.scene.shape.Circle.class)
		});
		
		for (NodeAssert lNodeAssert : lNodeAsserts) {
			lNodeAssert.assertNode(circularPane.getChildren(), 0.01);
		}
	}

	@Test
	public void twoNodes() {
		setLabel("twoNodes");

		// insert 2 circles
		PlatformUtil.runAndWait( () -> {
			circularPane.getChildren().clear();
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});
		TestUtil.waitForPaintPulse();

		//System.out.println(circularPane.getWidth());
		//System.out.println(circularPane.getHeight());
		// TODO: this should be 43: Assert.assertEquals(43, circularPane.getWidth(), 0.01);
		Assert.assertEquals(85, circularPane.getHeight(), 0.01);
		//generateNodeAsserts(circularPane.getChildren());
		List<NodeAssert> lNodeAsserts = Arrays.asList(new NodeAssert[]{  new NodeAssert(0, 21.5, 21.213203435596427, 15.0, 15.0, javafx.scene.shape.Circle.class)
	    	, new NodeAssert(1, 21.5, 63.78679656440357, 15.0, 15.0, javafx.scene.shape.Circle.class)
		});
		
		for (NodeAssert lNodeAssert : lNodeAsserts) {
			lNodeAssert.assertNode(circularPane.getChildren(), 0.01);
		}
	}

	@Test
	public void twoNodesAt90Degrees() {
		setLabel("twoNodesAt90Degrees");

		// insert 2 circles
		PlatformUtil.runAndWait( () -> {
			circularPane.getChildren().clear();
			circularPane.setStartAngle(90.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});
		TestUtil.waitForPaintPulse();

		//System.out.println(circularPane.getWidth());
		//System.out.println(circularPane.getHeight());
		Assert.assertEquals(85, circularPane.getWidth(), 0.01);
		// TODO: this should be 43: Assert.assertEquals(43, circularPane.getHeight(), 0.01);
		//generateNodeAsserts(circularPane.getChildren());
		List<NodeAssert> lNodeAsserts = Arrays.asList(new NodeAssert[]{  new NodeAssert(0, 63.78679656440357, 21.5, 15.0, 15.0, javafx.scene.shape.Circle.class)
	    , new NodeAssert(1, 21.213203435596427, 21.5, 15.0, 15.0, javafx.scene.shape.Circle.class)
		});
		
		for (NodeAssert lNodeAssert : lNodeAsserts) {
			lNodeAssert.assertNode(circularPane.getChildren(), 0.01);
		}
	}

	@Test
	public void basicLayout() {
		setLabel("basicLayout");

		// insert 8 circles
		PlatformUtil.runAndWait( () -> {
			circularPane.getChildren().clear();
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});
		TestUtil.waitForPaintPulse();

		Assert.assertEquals(154, circularPane.getWidth(), 0.01);
		Assert.assertEquals(154, circularPane.getHeight(), 0.01);
		//generateNodeAsserts(circularPane.getChildren());
		List<NodeAssert> lNodeAsserts = Arrays.asList(new NodeAssert[]{ new NodeAssert(0, 77.0, 21.213203435596427, 15.0, 15.0, javafx.scene.shape.Circle.class)
	        , new NodeAssert(1, 116.44722215136416, 37.552777848635834, 15.0, 15.0, javafx.scene.shape.Circle.class)
	        , new NodeAssert(2, 132.78679656440357, 77.0, 15.0, 15.0, javafx.scene.shape.Circle.class)
	        , new NodeAssert(3, 116.44722215136417, 116.44722215136414, 15.0, 15.0, javafx.scene.shape.Circle.class)
	        , new NodeAssert(4, 77.0, 132.78679656440357, 15.0, 15.0, javafx.scene.shape.Circle.class)
	        , new NodeAssert(5, 37.55277784863584, 116.44722215136416, 15.0, 15.0, javafx.scene.shape.Circle.class)
	        , new NodeAssert(6, 21.213203435596427, 77.0, 15.0, 15.0, javafx.scene.shape.Circle.class)
	        , new NodeAssert(7, 37.55277784863584, 37.55277784863584, 15.0, 15.0, javafx.scene.shape.Circle.class)
		});
		
		for (NodeAssert lNodeAssert : lNodeAsserts) {
			lNodeAssert.assertNode(circularPane.getChildren(), 0.01);
		}
	}

	
	// =============================================================================================================================================================================================================================
	// SUPPORT
	
	private void setLabel(String s) {
		PlatformUtil.runAndWait( () -> {
			label.setText(s);
		});

	}
	
	class NodeAssert {
		public NodeAssert(int idx, double x, double y, double w, double h, Class clazz) {
			this.idx = idx;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.clazz = clazz;
		}
		final int idx;
		final double x;
		final double y;
		final double w;
		final double h;
		final Class clazz;
		
		public void assertNode(List<Node> nodes, double accuracy) {
			Node lNode = nodes.get(this.idx);
			String lDescription = "idx " + this.idx;
			Assert.assertEquals(lDescription, this.clazz, lNode.getClass());
			Assert.assertEquals(lDescription, this.x, lNode.getLayoutX(), accuracy);
			Assert.assertEquals(lDescription, this.y, lNode.getLayoutY(), accuracy);
			Assert.assertEquals(lDescription, this.w, width(lNode), accuracy);
			Assert.assertEquals(lDescription, this.h, height(lNode), accuracy);
		}
	}
	
	private void generateNodeAsserts(List<Node> nodes) {
		System.out.print("List<NodeAssert> lNodeAsserts = Arrays.asList(new NodeAssert[]{ ");
		int idx = 0;
		for (Node lNode : nodes) {
			if (lNode.getClass().getName().endsWith(".CircularPane$Bead")) {
				continue;
			}
			if (idx > 0) {
				System.out.print("    , ");
			}
			System.out.println("new NodeAssert(" + idx + ", "+ lNode.getLayoutX() + ", " + lNode.getLayoutY() + ", " + width(lNode) + ", " + height(lNode) + ", " + lNode.getClass().getName() + ".class)");
			idx++;
		}
		System.out.println("    });");
		TestUtil.sleep(3000);
	}
	
	private double width(Node n) {
		return n.getLayoutBounds().getWidth() + n.getLayoutBounds().getMinX();
	}

	private double height(Node n) {
		return n.getLayoutBounds().getHeight() + n.getLayoutBounds().getMinY();
	}
}
