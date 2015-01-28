/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mcplissken.schedule.tracks;

import org.mcplissken.schedule.JobBuilder;
import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
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
	 * @see org.mcplissken.schedule.tracks.Track#scheduleTrack(org.mcplissken.schedule.JobBuilder)
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
