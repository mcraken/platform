/**
 * 
 */
package org.cradle.reporting;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 3, 2014
 */
public abstract class BasicLoggingEvent implements LoggingEvent {
	
	protected String reporter;
	protected String channel;
	protected SystemReportingService reportingService;
	
	public BasicLoggingEvent(String reporter, String channel,
			SystemReportingService reportingService) {
		this.reporter = reporter;
		this.channel = channel;
		this.reportingService = reportingService;
	}
	
}
