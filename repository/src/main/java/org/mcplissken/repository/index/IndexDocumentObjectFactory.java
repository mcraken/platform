/**
 * 
 */
package org.mcplissken.repository.index;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 19, 2014
 */
public interface IndexDocumentObjectFactory<T> {
	public T createDocument();
}
