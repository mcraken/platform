/**
 * 
 */
package org.mcplissken.cache.ehcache.criteriahandlers;

import java.util.Arrays;
import java.util.Map;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.expression.Criteria;

import org.mcplissken.repository.key.RestCriteria;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public class InCriteria extends BasicCriteriaHandler{

	/* (non-Javadoc)
	 * @see org.mcplissken.cache.ehcache.criteriahandlers.BasicCriteriaHandler#resolveCriteria(java.lang.String, org.mcplissken.cache.key.RestCriteria, java.util.Map)
	 */
	@Override
	protected Criteria resolveCriteria(RestCriteria criteria,
			Map<String, Criteria> criterias) throws Exception {
		
		String name = criteria.readCriteriaName();
		
		Attribute<Object> attr = getAttribute(name);
		
		return attr.in(Arrays.asList(criteria.getParsedValues()));
	}

}
