/**
 * 
 */
package org.mcplissken.repository.index;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public class QueryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public QueryException(Throwable e) {
		
		super("Error while querying index", e);
	}

}
