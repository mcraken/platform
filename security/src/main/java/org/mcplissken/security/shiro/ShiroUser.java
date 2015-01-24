/**
 * 
 */
package org.mcplissken.security.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.mcplissken.security.AuthenticationFailureException;
import org.mcplissken.security.User;

/**
 * @author 	Sherief Shawky
 * @email 	sherif.shawki@mubasher.info
 * @date 	Nov 9, 2014
 */
public class ShiroUser implements User {

	private Subject subject;
	private boolean hasSession;
	private Session userSession;


	public ShiroUser(Subject subject, boolean hasSession, Session userSession) {

		this.subject = subject;
		this.hasSession = hasSession;
		this.userSession = userSession;

	}

	public ShiroUser(Subject subject) {

		this(subject, false, subject.getSession());

		changeStatus(ANONYMOUS, false);
	}


	public ShiroUser(Subject subject, Session userSession) {

		this(subject, true, userSession);

	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.User#getSessionId()
	 */
	@Override
	public String sessionId() {

		return userSession.getId().toString();
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.User#sessionIdEquals(java.lang.String)
	 */
	@Override
	public boolean hasSession() {
		return hasSession;
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.User#isAccessible(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isAccessible(String method, String path) {

		return subject.isPermitted(method + ":" + path);
	}

	public void login(String email, String password, boolean rememberMe) throws AuthenticationFailureException  
	{
		try{
			
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
					email, password);

			usernamePasswordToken.setRememberMe(rememberMe);

			subject.login(usernamePasswordToken);
			
		} catch(AuthenticationException e){

			throw new AuthenticationFailureException(e);
		}

	}

	private void changeStatus(String status, boolean overwrite){

		String currentStatus = (String) userSession.getAttribute(STATUS);

		if(overwrite || currentStatus == null)
			userSession.setAttribute(STATUS, status);
	}


	/* (non-Javadoc)
	 * @see com.mubasher.market.security.User#addSessionParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public void addSessionParameter(String name, Object value) {

		userSession.setAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.User#readSessionParameter(java.lang.String)
	 */
	@Override
	public Object readSessionParameter(String name) {

		return userSession.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see com.mubasher.market.security.User#userId()
	 */
	@Override
	public String userId() {

		return (String) subject.getPrincipal();
	}

}
