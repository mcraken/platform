/**
 * 
 */
package org.mcplissken.repository.index;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public interface IndexPorter {
	
	public void port(Object target) throws IndexException;
	
	public void commit() throws IndexException;
	
	public void shutdown();
	
	public void delete(String id) throws IndexException;
}
