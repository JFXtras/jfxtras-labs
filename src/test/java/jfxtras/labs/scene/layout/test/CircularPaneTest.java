package jfxtras.labs.scene.layout.test;

import java.util.List;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.labs.test.JFXtrasGuiTest;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
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
	
	@Test
	public void singleNode() {
		setLabel("singleNode");
		
		// insert 1 circle
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.getChildren().add(new javafx.scene.shape.Rectangle(30,30));
		});

		//generateSource(circularPane);
		assertWH(circularPane, 42.42, 42.42);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(6.2132034355964265, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void twoNodes() {
		setLabel("twoNodes");

		// insert 2 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 2; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 42.42, 84.85);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(6.2132034355964265, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.21320343559643, 48.63961030678928, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
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

		//generateSource(circularPane);
		assertWH(circularPane, 84.85, 42.42);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(48.63961030678928, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 6.21320343559643, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
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

		//generateSource(circularPane);
		assertWH(circularPane, 72.42, 72.42);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(36.21320343559643, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.2132034355964265, 36.21320343559643, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
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

		//generateSource(circularPane);
		assertWH(circularPane, 63.63, 79.16);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(27.426406871192853, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(6.213203435596425, 42.9555495773441, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
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

		//generateSource(circularPane);
		assertWH(circularPane, 45.00, 55.98);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(30.0, 15.0, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(15.0, 40.98076211353316, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
	}

	@Test
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

		//generateSource(circularPane);
		assertWH(circularPane, 79.16, 84.85);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(42.95554957734411, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(42.955549577344115, 48.63961030678928, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(6.2132034355964265, 27.426406871192853, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void eightNodes() {
		setLabel("eightNodes");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 153.29, 153.29); 
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(61.645975386273626, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(100.84286433256493, 22.44908643998233, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(117.07874733695084, 61.645975386273626, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(100.84286433256494, 100.84286433256491, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(61.64597538627363, 117.07874733695084, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(22.449086439982338, 100.84286433256494, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 61.64597538627363, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(22.44908643998233, 22.449086439982338, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}

	@Test
	public void eightNodesAnimated() {
		setLabel("eightNodesAnimated");

		circularPane.setOnAnimateInFinished((event) -> {
			// these assertions must be identical to those in eightNodes
			assertWH(circularPane, 153.29, 153.29); 
			new AssertNode(circularPane.getChildren().get(0)).assertXYWH(61.645975386273626, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(1)).assertXYWH(100.84286433256493, 22.44908643998233, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(2)).assertXYWH(117.07874733695084, 61.645975386273626, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(3)).assertXYWH(100.84286433256494, 100.84286433256491, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(4)).assertXYWH(61.64597538627363, 117.07874733695084, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(5)).assertXYWH(22.449086439982338, 100.84286433256494, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 61.64597538627363, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
			new AssertNode(circularPane.getChildren().get(7)).assertXYWH(22.44908643998233, 22.449086439982338, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		});
		
		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
			circularPane.setAnimationInterpolation(CircularPane::animateOverTheArc);
		});
	}

	@Test
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

		//generateSource(circularPane);
		assertWH(circularPane, 108.39, 108.39); 
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(54.19688894629129, 15.0, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(81.91327492162989, 26.48050297095269, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(93.39377789258259, 54.19688894629129, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(81.9132749216299, 81.91327492162989, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(54.196888946291295, 93.39377789258259, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(26.480502970952696, 81.91327492162989, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(15.0, 54.196888946291295, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(26.480502970952692, 26.480502970952696, 15.0, 15.0, 0.01).assertClass(javafx.scene.shape.Circle.class);
	}
	
	@Test
	public void eightNodesGap() {
		setLabel("eightNodesGap");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setGap(10.0);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 179.4232100700748, 179.4232100700748);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(74.71160503503738, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(123.14728930644155, 26.275920763633223, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(143.21000663447836, 74.71160503503738, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(123.14728930644156, 123.14728930644154, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(74.7116050350374, 143.21000663447836, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(26.275920763633238, 123.14728930644156, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 74.7116050350374, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(26.27592076363323, 26.275920763633238, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
	}


	@Test
	public void eightNodesForcedDiameter() {
		setLabel("eightNodesForcedDiameter");

		// insert 8 circles
		TestUtil.runThenWaitForPaintPulse( () -> {
			circularPane.setDiameter(120.0);
			for (int i = 0; i < 8; i++) {
				javafx.scene.shape.Rectangle c = new javafx.scene.shape.Rectangle(30,30);
				circularPane.getChildren().add(c);
			}
		});

		//generateSource(circularPane);
		assertWH(circularPane, 120.0, 120.0);
		new AssertNode(circularPane.getChildren().get(0)).assertXYWH(44.99999999999999, 6.2132034355964265, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(1)).assertXYWH(72.42640687119285, 17.573593128807143, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(2)).assertXYWH(83.78679656440357, 44.99999999999999, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(3)).assertXYWH(72.42640687119285, 72.42640687119285, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(4)).assertXYWH(45.0, 83.78679656440357, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(5)).assertXYWH(17.57359312880715, 72.42640687119285, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(6)).assertXYWH(6.2132034355964265, 45.0, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
		new AssertNode(circularPane.getChildren().get(7)).assertXYWH(17.573593128807147, 17.57359312880715, 30.0, 30.0, 0.01).assertClass(javafx.scene.shape.Rectangle.class);
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

	private void generateSource(Pane pane) {
		System.out.println("> " + label.getText()); 
		System.out.println("assertWH(circularPane, " + pane.getWidth() + ", " + pane.getHeight() + ");");
		AssertNode.generateSource("circularPane", pane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASS);
		TestUtil.sleep(3000);
	}
}
