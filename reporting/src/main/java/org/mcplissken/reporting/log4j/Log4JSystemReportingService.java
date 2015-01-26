/**
 * 
 */
package org.mcplissken.reporting.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mcplissken.reporting.SystemReportingService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 2, 2014
 */
public class Log4JSystemReportingService implements SystemReportingService {
	
	private String path;
	
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	public void init(){

		PropertyConfigurator.configure(path);
		
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.system.reporting.SystemReportingService#info(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void info(String reporter, String channel, String message) {
		
		Logger logger = Logger.getLogger(channel);
		
		logger.info(reporter + "|" + message);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.system.reporting.SystemReportingService#exception(java.lang.String, java.lang.String, java.lang.Exception)
	 */
	@Override
	public void exception(String reporter, String channel, Throwable ex) {

		Logger logger = Logger.getLogger(channel);

		logger.info(reporter, ex);
	}

}
