/**
 * 
 */
package org.mcplissken.reporting;

import java.util.Map;

import org.mcplissken.disruptor.workerpool.WorkerPoolOperation;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 3, 2014
 */
public class SystemReportingServiceProxy implements SystemReportingService {

	private WorkerPoolOperation<LoggingEvent> loggingWorkersPool;
	private int concurrency;
	private LoggingWorkHandler[] loggingHandlers;
	private Map<String, SystemReportingService> reportingServices;
	
	/**
	 * @param concurrency the concurrency to set
	 */
	public void setConcurrency(int concurrency) {
		this.concurrency = concurrency;
	}
	
	/**
	 * @param reportingServices the reportingServices to set
	 */
	public void setReportingServices(
			Map<String, SystemReportingService> reportingServices) {
		
		this.reportingServices = reportingServices;
	}
	
	public void init(){
		
		loggingHandlers = new LoggingWorkHandler[concurrency];
		
		for(int i = 0; i < concurrency; i++)
			loggingHandlers[i] = new LoggingWorkHandler();
		
		loggingWorkersPool = new WorkerPoolOperation<>(loggingHandlers);
		
		loggingWorkersPool.start();
	}
	
	public void destroy(){
		
		loggingWorkersPool.stop();
	}
	
	/* (non-Javadoc)
	 * @see com.mubasher.system.reporting.SystemReportingService#info(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void info(String reporter, String channel, String message) {
		
		loggingWorkersPool.onData(new InfoLoggingEvent(reporter, channel, reportingServices.get(channel), message));
	}

	/* (non-Javadoc)
	 * @see com.mubasher.system.reporting.SystemReportingService#exception(java.lang.String, java.lang.String, java.lang.Exception)
	 */
	@Override
	public void exception(String reporter, String channel, Throwable ex) {
		
		loggingWorkersPool.onData(new ExceptionLoggingEvent(reporter, channel, reportingServices.get(channel), ex));
	}

}
