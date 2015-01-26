/**
 * @author Sherief Shawky(raken123@yahoo.com)
 */
package org.mcplissken.cache.ehcache.criteriahandlers;

import java.util.Map;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.expression.Criteria;

import org.mcplissken.repository.key.RestCriteria;


/**
 * @author Sherief Shawky(raken123@yahoo.com)
 *
 */
public class EqualCriteria extends BasicCriteriaHandler {

	/* (non-Javadoc)
	 * @see org.mcplissken.cache.ehcache.criteriahandlers.BasicCriteriaHandler#resolveCriteria(org.mcplissken.cache.key.RestCriteria, java.util.Map)
	 */
	@Override
	protected Criteria resolveCriteria(
			RestCriteria criteria,
			Map<String, Criteria> criterias) throws Exception {
		
		String name = criteria.readCriteriaName();
		
		Attribute<Object> attr = getAttribute(name);

		return attr.eq(criteria.getParsedValue());
	}

	

}
