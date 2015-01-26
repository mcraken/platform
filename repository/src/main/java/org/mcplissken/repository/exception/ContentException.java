/**
 * 
 */
package org.mcplissken.repository.exception;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Dec 1, 2014
 */
public class ContentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public ContentException(Throwable e) {
		
		super("Error while processing content", e);
	}

}
