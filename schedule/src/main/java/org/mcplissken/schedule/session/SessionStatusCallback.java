/**
 * 
 */
package org.mcplissken.schedule.session;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Oct 12, 2014
 */
public interface SessionStatusCallback {
	
	public void inSession();
	
	public void outSession();
	
	public void startSession();
	
	public void endSession();
}
