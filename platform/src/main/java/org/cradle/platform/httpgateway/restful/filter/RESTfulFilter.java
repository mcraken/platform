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
 * @date 	Aug 25, 2014
 */
public interface RESTfulFilter {
	
	public void filter(HttpAdapter httpAdapter) 
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void next(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void setNextFilter(RESTfulFilter nextFilter);
	
}