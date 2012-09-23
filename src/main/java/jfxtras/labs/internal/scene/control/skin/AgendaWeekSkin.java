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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
        lDaysCanvas = new Group();
        lScrollPane = ScrollPaneBuilder.create()
	        .prefWidth(getSkinnable().getWidth())
	        .prefHeight(getSkinnable().getHeight() - 100)
	        .layoutY(50)
	        .content(lDaysCanvas)
	        .hbarPolicy(ScrollBarPolicy.AS_NEEDED)
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

		// this will be replaced with CSS
		Map<String, Color> lGroupColor = new HashMap<String, Color>();
		lGroupColor.put("group1", Color.LIGHTBLUE);
		lGroupColor.put("group2", Color.LIGHTGREEN);
		lGroupColor.put("group3", Color.LIGHTSALMON);
		
		// put the appointments in the appropriate day
		Calendar lToday = Calendar.getInstance();
		TreeMap<Calendar, List<Agenda.Appointment>> lWholeDayAppointmentsPerDay = new TreeMap<Calendar, List<Appointment>>();
		TreeMap<Calendar, List<Agenda.Appointment>> lTimeframeAppointmentsPerDay = new TreeMap<Calendar, List<Appointment>>();
		TreeMap<Calendar, TreeMap<String, AppointsmentsPane>> lAppointmentNodesPerDayPerSlot = new TreeMap<Calendar, TreeMap<String, AppointsmentsPane>>();
		lWholeDayAppointmentsPerDay.put(lToday, new ArrayList<Agenda.Appointment>()); // temp
		lTimeframeAppointmentsPerDay.put(lToday, new ArrayList<Agenda.Appointment>()); // temp
		lAppointmentNodesPerDayPerSlot.put(lToday, new TreeMap<String, AppointsmentsPane>()); // temp
		int lMaxNumberOfWholedayAppointments = 0;
    	for (Agenda.Appointment lApp : getSkinnable().appointments())
    	{
    		if (lApp.isWholeDay())
    		{
    			lWholeDayAppointmentsPerDay.get(lToday).add(lApp);
    			if (lWholeDayAppointmentsPerDay.get(lToday).size() > lMaxNumberOfWholedayAppointments)
    			{
    				lMaxNumberOfWholedayAppointments = lWholeDayAppointmentsPerDay.get(lToday).size();    				 
    			}
    		}
			else
			{
				lTimeframeAppointmentsPerDay.get(lToday).add(lApp);
				
				// create a slot identifier
				String lSlotId = (lApp.getStartTime().get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + lApp.getStartTime().get(Calendar.HOUR_OF_DAY)
						       + ":"
						       + (lApp.getStartTime().get(Calendar.MINUTE) < 30 ? "00" : "30" )
						       ;
				if (lAppointmentNodesPerDayPerSlot.get(lToday).get(lSlotId) == null) lAppointmentNodesPerDayPerSlot.get(lToday).put(lSlotId, new AppointsmentsPane());
				lAppointmentNodesPerDayPerSlot.get(lToday).get(lSlotId).add(lApp);
			}
    	}
    	
    	// process appointment nodes for one day 
    	TreeMap<String, AppointsmentsPane> lAppointmentNodesMap = lAppointmentNodesPerDayPerSlot.get(lToday);
    	System.out.println(lAppointmentNodesMap);
    	TreeMap<String, AppointsmentsPane> lStartAndEndTimesMap = new TreeMap<String, AppointsmentsPane>();
    	for (String lStartTimeId : lAppointmentNodesMap.keySet())
    	{
    		System.out.println("starttime " + lStartTimeId);
    		AppointsmentsPane lAppointsmentsPane = lAppointmentNodesMap.get(lStartTimeId);
    		lStartAndEndTimesMap.put(lStartTimeId + "S", lAppointsmentsPane);
    		lStartAndEndTimesMap.put(lAppointsmentsPane.getEndTimeAsString() + "E", lAppointsmentsPane);
    	}
    	System.out.println(lStartAndEndTimesMap.keySet());
    	
		// determine the maximum number of whole day appointments
		double lWholedayTitleHeight = 25;
		double lWholedayBarWidth = 5; 
		
		// paint the header
		double lHeaderMargin = 5;
		double lHeaderHeight = new Text("XXX").getBoundsInParent().getHeight() + lHeaderMargin + (lMaxNumberOfWholedayAppointments * lWholedayTitleHeight) + 10; // 10 is margin
        Group lHeaderCanvas = new Group();
        lHeaderCanvas.getChildren().add(RectangleBuilder.create().width(getSkinnable().getWidth()).height(lHeaderHeight).stroke(null).fill(Color.WHITE).build()); // blow up to full width
        lHeaderCanvas.setTranslateX(2); // this is the border of the scrollpane
        borderPane.setTop(lHeaderCanvas);
		
		// 
        {
//	        Group lDays = new Group();
        	lDaysCanvas.getChildren().clear();

        	// determine sizes
	        double lTimeColumnWidth = new Text("88:88").getBoundsInParent().getWidth() + 25; // 25 is whitespace
	        double lDaycolumnWidth = (getSkinnable().getWidth() - lTimeColumnWidth - 15) / 7; // TODO use lScrollPane.viewportBoundsProperty().get().getWidth() and lose the 15 px for the scrollbar
        	if (lScrollPane.viewportBoundsProperty().get() != null) System.out.println(lScrollPane.viewportBoundsProperty().get().getWidth());
	        int lHourHeigh = 40;
	
	        // background
	        final Rectangle IBOUNDS = new Rectangle(0, 0, lTimeColumnWidth + (7 * lDaycolumnWidth), 24 * lHourHeigh);
//	        IBOUNDS.setOpacity(0.0);
	        IBOUNDS.setStroke(null);
	        IBOUNDS.setFill(Color.WHITE);
	        lDaysCanvas.getChildren().add(IBOUNDS);
	        
	
	        // draw times
	        for (int lHour = 0; lHour < 24; lHour++)
	        {
	        	// hour
	        	{
		        	Line l = new Line(0, lHour * lHourHeigh, IBOUNDS.getWidth(), lHour * lHourHeigh);
		        	l.setStroke(Color.LIGHTGRAY);
		        	lDaysCanvas.getChildren().add(l);
	        	}
	        	// text
	        	{
	        		Text t = new Text(0, lHour * lHourHeigh, lHour + ":00");	        		
		            t.setStroke(null);
		            t.setFontSmoothingType(FontSmoothingType.LCD);
		            t.setTranslateY(t.getBoundsInParent().getHeight());
		            t.setTranslateX(lTimeColumnWidth - t.getBoundsInParent().getWidth() - 10); // 10 is margin
		        	lDaysCanvas.getChildren().add(t);
	        	}
	        	// halfhour
	        	{
		        	Line l = new Line(lTimeColumnWidth, (lHour + 0.5) * lHourHeigh, IBOUNDS.getWidth(), (lHour + 0.5) * lHourHeigh);
		        	l.setStroke(Color.LIGHTGRAY);
		        	lDaysCanvas.getChildren().add(l);
	        	}
	        }
	    	
	        // draw day columns and their title
	        double lDayTitleHeight = 0;
	    	List<String> lWeekdayLabels = getWeekdayLabels();
	    	for (int i = 0; i < 7; i++)
	    	{
	        	Line l = new Line(lTimeColumnWidth + (i * lDaycolumnWidth), 0, lTimeColumnWidth + (i * lDaycolumnWidth), 24 * lHourHeigh);
	        	l.setStroke(Color.LIGHTGRAY);
	        	lDaysCanvas.getChildren().add(l);
	        	
	        	Text t = new Text(lTimeColumnWidth + (i * lDaycolumnWidth), 0, lWeekdayLabels.get(i));
	            t.setStroke(null);
	            t.setFontSmoothingType(FontSmoothingType.LCD);
	            t.setTranslateX((lDaycolumnWidth - t.getBoundsInParent().getWidth()) / 2); // center
	            lDayTitleHeight = t.getBoundsInParent().getHeight();
	            t.setTranslateY(lDayTitleHeight); 
	        	lHeaderCanvas.getChildren().add(t);
	    	}

	        // paint the appointments for each day
	    	// first the whole days appointments
	    	int lWholedayAppointmentCnt = 0;
	    	int lDayColIdx = 0;
	    	for (Agenda.Appointment lApp : lWholeDayAppointmentsPerDay.get(lToday))
	    	{
	    		// paint the header
	    		Rectangle r = RectangleBuilder.create()
	    						.x(lTimeColumnWidth + (lDayColIdx * lDaycolumnWidth) + (lWholedayAppointmentCnt * lWholedayBarWidth))
	    						.y(lDayTitleHeight + lHeaderMargin + (lWholedayAppointmentCnt * lWholedayTitleHeight))
	    						.width(lDaycolumnWidth - (lWholedayAppointmentCnt * lWholedayBarWidth))
	    						.height(lWholedayTitleHeight)
	    						.stroke(Color.DARKGRAY)
	    						.fill(lGroupColor.get(lApp.getGroup()))
	    						.build();
	        	lHeaderCanvas.getChildren().add(r);
	        	
	        	// add title
	        	Text t = new Text(r.getX() + 5, r.getY(), lApp.getSummary());
	            t.setStroke(null);
	            t.setFontSmoothingType(FontSmoothingType.LCD);
	            t.setTranslateY( t.getBoundsInParent().getHeight()); 
	        	lHeaderCanvas.getChildren().add(t);
	    			    		
	    		// paint the bar in the header
	    		Rectangle r2 = RectangleBuilder.create()
						.x(r.getX())
						.y(r.getY() + r.getHeight())
						.width(lWholedayBarWidth)
						.height(lHeaderHeight - r.getY() - r.getHeight())
						.stroke(Color.DARKGRAY)
						.fill(lGroupColor.get(lApp.getGroup()))
						.build();
	    		lHeaderCanvas.getChildren().add(r2);
	        	
	    		// paint the bar in the day
	    		Rectangle r3 = RectangleBuilder.create()
						.x(r.getX())
						.y(0)
						.width(lWholedayBarWidth)
						.height(24 * lHourHeigh)
						.stroke(Color.DARKGRAY)
						.fill(lGroupColor.get(lApp.getGroup()))
						.build();
	    		lDaysCanvas.getChildren().add(r3);
	        	
	        	// next
	        	lWholedayAppointmentCnt++;
	    	}
		}
    }
    Group lDaysCanvas = null;
    ScrollPane lScrollPane = null;
    
    /**
     * 
     *
     */
    class AppointsmentsPane extends Region
    {
    	List<Appointment> appointments = new ArrayList<Agenda.Appointment>();
    	
    	public AppointsmentsPane()
    	{
    		getChildren().add(pane);
    	}
    	Pane pane = new Pane();
    	
    	/**
    	 * 
    	 * @param appointment
    	 */
    	public void add(Appointment appointment)
    	{
    		appointments.add(appointment);
    	}
    	
    	/**
    	 * 
    	 * @return
    	 */
    	public String getStartTimeAsString()
    	{
    		if (appointments.size() < 1) return null;
    		String lSlotId = "AA:AA";
    		for (Appointment lAppointment : appointments)
    		{
				String lSlotId2 = (lAppointment.getStartTime().get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + lAppointment.getStartTime().get(Calendar.HOUR_OF_DAY)
						        + ":"
						        + (lAppointment.getStartTime().get(Calendar.MINUTE) < 30 ? "00" : "30" )
						        ;
				if (lSlotId2.compareTo(lSlotId) < 0) lSlotId = lSlotId2;
    		}
    		return lSlotId;
    	}

    	/**
    	 * 
    	 * @return
    	 */
    	public String getEndTimeAsString()
    	{
    		if (appointments.size() < 1) return null;
    		String lSlotId = "00:00";
    		for (Appointment lAppointment : appointments)
    		{
				String lSlotId2 = (lAppointment.getEndTime().get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + lAppointment.getEndTime().get(Calendar.HOUR_OF_DAY)
						        + ":"
						        + (lAppointment.getEndTime().get(Calendar.MINUTE) < 30 ? "00" : "30" )
						        ;
				if (lSlotId2.compareTo(lSlotId) > 0) lSlotId = lSlotId2;
    		}
    		return lSlotId;
    	}

    	/**
    	 * 
    	 */
    	public String toString()
    	{
    		return super.toString()
    		     + ";" + getStartTimeAsString() + "-" + getEndTimeAsString()
    		     + ";cnt=" + appointments.size()
    		     ;
    	}
    }

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
