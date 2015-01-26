/**
 * 
 */
package org.mcplissken.cache;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 2, 2014
 */
public interface KeySelectionAdapter extends SelectionAdapter {
	
	public SelectionAdapter eq(Object value);
	
	public SelectionAdapter like(Object value);
	
	public SelectionAdapter in(Object[] values);
	
	public SelectionAdapter orderBy(boolean asc);
	
}
