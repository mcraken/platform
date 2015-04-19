/**
 * 
 */
package org.cradle.platform.httpgateway.restful.filter;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.restful.exception.BadRequestException;
import org.cradle.platform.httpgateway.restful.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.restful.exception.UnknownResourceException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public abstract class BasicRESTfulFilter implements RESTfulFilter {

	private RESTfulFilter nextFilter;

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilter#next(org.cradle.gateway.HttpAdapter)
	 */
	@Override
	public void next(HttpAdapter httpAdapter) throws BadRequestException,
	UnauthorizedException, UnknownResourceException {

		nextFilter.filter(httpAdapter);

	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilter#setNextFilter(org.cradle.gateway.restful.filter.RESTfulFilter)
	 */
	@Override
	public void setNextFilter(RESTfulFilter nextFilter) {

		this.nextFilter = nextFilter;

	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilter#filter(org.cradle.gateway.HttpAdapter)
	 */
	@Override
	public void filter(HttpAdapter httpAdapter) throws BadRequestException, UnauthorizedException, UnknownResourceException {
		
		doFilter(httpAdapter);
		
		if(nextFilter != null)
			nextFilter.filter(httpAdapter);

	}

	protected abstract void doFilter(HttpAdapter httpAdapter)throws BadRequestException, UnauthorizedException, UnknownResourceException;

}
