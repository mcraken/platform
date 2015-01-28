/**
 * 
 */
package org.mcplissken.repository;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 28, 2015
 */
public class UnknowRepositoryTargetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public UnknowRepositoryTargetException(String name) {
		super("Target repository: " + name + " is unknown or not supported.");
		
	}
}
