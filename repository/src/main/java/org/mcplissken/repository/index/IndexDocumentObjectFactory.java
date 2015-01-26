/**
 * 
 */
package org.mcplissken.repository.index;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 19, 2014
 */
public interface IndexDocumentObjectFactory<T> {
	public T createDocument();
}
