/**
 * 
 */
package org.mcplissken.gateway.restful.filter.path;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.PathNotAccessibleException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;
import org.mcplissken.gateway.restful.filter.BasicRESTfulFilter;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 10, 2014
 */
public class RESTfulPathFilter extends BasicRESTfulFilter {

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.filter.BasicRESTfulFilter#doFilter(com.mubasher.gateway.HttpAdapter)
	 */
	@Override
	protected void doFilter(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException {
		
		try {
			
			httpAdapter.isPathAccessible();
			
		} catch (PathNotAccessibleException e) {
			
			throw new UnauthorizedException(e);
		}
		
	}


}
