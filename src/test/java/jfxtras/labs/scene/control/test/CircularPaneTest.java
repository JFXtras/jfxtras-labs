package jfxtras.labs.scene.control.test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.labs.test.TestUtil;
import jfxtras.test.NodeAssertXYWH;
import jfxtras.test.NodeAsserts;
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
		//NodeAssertXYWH.generateSource("circularPane", circularPane.getChildren(), java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"}));
		NodeAsserts nodeAsserts = new NodeAsserts();
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(0), 21.5, 21.213203435596427, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.doAssert();
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

		Assert.assertEquals(43, circularPane.getWidth(), 0.01);
		Assert.assertEquals(85, circularPane.getHeight(), 0.01);
		//NodeAssertXYWH.generateSource("circularPane", circularPane.getChildren(), java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"}));
		NodeAsserts nodeAsserts = new NodeAsserts();
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(0), 21.5, 21.213203435596427, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(1), 21.5, 63.78679656440357, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.doAssert();
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

		Assert.assertEquals(85, circularPane.getWidth(), 0.01);
		Assert.assertEquals(43, circularPane.getHeight(), 0.01);
		//NodeAssertXYWH.generateSource("circularPane", circularPane.getChildren(), java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"}));
		NodeAsserts nodeAsserts = new NodeAsserts();
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(0), 63.78679656440357, 21.5, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(1), 21.213203435596427, 21.5, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.doAssert();
	}

	@Test
	public void twoNodesAt45Degrees() {
		setLabel("twoNodesAt45Degrees");

		// insert 2 circles
		PlatformUtil.runAndWait( () -> {
			circularPane.getChildren().clear();
			circularPane.setStartAngle(45.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});
		TestUtil.waitForPaintPulse();

		Assert.assertEquals(73, circularPane.getWidth(), 0.01);
		Assert.assertEquals(73, circularPane.getHeight(), 0.01);
		//NodeAssertXYWH.generateSource("circularPane", circularPane.getChildren(), java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"}));
		NodeAsserts nodeAsserts = new NodeAsserts();
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(0), 51.55203820042827, 21.44796179957173, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(1), 21.447961799571733, 51.55203820042827, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.doAssert();
	}

	@Test
	public void twoNodesAt30Degrees() {
		setLabel("twoNodesAt45Degrees");

		// insert 2 circles
		PlatformUtil.runAndWait( () -> {
			circularPane.getChildren().clear();
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});
		TestUtil.waitForPaintPulse();

		Assert.assertEquals(65, circularPane.getWidth(), 0.01);
		Assert.assertEquals(81, circularPane.getHeight(), 0.01);
		//NodeAssertXYWH.generateSource("circularPane", circularPane.getChildren(), java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"}));
		NodeAsserts nodeAsserts = new NodeAsserts();
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(0), 43.14339828220179, 22.065093410035196, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(1), 21.856601717798213, 58.93490658996481, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.doAssert();
	}

	@Test
	public void threeNodes() {
		setLabel("threeNodes");

		// insert 2 circles
		PlatformUtil.runAndWait( () -> {
			circularPane.getChildren().clear();
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 3; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});
		TestUtil.waitForPaintPulse();

		Assert.assertEquals(92, circularPane.getWidth(), 0.01);
		Assert.assertEquals(92, circularPane.getHeight(), 0.01);
		//NodeAssertXYWH.generateSource("circularPane", circularPane.getChildren(), java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"}));
		NodeAsserts nodeAsserts = new NodeAsserts();
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(0), 58.39339828220179, 24.53400449678966, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(1), 58.3933982822018, 67.46599550321034, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(2), 21.213203435596427, 46.0, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.doAssert();	
	}

	@Test
	public void eightNodes() {
		setLabel("eightNodes");

		// insert 8 circles
		PlatformUtil.runAndWait( () -> {
			circularPane.getChildren().clear();
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});
		TestUtil.waitForPaintPulse();

		Assert.assertEquals(156, circularPane.getWidth(), 0.01);
		Assert.assertEquals(156, circularPane.getHeight(), 0.01);
		//NodeAssertXYWH.generateSource("circularPane", circularPane.getChildren(), java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"}));
		NodeAsserts nodeAsserts = new NodeAsserts();
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(0), 78.0, 21.213203435596427, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(1), 118.1543289325507, 37.84567106744928, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(2), 134.78679656440357, 78.0, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(3), 118.15432893255073, 118.1543289325507, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(4), 78.0, 134.78679656440357, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(5), 37.845671067449295, 118.15432893255071, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(6), 21.213203435596427, 78.0, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.add(new NodeAssertXYWH(circularPane.getChildren().get(7), 37.84567106744929, 37.845671067449295, 15.0, 15.0, javafx.scene.shape.Circle.class, 0.01));
		nodeAsserts.doAssert();	
	}

	
	// =============================================================================================================================================================================================================================
	// SUPPORT
	
	private void setLabel(String s) {
		PlatformUtil.runAndWait( () -> {
			label.setText(s);
		});

	}
	
	private double width(Node n) {
		return n.getLayoutBounds().getWidth() + n.getLayoutBounds().getMinX();
	}

	private double height(Node n) {
		return n.getLayoutBounds().getHeight() + n.getLayoutBounds().getMinY();
	}
}
