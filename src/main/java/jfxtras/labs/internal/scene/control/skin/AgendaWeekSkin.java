/**
 * Copyright (c) 2011, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *	 * Redistributions of source code must retain the above copyright
 *	   notice, this list of conditions and the following disclaimer.
 *	 * Redistributions in binary form must reproduce the above copyright
 *	   notice, this list of conditions and the following disclaimer in the
 *	   documentation and/or other materials provided with the distribution.
 *	 * Neither the name of the <organization> nor the
 *	   names of its contributors may be used to endorse or promote products
 *	   derived from this software without specific prior written permission.
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.labs.internal.scene.control.behavior.AgendaBehavior;
import jfxtras.labs.scene.control.Agenda;
import jfxtras.labs.scene.control.Agenda.Appointment;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * @author Tom Eugelink
 * TODO: the alignment of wholeday in header and day is not pixel perfect
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
		// setup component
		createNodes();

		// react to changes in the locale 
		getSkinnable().localeProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				refreshLocale();
			} 
		});
		refreshLocale();
		
		// react to changes in the appointments 
		getSkinnable().displayedCalendar().addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable observable)
			{
				assignCalendarToTheDays();
				setupAppointments();
			}
		});
		assignCalendarToTheDays();
		
		// react to changes in the appointments 
		getSkinnable().appointments().addListener(new ListChangeListener<Agenda.Appointment>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Appointment> arg0)
			{
				setupAppointments();
			} 
		});
		setupAppointments();
	}
	
	/**
	 * Assign a calendar to each day, so it knows what it must draw.
	 * 
	 */
	private void assignCalendarToTheDays()
	{
		// get the first day of week calendar
		Calendar lCalendar = getFirstDayOfWeekCalendar();
		Calendar lStartCalendar = (Calendar)lCalendar.clone();
		
		// assign it
		Calendar lEndCalendar = null;
		for (int i = 0; i < 7; i++)
		{
			week.days.get(i).calendarObjectProperty.set( (Calendar)lCalendar.clone() );
			if (i== 6) lEndCalendar = (Calendar)lCalendar.clone();
			lCalendar.add(Calendar.DATE, 1);
		}		
		
		// tell the skin what range is needed
		if (getSkinnable().getCalendarRangeCallback() != null)
		{
			Agenda.CalendarRange lCalendarRange = new Agenda.CalendarRange(lStartCalendar, lEndCalendar);
			getSkinnable().getCalendarRangeCallback().call(lCalendarRange);
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
		
		// force redraw the dayHeaders by reassigning the calendar
		for (DayPane lDay : week.days)
		{
			if (lDay.calendarObjectProperty.get() != null)
			{
				lDay.calendarObjectProperty.set( (Calendar)lDay.calendarObjectProperty.get().clone() );
			}
		}
	}
	private SimpleDateFormat iDayOfWeekDateFormat = null;
	private SimpleDateFormat iDateFormat = null;

	/**
	 * Have all days reconstruct the appointments
	 */
	private void setupAppointments()
	{
		calculateSizes();
		for (DayPane lDay : week.days)
		{
			lDay.setupAppointments();
		}
	}
	
	// ==================================================================================================================
	// PROPERTIES
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// we use a borderpane
		borderPane = new BorderPane();
		
		// center
		week = new WeekPane();
		weekScrollPane = ScrollPaneBuilder.create()
			.prefWidth(getSkinnable().getWidth())
			.prefHeight(getSkinnable().getHeight())
			.layoutY(50)
			.content(week)
			.hbarPolicy(ScrollBarPolicy.NEVER)
			.pannable(true)
			.build();
		borderPane.setCenter(weekScrollPane);
		// bind the size of the week to the scrollpane's viewport
		weekScrollPane.viewportBoundsProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable viewportBoundsProperty)
			{
				calculateSizes();
			}
		});
		
		// top: header have to be created after the week, because there is a binding to days
		weekHeader = new WeekHeaderPane();
		borderPane.setTop(weekHeader);
		
		// add to self
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(borderPane);
	}
	BorderPane borderPane = null;
	WeekHeaderPane weekHeader = null;
	ScrollPane weekScrollPane = null;
	WeekPane week = null;
	
	// ==================================================================================================================
	// PANES
	
	/**
	 * Responsible for rendering the days
	 */
	class WeekHeaderPane extends Pane
	{
		/**
		 * 
		 */
		public WeekHeaderPane()
		{
			// 7 days per week
			for (int i = 0; i < 7; i++)
			{
				DayHeaderPane lDayHeader = new DayHeaderPane(week.days.get(i)); // associate with a day, so we can use its administration. This needs only be done once
				lDayHeader.layoutXProperty().bind(week.days.get(i).layoutXProperty());			
				lDayHeader.layoutYProperty().set(0);
				lDayHeader.prefWidthProperty().bind(week.days.get(i).prefWidthProperty());			
				lDayHeader.prefHeightProperty().bind(heightProperty());			
				getChildren().add(lDayHeader);
				dayHeaders.add(lDayHeader);
			}
		}
		final List<DayHeaderPane> dayHeaders = new ArrayList<DayHeaderPane>();
	}

	/**
	 * Responsible for rendering the day header (whole day appointments).
	 * This class is connected to the day and uses its data.
	 */
	class DayHeaderPane extends Pane
	{
		public DayHeaderPane(DayPane day)
		{
			// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
			// remember
			this.day = day;
			day.dayHeader = this; // two way link
			
			// set label
			int lPadding = 3;
			text = new Text("?");
			Rectangle lClip = new Rectangle(0,0,0,0);
			lClip.widthProperty().bind(widthProperty().subtract(lPadding));
			lClip.heightProperty().bind(heightProperty());
			text.setClip(lClip);
			getChildren().add(text);
			day.calendarObjectProperty.addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					if (DayHeaderPane.this.day.calendarObjectProperty.get() == null) return;
					String lLabel = iDayOfWeekDateFormat.format(DayHeaderPane.this.day.calendarObjectProperty.get().getTime()) + " " + iDateFormat.format(DayHeaderPane.this.day.calendarObjectProperty.get().getTime());
					text.setText(lLabel);
					double lX = (dayWidth - text.prefWidth(0)) / 2;
					text.setX( lX < 0 ? 0 : lX );
					text.setY(text.prefHeight(0));
				}
			});
			
			// change the layout related to the size
			widthProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			heightProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			
			// layout
			relayout();
		}
		DayPane day = null;
		Text text = null;
		
		/**
		 * 
		 */
		public void relayout()
		{
			// create headers
			int lOffset = maxNumberOfWholedayAppointments - appointmentHeaderPanes.size(); // to make sure the appointments are renders aligned bottom
			for (AppointmentHeaderPane lAppointmentHeaderPane : appointmentHeaderPanes)
			{
				int lIdx = appointmentHeaderPanes.indexOf(lAppointmentHeaderPane);
				lAppointmentHeaderPane.setLayoutX(lIdx * wholedayAppointmentWidth);
				lAppointmentHeaderPane.setLayoutY( titleCalendarHeight + ((lIdx + lOffset) * wholedayTitleHeight) );
				lAppointmentHeaderPane.setPrefSize(dayWidth - (lIdx * wholedayAppointmentWidth), (appointmentHeaderPanes.size() - lIdx) * wholedayTitleHeight);
			}
		}
		
		/**
		 * 
		 */
		public void setupAppointments()
		{
			// create headers
			getChildren().removeAll(appointmentHeaderPanes);
			appointmentHeaderPanes.clear();
			for (AppointmentPane lAppointmentPane : day.wholedayAppointmentPanes)
			{
				AppointmentHeaderPane lAppointmentHeaderPane = new AppointmentHeaderPane(lAppointmentPane.appointment);
				getChildren().add(lAppointmentHeaderPane);				
				appointmentHeaderPanes.add(lAppointmentHeaderPane);	
			}
			
			// and layout
			relayout();
		}
		final List<AppointmentHeaderPane> appointmentHeaderPanes = new ArrayList<AgendaWeekSkin.AppointmentHeaderPane>();		
	}
	
	/**
	 * Responsible for rendering a single whole day appointment on a day header.
	 * 
	 */
	class AppointmentHeaderPane extends Pane
	{
		/**
		 * 
		 * @param calendar
		 * @param appointment
		 */
		public AppointmentHeaderPane(Agenda.Appointment appointment)
		{
			// remember
			this.appointment = appointment;
			
			// for debugging setStyle("-fx-border-color:GREEN;-fx-border-width:4px;");
			getStyleClass().add("Appointment");
			getStyleClass().add(appointment.getGroup());

			// add a text node
			double lPadding = 3;
			Text lSummaryText = new Text(appointment.getSummary());
			lSummaryText.getStyleClass().add("AppointmentLabel");
			lSummaryText.setX( lPadding );
			lSummaryText.setY( textHeight1M );
			Rectangle lClip = new Rectangle(0,0,0,0);
			lClip.widthProperty().bind(widthProperty().subtract(lPadding));
			lClip.heightProperty().bind(heightProperty());
			lSummaryText.setClip(lClip);
			getChildren().add(lSummaryText);			

		}
		final Agenda.Appointment appointment;
	}
	
	/**
	 * Responsible for rendering the days
	 */
	class WeekPane extends Pane
	{
		/**
		 * 
		 */
		public WeekPane()
		{
			getStyleClass().add("WeekPane");
			
			// draw times
			for (int lHour = 0; lHour < 24; lHour++)
			{
				// hour line
				{
					Line l = new Line(0,10,100,10);
					l.getStyleClass().add("HourLine");
					l.endXProperty().bind(widthProperty());
					l.endYProperty().bind(l.startYProperty());
					getChildren().add(l);
					hourLines.add(l);
				}
				// half hour line
				{
					Line l = new Line(0,10,100,10);
					l.getStyleClass().add("HalfHourLine");
					l.endXProperty().bind(widthProperty());
					l.endYProperty().bind(l.startYProperty());
					getChildren().add(l);
					halfHourLines.add(l);
				}
				// hour text
				{
					Text t = new Text(lHour + ":00");
					t.setTranslateY(t.getBoundsInParent().getHeight()); // move it under the line
					t.getStyleClass().add("HourLabel");
					t.setFontSmoothingType(FontSmoothingType.LCD);
					getChildren().add(t);
					hourTexts.add(t);
				}
			}

			// 7 days per week
			for (int i = 0; i < 7; i++)
			{
				DayPane lDay = new DayPane();
				getChildren().add(lDay);
				days.add(lDay);
			}
			
			// change the layout related to the size
			widthProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			heightProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			
			// layout
			relayout();
		}
		final List<DayPane> days = new ArrayList<DayPane>();
		final List<Text> hourTexts = new ArrayList<Text>();
		final List<Line> hourLines = new ArrayList<Line>();
		final List<Line> halfHourLines = new ArrayList<Line>();
		
		/**
		 * 
		 */
		private void relayout()
		{
			// position the hours
			for (int lHour = 0; lHour < 24; lHour++)
			{
				hourLines.get(lHour).startYProperty().set(lHour * hourHeight);
				halfHourLines.get(lHour).startXProperty().set(timeWidth);
				halfHourLines.get(lHour).startYProperty().set((lHour + 0.5) * hourHeight);
				hourTexts.get(lHour).xProperty().set(timeWidth - hourTexts.get(lHour).getBoundsInParent().getWidth());
				hourTexts.get(lHour).yProperty().set(lHour * hourHeight);
			}

			// postion the day panes
			for (int i = 0; i < 7; i++)
			{
				DayPane lDay = days.get(i);
				lDay.setLayoutX(dayFirstColumnX + (i * dayWidth));
				lDay.setLayoutY(0.0);
				lDay.setPrefSize(dayWidth, dayHeight);
			}
		}
	}
	
	
	/**
	 * Responsible for rendering the appointments within a day 
	 */
	class DayPane extends Pane
	{
		// know your header
		DayHeaderPane dayHeader = null;
		
		public DayPane()
		{
			// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");		
			getStyleClass().add("Day");
			
			// change the layout related to the size
			widthProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
			heightProperty().addListener(new InvalidationListener()
			{
				@Override
				public void invalidated(Observable arg0)
				{
					relayout();
				}
			});
		}
		ObjectProperty<Calendar> calendarObjectProperty = new SimpleObjectProperty<Calendar>(DayPane.this, "calendar");
		
		/**
		 * 
		 */
		private void relayout()
		{
			// first add all the whole day appointments
			int lWholedayCnt = 0;
			for (AppointmentPane lAppointmentPane : wholedayAppointmentPanes)
			{
				lAppointmentPane.setLayoutX(lWholedayCnt * wholedayAppointmentWidth);
				lAppointmentPane.setLayoutY(0);
				lAppointmentPane.setPrefSize(wholedayAppointmentWidth, dayHeight);
				lWholedayCnt++;
			}
			
			// then add all appointments to the day
			double lAppointmentsWidth = dayContentWidth - (lWholedayCnt * wholedayAppointmentWidth);			
			for (AppointmentPane lAppointmentPane : appointmentPanes)
			{
				int lOffsetY = (lAppointmentPane.start.get(Calendar.HOUR_OF_DAY) * 60) + lAppointmentPane.start.get(Calendar.MINUTE);
				lAppointmentPane.setLayoutX((lWholedayCnt * wholedayAppointmentWidth) + (lAppointmentsWidth / lAppointmentPane.clusterOwner.clusterTracks.size() * lAppointmentPane.clusterTrackIdx));
				lAppointmentPane.setLayoutY(dayHeight / (24 * 60) * lOffsetY );
				double lW = (dayContentWidth - (wholedayAppointmentPanes.size() * wholedayAppointmentWidth)) * (1.0 / (((double)lAppointmentPane.clusterOwner.clusterTracks.size())));
				if (lAppointmentPane.clusterTrackIdx < lAppointmentPane.clusterOwner.clusterTracks.size() - 1) lW *= 1.5;
				double lH = (dayHeight / (24 * 60) * (lAppointmentPane.durationInMS / 1000 / 60) );
				lAppointmentPane.setPrefSize(lW, lH);
			}
		}			

		/**
		 * This method prepares a day for being drawn.
		 * The appointments within one day might overlap, this method will create a data structure so it is clear how these overlapping appointments should be drawn.
		 * All appointments in one day are process based on their start time; earliest first, and if there are more with the same start time, longest duration first.
		 * The appointments are then place onto (parallel) tracks; an appointment initially is placed in track 1. 
		 * But if there is already an (partially overlapping) appointment there, then the appointment is placed in track 2. 
		 * Unless there also is an appointment already in track 2, then track 3 is tried, then track 4, track 5, until a free slot is found.
		 * For example (the letters are not the sequence in which the appointments are processed, they're just for identifying them):
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
			getChildren().removeAll(appointmentPanes);
			appointmentPanes.clear();
			getChildren().removeAll(wholedayAppointmentPanes);
			wholedayAppointmentPanes.clear();			
			if (calendarObjectProperty.get() == null) return;
			
			// scan all appointments and filter the ones for this day
			for (Agenda.Appointment lAppointment : getSkinnable().appointments())
			{				
				// check if the appointment falls in today
				// appointments may span multiple days, but the appointment pane will clamp the start and end date
				AppointmentPane lAppointmentPane = new AppointmentPane(calendarObjectProperty.get(), lAppointment);
				lAppointmentPane.day = this;
				if ( sameDay(calendarObjectProperty.get(), lAppointmentPane.start) 
				  && sameDay(calendarObjectProperty.get(), lAppointmentPane.end)
				   )
				{
					if (lAppointment.isWholeDay())
					{
						wholedayAppointmentPanes.add(lAppointmentPane);
					}
					else
					{
						appointmentPanes.add(lAppointmentPane);
					}
				}
			}
			
			// sort on start time and then decreasing duration
			Collections.sort(appointmentPanes, new Comparator<AppointmentPane>()
			{
				@Override
				public int compare(AppointmentPane o1, AppointmentPane o2)
				{
					if (o1.startAsString.equals(o2.startAsString) == false)
					{
						return o1.startAsString.compareTo(o2.startAsString);
					}
					return o1.durationInMS > o2.durationInMS ? -1 : 1;
				}
			});
			
			// start placing appointments in the tracks
			AppointmentPane lClusterOwner = null;
			for (AppointmentPane lAppointmentPane : appointmentPanes)
			{
				// if there is no cluster owner
				if (lClusterOwner == null)
				{
					// than the current becomes an owner
					// only create a minimal cluster, because it will be setup fully in the code below
					lClusterOwner = lAppointmentPane;
					lClusterOwner.clusterTracks = new ArrayList<List<AppointmentPane>>();
				}
				
				// in which track should it be added
				int lTrackNr = determineTrackWhereAppointmentCanBeAdded(lClusterOwner.clusterTracks, lAppointmentPane);
				// if it can be added to track 0, then we have a "situation". Track 0 could mean 
				// - we must start a new cluster
				// - the appointment is still linked to the running cluster by means of a linking appointment in the higher tracks
				if (lTrackNr == 0)
				{
					// So let's see if there is a linking appointment higher up
					boolean lOverlaps = false;
					for (int i = 1; i < lClusterOwner.clusterTracks.size() && lOverlaps == false; i++)
					{
						lOverlaps = checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(lClusterOwner.clusterTracks, i, lAppointmentPane);
					}
					
					// if it does not overlap, we start a new cluster
					if (lOverlaps == false)
					{
						lClusterOwner = lAppointmentPane;
						lClusterOwner.clusterMembers = new ArrayList<AppointmentPane>(); 
						lClusterOwner.clusterTracks = new ArrayList<List<AppointmentPane>>();
						lClusterOwner.clusterTracks.add(new ArrayList<AppointmentPane>());
					}
				}
				
				// add it to the track (and setup all other cluster data)
				lClusterOwner.clusterMembers.add(lAppointmentPane);
				lClusterOwner.clusterTracks.get(lTrackNr).add(lAppointmentPane);
				lAppointmentPane.clusterOwner = lClusterOwner;
				lAppointmentPane.clusterTrackIdx = lTrackNr;				
				// for debug  System.out.println("----"); for (int i = 0; i < lClusterOwner.clusterTracks.size(); i++) { System.out.println(i + ": " + lClusterOwner.clusterTracks.get(i) ); } System.out.println("----");
			}
			
			// and layout
			getChildren().addAll(wholedayAppointmentPanes);
			getChildren().addAll(appointmentPanes);
			relayout();
			
			// we're done, now have the header updated
			dayHeader.setupAppointments();
		}
		final List<AppointmentPane> appointmentPanes = new ArrayList<AppointmentPane>(); // all appointments that need to be drawn
		final List<AppointmentPane> wholedayAppointmentPanes = new ArrayList<AppointmentPane>(); // all appointments that need to be drawn
	}
	
	/**
	 * Responsible for rendering a single appointment on a single day.
	 * An appointment region is a representation of an appointment in one single day.
	 * Appointments may span multiple days, each day gets its own appointment region.
	 * 
	 */
	class AppointmentPane extends Pane
	{
		DayPane day = null;
		// for the role of cluster owner
		List<AppointmentPane> clusterMembers = null; 
		List<List<AppointmentPane>> clusterTracks = null;
		// for the role of cluster member
		AppointmentPane clusterOwner = null;
		int clusterTrackIdx = -1;

		/**
		 * 
		 * @param calendar
		 * @param appointment
		 */
		public AppointmentPane(Calendar calendar, Agenda.Appointment appointment)
		{
			// for debugging setStyle("-fx-border-color:BLUE;-fx-border-width:4px;");
			getStyleClass().add("Appointment");
			getStyleClass().add(appointment.getGroup());
			
			// remember
			this.appointment = appointment;
			
			// wholeday
			if (appointment.isWholeDay())
			{
				// start
				this.start = (Calendar)appointment.getStartTime().clone();
				this.start.set(Calendar.HOUR_OF_DAY, 0);
				this.start.set(Calendar.MINUTE, 0);
				this.start.set(Calendar.SECOND, 0);
				this.start.set(Calendar.MILLISECOND, 0);
				
				// end
				this.end = (Calendar)appointment.getStartTime().clone();
				this.end.set(Calendar.HOUR_OF_DAY, 23);
				this.end.set(Calendar.MINUTE, 59);
				this.end.set(Calendar.SECOND, 59);
				this.end.set(Calendar.MILLISECOND, 999);
			}
			else
			{
				// start
				Calendar lDayStartCalendar = (Calendar)calendar.clone();
				lDayStartCalendar.set(Calendar.HOUR_OF_DAY, 0);
				lDayStartCalendar.set(Calendar.MINUTE, 0);
				lDayStartCalendar.set(Calendar.SECOND, 0);
				lDayStartCalendar.set(Calendar.MILLISECOND, 0);
				this.start = (appointment.getStartTime().before(lDayStartCalendar) ? lDayStartCalendar : appointment.getStartTime());
				
				// end
				Calendar lDayEndCalendar = (Calendar)calendar.clone();
				lDayEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
				lDayEndCalendar.set(Calendar.MINUTE, 59);
				lDayEndCalendar.set(Calendar.SECOND, 59);
				lDayEndCalendar.set(Calendar.MILLISECOND, 999);
				this.end = (appointment.getEndTime().after(lDayEndCalendar) ? lDayEndCalendar : appointment.getEndTime());
			}
		
			// strings
			this.startAsString = (this.start.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.start.get(Calendar.HOUR_OF_DAY)
					   + ":"
					   + (this.start.get(Calendar.MINUTE) < 10 ? "0" : "" ) + this.start.get(Calendar.MINUTE)
					   ;
			this.endAsString = (this.end.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + this.end.get(Calendar.HOUR_OF_DAY)
							 + ":"
							 + (this.end.get(Calendar.MINUTE) < 10 ? "0" : "" ) + this.end.get(Calendar.MINUTE)
							 ;
			
			// duration
			durationInMS = this.end.getTimeInMillis() - this.start.getTimeInMillis();
			
			// only for whole day events
			if (appointment.isWholeDay() == false)
			{
				// add the duration as text
				double lPadding = 3;
				Text lTimeText = new Text(startAsString + "-" + endAsString);
				{
					lTimeText.getStyleClass().add("AppointmentTimeLabel");
					lTimeText.setX( lPadding );
					lTimeText.setY(lTimeText.prefHeight(0));
					Rectangle lClip = new Rectangle(0,0,0,0);
					lClip.widthProperty().bind(widthProperty().subtract(lPadding));
					lClip.heightProperty().bind(heightProperty());
					lTimeText.setClip(lClip);
					getChildren().add(lTimeText);
				}
				// add summary
				Text lSummaryText = new Text(appointment.getSummary());
				{
					lSummaryText.getStyleClass().add("AppointmentLabel");
					lSummaryText.setX( lPadding );
					lSummaryText.setY( lTimeText.getY() + textHeight1M);
					lSummaryText.wrappingWidthProperty().bind(widthProperty().subtract(lPadding));
					Rectangle lClip = new Rectangle(0,0,0,0);
					lClip.widthProperty().bind(widthProperty());
					lClip.heightProperty().bind(heightProperty().subtract(lPadding));
					lSummaryText.setClip(lClip);
					getChildren().add(lSummaryText);			
				}
			}
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
	}
	
	// ==================================================================================================================
	// SUPPORT

	/**
	 * 
	 */
	private void calculateSizes()
	{
		// generic
		double scrollbarSize = 15; // TODO: derive this from an actual ScrollBar.
		textHeight1M = new Text("X").getBoundsInParent().getHeight();
		
		// header
		maxNumberOfWholedayAppointments = 0;
		for (DayPane lDay : week.days)
		{
			if (lDay.wholedayAppointmentPanes.size() > maxNumberOfWholedayAppointments)
			{
				maxNumberOfWholedayAppointments = lDay.wholedayAppointmentPanes.size();
			}
		}
		titleCalendarHeight = 1.5 * textHeight1M; 
		wholedayTitleHeight = textHeight1M + 5; // not sure why the 5 is needed
		headerHeight = titleCalendarHeight + (maxNumberOfWholedayAppointments * wholedayTitleHeight);

		// time column
		int lTimeColumnWhitespace = 10;
		timeWidth = new Text("88:88").getBoundsInParent().getWidth() + lTimeColumnWhitespace;
		
		// day columns
		dayFirstColumnX = timeWidth + lTimeColumnWhitespace;
		dayWidth = (getSkinnable().getWidth() - timeWidth - scrollbarSize) / 7;
		if (weekScrollPane.viewportBoundsProperty().get() != null) 
		{
			dayWidth = (weekScrollPane.viewportBoundsProperty().get().getWidth() - timeWidth) / 7;
		}
		dayContentWidth = dayWidth - 10;
		
		// hour height
		hourHeight = (2 * textHeight1M) + 10; // 10 is padding
		if (weekScrollPane.viewportBoundsProperty().get() != null && (weekScrollPane.viewportBoundsProperty().get().getHeight() - scrollbarSize) > hourHeight * 24)
		{
			// if there is more room than absolutely required, let the height grow with the available room
			hourHeight = (weekScrollPane.viewportBoundsProperty().get().getHeight() - scrollbarSize) / 24;
		}
		dayHeight = hourHeight * 24;
		
		// if the viewport is active
		if (weekScrollPane.viewportBoundsProperty() != null && weekScrollPane.viewportBoundsProperty().get() != null)
		{
			// make sure the border pane uses these sizes
			weekHeader.setPrefSize(weekScrollPane.viewportBoundsProperty().get().getWidth(), headerHeight);
			week.setPrefSize(weekScrollPane.viewportBoundsProperty().get().getWidth(), 24 * hourHeight);				
		}
	}
	double textHeight1M = 0;
	double titleCalendarHeight = 0;
	double headerHeight = 0;
	int maxNumberOfWholedayAppointments = 0;
	double wholedayTitleHeight = 0;
	double wholedayAppointmentWidth = 5;
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
	 * @param appointmentPane
	 * @return
	 */
	private int determineTrackWhereAppointmentCanBeAdded(List<List<AppointmentPane>> tracks, AppointmentPane appointmentPane)
	{
		int lTrackNr = 0;
		while (true)
		{
			// make sure there is a arraylist for this track
			if (lTrackNr == tracks.size()) tracks.add(new ArrayList<AppointmentPane>());
			
			// scan all existing appointments in this track and see if there is an overlap
			if (checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(tracks, lTrackNr, appointmentPane) == false)
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
	 * @param appointmentPane
	 * @return
	 */
	private boolean checkIfTheAppointmentOverlapsAnAppointmentAlreadyInThisTrack(List<List<AppointmentPane>> tracks, int tracknr, AppointmentPane appointmentPane)
	{
		// get the track
		List<AppointmentPane> lTrack = tracks.get(tracknr);
		
		// scan all existing appointments in this track
		for (AppointmentPane lAppointmentPane : lTrack)
		{
			// There is an overlap:
			// if the start time of the already placed appointment is before or equals the new appointment's end time 
			// and the end time of the already placed appointment is after or equals the new appointment's start time
			// ...PPPPPPPPP...
			// .NNNN.......... -> Ps <= Ne & Pe >= Ns -> overlap
			// .....NNNNN..... -> Ps <= Ne & Pe >= Ns -> overlap
			// ..........NNN.. -> Ps <= Ne & Pe >= Ns -> overlap
			// .NNNNNNNNNNNNN. -> Ps <= Ne & Pe >= Ns -> overlap
			// .N............. -> false	& Pe >= Ns -> no overlap
			// .............N. -> Ps <= Ne & false	-> no overlap
			if ( (lAppointmentPane.start.equals(appointmentPane.start) || lAppointmentPane.start.before(appointmentPane.end)) 
			  && (lAppointmentPane.end.equals(appointmentPane.start) || lAppointmentPane.end.after(appointmentPane.start))
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
