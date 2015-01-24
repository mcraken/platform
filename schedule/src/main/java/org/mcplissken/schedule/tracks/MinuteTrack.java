/**
 * 
 */
package org.mcplissken.schedule.tracks;

import org.mcplissken.schedule.JobBuilder;
import org.mcplissken.schedule.exception.ScheduleException;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.market.schedule.tracks.Track#scheduleTrack(com.mubasher.market.schedule.JobBuilder)
	 */
	@Override
	protected void scheduleTrack(JobBuilder jobBuilder) throws ScheduleException {
		
		jobBuilder.secondly(minutes * 60)
		.ignoreMisfires()
		.scheulde();
	}
	
}
