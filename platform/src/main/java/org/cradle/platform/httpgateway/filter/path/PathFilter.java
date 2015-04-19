/**
 * 
 */
package org.cradle.platform.httpgateway.filter.path;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.PathNotAccessibleException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.filter.BasicFilter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 10, 2014
 */
public class PathFilter extends BasicFilter {

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
