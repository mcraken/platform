/**
 * 
 */
package org.mcplissken.repository.cassandra.criteriahandlers;

import java.util.Map;

import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.exception.InvalidCriteriaException;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Sep 8, 2014
 */
public interface CriteriaHandler {
	
	public void handle(
			RestCriteria criteria, 
			Map<String, Clause> criterias,
			Where query
			) throws InvalidCriteriaException;
}
