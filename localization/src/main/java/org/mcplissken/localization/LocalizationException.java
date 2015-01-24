/**
 * 
 */
package org.mcplissken.localization;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 21, 2015
 */
public class LocalizationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public LocalizationException(Throwable e) {
		super("Error in localization operation", e);
	}

}
