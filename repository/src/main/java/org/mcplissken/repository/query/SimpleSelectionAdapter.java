/**
 * 
 */
package org.mcplissken.repository.query;

import java.util.List;

import org.mcplissken.repository.exception.NoResultException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Aug 31, 2014
 */
public interface SimpleSelectionAdapter<T> {
	
	public SimpleSelectionAdapter<T> eq(String name, Object value);
	
	public SimpleSelectionAdapter<T> gt(String name, Object value);
	
	public SimpleSelectionAdapter<T> gte(String name, Object value);
	
	public SimpleSelectionAdapter<T> and();
	
	public SimpleSelectionAdapter<T> or();
	
	public SimpleSelectionAdapter<T> orderBy(boolean asc, String... columns);
	
	public SimpleSelectionAdapter<T> page(int size);
	
	public SimpleSelectionAdapter<T> lt(String name, Object value);
	
	public List<T> result() throws NoResultException;

}
