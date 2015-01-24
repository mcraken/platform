/**
 * 
 */
package org.mcplissken.schedule;

import org.mcplissken.reporting.SystemReportingService;
import org.mcplissken.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Dec 14, 2014
 */
public abstract class SecondlyScheduledChainJob extends ScheduledChainJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int interval;
	private int repitions;
	
	public SecondlyScheduledChainJob(ScheduleService scheduleService,
			String jobName, SystemReportingService reportingService,
			ScheduledChainJob nextJob, int interval, int repitions) {
		
		super(scheduleService, jobName, reportingService, nextJob);
		
		this.interval = interval;
		
		this.repitions = repitions;
	}



	public void schedule() {
		
		JobBuildResult result;
		
		try {
			
			result = scheduleService.buildJob(jobName, SCHEDULE_NAME, this, true);
			
			result.builder.secondly(repitions, interval).scheulde();
			
		} catch (ScheduleException e) {
			
			reportingService.exception(getClass().getSimpleName(), SystemReportingService.FILE, e);

		}
	}


}
