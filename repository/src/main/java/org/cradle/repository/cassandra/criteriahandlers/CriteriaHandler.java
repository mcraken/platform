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
public interface CriteriaHandler {
	
	public void handle(
			RestCriteria criteria, 
			Map<String, Clause> criterias,
			Where query
			) throws InvalidCriteriaException;
}
