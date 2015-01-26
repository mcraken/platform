/**
 * 
 */
package org.mcplissken.gateway.restful.filter.session;

import org.mcplissken.gateway.restful.filter.RESTfulFilter;
import org.mcplissken.gateway.restful.filter.RESTfulFilterFactory;
import org.mcplissken.security.SecurityService;

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
	 * @see org.mcplissken.gateway.restful.filter.RESTfulFilterFactory#createFilter()
	 */
	@Override
	public RESTfulFilter createFilter() {
		
		return new RESTfulSessionFilter(securityService);
	}

}
