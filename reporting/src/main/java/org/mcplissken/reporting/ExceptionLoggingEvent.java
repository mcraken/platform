/**
 * 
 */
package org.mcplissken.reporting;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.system.reporting.LoggingEvent#execute()
	 */
	@Override
	public void execute() {
		
		reportingService.exception(reporter, channel, error);
	}

}
