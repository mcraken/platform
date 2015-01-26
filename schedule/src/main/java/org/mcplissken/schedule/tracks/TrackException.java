/**
 * 
 */
package org.mcplissken.schedule.tracks;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 23, 2014
 */
public class TrackException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public TrackException(Throwable e) {
		super("Error while executing track", e);
	}

}
