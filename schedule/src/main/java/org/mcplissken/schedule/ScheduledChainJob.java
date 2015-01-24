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
public abstract class ScheduledChainJob implements ScheduledJob{
	
	private static final long serialVersionUID = 1L;
	
	protected static final String SCHEDULE_NAME = "chains";
	
	protected ScheduleService scheduleService;
	protected String jobName;
	protected SystemReportingService reportingService;
	private ScheduledChainJob nextJob;
	
	public ScheduledChainJob(ScheduleService scheduleService, String jobName,
			SystemReportingService reportingService, ScheduledChainJob nextJob) {
		
		this.scheduleService = scheduleService;
		
		this.jobName = jobName;
		
		this.reportingService = reportingService;
		
		this.nextJob = nextJob;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.schedule.ScheduledJob#execute(com.mubasher.market.schedule.ScheduleContext)
	 */
	@Override
	public void execute(ScheduleContext context) throws ScheduleException {
		
		executeChain();
		
		if(context.getNextFireTime() == null && nextJob != null){
			
			end();
			
			nextJob.schedule();
			
		}
		
	}

	public abstract void schedule();
	
	protected abstract void executeChain();
	
	protected abstract void end();
}
