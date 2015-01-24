/**
 * 
 */
package org.mcplissken.schedule.tracks;

import org.mcplissken.reporting.SystemReportingService;
import org.mcplissken.schedule.JobBuildResult;
import org.mcplissken.schedule.JobBuilder;
import org.mcplissken.schedule.ScheduleContext;
import org.mcplissken.schedule.ScheduleService;
import org.mcplissken.schedule.ScheduledJob;
import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 29, 2014
 */
public abstract class Track implements ScheduledJob{

	public static final String TRACKS_SCHEDULE = "tracks";

	public static final String TRACKS_PREFIX = "/broadcast/tracks/";

	private static final long serialVersionUID = 1L;

	protected ScheduleService scheduleService;

	protected String trackName;

	protected SystemReportingService reportingService;

	/**
	 * 
	 */
	public Track() {

	}

	public Track(ScheduleService scheduleService, String trackName,
			SystemReportingService reportingService) {

		this.scheduleService = scheduleService;

		this.trackName = trackName;

		this.reportingService = reportingService;
	}


	/**
	 * @param scheduleService the scheduleService to set
	 */
	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	/**
	 * @param trackName the trackName to set
	 */
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public void setReportingService(SystemReportingService reportingService) {
		this.reportingService = reportingService;
	}

	public void init(){

		try {

			initTrack();

			startTrack();

		} catch (Exception e) {

			reportingService.exception(getClass().getSimpleName(), SystemReportingService.FILE, e);
		}
	}

	public void startTrack() throws TrackException {
		
		try {
			
			JobBuildResult result = scheduleService.buildJob(trackName, TRACKS_SCHEDULE, this, true);

			if(!result.scheduled){

				reportingService.info(getClass().getSimpleName(), SystemReportingService.CONSOLE, "Scheduling track " + trackName);

				scheduleTrack(result.builder);
			}
			
		} catch (ScheduleException e) {
			
			throw new TrackException(e);
		}
	}

	public void stopTrack() throws TrackException{

		try {

			scheduleService.removeJob(trackName, TRACKS_SCHEDULE);

		} catch (ScheduleException e) {

			throw new TrackException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.ScheduledJob#execute(com.mubasher.market.schedule.ScheduleContext)
	 */
	@Override
	public void execute(ScheduleContext context) throws ScheduleException {

		try {

			executeTrack();

		} catch (TrackException e) {

			reportingService.exception(getClass().getSimpleName(), SystemReportingService.CONSOLE, e);
		}

	}

	protected abstract void scheduleTrack(JobBuilder jobBuilder) throws ScheduleException;

	protected abstract void executeTrack() throws TrackException;

	public abstract void initTrack() throws TrackException;

}
