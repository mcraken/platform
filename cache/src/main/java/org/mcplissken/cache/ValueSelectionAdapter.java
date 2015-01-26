/**
 * 
 */
package org.mcplissken.cache;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 3, 2014
 */
public interface ValueSelectionAdapter extends SelectionAdapter{

	public ValueSelectionAdapter eq(String name, Object value);
	
	public ValueSelectionAdapter in(String name, Object[] values);
	
	public ValueSelectionAdapter orderBy(boolean asc, String name);
	
}
