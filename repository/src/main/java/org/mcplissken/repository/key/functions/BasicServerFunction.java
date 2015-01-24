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
public abstract class BasicServerFunction implements ServerFunctionHandler {

	/* (non-Javadoc)
	 * @see com.mubasher.market.repository.key.functions.ServerFunctionHandler#handle(com.mubasher.market.repository.key.RestCriteria, com.mubasher.market.repository.ModelRepository, java.lang.String, java.lang.String, java.lang.String)
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
