/**
 * 
 */
package org.mcplissken.cache.ehcache.criteriahandlers;

import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.search.expression.Criteria;

import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 4, 2014
 */
public interface CriteriaHandler {
	
	public void handle(
			RestCriteria criteria, 
			Map<String, Criteria> criterias,
			Ehcache cache
			) throws InvalidCriteriaException;
}
