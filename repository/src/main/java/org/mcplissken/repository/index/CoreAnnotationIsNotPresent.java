/**
 * 
 */
package org.mcplissken.repository.index;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 28, 2015
 */
public class CoreAnnotationIsNotPresent extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public CoreAnnotationIsNotPresent() {
		
		super("You must add core annotation for indexable models");
	}

}
