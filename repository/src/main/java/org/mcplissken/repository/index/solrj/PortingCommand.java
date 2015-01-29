/**
 * 
 */
package org.mcplissken.repository.index.solrj;

import org.mcplissken.repository.index.IndexException;
import org.mcplissken.repository.index.IndexPorter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Jan 29, 2015
 */
public abstract class PortingCommand<T> {
	
	public abstract void execute(T model, IndexPorter<T> porter) throws IndexException;
}