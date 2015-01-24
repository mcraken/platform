/**
 * 
 */
package org.mcplissken.security;

import org.mcplissken.repository.models.account.Account;


/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
public interface SecurityService {
	
	public User identify(String sessionId);
	
	public void register(Account account);
}
