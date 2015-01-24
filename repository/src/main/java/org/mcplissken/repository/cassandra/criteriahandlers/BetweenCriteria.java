/**
 * 
 */
package org.mcplissken.repository.cassandra.criteriahandlers;

import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 9, 2014
 */
public class BetweenCriteria extends BasicCriteriaHandler {

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.cassandra.criteriahandlers.BasicCriteriaHandler#resolveCriteria(com.mubasher.market.repository.key.RestCriteria, com.datastax.driver.core.querybuilder.Select.Where)
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
