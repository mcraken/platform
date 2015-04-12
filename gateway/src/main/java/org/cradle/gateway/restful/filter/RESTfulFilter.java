/**
 * 
 */
package org.cradle.gateway.restful.filter;

import org.cradle.gateway.HttpAdapter;
import org.cradle.gateway.restful.exception.BadRequestException;
import org.cradle.gateway.restful.exception.UnauthorizedException;
import org.cradle.gateway.restful.exception.UnknownResourceException;

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