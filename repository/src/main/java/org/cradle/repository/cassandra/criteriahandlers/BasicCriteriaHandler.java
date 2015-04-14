/**
 * 
 */
package org.cradle.repository.cassandra.criteriahandlers;

import java.util.Map;

import org.cradle.repository.key.RestCriteria;
import org.cradle.repository.key.exception.InvalidCriteriaException;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 8, 2014
 */
public abstract class BasicCriteriaHandler implements CriteriaHandler {

	private Map<String, Clause> criterias;
	private String id;
	
	/* (non-Javadoc)
	 * @see org.cradle.repository.cassandra.criteriahandlers.CriteriaHandler#handle(org.cradle.repository.key.RestCriteria, java.util.Map, com.datastax.driver.core.querybuilder.Select.Where)
	 */
	@Override
	public void handle(RestCriteria criteria, Map<String, Clause> criterias,
			Where query) throws InvalidCriteriaException {

		try {
			
			this.criterias = criterias;
		
			String id = criteria.readCriteriaId();
			
			this.id = id;
		
			resolveCriteria(criteria, query);

		} catch (Exception e) {

			throw new InvalidCriteriaException(e);
		}

	}
	
	protected void addClause(Clause clause){
		criterias.put(id, clause);
	}
	
	protected Clause readClause(String id){
		return criterias.get(id);
	}
	
	protected abstract void resolveCriteria(
			RestCriteria criteria, Where query) throws Exception;
}
