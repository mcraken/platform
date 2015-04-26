/**
 * 
 */
package org.cradle.platform.httpgateway.filter.path;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.PathNotAccessibleException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.filter.Filter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 10, 2014
 */
public class PathFilter implements Filter {

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.BasicRESTfulFilter#doFilter(org.cradle.gateway.HttpAdapter)
	 */
	@Override
	public void filter(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException {
		
		try {
			
			httpAdapter.isPathAccessible();
			
		} catch (PathNotAccessibleException e) {
			
			throw new UnauthorizedException(e);
		}
		
	}


}
