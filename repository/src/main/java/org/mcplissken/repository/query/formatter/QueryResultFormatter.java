/**
 * 
 */
package org.mcplissken.repository.query.formatter;

import java.util.Map;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 23, 2014
 */
public interface QueryResultFormatter {
	
	public Map<String, Object> format(Map<String, Object> result);
}
