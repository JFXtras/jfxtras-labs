/**
 * Copyright (c) 2011, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.internal.scene.control.skin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.labs.internal.scene.control.behavior.AgendaBehavior;
import jfxtras.labs.scene.control.Agenda;
import jfxtras.labs.scene.control.Agenda.Appointment;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * @author Tom Eugelink
 *
 */
public class AgendaWeekSkin extends SkinBase<Agenda, AgendaBehavior>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public AgendaWeekSkin(Agenda control)
	{
		super(control, new AgendaBehavior(control));
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{	
		refreshLocale();
		
		// setup component
		createNodes();
		
		// react to changes in the locale 
		getSkinnable().localeProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				refreshLocale();
				
				// paint
				paint();
			} 
		});
		
		// react to changes in the appointments 
		getSkinnable().appointments().addListener(new ListChangeListener<Agenda.Appointment>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Appointment> arg0)
			{
				// just repaint
				paint();
			} 
		});
	}
	
	// ==================================================================================================================
	// PROPERTIES
	

	// ==================================================================================================================
	// DRAW
	
	// this is needed to react to resizing of the control
    @Override public void layoutChildren() {
        if (lastWidth != getSkinnable().getWidth()) {
            paint();
        }
        super.layoutChildren();
    }
    double lastWidth = 0;

    /**
	 * construct the nodes
	 */
	private void createNodes()
	{
        lDays = new Group();
        lScrollPane = ScrollPaneBuilder.create()
	        .prefWidth(getSkinnable().getWidth())
	        .prefHeight(getSkinnable().getHeight() - 100)
	        .layoutY(50)
	        .content(lDays)
	        .hbarPolicy(ScrollBarPolicy.NEVER)
	        .pannable(true)
	        .build();
        borderPane.setCenter(lScrollPane);
		getChildren().add(borderPane);
		paint();
	}
	BorderPane borderPane = new BorderPane();
	
	/**
	 * 
	 */
	private void paint()
	{
		// !!!!!!!!!!!!!!!!!!!!
		// BEWARE: this is scratch code, just hacking until all the graphical elements are in place and then MAJOR refactoring.
		// TODO: move all kinds of stuff to CSS
		
		System.out.println("paint()");
		lastWidth = getSkinnable().getWidth();
		
		double lHeaderHeight = new Text("XXX").getBoundsInParent().getHeight() + 10; // 10 is margin
        Group lHeader = new Group();
        lHeader.getChildren().add(RectangleBuilder.create().width(getSkinnable().getWidth()).height(lHeaderHeight).stroke(null).fill(Color.WHITE).build()); // blow up to full width
        borderPane.setTop(lHeader);
		
		// 
        {
//	        Group lDays = new Group();
        	lDays.getChildren().clear();

        	// determine sizes
	        double lTimeColumnWidth = new Text("88:88").getBoundsInParent().getWidth() + 25; // 25 is whitespace
	        double lDaycolumnWidth = (getSkinnable().getWidth() - lTimeColumnWidth - 12) / 7; // TODO use lScrollPane.viewportBoundsProperty().get().getWidth() and lose the 12 px for the scrollbar
        	if (lScrollPane.viewportBoundsProperty().get() != null) System.out.println(lScrollPane.viewportBoundsProperty().get().getWidth());
	        int lHourHeigh = 40;
	
	        // background
	        final Rectangle IBOUNDS = new Rectangle(0, 0, lTimeColumnWidth + (7 * lDaycolumnWidth), 24 * lHourHeigh);
//	        IBOUNDS.setOpacity(0.0);
	        IBOUNDS.setStroke(null);
	        IBOUNDS.setFill(Color.WHITE);
	        lDays.getChildren().add(IBOUNDS);
	        
	
	        // draw times
	        for (int lHour = 0; lHour < 24; lHour++)
	        {
	        	// hour
	        	{
		        	Line l = new Line(0, lHour * lHourHeigh, IBOUNDS.getWidth(), lHour * lHourHeigh);
		        	l.setStroke(Color.LIGHTGRAY);
		        	lDays.getChildren().add(l);
	        	}
	        	// text
	        	{
	        		Text t = new Text(0, lHour * lHourHeigh, lHour + ":00");	        		
		        	t.setStroke(Color.LIGHTGRAY);
		            t.setStroke(null);
		            t.setFontSmoothingType(FontSmoothingType.LCD);
		            t.setTranslateY(t.getBoundsInParent().getHeight());
		            t.setTranslateX(lTimeColumnWidth - t.getBoundsInParent().getWidth() - 10); // 10 is margin
		        	lDays.getChildren().add(t);
	        	}
	        	// halfhour
	        	{
		        	Line l = new Line(lTimeColumnWidth, (lHour + 0.5) * lHourHeigh, IBOUNDS.getWidth(), (lHour + 0.5) * lHourHeigh);
		        	l.setStroke(Color.LIGHTGRAY);
		        	lDays.getChildren().add(l);
	        	}
	        }
	    	
	        // draw day columns and their title
	    	List<String> lWeekdayLabels = getWeekdayLabels();
	    	for (int i = 0; i < 7; i++)
	    	{
	        	Line l = new Line(lTimeColumnWidth + (i * lDaycolumnWidth), 0, lTimeColumnWidth + (i * lDaycolumnWidth), 24 * lHourHeigh);
	        	l.setStroke(Color.LIGHTGRAY);
	        	lDays.getChildren().add(l);
	        	
	        	Text t = new Text(lTimeColumnWidth + (i * lDaycolumnWidth), lHeaderHeight, lWeekdayLabels.get(i));
	            t.setTranslateX((lDaycolumnWidth - t.getBoundsInParent().getWidth()) / 2); // center
	        	lHeader.getChildren().add(t);
	    	}

	        
		}
    }
    Group lDays = null;
    ScrollPane lScrollPane = null;

	private void blowupToFullSize(Group group)
	{
		// create a rectangle that is exactly the available size
        final Rectangle IBOUNDS = new Rectangle(0, 0, getSkinnable().getWidth(), getSkinnable().getHeight());
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        group.getChildren().add(IBOUNDS);
	}

	/**
	 * get the weekday labels starting with the weekday that is the first-day-of-the-week according to the locale in the displayed calendar
	 */
	protected List<String> getWeekdayLabels()
	{
		// result
		List<String> lWeekdayLabels = new ArrayList<String>();
		Calendar lLocalCalendar = Calendar.getInstance(getSkinnable().getLocale());

		// setup the dayLabels
		// Calendar.SUNDAY = 1 and Calendar.SATURDAY = 7
		iSimpleDateFormat.applyPattern("E");
		Calendar lCalendar = new java.util.GregorianCalendar(2009, 6, 5); // july 5th 2009 is a Sunday
		for (int i = 0; i < 7; i++)
		{
			// next
			lCalendar.set(java.util.Calendar.DATE, 4 + lLocalCalendar.getFirstDayOfWeek() + i);

			// assign day
			lWeekdayLabels.add( iSimpleDateFormat.format(lCalendar.getTime()));
		}
		
		// done
		return lWeekdayLabels;
	}
	
	private void refreshLocale()
	{
		// create the formatter to use
		iSimpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, getSkinnable().getLocale());
	}
	private SimpleDateFormat iSimpleDateFormat = null;


}
