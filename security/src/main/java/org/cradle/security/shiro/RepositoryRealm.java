/**
 * 
 */
package org.cradle.security.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.cradle.reporting.SystemReportingService;
import org.cradle.repository.ModelRepository;
import org.cradle.repository.exception.NoResultException;
import org.cradle.repository.models.account.Account;
import org.cradle.repository.models.account.Role;
import org.cradle.repository.query.SimpleSelectionAdapter;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class RepositoryRealm extends AuthorizingRealm {

	private ModelRepository modelRepository;
	private List<Role> allRoles;
	private SystemReportingService reportingService;

	/**
	 * @param reportingService the reportingService to set
	 */
	public void setReportingService(SystemReportingService reportingService) {
		this.reportingService = reportingService;
	}

	/**
	 * @param modelRepository the modelRepository to set
	 */
	public void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	public void start(){

		try {

			selectAllRoles();

		} catch (NoResultException e) {

			reportingService.exception(getClass().getSimpleName(), SystemReportingService.CONSOLE, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {

		Account userAccount = (Account) principals.getPrimaryPrincipal();

		List<Role> userRoles = userAccount.filterRoles(allRoles);

		return new AccountAuthorizationInfo(userAccount, userRoles);
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		try {

			Account loginAccount = createLoginAccount(token);

			loginAccount.find();

			return new AccountAuthenticationInfo(loginAccount, getName());

		} catch (NoResultException e) {

			throw new AuthenticationException();
		}

	}

	private Account createLoginAccount(AuthenticationToken token) {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;

		String email = upToken.getUsername();

		String password = new String(upToken.getPassword());

		Account loginAccount = new Account(email, password, modelRepository);

		return loginAccount;
	}

	private void selectAllRoles() throws NoResultException {

		SimpleSelectionAdapter<Role> selectionAdapter = modelRepository.createSimpleSelectionAdapter("role");

		allRoles = selectionAdapter.result();
	}

}
