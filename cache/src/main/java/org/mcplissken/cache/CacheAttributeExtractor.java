/**
 * 
 */
package org.mcplissken.cache;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 27, 2015
 */
public abstract class CacheAttributeExtractor<T> {

	private String attributeName;

	public CacheAttributeExtractor(String attributeName) {
		
		this.attributeName = attributeName;
	}
	
	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}
	
	public abstract Object extract(T cachedObject);
	
}
