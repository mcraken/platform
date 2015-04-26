/**
 * 
 */
package org.cradle.platform.httpgateway.filter.session;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.BadRequestException;
import org.cradle.platform.httpgateway.exception.UnauthorizedException;
import org.cradle.platform.httpgateway.filter.Filter;
import org.cradle.security.SecurityService;
import org.cradle.security.User;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class SessionFilter implements Filter {

	private SecurityService securityService;

	public SessionFilter(SecurityService securityService) {
		this.securityService = securityService;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilter#filter(org.cradle.gateway.HttpAdapter)
	 */
	@Override
	public void filter(HttpAdapter httpAdapter) throws BadRequestException,
	UnauthorizedException {

		String sessionId = httpAdapter.sessionId();
		
		User user = securityService.identify(sessionId);
		
		httpAdapter.setUser(user);
	}

}
