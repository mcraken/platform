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
package org.cradle.cache.ehcache;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.expression.Criteria;

import org.cradle.cache.CacheAttributeExtractor;
import org.cradle.cache.CacheService;
import org.cradle.cache.KeySelectionAdapter;
import org.cradle.cache.ValueSelectionAdapter;
import org.cradle.cache.ehcache.criteriahandlers.CriteriaHandler;
import org.cradle.cache.ehcache.factory.RepositoryFactory;
import org.cradle.repository.ModelRepository;
import org.cradle.repository.key.RestCriteria;
import org.cradle.repository.key.RestSearchKey;
import org.cradle.repository.key.exception.CriteriaNotFoundException;
import org.cradle.repository.key.exception.InvalidCriteriaException;
import org.cradle.repository.key.exception.InvalidCriteriaSyntaxException;
import org.cradle.repository.key.exception.UnknowModelException;
import org.cradle.repository.models.RestModel;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jul 30, 2014
 */
public class EhcacheService implements CacheService {

	private CacheManager cacheManager;
	private String config;
	private Map<String, CriteriaHandler> criteriaHandlers;
	private List<String> repoFactoryCaches;
	
	private ModelRepository modelRepository;

	public void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	public void init(){

		cacheManager = CacheManager.newInstance(config);
		
		for(String repFactoryCache :  repoFactoryCaches){
			
			createRepositoryFactory(repFactoryCache);
			
		}
		
	}

	private void createRepositoryFactory(String cacheName) {
		
		RepositoryFactory factory = new RepositoryFactory(modelRepository);
		
		Ehcache cache = cacheManager.getEhcache(cacheName); 
		
		SelfPopulatingCache decoratedIntervalQuotesEhcache = new SelfPopulatingCache(
				cache, 
				factory
				);
		
		cacheManager.replaceCacheWithDecoratedCache(cache, decoratedIntervalQuotesEhcache);
	}
	
	/**
	 * @param repoFactoryCaches the repoFactoryCaches to set
	 */
	public void setRepoFactoryCaches(List<String> repoFactoryCaches) {
		this.repoFactoryCaches = repoFactoryCaches;
	}
	
	/**
	 * @param criteriaHandlers the criteriaHandlers to set
	 */
	public void setCriteriaHandlers(
			Map<String, CriteriaHandler> criteriaHandlers) {
		this.criteriaHandlers = criteriaHandlers;
	}
	

	/**
	 * @param config the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	public void write(String cacheName, Object key, Object modelObject) {

		Ehcache cache = cacheManager.getEhcache(cacheName);

		Element element = new Element(key, modelObject);

		cache.put(element);
		
	}
	
	@Override
	public boolean remove(String cacheName, Object key) {
		
		Cache cache = cacheManager.getCache(cacheName);
		
		return cache.remove(key);
	}
	
	@Override
	public void writeThrough(String cacheName, Object key, Object modelObject) {
		
		Cache cache = cacheManager.getCache(cacheName);

		Element element = new Element(key, modelObject);
		
		cache.putWithWriter(element);
	}
	
	/**
	 * @param keys
	 * @return
	 */
	private Map<Object, Object> map(List<Result> results, Ehcache cache) {

		Hashtable<Object, Object> all = new Hashtable<>(results.size());

		Object key;

		for(Result result : results){

			key = result.getKey();

			all.put(key, cache.get(key).getObjectValue());
		}

		return all;
	}

	@Override
	public KeySelectionAdapter createKeySelectionAdapter(String cacheName) {
		
		return new EhcacheKeySelectionAdapter(cacheManager.getCache(cacheName));
	}

	@Override
	public Object read(String modelName, Object key) {
		
		Element element = cacheManager.getEhcache(modelName).get(key);
		
		Object value = element == null ? null : element.getObjectValue();
		
		return value;
	}
	
	@Override
	public ValueSelectionAdapter createValueSelectionAdapter(String cacheName) {
		return new EhcacheValueSelectionAdapter(cacheManager.getCache(cacheName));
	}

	@Override
	public Object read(RestSearchKey key) 
			throws InvalidCriteriaSyntaxException, InvalidCriteriaException, UnknowModelException 
	{
		
		Ehcache cache = getTargetCache(key);
		
		if(cache instanceof SelfPopulatingCache){
			
			return read(key.getResourceName(), key);
			
		} else {
			
			Map<String, Criteria> crtsMap = buildCriteriaMap(cache, key);
			
			Criteria[] criterions = crtsMap.values().toArray(new Criteria[]{});
			
			return executeSearchQuery(key, cache, criterions);
		}

	}

	private Map<Object, Object> executeSearchQuery(RestSearchKey key, Ehcache cache, Criteria[] criterions) {
		
		Query query = cache.createQuery();
		
		query.includeKeys();
		
		for(Criteria crt : criterions){
			query = query.addCriteria(crt);
		}
		
		int count = key.getCount();
		
		if(count != 0)
			query.maxResults(count);
		
		return map(query.execute().all(), cache);
	}

	private Ehcache getTargetCache(RestSearchKey key) throws UnknowModelException{
		
		String cacheName = key.getResourceName();
		
		Ehcache cache = cacheManager.getEhcache(cacheName);
		
		if(cache == null)
			throw new UnknowModelException(cacheName);
		
		return cache;
	}

	private Map<String, Criteria> buildCriteriaMap(Ehcache cache, 
			RestSearchKey key) throws InvalidCriteriaException {
		
		Iterator<RestCriteria> crtsIter = key.criteriasIterator();
		
		RestCriteria crt;
		
		CriteriaHandler handler = null;
		
		Map<String, Criteria> crtsMap = new HashMap<String, Criteria>();
		
		while(crtsIter.hasNext()){
			
			crt = crtsIter.next();
			
			handler = criteriaHandlers.get(crt.readCriteriaFunction());
			
			if(handler == null)
				throw new InvalidCriteriaException(new CriteriaNotFoundException(crt.readCriteriaFunction()));
			
			handler.handle(crt, crtsMap, cache);
		}
		
		return crtsMap;
	}

	@Override
	public void write(String cacheName, RestModel[] models) {
		
		for(RestModel model : models)
			write(cacheName, model.getUniqueId(), model);
	}

	@Override
	public <T> void regitserCache(String name, boolean eternal,
			List<CacheAttributeExtractor<T>> extractors) {
		
		CacheConfiguration cacheConfiguration = 
				new CacheConfiguration(name, 0).eternal(eternal); 
		
		if(extractors != null){
			
			Searchable searchable = new Searchable();
			
			cacheConfiguration.addSearchable(searchable); 
			
			for(CacheAttributeExtractor<T> cacheAttributeExtractor : extractors){
				
				searchable.addSearchAttribute(
						new ExtractorSupportedSearchAttribute(
								new EhcacheAttributeExtractor<T>(cacheAttributeExtractor))
						.name(cacheAttributeExtractor.getAttributeName()));
			}
			
		}
		
		cacheManager.addCache(new Cache(cacheConfiguration)); 
	}

}