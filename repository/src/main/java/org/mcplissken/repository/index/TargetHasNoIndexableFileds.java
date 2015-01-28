/**
 * 
 */
package org.mcplissken.repository.index;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 28, 2015
 */
public class TargetHasNoIndexableFileds extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public TargetHasNoIndexableFileds() {
		
		super("At least one filed should has Index annotation present.");
		
	}

}
