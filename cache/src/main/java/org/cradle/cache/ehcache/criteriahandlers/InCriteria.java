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
package org.cradle.cache.ehcache.criteriahandlers;

import java.util.Arrays;
import java.util.Map;

import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.expression.Criteria;

import org.cradle.repository.key.RestCriteria;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 4, 2014
 */
public class InCriteria extends BasicCriteriaHandler{

	/* (non-Javadoc)
	 * @see org.cradle.cache.ehcache.criteriahandlers.BasicCriteriaHandler#resolveCriteria(java.lang.String, org.cradle.cache.key.RestCriteria, java.util.Map)
	 */
	@Override
	protected Criteria resolveCriteria(RestCriteria criteria,
			Map<String, Criteria> criterias) throws Exception {
		
		String name = criteria.readCriteriaName();
		
		Attribute<Object> attr = getAttribute(name);
		
		return attr.in(Arrays.asList(criteria.getParsedValues()));
	}

}
