/**
 * 
 */
package org.mcplissken.cache;

import java.util.List;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 27, 2015
 */
public abstract class CacheConfig<T> {
	
	private String name;
	
	private CacheService cacheService;
	
	private boolean eternal;
	
	public CacheConfig(String name, CacheService cacheService, boolean eternal) {
		this.name = name;
		this.cacheService = cacheService;
		this.eternal = eternal;
	}

	public CacheConfig() {
		
		eternal = false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param cacheService the cacheService to set
	 */
	public void setCacheService(CacheService cacheService) {
		
		this.cacheService = cacheService;
	}
	
	/**
	 * @param eternal the eternal to set
	 */
	public void setEternal(boolean eternal) {
		
		this.eternal = eternal;
	}
	
	public CacheService getCacheService() {
		return cacheService;
	}

	public boolean isEternal() {
		return eternal;
	}

	public void init(){
		
		cacheService.regitserCache(name, eternal, createExtractors());
		
	}

	protected abstract List<CacheAttributeExtractor<T>> createExtractors();
	
}
