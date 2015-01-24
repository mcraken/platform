/**
 * 
 */
package org.mcplissken.gateway.restful.filter.session;

import org.mcplissken.gateway.HttpAdapter;
import org.mcplissken.gateway.restful.exception.BadRequestException;
import org.mcplissken.gateway.restful.exception.UnauthorizedException;
import org.mcplissken.gateway.restful.filter.BasicRESTfulFilter;
import org.mcplissken.security.SecurityService;
import org.mcplissken.security.User;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
public class RESTfulSessionFilter extends BasicRESTfulFilter {

	private SecurityService securityService;

	public RESTfulSessionFilter(SecurityService securityService) {
		this.securityService = securityService;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.gateway.restful.filter.RESTfulFilter#filter(com.mubasher.gateway.HttpAdapter)
	 */
	@Override
	public void doFilter(HttpAdapter httpAdapter) throws BadRequestException,
	UnauthorizedException {

		String sessionId = httpAdapter.sessionId();
		
		User user = securityService.identify(sessionId);
		
		httpAdapter.setUser(user);
	}

}
