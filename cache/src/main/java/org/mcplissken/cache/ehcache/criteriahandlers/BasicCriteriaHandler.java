/**
 * 
 */
package org.mcplissken.cache.ehcache.criteriahandlers;

import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.expression.Criteria;

import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public abstract class BasicCriteriaHandler implements CriteriaHandler{

	private Ehcache cache;

	/**
	 * 
	 */
	public BasicCriteriaHandler() {
	}

	@Override
	public void handle(RestCriteria criteria, Map<String, Criteria> criterias, Ehcache cache)
			throws InvalidCriteriaException {

		this.cache = cache;
		
		try {

			String id = criteria.readCriteriaId();

			criterias.put(id, resolveCriteria(criteria, criterias));

		} catch (Exception e) {

			throw new InvalidCriteriaException(e);
		}

	}

	protected Attribute<Object> getAttribute(String name) {
		
		return cache.getSearchAttribute(name);
	}
	
	protected abstract Criteria resolveCriteria(RestCriteria criteria, Map<String, Criteria> criterias) throws Exception;
}