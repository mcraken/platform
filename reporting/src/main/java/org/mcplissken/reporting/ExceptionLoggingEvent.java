/**
 * 
 */
package org.mcplissken.reporting;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 3, 2014
 */
public class ExceptionLoggingEvent extends BasicLoggingEvent {
	
	private Throwable error;
	
	public ExceptionLoggingEvent(String reporter, String channel,
			SystemReportingService reportingService, Throwable error) {
		
		super(reporter, channel, reportingService);
		
		this.error = error;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.system.reporting.LoggingEvent#execute()
	 */
	@Override
	public void execute() {
		
		reportingService.exception(reporter, channel, error);
	}

}
