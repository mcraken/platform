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
public interface SimpleSelectionAdapter {
	
	public SimpleSelectionAdapter eq(String name, Object value);
	
	public SimpleSelectionAdapter gt(String name, Object value);
	
	public SimpleSelectionAdapter gte(String name, Object value);
	
	public SimpleSelectionAdapter and();
	
	public SimpleSelectionAdapter or();
	
	public SimpleSelectionAdapter orderBy(boolean asc, String... columns);
	
	public SimpleSelectionAdapter page(int size);
	
	public SimpleSelectionAdapter lt(String name, Object value);
	
	public List<?> result() throws NoResultException;

}
