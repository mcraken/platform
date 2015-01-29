/**
 * 
 */
package org.mcplissken.repository.index;



/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public interface IndexPorter<T> {

	public void port(T model) throws IndexException;
	
	public void delete(T model) throws IndexException;
	
	public void deleteById(String id) throws IndexException;
	
	public void commit() throws IndexException;

	public void shutdown();

}
