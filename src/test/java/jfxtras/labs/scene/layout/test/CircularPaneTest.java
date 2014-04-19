package jfxtras.labs.scene.layout.test;

import java.util.List;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;
import jfxtras.util.PlatformUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestFX is able to layout in the getRootNode() method a single node per class.
 * This would result in one class with usually one test method per to-be-tested layout, and thus is a LOT of classes.
 * By placing CircularPane in a presized Pane, it is possible to test different layouts in separate methods, all in a single class.
 * The drawback is that CircularPane is never tested stand alone, as the root node, so for each test it must be decided if we can put it as a test in here, or if it needs a test class on its own.
 * 
 * @author Tom Eugelink
 *
 */
public class CircularPaneTest extends JFXtrasGuiTest {

	@Override
	protected Parent getRootNode() {
		// use a pane to force the scene large enough, the circular pane is placed top-left
		Pane lPane = new Pane();
		lPane.setMinSize(600, 600);
		label = new Label();
		label.setLayoutY(lPane.getMinHeight() - 20);
		lPane.getChildren().add(label);		
		
		// add the circularePane (this is what we want to test)
		circularPane = new CircularPane();
		circularPane.setShowDebug(Color.GREEN);
		lPane.getChildren().add(circularPane);		
		circularPane.setStyle("-fx-border-color:black;");
		return lPane;
	}
	private CircularPane circularPane = null;
	private Label label = null;
	
	//@Test
	public void singleNode() {
		setLabel("singleNode");
		
		// insert 1 circle
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.getChildren().add(new javafx.scene.shape.Rectangle(30,30));
		});

		assertWH(circularPane, 42.42, 42.42);
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(6.5, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	//@Test
	public void twoNodes() {
		setLabel("twoNodes");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 42.42, 84.85);
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(6.4999999999999964, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.5, 48.78679656440357, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	//@Test
	public void twoNodesAt90Degrees() {
		setLabel("twoNodesAt90Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(90.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 84.85, 43);
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(48.78679656440357, 6.4999999999999964, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 6.5, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	//@Test
	public void twoNodesAt45Degrees() {
		setLabel("twoNodesAt45Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(45.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 72.42, 73);
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(36.55203820042827, 6.447961799571727, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.447961799571733, 36.55203820042827, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);

	}

	//@Test
	public void twoNodesAt30Degrees() {
		setLabel("twoNodesAt45Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 63.63, 79.16);
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(28.143398282201787, 7.065093410035196, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.856601717798213, 43.93490658996481, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	//@Test
	public void twoCirclesAt30Degrees() {
		setLabel("twoCirclesAt30Degrees");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setChildrenAreCircular(true);
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 45, 56);
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(30.5, 15.00961894323342, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(15.5, 40.99038105676658, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
	}

	//@Test
	public void threeNodes() {
		setLabel("threeNodes");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setStartAngle(30.0);
			for (int i = 0; i < 3; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 92, 92);
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(43.39339828220179, 9.53400449678966, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(43.3933982822018, 52.46599550321034, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(6.2132034355964265, 31.0, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	//@Test
	public void eightNodes() {
		setLabel("eightNodes");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 156, 156); 
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(62.99999999999999, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(103.1543289325507, 22.84567106744928, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(119.78679656440357, 62.99999999999999, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(103.15432893255073, 103.1543289325507, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(63.0, 119.78679656440357, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(22.845671067449295, 103.15432893255071, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 63.0, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(22.845671067449288, 22.845671067449295, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}


	//@Test
	public void eightCircles() {
		setLabel("eightCircles");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setChildrenAreCircular(true);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(15);
				circularPane.getChildren().add(c);
			}
		});

		assertWH(circularPane, 111, 111); 
		//AssertNode.generateSource("circularPane", circularPane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(55.49999999999999, 15.0, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(84.13782463805518, 26.862175361944818, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(96.0, 55.49999999999999, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(84.13782463805518, 84.13782463805516, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(55.5, 96.0, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(26.86217536194483, 84.13782463805518, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(15.0, 55.5, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(26.862175361944825, 26.86217536194483, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
	}
	
	// =============================================================================================================================================================================================================================
	// SUPPORT

	List<String> EXCLUDED_CLASSES = java.util.Arrays.asList(new String[]{"jfxtras.labs.scene.layout.CircularPane$Bead"});
	
	private void assertWH(CircularPane pane, double w, double h) {
		Assert.assertEquals(w, pane.getWidth(), 0.01);
		Assert.assertEquals(h, pane.getHeight(), 0.01);
	}
	
	private void setLabel(String s) {
		PlatformUtil.runAndWait( () -> {
			label.setText(s);
		});
	}

}
