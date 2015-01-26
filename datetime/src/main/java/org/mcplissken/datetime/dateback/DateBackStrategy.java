/**
 * 
 */
package org.mcplissken.datetime.dateback;

import org.mcplissken.datetime.DateTimeOperation;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 17, 2014
 */
public interface DateBackStrategy {
	
	public DateTimeOperation back(DateTimeOperation start, int amount);
	
	public DateTimeOperation shift(DateTimeOperation start);
}
