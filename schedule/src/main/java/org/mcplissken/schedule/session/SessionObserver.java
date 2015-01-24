/**
 * 
 */
package org.mcplissken.schedule.session;

import org.mcplissken.disruptor.DisruptorEventHandler;
import org.mcplissken.reporting.SystemReportingService;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 12, 2014
 */
public abstract class SessionObserver extends DisruptorEventHandler<SessionStatus> implements SessionStatusCallback{
	
	protected SystemReportingService reportingService;
	
	/**
	 * @param reportingService the reportingService to set
	 */
	public void setReportingService(SystemReportingService reportingService) {
		
		this.reportingService = reportingService;
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.disruptor.DisruptorEventHandler#consume(java.lang.Object)
	 */
	@Override
	protected void consume(SessionStatus status) {
		
		status.apply(this);
	}
	
	protected abstract void init();
	
}
