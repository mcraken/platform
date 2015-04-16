/**
 * 
 */
package org.cradle.platform.gateway.restful.filter;

import org.cradle.platform.gateway.HttpAdapter;
import org.cradle.platform.gateway.restful.exception.BadRequestException;
import org.cradle.platform.gateway.restful.exception.UnauthorizedException;
import org.cradle.platform.gateway.restful.exception.UnknownResourceException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 25, 2014
 */
public interface RESTfulFilter {
	
	public void filter(HttpAdapter httpAdapter) 
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void next(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void setNextFilter(RESTfulFilter nextFilter);
	
}