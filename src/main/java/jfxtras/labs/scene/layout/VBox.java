package jfxtras.labs.scene.layout;

import java.util.WeakHashMap;

import javafx.scene.Node;

/**
 * A drop-in replace ment for JavaFX's VBox using layout constraints.
 * So instead of:
 * 	VBox lVBox = new VBox(5.0);		
 * 	Button b1 = new Button("short");
 * 	lVBox.getChildren().add(b1);
 * 	VBox.setVgrow(b1, Priority.ALWAYS); 
 *
 * You can write:
 * 	VBox lVBox = new VBox(5.0);		
 *  lVBox.add(new Button("short"), new VBox.C().vgrow(Priority.ALWAYS));
 * 
 * @author Tom Eugelink
 *
 */
public class VBox extends javafx.scene.layout.VBox
{
	// ========================================================================================================================================================
	// Constructors
	
	/**
	 * 
	 */
	public VBox()
	{
		super();
	}
	
	/**
	 * 
	 * @param spacing
	 */
	public VBox(double spacing)
	{
		super(spacing);
	}
	
	
	// ========================================================================================================================================================
	// Layout constraints
	/**
	 * The layout constraints
	 *
	 */
	public static class C
	{
		public C vgrow(javafx.scene.layout.Priority value) { this.priority = value; return this; }
		private javafx.scene.layout.Priority priority = null;
	}
	
	/**
	 * The collection of layout constraints
	 */
	private WeakHashMap<Node, C> cMap = new WeakHashMap<>();
	
	/**
	 * Add
	 */
	public void add(Node node, C c)
	{
		// remember constraints
		cMap.put(node, c);
		
		// add node
		getChildren().add(node);
		
		// apply the constraints
		applyConstraints(node);
	}

	/**
	 * Remove a node completely
	 * @param node
	 */
	public void remove(Node node)
	{
		// remove node
		getChildren().remove(node);
		
		// remove constraints
		cMap.remove(node);
	}

	/**
	 * set constraint without adding the node (in case the node might end up here because of an animation or something) 
	 */
	public void setConstraint(Node node, C c)
	{
		// remember constraints
		cMap.put(node, c);
	}

	/**
	 * Remove a constraint, not the node.
	 * @param node
	 */
	public void removeConstraintsFor(Node node)
	{
		cMap.remove(node);
	}
	
	/**
	 * Remove a node, not the constraints.
	 * @param node
	 */
	public void removeNode(Node node)
	{
		getChildren().remove(node);
	}
	
	/**
	 * 
	 * @param node
	 */
	private void applyConstraints(Node node)
	{
		// get constraints
		C lC = cMap.get(node);
		if (lC == null) throw new IllegalStateException("No constraints for node " + node);
		
		// sanatize the node
		LayoutUtil.sanatizeNodeReset(node);
		
		// apply constraints
		if (lC.priority != null) 
		{
			LayoutUtil.sanatizeNodeSetMaxWidth(node);
			javafx.scene.layout.VBox.setVgrow(node, lC.priority);
		}
	}
}
