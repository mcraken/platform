/**
 * 
 */
package org.mcplissken.reporting;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 2, 2014
 */
public interface SystemReportingService {
	
	public static final String CONSOLE = "console";
	public static final String FILE = "file";
	public static final String MAIL = "mail";
	
	public void info(String reporter, String channel, String message);
	
	public void exception(String reporter, String channel, Throwable ex);
	
}
