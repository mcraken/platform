/**
 * 
 */
package org.cradle.schedule.session;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Oct 9, 2014
 */
public class SessionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public SessionException(Throwable e) {
		super("Session error", e);
	}

}
