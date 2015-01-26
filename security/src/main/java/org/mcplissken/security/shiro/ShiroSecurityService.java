/**
 * 
 */
package org.mcplissken.security.shiro;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.mcplissken.repository.exception.NoResultException;
import org.mcplissken.repository.models.account.Account;
import org.mcplissken.security.SecurityService;
import org.mcplissken.security.User;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public class ShiroSecurityService implements SecurityService{

	private DefaultSecurityManager securityManager;
	private String config;
	private RepositoryRealm repositoryRealm;
	private String defaultRole;

	/**
	 * @param defaultRole the defaultRole to set
	 */
	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}

	/**
	 * @param repositoryRealm the repositoryRealm to set
	 */
	public void setRepositoryRealm(RepositoryRealm repositoryRealm) {
		this.repositoryRealm = repositoryRealm;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	public void init(){

		Factory<SecurityManager> factory = new IniSecurityManagerFactory(config);

		securityManager =  (DefaultSecurityManager) factory.getInstance();

		securityManager.setRealm(repositoryRealm);

		SecurityUtils.setSecurityManager(securityManager);

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.security.SecurityService#identify(java.lang.String)
	 */
	@Override
	public User identify(String sessionId) {

		Subject subject;

		try{

			Session session = SecurityUtils.getSecurityManager().getSession(new SimpleSessionKey(sessionId));

			subject = new Subject.Builder(securityManager).sessionId(sessionId).buildSubject();

			return new ShiroUser(subject, session);

		} catch(UnknownSessionException | ExpiredSessionException e){

			subject = (new Subject.Builder()).buildSubject();

			return new ShiroUser(subject);
		}

	}

	private String createRandomPassword() {
		
		SecureRandom random = new SecureRandom();

		return new BigInteger(130, random).toString(8);
	}
	
	private String hashPassword(String plainTextPassword) {
		
		RandomNumberGenerator rng = new SecureRandomNumberGenerator();

		Object salt = rng.nextBytes();

		return new Sha256Hash(plainTextPassword, salt, 1024).toBase64();
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.security.SecurityService#register(org.mcplissken.repository.models.account.Account)
	 */
	@Override
	public void register(Account account) {
		
		try {
			
			account.findByEmail();
			
		} catch (NoResultException e) {
			
			account.create(hashPassword(createRandomPassword()), new String[]{defaultRole});
		}
	}

}