/**
 * 
 */
package org.mcplissken.schedule.tracks;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
