/**
 * 
 */
package org.cradle.cache.ehcache.criteriahandlers;

import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.search.expression.Criteria;

import org.cradle.repository.key.RestCriteria;
import org.cradle.repository.key.exception.InvalidCriteriaException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public interface CriteriaHandler {
	
	public void handle(
			RestCriteria criteria, 
			Map<String, Criteria> criterias,
			Ehcache cache
			) throws InvalidCriteriaException;
}
