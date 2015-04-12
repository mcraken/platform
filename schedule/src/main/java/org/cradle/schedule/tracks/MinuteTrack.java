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
package org.cradle.schedule.tracks;

import org.cradle.schedule.JobBuilder;
import org.cradle.schedule.exception.ScheduleException;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 22, 2014
 */
public abstract class MinuteTrack extends Track {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected int minutes;


	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/* (non-Javadoc)
	 * @see org.cradle.schedule.tracks.Track#scheduleTrack(org.cradle.schedule.JobBuilder)
	 */
	@Override
	protected void scheduleTrack(JobBuilder jobBuilder) throws ScheduleException {
		
		jobBuilder.secondly(minutes * 60)
		.ignoreMisfires()
		.scheulde();
	}
	
}
