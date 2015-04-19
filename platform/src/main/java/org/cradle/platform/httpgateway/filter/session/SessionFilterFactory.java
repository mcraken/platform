/**
 * 
 */
package org.cradle.platform.httpgateway.filter.session;

import org.cradle.platform.httpgateway.filter.Filter;
import org.cradle.platform.httpgateway.filter.FilterFactory;
import org.cradle.security.SecurityService;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class SessionFilterFactory implements FilterFactory{
	
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
	public Filter createFilter() {
		
		return new SessionFilter(securityService);
	}

}
