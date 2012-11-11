package jfxtras.labs.scene.layout;

import java.util.WeakHashMap;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;

/**
 * A drop-in replacement for JavaFX's GridPane using layout constraints for the nodes.
 * You should still use the RowConstraints(Builder) and ColumnConstraints(Builder)
 *
 * This class is not a reimplementation of GridPane, but only applies a different API.
 *   
 * @author Tom Eugelink
 *
 */
public class GridPane extends javafx.scene.layout.GridPane
{
	// ========================================================================================================================================================
	// Constructors
	
	/**
	 * 
	 */
	public GridPane()
	{
		super();
		construct();
	}
	
	/**
	 * 
	 */
	private void construct()
	{
		getChildren().addListener(new ListChangeListener<Node>()
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Node> changes)
			{
				while (changes.next())
				{
					for (Node lNode : changes.getAddedSubList())
					{
						C lC = cMap.get(lNode);
						if (lC != null) lC.apply(lNode);
					}
				}
			}
		});
	}
	
	
	// ========================================================================================================================================================
	// Properties
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public GridPane withHGap(double value)
	{
		super.setHgap(value);
		return this;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public GridPane withVGap(double value)
	{
		super.setVgap(value);
		return this;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public GridPane withPadding(Insets value)
	{
		super.setPadding(value);
		return this;
	}
	
	
	// ========================================================================================================================================================
	// Layout constraints
	
	/**
	 * The layout constraints
	 *
	 */
	public static class C extends GenericLayoutConstraints.C<C>
	{
		// row
		public C row(int value) { this.row = value; return this; }
		private int row = -1;
		
		// col
		public C col(int value) { this.col = value; return this; }
		private int col = -1;
		
		// rowSpan
		public C rowSpan(int value) { this.rowSpan = value; return this; }
		private int rowSpan = 1;
		
		// colSpan
		public C colSpan(int value) { this.colSpan = value; return this; }
		private int colSpan = 1;
		
		// margin
		public C margin(javafx.geometry.Insets value) { this.margin = value; return this; }
		private javafx.geometry.Insets margin = null;
		private javafx.geometry.Insets marginReset = null;

		// halignment
		public C halignment(javafx.geometry.HPos value) { this.halignment = value; return this; }
		private javafx.geometry.HPos halignment = null;
		private javafx.geometry.HPos halignmentReset = null;
		
		// hgrow
		public C hgrow(javafx.scene.layout.Priority value) { this.hgrow = value; return this; }
		private javafx.scene.layout.Priority hgrow = null;
		private javafx.scene.layout.Priority hgrowReset = null;

		// valignment
		public C valignment(javafx.geometry.VPos value) { this.valignment = value; return this; }
		private javafx.geometry.VPos valignment = null;
		private javafx.geometry.VPos valignmentReset = null;
		
		// hgrow
		public C vgrow(javafx.scene.layout.Priority value) { this.vgrow = value; return this; }
		private javafx.scene.layout.Priority vgrow = null;
		private javafx.scene.layout.Priority vgrowReset = null;
		
		/**
		 * @param node
		 */
		protected void rememberResetValues(Node node)
		{
			super.rememberResetValues(node);
			marginReset = javafx.scene.layout.GridPane.getMargin(node);
			halignmentReset = javafx.scene.layout.GridPane.getHalignment(node);
			hgrowReset = javafx.scene.layout.GridPane.getHgrow(node);
			valignmentReset = javafx.scene.layout.GridPane.getValignment(node);
			vgrowReset = javafx.scene.layout.GridPane.getVgrow(node);
		}
		
		/**
		 * 
		 * @param node
		 */
		protected void apply(Node node)
		{
			// sanatize the node
			super.apply(node);

			// apply constraints
			if (row < 0) throw new IllegalArgumentException("You must set the row index");
			javafx.scene.layout.GridPane.setRowIndex(node, row);
			if (col < 0) throw new IllegalArgumentException("You must set the col index");
			javafx.scene.layout.GridPane.setColumnIndex(node, col);
			javafx.scene.layout.GridPane.setRowSpan(node, rowSpan);
			javafx.scene.layout.GridPane.setColumnSpan(node, colSpan);
			javafx.scene.layout.GridPane.setMargin(node, margin != null ? margin : marginReset);
			if (hgrow != null) GenericLayoutConstraints.overrideMaxHeight(node, this);
			javafx.scene.layout.GridPane.setHalignment(node, halignment != null ? halignment : halignmentReset);
			javafx.scene.layout.GridPane.setHgrow(node, hgrow != null ? hgrow : hgrowReset);
			javafx.scene.layout.GridPane.setValignment(node, valignment != null ? valignment : valignmentReset);
			if (vgrow != null) GenericLayoutConstraints.overrideMaxWidth(node, this);
			javafx.scene.layout.GridPane.setVgrow(node, vgrow != null ? vgrow : vgrowReset);
		}
	}
	
	/**
	 * The collection of layout constraints
	 */
	private WeakHashMap<Node, C> cMap = new WeakHashMap<>();
	
	/**
	 * Add
	 */
	public void add(Node node)
	{
		// add node
		getChildren().add(node);
	}

	/**
	 * Add
	 */
	public void add(Node node, C c)
	{
		// remember constraints
		cMap.put(node, c);
		c.rememberResetValues(node);
		
		// add node
		getChildren().add(node);
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
		c.rememberResetValues(node);
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
}
