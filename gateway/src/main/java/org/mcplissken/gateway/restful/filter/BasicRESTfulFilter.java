/**
 * 
 */
package org.mcplissken.gateway.restful.filter;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;
import org.mcplissken.gateway.restful.exception.UnknownResourceException;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
public abstract class BasicRESTfulFilter implements RESTfulFilter {

	private RESTfulFilter nextFilter;

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.filter.RESTfulFilter#next(com.mubasher.gateway.HttpAdapter)
	 */
	@Override
	public void next(HttpAdapter httpAdapter) throws BadRequestException,
	UnauthorizedException, UnknownResourceException {

		nextFilter.filter(httpAdapter);

	}

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.filter.RESTfulFilter#setNextFilter(com.mubasher.gateway.restful.filter.RESTfulFilter)
	 */
	@Override
	public void setNextFilter(RESTfulFilter nextFilter) {

		this.nextFilter = nextFilter;

	}

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.filter.RESTfulFilter#filter(com.mubasher.gateway.HttpAdapter)
	 */
	@Override
	public void filter(HttpAdapter httpAdapter) throws BadRequestException, UnauthorizedException, UnknownResourceException {
		
		doFilter(httpAdapter);
		
		if(nextFilter != null)
			nextFilter.filter(httpAdapter);

	}

	protected abstract void doFilter(HttpAdapter httpAdapter)throws BadRequestException, UnauthorizedException, UnknownResourceException;

}
