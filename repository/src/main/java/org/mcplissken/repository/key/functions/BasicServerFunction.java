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
package org.mcplissken.repository.key.functions;

import org.mcplissken.repository.StructuredRepository;
import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.exception.ServerFunctionException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 9, 2014
 */
public abstract class BasicServerFunction implements ServerFunctionHandler {

	/* (non-Javadoc)
	 * @see org.mcplissken.repository.key.functions.ServerFunctionHandler#handle(org.mcplissken.repository.key.RestCriteria, org.mcplissken.repository.ModelRepository, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void handle(RestCriteria crt, StructuredRepository repository, String propertyName,
			String resourceName)
					throws ServerFunctionException {

		try{

			String parameters = crt.readFunctionParameters();

			String result = resolveFunction(repository, resourceName, propertyName, parameters);

			crt.setServerFunctionValue(result);

		} catch(Exception e){
			
			throw new ServerFunctionException(e);
		}
	}

	/**
	 * @param repository
	 * @param resourceName
	 * @param propertyName
	 * @param parameters
	 * @return
	 * @throws ServerFunctionException
	 */
	protected abstract String resolveFunction(StructuredRepository repository,
			String resourceName, String propertyName, String parameters) throws ServerFunctionException;

}
