/**
 * 
 */
package org.mcplissken.schedule.exception;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 11, 2014
 */
public class ScheduleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public ScheduleException(Throwable e) {
		super("Error occured while executing job", e);
	}

}
