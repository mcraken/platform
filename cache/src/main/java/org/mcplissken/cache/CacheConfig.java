/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
