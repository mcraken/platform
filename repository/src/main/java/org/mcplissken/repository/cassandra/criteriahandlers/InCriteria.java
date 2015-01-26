/**
 * 
 */
package org.mcplissken.repository.cassandra.criteriahandlers;

import org.mcplissken.repository.key.RestCriteria;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 8, 2014
 */
public class InCriteria extends BasicCriteriaHandler{

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.cassandra.criteriahandlers.BasicCriteriaHandler#resolveCriteria(org.mcplissken.repository.key.RestCriteria, com.datastax.driver.core.querybuilder.Select.Where)
	 */
	@Override
	protected void resolveCriteria(RestCriteria criteria, Where query)
			throws Exception {
		
		addClause(
				QueryBuilder.in(
						criteria.readCriteriaName(), 
						criteria.getParsedValues()
						)
					);
	}

}
