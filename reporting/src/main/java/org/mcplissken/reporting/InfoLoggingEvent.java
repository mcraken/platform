/**
 * 
 */
package org.mcplissken.reporting;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
	 * @see com.mubasher.system.reporting.LoggingEvent#execute()
	 */
	@Override
	public void execute() {
		reportingService.info(reporter, channel, message);
	}

}
