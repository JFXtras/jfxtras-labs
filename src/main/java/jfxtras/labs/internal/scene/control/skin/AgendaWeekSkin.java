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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
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
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.control.CalendarTimePicker;

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
				assignCalendars();
				setupAppointments();
				paint();
			} 
		});
		assignCalendars();
		setupAppointments();
	}
	
	// ==================================================================================================================
	// PROPERTIES
	
	// ==================================================================================================================
	// DRAW
	
	// this is needed to react to resizing of the control
    @Override public void layoutChildren() 
    {
        if (lastWidth != getSkinnable().getWidth()) 
        {
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
		// we use a borderpane
		borderPane = new BorderPane();
		
		// top
		weekHeader = new WeekHeader();
        borderPane.setTop(weekHeader);
        
        // center
        week = new Week();
        weekScrollPane = ScrollPaneBuilder.create()
	        .prefWidth(getSkinnable().getWidth())
	        .prefHeight(getSkinnable().getHeight() - 100)
	        .layoutY(50)
	        .content(week)
	        .hbarPolicy(ScrollBarPolicy.AS_NEEDED)
	        .pannable(true)
	        .build();
        borderPane.setCenter(weekScrollPane);
        
        // add to self
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
        getChildren().add(borderPane);
	}
	BorderPane borderPane = null;
	WeekHeader weekHeader = null;
	Week week = null;
    ScrollPane weekScrollPane = null;
    
	/**
	 * 
	 */
	private void paint()
	{
		//System.out.println("!!! paint");
        calculateSizes();        
		borderPane.layout();
		weekHeader.layoutChildren();
		for (DayHeader lDayHeader : weekHeader.dayHeaders) 
		{
			lDayHeader.layoutChildren();
		}
		week.layoutChildren();
		for (Day lDay : week.days) 
		{
			lDay.layoutChildren();
			for (AppointmentArea lAppointmentArea : lDay.appointmentAreas)
			{
				lAppointmentArea.layoutChildren();
			}
		}
	}
	

	// ==================================================================================================================
	// LOGIC
	
	/**
	 * Responsible for rendering the days
	 */
	class WeekHeader extends Pane
	{
		/**
		 * 
		 */
		public WeekHeader()
		{
			// 7 days per week
			for (int i = 0; i < 7; i++)
			{
				dayHeaders.add(new DayHeader());
			}
		}
		final List<DayHeader> dayHeaders = new ArrayList<DayHeader>();
		
		/**
		 * Draw the time labels and allocate space to the days
		 */
		protected void layoutChildren()
		{
			// paint the header
			getChildren().clear();
			
			// now place the day nodes
			for (int i = 0; i < 7; i++)
			{
				// position header
				DayHeader lDayHeader = dayHeaders.get(i);
				lDayHeader.setLayoutX(dayFirstColumnX + (i * dayWidth));
				lDayHeader.setLayoutY(0.0);
				getChildren().add(lDayHeader);
			}
//	        setTranslateX(2); // this is the border of the scrollpane
	    }
	}
	
	/**
	 *
	 */
	class DayHeader extends Pane
	{
		Calendar calendar = null; // the date of this day
		
		protected void layoutChildren()
		{
			getChildren().clear();
			
			// add the background; this is needed to make sure the day node is the appropriate size, therefore it is transparent
	        Rectangle lBackground = new Rectangle(0, 0, dayWidth, headerHeight);
	        lBackground.getStyleClass().clear();
	        lBackground.getStyleClass().add("DayHeader");
	        getChildren().add(lBackground);
			
	        // set label
			String lLabel = iDayOfWeekDateFormat.format(calendar.getTime()) + " " + iDateFormat.format(calendar.getTime());
			Text lText = new Text(lLabel);
			lText.setX( (dayWidth - lText.prefWidth(0)) / 2);
			lText.setY(lText.prefHeight(0));
			getChildren().add(lText);
		}
		
	}

	/**
	 * Responsible for rendering the days
	 */
	class Week extends Pane
	{
		/**
		 * 
		 */
		public Week()
		{
			// 7 days per week
			for (int i = 0; i < 7; i++)
			{
				days.add(new Day());
			}
		}
		final List<Day> days = new ArrayList<Day>();
		
		/**
		 * Draw the time labels and allocate space to the days
		 */
		protected void layoutChildren()
		{
			// clear
			getChildren().clear();
			
			// add the background
	        final Rectangle lBackground = new Rectangle(0, 0, timeWidth + (7 * dayWidth), 24 * hourHeight);
	        lBackground.getStyleClass().clear();
	        lBackground.getStyleClass().add("Week");
	        getChildren().add(lBackground);
			
	        // draw times
	        for (int lHour = 0; lHour < 24; lHour++)
	        {
	        	// hour
	        	{
		        	Line l = new Line(0, lHour * hourHeight, lBackground.getWidth(), lHour * hourHeight);
			        l.getStyleClass().clear();
	        		l.getStyleClass().add("HourLine");
		        	getChildren().add(l);
	        	}
	        	// text
	        	{
	        		Text t = new Text(0, lHour * hourHeight, lHour + ":00");
	    	        t.getStyleClass().clear();
	        		t.getStyleClass().add("HourLabel");
		            t.setFontSmoothingType(FontSmoothingType.LCD);
		            t.setTranslateY(t.getBoundsInParent().getHeight());
		            t.setTranslateX(timeWidth - t.getBoundsInParent().getWidth() - 10); // 10 is margin
		        	getChildren().add(t);
	        	}
	        	// halfhour
	        	{
		        	Line l = new Line(timeWidth, (lHour + 0.5) * hourHeight, lBackground.getWidth(), (lHour + 0.5) * hourHeight);
			        l.getStyleClass().clear();
	        		l.getStyleClass().add("HalfHourLine");
		        	getChildren().add(l);
	        	}
	        }
	        
	        // now place the day nodes
			for (int i = 0; i < 7; i++)
			{
				Day lDay = days.get(i);
				lDay.setLayoutX(dayFirstColumnX + (i * dayWidth));
				lDay.setLayoutY(0.0);
				getChildren().add(lDay);
				
				Line l = new Line(lDay.getLayoutX(), 0, lDay.getLayoutX(), dayHeight);
		        l.getStyleClass().clear();
				l.getStyleClass().add("DaySeparator");
				getChildren().add(l);
			}
		}

	}
	
	/**
	 * Responsible for rendering the appointments within a day 
	 */
	class Day extends Pane
	{
		Calendar calendar = null; // the date of this day
		
		/**
		 * This method prepares a day for being drawn.
		 * The appointments within one day might overlap, this method will create a data structure so it is clear how these overlapping appointments should be drawn.
		 * All appointments in one day are process based on their start time; earliest first, and if there are more with the same start time, longest duration first.
		 * The appointments are then place onto (parallel) tracks; an appointment initially is placed in track 1. 
		 * But if there is already an (partially overlapping) appointment there, then the appointment is placed in track 2. 
		 * Unless there also is an appointment already in track 2, then track 3 is tried, then track 4, track 5, until a free slot is found.
		 * For example (the letters are not the sequence in which the appointments are processed, theý're just for identifying them):
		 * 
		 *  tracks
		 *  1 2 3 4
		 *  -------
		 *  . . . .
		 *  . . . .
		 *  A . . .
		 *  A B C .
		 *  A B C D
		 *  A B . D
		 *  A . . D
		 *  A E . D
		 *  A . . D
		 *  . . . D
		 *  . . . D
		 *  F . . D
		 *  F H . D 
		 *  . . . .
		 *  G . . . 
		 *  . . . .
		 * 
		 * Appointment A was rendered first and put into track 1 and its start time.
		 * Then appointment B was added, initially it was put in track 1, but appointment A already uses the same timeslot, so B was moved into track 2.
		 * C moved from track 1, conflicting with A, to track 2, conflicting with B, and ended up in track 3.
		 * And so forth.
		 * F and H show that even though D overlaps them, they could perfectly be placed in lower tracks.
		 * 
		 * A cluster of appointments always starts with a free standing appointment in track 1, for example A or G, this appointment is called the cluster owner.
		 * When the next appointment is added to the tracks, and finds that it cannot be put in track 1, it will be added as a member to the cluster denoted by the appointment in track 1.
		 * The word "denoted" is used on purpose, because special attention must be paid to an appointment that is placed in track 1, but is linked to a cluster by a earlier appointment in a higher track.
		 * In the example above, F is linked by D to the cluster owned by A. So F is not a cluster of its own, but a member of the cluster owned by A.
		 * And appointment H through F is also part of the cluster owned by A.  
		 * G finally starts a new cluster.
		 * The cluster owner knows all members and how many tracks there are, each member has a direct link to the cluster owner. 
		 *   
		 * When rendering the appointments above, parallel appointments are rendered narrower & indented, so appointments partially overlap and a piece of all appointments is always visible to the user.
		 * In the example above the single appointment G is rendered full width, while for example A, B, C and D are overlapping.
		 * F and H are drawn in the same dimensions as A and B in order to allow D to overlap then.
		 * The size and amount of indentation depends on the number of appointments that are rendered next to each other.
		 * In order to compute its location and size, each appointment needs to know:
		 * - its start and ending time,
		 * - its track number,
		 * - its total number of tracks,
		 * - and naturally the total width and height available to draw the day.
		 * 
		 */
		public void setupAppointments()
		{
			// clear
			appointmentAreas.clear();
			if (calendar == null) return;
			
			// scan all appointments and filter the ones for this day
			for (Agenda.Appointment lAppointment : getSkinnable().appointments())
			{
				if (lAppointment.isWholeDay()) continue; // not yet
				
				// if the appointment fall in today
				// appointments may span multiple days, but the appointment area will clamp the start and end date
				AppointmentArea lAppointmentArea = new AppointmentArea(calendar, lAppointment);
				if ( sameDay(calendar, lAppointmentArea.start) 
				  && sameDay(calendar, lAppointmentArea.end)
				   )
				{
					appointmentAreas.add(lAppointmentArea);
				}
			}
			
			// sort on start time and then decreasing duration
			Collections.sort(appointmentAreas, new Comparator<AppointmentArea>()
			{
				@Override
				public int compare(AppointmentArea o1, AppointmentArea o2)
				{
					if (o1.startAsString.equals(o2.startAsString) == false)
					{
						return o1.startAsString.compareTo(o2.startAsString);
					}
					return o1.durationInMS > o2.durationInMS ? -1 : 1;
				}
			});
			
			// start placing appointments
			AppointmentArea lClusterOwner = null;
			for (AppointmentArea lAppointmentArea : appointmentAreas)
			{
				// if there is no cluster owner
				if (lClusterOwner == null)
				{
					// than the current becomes an owner
					// only create a minimal cluster, because it will be setup fully in the code below
					lClusterOwner = lAppointmentArea;
					lClusterOwner.clusterTracks = new ArrayList<List<AppointmentArea>>();
				}
				
				// in which track should it be added
				int lTrackNr = determineTrackWhereAppointmentCanBeAdded(lClusterOwner.clusterTracks, lAppointmentArea);
				// if it can be added to track 0, then we have a "situation". Track 0 could mean 
				// - we must start a new cluster
				// - the appointment is still linked to the running cluster by means of a linking appointment in the higher tracks
				if (lTrackNr == 0)
				{
					// So let's see if there is a linking appointment higher up
					boolean lOverlaps = false;
					for (int i = 1; i < lClusterOwner.clusterTracks.size() && lOverlaps == false; i++)
					{
						lOverlaps = checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(lClusterOwner.clusterTracks, i, lAppointmentArea);
					}
					
					// if it does not overlap, we start a new cluster
					if (lOverlaps == false)
					{
						lClusterOwner = lAppointmentArea;
						lClusterOwner.clusterMembers = new ArrayList<AppointmentArea>(); 
						lClusterOwner.clusterTracks = new ArrayList<List<AppointmentArea>>();
						lClusterOwner.clusterTracks.add(new ArrayList<AgendaWeekSkin.AppointmentArea>());
					}
				}
				
				// add it to the track (and setup all other cluster data)
				lClusterOwner.clusterMembers.add(lAppointmentArea);
				lClusterOwner.clusterTracks.get(lTrackNr).add(lAppointmentArea);
				lAppointmentArea.clusterOwner = lClusterOwner;
				lAppointmentArea.clusterTrackIdx = lTrackNr;				
				// for debug  System.out.println("----"); for (int i = 0; i < lClusterOwner.clusterTracks.size(); i++) { System.out.println(i + ": " + lClusterOwner.clusterTracks.get(i) ); } System.out.println("----");
			}
		}
		final List<AppointmentArea> appointmentAreas = new ArrayList<AgendaWeekSkin.AppointmentArea>(); // all appointments that need to be drawn

		/**
		 * 
		 */
		protected void layoutChildren()
		{
			getChildren().clear();
			
			// add the background; this is needed to make sure the day node is the appropriate size, therefore it is transparent
	        final Rectangle lBackground = new Rectangle(0, 0, dayWidth, dayHeight);
	        lBackground.getStyleClass().clear();
	        lBackground.getStyleClass().add("Day");
	        getChildren().add(lBackground);
			
	        // add all appoinments to the day
	        for (AppointmentArea lAppointmentArea : appointmentAreas)
	        {
	        	int lOffsetY = (lAppointmentArea.start.get(Calendar.HOUR_OF_DAY) * 60) + lAppointmentArea.start.get(Calendar.MINUTE);
	        	lAppointmentArea.setLayoutX(dayContentWidth / lAppointmentArea.clusterOwner.clusterTracks.size() * lAppointmentArea.clusterTrackIdx);
	        	lAppointmentArea.setLayoutY(dayHeight / (24 * 60) * lOffsetY );
				getChildren().add(lAppointmentArea);	        	
	        }
		}
	}
	
    /**
     * Responsible for rendering a single appointment on a single day.
     * An appointment region is a representation of an appointment in one single day.
     * Appointments may span multiple days, each day gets its own appointment region.
     * 
     */
    class AppointmentArea extends Pane
    {
    	// for the role of cluster owner
		List<AppointmentArea> clusterMembers = null; 
		List<List<AppointmentArea>> clusterTracks = null;
		// for the role of cluster member
		AppointmentArea clusterOwner = null;
		int clusterTrackIdx = -1;

		/**
		 * 
		 * @param calendar
		 * @param appointment
		 */
    	public AppointmentArea(Calendar calendar, Agenda.Appointment appointment)
    	{
    		// remember
    		this.appointment = appointment;
    		
    		// start
    		Calendar lDayStartCalendar = (Calendar)calendar.clone();
    		lDayStartCalendar.set(Calendar.HOUR, 0);
    		lDayStartCalendar.set(Calendar.MINUTE, 0);
    		lDayStartCalendar.set(Calendar.SECOND, 0);
    		lDayStartCalendar.set(Calendar.MILLISECOND, 0);
    		this.start = (appointment.getStartTime().before(lDayStartCalendar) ? lDayStartCalendar : appointment.getStartTime());
			this.startAsString = (this.start.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.start.get(Calendar.HOUR_OF_DAY)
				               + ":"
				               + (this.start.get(Calendar.MINUTE) < 30 ? "00" : "30" )
				               ;
    		
    		// end
    		Calendar lDayEndCalendar = (Calendar)calendar.clone();
    		lDayEndCalendar.set(Calendar.HOUR, 23);
    		lDayEndCalendar.set(Calendar.MINUTE, 59);
    		lDayEndCalendar.set(Calendar.SECOND, 59);
    		lDayEndCalendar.set(Calendar.MILLISECOND, 999);
    		this.end = (appointment.getEndTime().after(lDayEndCalendar) ? lDayEndCalendar : appointment.getEndTime());
			this.endAsString = (this.end.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.end.get(Calendar.HOUR_OF_DAY)
				             + ":"
				             + (this.end.get(Calendar.MINUTE) < 30 ? "00" : "30" )
				             ;
			
			// duration
			durationInMS = this.end.getTimeInMillis() - this.start.getTimeInMillis();
    	}
    	final Agenda.Appointment appointment;
    	final Calendar start;
    	final String startAsString;
    	final Calendar end;
    	final String endAsString;
    	final long durationInMS;
    	
    	/**
    	 * 
    	 */
    	public String toString()
    	{
    		return super.toString()
    		     + ";" + startAsString + "-" + endAsString
    		     + ";" + durationInMS + "ms"
    		     ;
    	}
    	

		/**
		 * 
		 */
		protected void layoutChildren()
		{
			getChildren().clear();
			
			// add the background; this is needed to make sure the day node is the appropriate size and it doubles as the styling
        	double lW = ((double)dayContentWidth) * (1.0 / (((double)clusterOwner.clusterTracks.size())));
        	if (clusterTrackIdx < clusterOwner.clusterTracks.size() - 1) lW *= 1.5;
        	double lH = (dayHeight / (24 * 60) * (durationInMS / 1000 / 60) );
	        final Rectangle lBackground = new Rectangle(0, 0, lW, lH);
	        lBackground.getStyleClass().clear();
	        lBackground.getStyleClass().add("Appointment");
	        lBackground.getStyleClass().add(appointment.getGroup());
	        getChildren().add(lBackground);
	        
	        // add a text node
			String lLabel = appointment.getSummary();
			Text lText = new Text(lLabel);
			lText.getStyleClass().clear();
			lText.getStyleClass().add("AppointmentLabel");
			lText.setX( 3 );
			lText.setY(lText.prefHeight(0));
			getChildren().add(lText);	        
		}

    }

	// ==================================================================================================================
	// SUPPORT

    /**
     * 
     */
    private void calculateSizes()
    {
    	// generic
		scrollbarSize = 15; // TODO: derive this from an actual ScrollBar.

		// header
		maxNumberOfWholedayAppointments = 0;
		wholedayTitleHeight = 25;
		headerWhitespace = 5;
		headerHeight = new Text("XXX").getBoundsInParent().getHeight() + headerWhitespace + (maxNumberOfWholedayAppointments * wholedayTitleHeight) + headerWhitespace;

		// time column
		int lTimeColumnWhitespace = 10;
        timeWidth = new Text("88:88").getBoundsInParent().getWidth() + lTimeColumnWhitespace;
        
        // day columns
        dayFirstColumnX = timeWidth + lTimeColumnWhitespace;
        dayWidth = (getSkinnable().getWidth() - timeWidth - scrollbarSize) / 7;
    	if (weekScrollPane.viewportBoundsProperty().get() != null) 
    	{
	        dayWidth = (weekScrollPane.viewportBoundsProperty().get().getWidth() - timeWidth - scrollbarSize) / 7;
    	}
    	dayContentWidth = dayWidth - 10;
    	
    	// hour height
        hourHeight = (2 * new Text("X").getBoundsInParent().getHeight()) + 10;
        if (weekScrollPane.viewportBoundsProperty().get() != null && (weekScrollPane.viewportBoundsProperty().get().getHeight() - scrollbarSize) > hourHeight * 24)
        {
            // if there is more room than absolutely required, let the height grow with the available room
        	hourHeight = (weekScrollPane.viewportBoundsProperty().get().getHeight() - scrollbarSize) / 24;
        }
        dayHeight = hourHeight * 24;
    }
	double scrollbarSize = 0;
	double headerHeight = 0;
	double maxNumberOfWholedayAppointments = 0;
	double wholedayTitleHeight = 25;
	double headerWhitespace = 5;
    double timeWidth = 0;
    double dayFirstColumnX = 0;
    double dayWidth = 0;
    double dayContentWidth = 0;
    double dayHeight = 0;
    double hourHeight = 0;
	
	/**
	 * get the calendar for the first day of the week
	 */
	protected Calendar getFirstDayOfWeekCalendar()
	{
		// result
		Calendar lLocalCalendar = Calendar.getInstance(getSkinnable().getLocale());
		int lFirstDayOfWeek = lLocalCalendar.getFirstDayOfWeek();
		
		// now get the displayed calendar
		Calendar lDisplayedCalendar = getSkinnable().getDisplayedCalendar();
		
		// this is the first day of week calendar
		Calendar lFirstDayOfWeekCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		
		// if not on the first day of the week, correct with the appropriate amount
		lFirstDayOfWeekCalendar.add(Calendar.DATE, lFirstDayOfWeek - lFirstDayOfWeekCalendar.get(Calendar.DAY_OF_WEEK));
		
		// make sure we are in the same week
		while ( lFirstDayOfWeekCalendar.get(Calendar.YEAR) > lDisplayedCalendar.get(Calendar.YEAR)
			 || (lFirstDayOfWeekCalendar.get(Calendar.YEAR) == lDisplayedCalendar.get(Calendar.YEAR) && lFirstDayOfWeekCalendar.get(Calendar.WEEK_OF_YEAR) > lDisplayedCalendar.get(Calendar.WEEK_OF_YEAR))
			  )
		{
			lFirstDayOfWeekCalendar.add(Calendar.DATE, -7);
		}
		while ( lFirstDayOfWeekCalendar.get(Calendar.YEAR) < lDisplayedCalendar.get(Calendar.YEAR)
				 || (lFirstDayOfWeekCalendar.get(Calendar.YEAR) == lDisplayedCalendar.get(Calendar.YEAR) && lFirstDayOfWeekCalendar.get(Calendar.WEEK_OF_YEAR) < lDisplayedCalendar.get(Calendar.WEEK_OF_YEAR))
				  )
		{
			lFirstDayOfWeekCalendar.add(Calendar.DATE, 7);
		}
		
		// done
		return lFirstDayOfWeekCalendar;
	}

	/**
	 * 
	 */
	private void assignCalendars()
	{
		// get the first day of week calendar
		Calendar lCalendar = getFirstDayOfWeekCalendar();
		
		// assign it
		for (int i = 0; i < 7; i++)
		{
			weekHeader.dayHeaders.get(i).calendar = (Calendar)lCalendar.clone();
			week.days.get(i).calendar = (Calendar)lCalendar.clone();
			lCalendar.add(Calendar.DATE, 1);
		}		
	}
	
	/**
	 * 
	 */
	private void refreshLocale()
	{
		// create the formatter to use
		iDayOfWeekDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, getSkinnable().getLocale());
		iDayOfWeekDateFormat.applyPattern("E");
		iDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, getSkinnable().getLocale());
	}
	private SimpleDateFormat iDayOfWeekDateFormat = null;
	private SimpleDateFormat iDateFormat = null;

	/**
	 * 
	 */
	private void setupAppointments()
	{
		for (Day lDay : week.days)
		{
			lDay.setupAppointments();
		}
	}
	
	/**
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	private boolean sameDay(Calendar c1, Calendar c2)
	{
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
		    && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
		    && c1.get(Calendar.DATE) == c2.get(Calendar.DATE)
		     ; 
	}

	/**
	 * 
	 * @param tracks
	 * @param appointmentArea
	 * @return
	 */
	private int determineTrackWhereAppointmentCanBeAdded(List<List<AppointmentArea>> tracks, AppointmentArea appointmentArea)
	{
		int lTrackNr = 0;
		while (true)
		{
			// make sure there is a arraylist for this track
			if (lTrackNr == tracks.size()) tracks.add(new ArrayList<AppointmentArea>());
			
			// scan all existing appointments in this track and see if there is an overlap
			if (checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(tracks, lTrackNr, appointmentArea) == false)
			{
				// no overlap, it can be added here
				return lTrackNr;
			}

			// overlap, try next track
			lTrackNr++;
		}
	}
	
	/**
	 * 
	 * @param tracks
	 * @param tracknr
	 * @param appointmentArea
	 * @return
	 */
	private boolean checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(List<List<AppointmentArea>> tracks, int tracknr, AppointmentArea appointmentArea)
	{
		// get the track
		List<AppointmentArea> lTrack = tracks.get(tracknr);
		
		// scan all existing appointments in this track
		for (AppointmentArea lAppointmentArea : lTrack)
		{
			// There is an overlap:
			// if the start time of the already placed appointment is before or equals the new appointment's end time 
			// and the end time of the already placed appointment is after or equals the new appointment's start time
			// ...PPPPPPPPP...
			// .NNNN.......... -> Ps <= Ne & Pe >= Ns -> overlap
			// .....NNNNN..... -> Ps <= Ne & Pe >= Ns -> overlap
			// ..........NNN.. -> Ps <= Ne & Pe >= Ns -> overlap
			// .NNNNNNNNNNNNN. -> Ps <= Ne & Pe >= Ns -> overlap
			// .N............. -> false    & Pe >= Ns -> no overlap
			// .............N. -> Ps <= Ne & false    -> no overlap
			if ( (lAppointmentArea.start.equals(appointmentArea.start) || lAppointmentArea.start.before(appointmentArea.end)) 
			  && (lAppointmentArea.end.equals(appointmentArea.start) || lAppointmentArea.end.after(appointmentArea.start))
			   )
			{
				// overlap
				return true;
			}
		}
		
		// no overlap
		return false;
	}

}
