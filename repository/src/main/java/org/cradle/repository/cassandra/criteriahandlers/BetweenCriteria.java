/**
 * 
 */
package org.cradle.repository.cassandra.criteriahandlers;

import org.cradle.repository.key.RestCriteria;
import org.cradle.repository.key.exception.InvalidCriteriaException;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 9, 2014
 */
public class BetweenCriteria extends BasicCriteriaHandler {

	/* (non-Javadoc)
	 * @see org.cradle.repository.cassandra.criteriahandlers.BasicCriteriaHandler#resolveCriteria(org.cradle.repository.key.RestCriteria, com.datastax.driver.core.querybuilder.Select.Where)
	 */
	@Override
	protected void resolveCriteria(RestCriteria criteria, Where query)
			throws Exception {
		
		Object[] range =  criteria.getParsedValues();
		
		String criteriaName = criteria.readCriteriaName();
		
		if(range.length > 2 || range.length < 2)
			throw new InvalidCriteriaException("between requires range of two.");
		
		query.and(QueryBuilder.lte(criteriaName, range[0]))
			 .and(QueryBuilder.gte(criteriaName, range[1]));
	}

}
