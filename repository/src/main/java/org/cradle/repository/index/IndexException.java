/**
 * 
 */
package org.cradle.repository.index;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public class IndexException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public IndexException(Throwable e) {
		super("Error while indexing target", e);
	}

}
