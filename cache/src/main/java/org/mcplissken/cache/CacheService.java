/**
 * 
 */
package org.mcplissken.cache;

import org.mcplissken.repository.key.RestSearchKey;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;
import org.mcplissken.repository.key.exception.InvalidCriteriaSyntaxException;
import org.mcplissken.repository.key.exception.UnknowModelException;
import org.mcplissken.repository.models.RestModel;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 4, 2014
 */
public interface CacheService {

	public Object read(String modelName, Object key);
	
	public Object read(RestSearchKey key) throws InvalidCriteriaSyntaxException, InvalidCriteriaException, UnknowModelException;
	
	public KeySelectionAdapter createKeySelectionAdapter(String cacheName);
	
	public ValueSelectionAdapter createValueSelectionAdapter(String cacheName);
	
	public void write(String cacheName, Object key, Object modelObject); 

	public void writeThrough(String cacheName, Object key, Object modelObject);

	public void write(String cacheName, RestModel[] models);
	
	public boolean remove(String cacheName, Object key);

}