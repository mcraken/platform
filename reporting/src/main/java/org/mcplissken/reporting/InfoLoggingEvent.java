/**
 * 
 */
package org.mcplissken.reporting;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 3, 2014
 */
public class InfoLoggingEvent extends BasicLoggingEvent {
	
	private String message;
	
	public InfoLoggingEvent(String reporter, String channel,
			SystemReportingService reportingService, String message) {
		
		super(reporter, channel, reportingService);
		
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.system.reporting.LoggingEvent#execute()
	 */
	@Override
	public void execute() {
		reportingService.info(reporter, channel, message);
	}

}
