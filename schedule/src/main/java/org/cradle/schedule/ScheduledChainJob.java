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
package org.cradle.schedule;

import org.cradle.reporting.SystemReportingService;
import org.cradle.schedule.exception.ScheduleException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
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
	 * @see org.cradle.schedule.ScheduledJob#execute(org.cradle.schedule.ScheduleContext)
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
