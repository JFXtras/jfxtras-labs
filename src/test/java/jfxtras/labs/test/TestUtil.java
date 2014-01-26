package jfxtras.labs.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.sun.javafx.tk.Toolkit;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import jfxtras.labs.util.PlatformUtil;

public class TestUtil {

	/**
	 * 
	 * @param n
	 * @return
	 */
	static public void scanHierarchy(Node n) {
		StringBuilder lStringBuilder = new StringBuilder();
		scanHierarchy(lStringBuilder, n, 0);
		if (lStringBuilder.length() > 0) {
			lStringBuilder.append("\n");
		}
		System.out.println(lStringBuilder.toString());
	}
	static public void scanHierarchy(StringBuilder stringBuilder, Node n, int offset) {
		if (stringBuilder.length() > 0) {
			stringBuilder.append("\n");
		}
		for (int i = 0; i < offset; i++) stringBuilder.append("|   ");		
		stringBuilder.append(n.getClass().getSimpleName());
		if (n.getId() != null) {
			stringBuilder.append(" id='" + n.getId() + "'");
		}
		if (n.getStyle() != null && n.getStyle().length() > 0) {
			stringBuilder.append(" style='" + n.getStyle() + "'");
		}
		if (n.getStyleClass() != null && n.getStyleClass().size() > 0) {
			stringBuilder.append(" styleClass='" + n.getStyleClass() + "'");
		}
		
		// scan children
		if (n instanceof Control) {
			Control lControl = (Control)n;
			Skin lSkin = lControl.getSkin();
			stringBuilder.append(" skin=" + (lSkin == null ? "null" : lSkin.getClass().getSimpleName()) );
			if (lSkin instanceof SkinBase) {
				SkinBase lSkinBase = (SkinBase)lSkin;
				for (Object lChild : lSkinBase.getChildren()) {
					scanHierarchy(stringBuilder, (Node)lChild, offset + 1);
				}
			}
		}
		else if (n instanceof Pane) {
			Pane lPane = (Pane)n;
			for (Node lChild : lPane.getChildren()) {
				scanHierarchy(stringBuilder, lChild, offset + 1);
			}
		}
	}
	
	/**
	 * 
	 */
	static public String quickFormatCalendarAsDate(Calendar value) {
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
		lSimpleDateFormat.applyPattern("yyyy-MM-dd");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	
	/**
	 * 
	 */
	static public String quickFormatCalendarAsDateTime(Calendar value) {
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
		lSimpleDateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	static public String quickFormatCalendarsAsDate(List<Calendar> value) {
		if (value == null) return "null";
		String s = "[";
		for (Calendar lCalendar : value)
		{
			if (s.length() > 1) s += ", ";
			s += quickFormatCalendarAsDate(lCalendar);
		}
		s += "]";
		return s;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	static public String quickFormatCalendarsAsDateTime(List<Calendar> value) {
		if (value == null) return "null";
		String s = "[";
		for (Calendar lCalendar : value)
		{
			if (s.length() > 1) s += ", ";
			s += quickFormatCalendarAsDateTime(lCalendar);
		}
		s += "]";
		return s;
	}
	
	
	/**
	 * 
	 * @param ms
	 */
	static public void sleep(int ms) {
		try { 
			Thread.sleep(ms); 
		} 
		catch (InterruptedException e) { 
			throw new RuntimeException(e); 
		}
	}
	
	/**
	 * 
	 */
	static public void waitForPaintPulse() {
		PlatformUtil.runAndWait( () -> {
			Toolkit.getToolkit().firePulse();
		});
	}
}
