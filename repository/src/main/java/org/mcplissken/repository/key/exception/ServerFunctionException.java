/**
 * 
 */
package org.mcplissken.repository.key.exception;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 9, 2014
 */
public class ServerFunctionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public ServerFunctionException(Throwable e) {
		super("Function execetion failed", e);
	}
}
