/**
 * 
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
