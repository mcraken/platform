/**
 * 
 */
package org.cradle.security;

import org.cradle.repository.models.account.Account;


/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public interface SecurityService {
	
	public User identify(String sessionId);
	
	public void register(Account account);
}
