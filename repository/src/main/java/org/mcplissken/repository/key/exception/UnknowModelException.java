/**
 * 
 */
package org.mcplissken.repository.key.exception;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 7, 2014
 */
public class UnknowModelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public UnknowModelException(String modelName) {
		super("Unknow model: " + modelName);
	}

}
