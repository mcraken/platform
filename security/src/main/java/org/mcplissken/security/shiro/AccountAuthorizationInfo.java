/**
 * 
 */
package org.mcplissken.security.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.mcplissken.repository.models.account.Account;
import org.mcplissken.repository.models.account.Role;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
public class AccountAuthorizationInfo implements AuthorizationInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Account userAccount;
	private List<Permission> permissions;
	
	public AccountAuthorizationInfo(Account userAccount, List<Role> roles) {
		this.userAccount = userAccount;
		this.permissions = new ArrayList<>();
		
		List<String> textPermissions;
		
		for(Role role : roles){
			
			textPermissions = role.getPermissions();
			
			for(String formattedPermission : textPermissions)
				permissions.add(new WildcardPermission(formattedPermission));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authz.AuthorizationInfo#getRoles()
	 */
	@Override
	public Collection<String> getRoles() {
		return userAccount.getRoles();
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authz.AuthorizationInfo#getStringPermissions()
	 */
	@Override
	public Collection<String> getStringPermissions() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authz.AuthorizationInfo#getObjectPermissions()
	 */
	@Override
	public Collection<Permission> getObjectPermissions() {
		return permissions;
	}

}
