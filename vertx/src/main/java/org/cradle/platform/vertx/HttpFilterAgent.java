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
package org.cradle.platform.vertx;

import org.cradle.platform.httpgateway.HttpFilter;
import org.cradle.platform.httpgateway.filter.PrecedenceFilter;
import org.cradle.platform.spi.RegistrationAgent;

import com.google.common.collect.Multimap;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 27, 2015
 */
public class HttpFilterAgent implements
		RegistrationAgent<PrecedenceFilter, HttpFilter> {
	
	private Multimap<String, PrecedenceFilter> filters;
	
	/**
	 * @param filters
	 */
	public HttpFilterAgent(Multimap<String, PrecedenceFilter> filters) {
		this.filters = filters;
	}

	/* (non-Javadoc)
	 * @see org.cradle.platform.spi.RegistrationAgent#register(java.lang.annotation.Annotation, java.lang.Object)
	 */
	@Override
	public void register(HttpFilter annotation, PrecedenceFilter handler) {
		
		filters.put(annotation.pattern(), handler);
	}

}
