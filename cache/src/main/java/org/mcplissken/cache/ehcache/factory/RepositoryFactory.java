/**
 * 
 */
package org.mcplissken.cache.ehcache.factory;

import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

import org.mcplissken.repository.ModelRepository;
import org.mcplissken.repository.key.RestSearchKey;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 20, 2014
 */
public class RepositoryFactory implements CacheEntryFactory{
	
	private ModelRepository repository;
	
	public RepositoryFactory(ModelRepository repository) {
		
		this.repository = repository;
	}


	/* (non-Javadoc)
	 * @see net.sf.ehcache.constructs.blocking.CacheEntryFactory#createEntry(java.lang.Object)
	 */
	@Override
	public Object createEntry(Object key) throws Exception {
		
		return repository.read((RestSearchKey) key);
	}
}
