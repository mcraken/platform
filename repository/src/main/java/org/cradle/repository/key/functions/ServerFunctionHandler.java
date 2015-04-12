/**
 * 
 */
package org.cradle.repository.key.functions;

import org.cradle.repository.StructuredRepository;
import org.cradle.repository.key.RestCriteria;
import org.cradle.repository.key.exception.ServerFunctionException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Sep 9, 2014
 */
public interface ServerFunctionHandler {

	/**
	 * @param crt
	 * @param repository
	 * @param resourceName
	 * @throws ServerFunctionException
	 */
	void handle(
			RestCriteria crt, 
			StructuredRepository repository, 
			String propertyName,
			String resourceName
			) throws ServerFunctionException;
}
