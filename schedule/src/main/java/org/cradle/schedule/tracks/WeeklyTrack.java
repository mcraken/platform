/**
 * 
 */
package org.cradle.schedule.tracks;

import org.cradle.reporting.SystemReportingService;
import org.cradle.schedule.JobBuilder;
import org.cradle.schedule.ScheduleService;
import org.cradle.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 29, 2014
 */
public abstract class WeeklyTrack extends Track {

	private static final long serialVersionUID = 1L;
	
	private int minute;
	
	private int hour;
	
	private int day;

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * 
	 */
	public WeeklyTrack() {
	}
	

	public WeeklyTrack(ScheduleService scheduleService, String trackName,
			SystemReportingService reportingService, int minute, int hour,
			int day) {
		
		super(scheduleService, trackName, reportingService);
		
		this.minute = minute;
		this.hour = hour;
		this.day = day;
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.tracks.Track#scheduleTrack(org.cradle.schedule.JobBuilder)
	 */
	@Override
	protected void scheduleTrack(JobBuilder jobBuilder) throws ScheduleException {
		
		jobBuilder
		.weekly(day, hour, minute)
		.ignoreMisfires()
		.scheulde();
	}
}
