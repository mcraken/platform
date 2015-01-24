/**
 * 
 */
package org.mcplissken.security.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.mcplissken.repository.models.account.Account;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
public class AccountAuthenticationInfo implements AuthenticationInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Account userAccount;
	private PrincipalCollection principalCollection;
	
	
	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.AuthenticationInfo#getPrincipals()
	 */
	public AccountAuthenticationInfo(Account userAccount, String realmName) {
		
		this.userAccount = userAccount;
		
		this.principalCollection = new SimplePrincipalCollection(userAccount, realmName);
	}

	@Override
	public PrincipalCollection getPrincipals() {
		
		return principalCollection;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.AuthenticationInfo#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return userAccount.getPassword();
	}

}
