/**
 * 
 */
package org.mcplissken.gateway;

import org.mcplissken.gateway.restful.exception.PathNotAccessibleException;
import org.mcplissken.gateway.restful.exception.RESTfulException;
import org.mcplissken.security.AuthenticationFailureException;
import org.mcplissken.security.User;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 9, 2014
 */
public abstract class BasicHttpAdapter implements HttpAdapter{

	private static final int INTERNAL_ERROR_CODE = 500;
	protected static final int REDIRECT_RESPONSE_CODE = 302;

	protected String sessionId;

	protected User user;

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.HttpAdapter#getUser()
	 */
	@Override
	public User getUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.HttpAdapter#updateSession()
	 */
	@Override
	public void setUser(User user) {

		this.user = user;

		if(!user.hasSession())
			setSessionId(user.sessionId());

	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.HttpAdapter#isPathAccessible()
	 */
	@Override
	public void isPathAccessible() throws PathNotAccessibleException{

		String path = path();

		String method = method();

		if(!user.isAccessible(method, path))
			throw new PathNotAccessibleException(path, method);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.HttpAdapter#exception(org.mcplissken.gateway.restful.exception.RESTfulException)
	 */
	@Override
	public void exception(RESTfulException exception) {

		handleErrorResponse(exception.getErrorCode(), exception.getMessage());
	}

	protected void extractSessionId(String cookie) {

		String[] parts = cookie.split(HEADER_SEP);

		for (String part: parts) {

			if (part.trim().startsWith(JSESSIONID)) {
				sessionId = part.split(COOKIE_PART_SEP)[1];
			}

		}
	}

	protected String createCookie(String name, String value, String cookie) {

		if(cookie == null)
			cookie = "";

		cookie +=  name + COOKIE_PART_SEP + value + "; path=/";

		return cookie;
	}

	protected void setSessionId(String sessionId) {

		setCookie(JSESSIONID, sessionId);
	}

	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.HttpAdapter#error(java.lang.Exception)
	 */
	@Override
	public void error(Exception exception) {

		handleErrorResponse(INTERNAL_ERROR_CODE, exception.getMessage());
	}
	
	/* (non-Javadoc)
	 * @see org.mcplissken.gateway.HttpAdapter#login(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void login(String email, String password, boolean rememberMe) throws AuthenticationFailureException {
		
		user.login(email, password, rememberMe);
		
		setCookie("email", email);
	}
	
	protected abstract void handleErrorResponse(int errorCode, String message);

}
