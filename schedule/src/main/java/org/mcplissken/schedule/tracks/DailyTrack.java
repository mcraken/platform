/**
 * 
 */
package org.mcplissken.schedule.tracks;

import org.mcplissken.schedule.JobBuilder;
import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 8, 2014
 */
public abstract class DailyTrack extends Track {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int hour;

	private int minute;

	private String calendarName;
	
	public DailyTrack(){
	}
	
	public DailyTrack(int hour, int minute, String calendarName) {

		this.hour = hour;

		this.minute = minute;

		this.calendarName = calendarName;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	/**
	 * @param calendarName the calendarName to set
	 */
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.tracks.Track#scheduleTrack(com.mubasher.market.schedule.JobBuilder)
	 */
	@Override
	protected void scheduleTrack(JobBuilder jobBuilder)
			throws ScheduleException {

		jobBuilder.daily(hour, minute);

		if(calendarName != null){

			jobBuilder.scheulde(calendarName);
			
		} else {
			jobBuilder.scheulde();
		}


	}

}
