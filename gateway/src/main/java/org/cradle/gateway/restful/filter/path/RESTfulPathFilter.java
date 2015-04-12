/**
 * 
 */
package org.cradle.gateway.restful.filter.path;

import org.cradle.gateway.HttpAdapter;
import org.cradle.gateway.restful.exception.BadRequestException;
import org.cradle.gateway.restful.exception.PathNotAccessibleException;
import org.cradle.gateway.restful.exception.UnauthorizedException;
import org.cradle.gateway.restful.filter.BasicRESTfulFilter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 10, 2014
 */
public class RESTfulPathFilter extends BasicRESTfulFilter {

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.BasicRESTfulFilter#doFilter(org.cradle.gateway.HttpAdapter)
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
