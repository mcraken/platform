/**
 * 
 */
package org.mcplissken.repository.key.functions;

import org.mcplissken.repository.StructuredRepository;
import org.mcplissken.repository.key.RestCriteria;
import org.mcplissken.repository.key.exception.ServerFunctionException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
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
