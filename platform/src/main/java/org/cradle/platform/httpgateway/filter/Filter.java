/**
 * 
 */
package org.cradle.platform.httpgateway.filter;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.exception.UnknownResourceException;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Aug 25, 2014
 */
public interface Filter {
	
	public void filter(HttpAdapter httpAdapter) 
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void next(HttpAdapter httpAdapter)
			throws BadRequestException, UnauthorizedException, UnknownResourceException;
	
	public void setNextFilter(Filter nextFilter);
	
}