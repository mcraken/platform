/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cradle.repository.cassandra.criteriahandlers;

import org.cradle.repository.key.RestCriteria;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 8, 2014
 */
public class LeCriteria extends BasicCriteriaHandler {

	/* (non-Javadoc)
	 * @see org.cradle.repository.cassandra.criteriahandlers.BasicCriteriaHandler#resolveCriteria(org.cradle.repository.key.RestCriteria, com.datastax.driver.core.querybuilder.Select.Where)
	 */
	@Override
	protected void resolveCriteria(RestCriteria criteria, Where query)
			throws Exception {
		
		addClause(
				QueryBuilder.lte(
						criteria.readCriteriaName(), 
						criteria.getParsedValue()
						)
					);
		
	}

}