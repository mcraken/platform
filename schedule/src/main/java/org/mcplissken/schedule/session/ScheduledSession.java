/**
 * 
 */
package org.mcplissken.schedule.session;

import org.mcplissken.reporting.SystemReportingService;
import org.mcplissken.schedule.JobBuildResult;
import org.mcplissken.schedule.JobBuilder;
import org.mcplissken.schedule.ScheduleContext;
import org.mcplissken.schedule.ScheduleService;
import org.mcplissken.schedule.ScheduledJob;
import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 9, 2014
 */
public abstract class ScheduledSession {

	public static final String SESSION_SCHEDULE = "sessions";

	public static final String TRACKS_PREFIX = "/broadcast/sessions/";

	private String sessionName;

	private int startHour;
	private int startMinute;

	private int endHour;
	private int endMinute;

	private String startCalendar;

	private String endCalendar;

	protected ScheduleService scheduleService;

	protected SystemReportingService reportingService;

	/**
	 * @param sessionName the sessionName to set
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	/**
	 * @param reportingService the reportingService to set
	 */
	public void setReportingService(SystemReportingService reportingService) {
		this.reportingService = reportingService;
	}

	/**
	 * @param scheduleService the scheduleService to set
	 */
	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	/**
	 * @param startCalendar the startCalendar to set
	 */
	public void setStartCalendar(String startCalendar) {
		this.startCalendar = startCalendar;
	}

	/**
	 * @param endCalendar the endCalendar to set
	 */
	public void setEndCalendar(String endCalendar) {
		this.endCalendar = endCalendar;
	}

	public void init(){

		try {
			
			initSession();
			
			scheduleSessionStart();

			scheduleSessionEnd();

			long nextStart = scheduleService.getFireTime("start." + sessionName, SESSION_SCHEDULE).getTime();

			long nextEnd = scheduleService.getFireTime("end." + sessionName, SESSION_SCHEDULE).getTime();
			

			if(nextStart > nextEnd){
				
				inSession();

			} else {

				outSession();
			}
			
		} catch (Exception e) {

			reportingService.exception(getClass().getSimpleName(), SystemReportingService.FILE, e);
		}

	}

	public void scheduleSessionEnd() throws ScheduleException {

		scheduleDailyJob("end." + sessionName, endHour, endMinute, new ScheduledJob() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(ScheduleContext context) throws ScheduleException {

				try {
					
					endSession();
					
				} catch (SessionException e) {
					
					reportingService.exception(getClass().getSimpleName(), SystemReportingService.MAIL, e);
				}
			}
		}, endCalendar);
	}

	public void scheduleSessionStart() throws ScheduleException {

		scheduleDailyJob("start." + sessionName, startHour, startMinute, new ScheduledJob() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void execute(ScheduleContext context) throws ScheduleException {

				try {
					
					startSession();
					
				} catch (SessionException e) {
					
					reportingService.exception(getClass().getSimpleName(), SystemReportingService.MAIL, e);
				}
			}
		}, startCalendar);
	}

	private void scheduleDailyJob(String name, int hour, int minute, ScheduledJob job, String calendarName) throws ScheduleException {

		JobBuildResult result = scheduleService.buildJob(name, SESSION_SCHEDULE, job, true);

		if(!result.scheduled){

			JobBuilder jobBuilder = result.builder;

			jobBuilder
			.daily(hour, minute)
			.inTimeZone("GMT")
			.runOnceOnMisfire();

			jobBuilder.scheulde(calendarName);
		}
	}

	public String getSessionName() {
		return sessionName;
	}
	
	protected abstract void initSession() throws SessionException;

	protected abstract void startSession() throws SessionException;

	protected abstract void inSession() throws SessionException;

	protected abstract void outSession() throws SessionException;

	protected abstract void endSession() throws SessionException;
}
