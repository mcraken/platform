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
package org.mcplissken.gateway;

import org.mcplissken.cache.CacheService;
import org.mcplissken.gateway.restful.ResponseObject;
import org.mcplissken.gateway.restful.exception.RESTfulException;
import org.mcplissken.gateway.restful.exception.RedirectException;
import org.mcplissken.repository.ModelRepository;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Jan 29, 2015
 */
public class TestOutputHttpHandler extends OutputHttpHandler {

	private CacheService cacheService;
	
	private ModelRepository repoitory;
	
	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.OutputHttpHandler#execute(org.mcplissken.gateway.HttpAdapter)
	 */
	@Override
	protected ResponseObject execute(HttpAdapter httpAdapter)
			throws RedirectException, RESTfulException {
		
		
		return null;
	}

}
