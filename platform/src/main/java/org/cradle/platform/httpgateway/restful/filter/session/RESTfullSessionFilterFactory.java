/**
 * 
 */
package org.cradle.platform.httpgateway.restful.filter.session;

import org.cradle.platform.httpgateway.restful.filter.RESTfulFilter;
import org.cradle.platform.httpgateway.restful.filter.RESTfulFilterFactory;
import org.cradle.security.SecurityService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class RESTfullSessionFilterFactory implements RESTfulFilterFactory{
	
	private SecurityService securityService;
	
	/**
	 * @param securityService the securityService to set
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/* (non-Javadoc)
	 * @see org.cradle.gateway.restful.filter.RESTfulFilterFactory#createFilter()
	 */
	@Override
	public RESTfulFilter createFilter() {
		
		return new RESTfulSessionFilter(securityService);
	}

}
