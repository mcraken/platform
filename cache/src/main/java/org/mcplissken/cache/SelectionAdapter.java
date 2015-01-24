/**
 * 
 */
package org.mcplissken.cache;

import java.util.Map;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 4, 2014
 */
public interface SelectionAdapter {

	public abstract SelectionAdapter page(int size);

	public abstract Map<Object, Object> result();
	
}