/**
 * 
 */
package org.mcplissken.repository.exception;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 14, 2014
 */
public class NoResultException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NoResultException() {
		super("Query could not return any results.");
	}
	
	public NoResultException(Throwable e){
		super("Query could not return any results." + e);
	}
}
