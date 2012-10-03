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
package jfxtras.labs.scene.control;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Tom Eugelink
 */
public class AgendaTrial1 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

        // add a node
		Agenda lAgenda = new Agenda();		
    	//lAgenda.setLocale(new java.util.Locale("de")); // weeks starts on monday
		int lTodayYear = Calendar.getInstance().get(Calendar.YEAR);
		int lTodayMonth = Calendar.getInstance().get(Calendar.MONTH);
		int lTodayDay = Calendar.getInstance().get(Calendar.DATE);
		lAgenda.appointments().addAll(
		/*
		 *  . . . .
		 *  . . . . 
		 *  A . . .  8:00
		 *  A B C .  8:30
		 *  A B C D  9:00
		 *  A B . D  9:30
		 *  A . . D 10:00
		 *  A E . D 10:30
		 *  A . . D 11:00
		 *  . . . D 11:30
		 *  . . . D 12:00
		 *  F . . D 12:30
		 *  F H . D 13:00
		 *  . . . . 13:30
		 *  G . . . 14:00
		 *  . . . . 14:30
		 * 
		 */
			new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 00))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 11, 30))
				.withSummary("A")
				.withDescription("A much longer test description")
				.withGroup("group1")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 10, 00))
				.withSummary("B")
				.withDescription("A description 2")
				.withGroup("group2")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 9, 30))
				.withSummary("C")
				.withDescription("A description 3")
				.withGroup("group3")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 9, 00))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
				.withSummary("D")
				.withDescription("A description 4")
				.withGroup("group1")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 10, 30))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 11, 00))
				.withSummary("E")
				.withDescription("A description 4")
				.withGroup("group1")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 12, 30))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
				.withSummary("F")
				.withDescription("A description 4")
				.withGroup("group1")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 00))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
				.withSummary("H")
				.withDescription("A description 4")
				.withGroup("group1")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 14, 00))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 14, 30))
				.withSummary("G")
				.withDescription("A description 4")
				.withGroup("group1")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 15, 00))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
				.withSummary("I")
				.withDescription("A description 4")
				.withGroup("group1")
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 15, 30))
				.withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
				.withSummary("J")
				.withDescription("A description 4")
				.withGroup("group1")
		// -----
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 00, 00))
				.withSummary("all day")
				.withDescription("A description")
				.withGroup("group2")
				.withWholeDay(true)
		, 	new Agenda.AppointmentImpl()
				.withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 00, 00))
				.withSummary("all day2")
				.withDescription("A description3")
				.withGroup("group3")
				.withWholeDay(true)
		);
		
        
        // create scene
        Scene scene = new Scene(lAgenda, 900, 900);

        // create stage
        stage.setTitle("Agenda");
        stage.setScene(scene);
        stage.show();	
    }
}
