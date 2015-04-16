/**
 * 
 */
package org.cradle.platform.gateway.restful.filter.session;

import org.cradle.platform.gateway.HttpAdapter;
import org.cradle.platform.gateway.restful.exception.BadRequestException;
import org.cradle.platform.gateway.restful.exception.UnauthorizedException;
import org.cradle.platform.gateway.restful.filter.BasicRESTfulFilter;
import org.cradle.security.SecurityService;
import org.cradle.security.User;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class RESTfulSessionFilter extends BasicRESTfulFilter {

	private SecurityService securityService;

	public RESTfulSessionFilter(SecurityService securityService) {
		this.securityService = securityService;
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilter#filter(org.cradle.gateway.HttpAdapter)
	 */
	@Override
	public void doFilter(HttpAdapter httpAdapter) throws BadRequestException,
	UnauthorizedException {

		String sessionId = httpAdapter.sessionId();
		
		User user = securityService.identify(sessionId);
		
		httpAdapter.setUser(user);
	}

}
