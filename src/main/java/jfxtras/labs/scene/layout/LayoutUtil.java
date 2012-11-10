package jfxtras.labs.scene.layout;

import javafx.scene.Node;

public class LayoutUtil
{
	/**
	 * The layout constraints
	 *
	 */
	public static class C<T>
	{
		// minWidth
		public T minWidth(double value) { this.minWidth = value; return (T)this; }
		double minWidth = -1;
		
		// prefWidth
		public T prefWidth(double value) { this.prefWidth = value; return (T)this; }
		double prefWidth = -1;
		
		// maxWidth
		public T maxWidth(double value) { this.maxWidth = value; return (T)this; }
		double maxWidth = -1;
		
		// minHeight
		public T minHeight(double value) { this.minHeight = value; return (T)this; }
		double minHeight = -1;
		
		// prefHeight
		public T prefHeight(double value) { this.prefHeight = value; return (T)this; }
		double prefHeight = -1;
		
		// maxHeight
		public T maxHeight(double value) { this.maxHeight = value; return (T)this; }
		double maxHeight = -1;
	}
	
	/**
	 * 
	 * @param node
	 * @param c
	 */
	static public void applyConstraints(Node node, C c)
	{
		if (node instanceof javafx.scene.control.Control)
		{
			javafx.scene.control.Control lControl = (javafx.scene.control.Control)node;
			if (c.minWidth >= 0) lControl.setMinWidth(c.minWidth);
			if (c.prefWidth >= 0) lControl.setMinWidth(c.prefWidth);
			if (c.maxWidth >= 0) lControl.setMinWidth(c.maxWidth);
			if (c.minHeight >= 0) lControl.setMinHeight(c.minHeight);
			if (c.prefHeight >= 0) lControl.setMinHeight(c.prefHeight);
			if (c.maxHeight >= 0) lControl.setMinHeight(c.maxHeight);
		}
	}
	
	/**
	 * 
	 */
	static public void resetSizesToDefault(Node node)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if ( node instanceof javafx.scene.control.Button
		  || node instanceof javafx.scene.control.ToggleButton
		   )
		{
			javafx.scene.control.Control lControl = (javafx.scene.control.Control)node;
			lControl.setMaxWidth(lControl.getPrefWidth());
			lControl.setMaxHeight(lControl.getPrefHeight());
		}
	}
	
	/**
	 * 
	 */
	static public void overrideMaxWidth(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if ( node instanceof javafx.scene.control.Button
		  || node instanceof javafx.scene.control.ToggleButton
		  || node instanceof javafx.scene.control.CheckBox
		  || node instanceof javafx.scene.control.RadioButton
		  || node instanceof javafx.scene.control.ChoiceBox
		   )
		{
			javafx.scene.control.Control lControl = (javafx.scene.control.Control)node;
			lControl.setMaxWidth( c.maxWidth >= 0 ? c.maxWidth : Double.MAX_VALUE);
		}
	}
	
	/**
	 * 
	 */
	static public void overrideMaxHeight(Node node, C c)
	{
		// just to prevent problems
		if (node == null) return;
		
		// make things go away
		if ( node instanceof javafx.scene.control.Button
		  || node instanceof javafx.scene.control.ToggleButton
		   )
		{
			javafx.scene.control.Control lControl = (javafx.scene.control.Control)node;
			lControl.setMaxHeight( c.maxHeight >= 0 ? c.maxHeight : Double.MAX_VALUE);
		}
	}
	
}
